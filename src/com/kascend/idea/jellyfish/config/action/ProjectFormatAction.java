package com.kascend.idea.jellyfish.config.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.kascend.idea.jellyfish.config.constant.JellyfishConfigPluginConstant;
import com.kascend.idea.jellyfish.config.project.ProjectEnum;
import com.kascend.idea.jellyfish.config.project.format.ProjectFormatExecutor;
import com.kascend.idea.jellyfish.config.util.MessagesUtil;

/**
 * 项目格式化（结构补充）action
 *
 * @author junhui.si
 */
public class ProjectFormatAction extends AnAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectFormatAction.class);

    public ProjectFormatAction() {
        super("Cover-Format Project", "", JellyfishConfigPluginConstant.ICON_JELLYFISH_16);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        try {
            String selectedProjectName = MessagesUtil.getSelectedProjectName(project);
            if (selectedProjectName == null || selectedProjectName.isEmpty()) {
                return;
            }

            String selectedProjectFormatName = MessagesUtil.getSelectedValue("项目类型选择", "请选择需要初始化的项目类型：", ProjectEnum.getDisplayNames());
            if (selectedProjectFormatName == null || selectedProjectFormatName.isEmpty()) {
                return;
            }

            String projectBasePath = MessagesUtil.getProjectBasePath(project, selectedProjectName);
            String generateConfirmMessage = "项目路径：" + projectBasePath;
            generateConfirmMessage += "\r\n项目类型：" + selectedProjectFormatName;
            if (MessagesUtil.confirm(project, "是否初始化该项目？", generateConfirmMessage)) {
                boolean isSuccess = ProjectFormatExecutor.format(projectBasePath, ProjectEnum.toProjectFormatEnum(selectedProjectFormatName));
                if (isSuccess) {
                    Messages.showMessageDialog(project, "`" + selectedProjectFormatName + "` 项目初始化成功。", "项目初始化成功", JellyfishConfigPluginConstant.ICON_JELLYFISH_32);
                } else {
                    Messages.showErrorDialog(project, "`" + selectedProjectFormatName + "` 项目初始化失败。", "项目初始化失败");
                }
            }
        } catch (Exception e) {
            Messages.showErrorDialog(project, "Format project failed: `" + e.getMessage() + "`.", "Format project failed");
            LOGGER.error("Format project failed.", e);
        }

    }
}
