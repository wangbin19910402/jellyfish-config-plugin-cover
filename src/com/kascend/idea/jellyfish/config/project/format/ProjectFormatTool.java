package com.kascend.idea.jellyfish.config.project.format;

import com.kascend.idea.jellyfish.config.project.ProjectEnum;
import com.kascend.idea.jellyfish.config.project.format.support.ConsoleProjectFormat;
import com.kascend.idea.jellyfish.config.project.format.support.ServerProjectFormat;

/**
 * 项目初始化工具
 *
 * @author junhui.si
 */
public class ProjectFormatTool {

    private static final ServerProjectFormat SERVER_PROJECT_FORMAT = new ServerProjectFormat();

    private static final ConsoleProjectFormat CONSOLE_PROJECT_FORMAT = new ConsoleProjectFormat();

    /**
     * 根据不同项目类型格式化（结构补充）项目
     *
     * @param projectBasePath module绝对路径
     * @param projectType     项目类型
     * @return
     * @throws Exception
     */
    public static boolean format(String projectBasePath, ProjectEnum projectType) throws Exception {
        return getProjectFormat(projectType).format(projectBasePath);
    }

    /**
     * 获取对应项目的处理类
     *
     * @param projectType 项目类型
     * @return 对应项目的处理类
     */
    private static ProjectFormat getProjectFormat(ProjectEnum projectType) {
        switch (projectType) {
            case SERVER_WEB:
                return SERVER_PROJECT_FORMAT;
            case CONSOLE_WEB:
                return CONSOLE_PROJECT_FORMAT;
            default:
                throw new IllegalArgumentException("Invalid project type: " + projectType);
        }
    }
}
