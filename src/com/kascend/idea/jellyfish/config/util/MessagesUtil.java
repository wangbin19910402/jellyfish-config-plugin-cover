package com.kascend.idea.jellyfish.config.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.InputValidator;
import com.intellij.openapi.ui.Messages;
import com.kascend.idea.jellyfish.config.constant.JellyfishConfigPluginConstant;

public class MessagesUtil {

    /**
     * 选择需要格式化的项目
     *
     * @param project 当前项目
     * @return 需要格式化的项目名
     */
    public static String getSelectedProjectName(Project project) {
        String selectedProjectName = null;
        String[] validProjectNames = getValidProjectNames(project);
        if (validProjectNames.length == 0) {
            Messages.showErrorDialog(project, "没有符合条件的项目。", "没有符合条件的项目");
        } else if (validProjectNames.length == 1) {
            selectedProjectName = validProjectNames[0];
        } else {
            selectedProjectName = getSelectedValue("项目选择", "请选择需要配置的项目：", validProjectNames);
        }
        return selectedProjectName;
    }

    /**
     * 选项
     * @param title
     * @param message
     * @param values
     * @return
     */
    public static String getSelectedValue(String title, String message, String[] values) {
        String selectedValue = Messages.showEditableChooseDialog(message, title,
                JellyfishConfigPluginConstant.ICON_JELLYFISH_32, values, values[0], new InputValidator() {
                    @Override
                    public boolean checkInput(String s) {
                        for (String value : values) {
                            if (value.equals(s)) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override
                    public boolean canClose(String s) {
                        return true;
                    }
                });
        if (selectedValue != null && !selectedValue.isEmpty()) {
            return selectedValue;
        } else {
            return null;
        }
    }

    public static boolean confirm(Project project, String title, String message) {
        int response = Messages.showOkCancelDialog(project, message, title, "YES", "NO", JellyfishConfigPluginConstant.ICON_JELLYFISH_32);
        return response == Messages.OK;
    }

    public static String getProjectBasePath(Project project, String projectName) {
        if (projectName.contains("/")) {
            return project.getBasePath() + projectName.substring(projectName.indexOf("/"));
        } else {
            return project.getBasePath();
        }
    }

    /**
     * 获取项目路径下的所有项目的相对路径
     *
     * @param project 当前项目
     * @return 所有项目的相对路径
     */
    private static String[] getValidProjectNames(Project project) {
        String projectBasePath = project.getBasePath();//项目绝对路径
        File projectDir = new File(projectBasePath);
        ArrayList<String> validProjectNames = new ArrayList<>();
        if (isValidProject(projectDir)) {
            validProjectNames.add(projectDir.getName());
        }
        for (File file : projectDir.listFiles()) {
            if (isValidProject(file)) {
                validProjectNames.add(projectDir.getName() + "/" + file.getName());// 相对地址，例jellyfish/jellfish-console
            }
        }
        Collections.sort(validProjectNames);
        return validProjectNames.toArray(new String[validProjectNames.size()]);
    }

    /**
     * 简单判断该文件是否为一个项目
     *
     * @param file 文件
     * @return true-是；false-否
     */
    private static boolean isValidProject(File file) {
        if (file.isDirectory()) {
            File[] targetFiles = file.listFiles(pathname -> {// 文件筛选器
                if (pathname.isFile() && pathname.getName().equals("pom.xml")) {
                    return true;//
                } else {
                    return false;
                }
            });
            return targetFiles != null && targetFiles.length == 1;// 筛选出下面有pom.xml的文件夹
        }
        return false;
    }
}
