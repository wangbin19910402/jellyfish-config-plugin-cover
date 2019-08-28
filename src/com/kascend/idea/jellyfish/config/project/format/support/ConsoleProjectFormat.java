package com.kascend.idea.jellyfish.config.project.format.support;

/**
 * server项目初始化（结构补充）处理类
 *
 * @author junhui.si
 */
public class ConsoleProjectFormat extends AbstractProjectFormat {

    @Override
    protected String getPomTemplatePath() {
        return "/project/maven/web/pom_template.xml";
    }

    @Override
    protected void buildSpecificResources(String projectBasePath, String packageName, String projectName) throws Exception {
        buildWebResources(projectBasePath, packageName, projectName);
    }
}
