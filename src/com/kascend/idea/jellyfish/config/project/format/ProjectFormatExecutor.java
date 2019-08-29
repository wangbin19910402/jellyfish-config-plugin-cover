package com.kascend.idea.jellyfish.config.project.format;

import com.kascend.idea.jellyfish.config.project.ProjectEnum;
import com.kascend.idea.jellyfish.config.project.format.support.ConsoleProjectFormat;
import com.kascend.idea.jellyfish.config.project.format.support.ServerProjectFormat;

public class ProjectFormatExecutor {

    private static final ServerProjectFormat SERVER_PROJECT_FORMAT = new ServerProjectFormat();

    private static final ConsoleProjectFormat CONSOLE_PROJECT_FORMAT = new ConsoleProjectFormat();
    // projectBasePath module绝对路径；projectType：项目类型
    public static boolean format(String projectBasePath, ProjectEnum projectType) throws Exception {
        return getProjectFormat(projectType).format(projectBasePath);
    }

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
