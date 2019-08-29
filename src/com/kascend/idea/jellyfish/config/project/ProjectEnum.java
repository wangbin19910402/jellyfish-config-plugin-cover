package com.kascend.idea.jellyfish.config.project;

/**
 * 项目类型枚举
 *
 * @author junhui.si
 */
public enum ProjectEnum {

    /**
     * server项目
     */
    SERVER_WEB("Server Web"),

    /**
     * console项目
     */
    CONSOLE_WEB("console Web");

    private final String displayName;

    ProjectEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static String[] getDisplayNames() {
        return new String[]{SERVER_WEB.displayName, CONSOLE_WEB.displayName};
    }

    public static ProjectEnum toProjectFormatEnum(String displayName) {
        if (SERVER_WEB.displayName.equals(displayName)) {
            return SERVER_WEB;
        } else if (CONSOLE_WEB.displayName.equals(displayName)) {
            return CONSOLE_WEB;
        } else {
            throw new IllegalArgumentException("Unknown project format enum: `" + displayName + "`.");
        }
    }
}
