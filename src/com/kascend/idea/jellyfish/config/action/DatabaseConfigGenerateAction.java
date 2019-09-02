package com.kascend.idea.jellyfish.config.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.kascend.idea.jellyfish.config.constant.JellyfishConfigPluginConstant;
import com.kascend.idea.jellyfish.config.database.DatabaseConfig;
import com.kascend.idea.jellyfish.config.database.DatabaseConfigFileGenerator;
import com.kascend.idea.jellyfish.config.database.support.SvnPropertyDatabaseConfigLoader;
import com.kascend.idea.jellyfish.config.util.MessagesUtil;

/**
 * 数据库配置生成器Action
 *
 * @author junhui.si
 */
public class DatabaseConfigGenerateAction extends AnAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConfigGenerateAction.class);

    public DatabaseConfigGenerateAction() {
        super("Cover-Database Config generate", "", JellyfishConfigPluginConstant.ICON_JELLYFISH_16);
    }

    @Override
    public void actionPerformed(AnActionEvent event) {
        Project project = event.getData(PlatformDataKeys.PROJECT);
        try {
            String selectedProjectName = MessagesUtil.getSelectedProjectName(project);
            if (selectedProjectName == null || selectedProjectName.isEmpty()) {
                return;
            }

            SvnPropertyDatabaseConfigLoader loader = new SvnPropertyDatabaseConfigLoader();
            String[] databaseNames = loader.getDatabaseNames();
            if (databaseNames == null || databaseNames.length == 0) {
                Messages.showErrorDialog(project, "没有可用的数据库配置。", "没有可用的数据库配置");
                return;
            }

            String selectedDatabaseName = MessagesUtil.getSelectedValue("数据库选择", "请选择需要配置的数据库名称：", databaseNames);
            if (selectedDatabaseName == null || selectedDatabaseName.isEmpty()) {
                return;
            }

            DatabaseConfig config = loader.get(selectedDatabaseName);
            if (config != null) {
                boolean isFrequently = MessagesUtil.confirm(project, "数据库调用频率", "数据库调用是否频繁？(仅影响初始连接数和慢查时间配置)");
                boolean includeFalconConfig = MessagesUtil.confirm(project, "Falcon 配置文件", "是否生成 Falcon 配置文件？");

                String projectBasePath = MessagesUtil.getProjectBasePath(project, selectedProjectName);
                String generateConfirmMessage = "项目路径：" + projectBasePath;
                generateConfirmMessage += "\r\n数据库名称：" + config.getName();
                generateConfirmMessage += "\r\n数据库调用频率：" + (isFrequently ? "高" : "低");
                generateConfirmMessage += "\r\n是否生成 Falcon 配置文件：" + (includeFalconConfig ? "是" : "否");
                generateConfirmMessage += "\r\n是否生成该数据库配置文件？";
                if (MessagesUtil.confirm(project, "是否生成数据库配置", generateConfirmMessage)) {
                    DatabaseConfigFileGenerator.generate(projectBasePath, config, isFrequently, includeFalconConfig, getProjectName(selectedProjectName));
                    Messages.showMessageDialog(project, "`" + selectedDatabaseName + "` 配置文件生成成功。", "配置文件生成成功", JellyfishConfigPluginConstant.ICON_JELLYFISH_32);
                }
            } else {
                Messages.showErrorDialog(project, "`" + selectedDatabaseName + "` 数据库不存在，请检查数据库名称。", "数据库不存在");
            }
        } catch (Exception e) {
            Messages.showErrorDialog(project, "Generate database config failed : `" + e.getMessage() + "`.", "Generate database config failed");
            LOGGER.error("Generate database config failed.", e);
        }
    }

    /**
     * 获取项目名
     *
     * @param selectedProjectName 选择的项目名（项目名/module名）
     * @return
     */
    private String getProjectName(String selectedProjectName) {
        return selectedProjectName.substring(selectedProjectName.indexOf("/") + 1, selectedProjectName.length());
    }
}
