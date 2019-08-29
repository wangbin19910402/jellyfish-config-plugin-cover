package com.kascend.idea.jellyfish.config.project.format.support;

import java.io.File;
import java.util.HashMap;

import com.kascend.idea.jellyfish.config.maven.ArtifactInfoGroup;
import com.kascend.idea.jellyfish.config.maven.PomUtil;
import com.kascend.idea.jellyfish.config.project.format.ProjectFormat;
import com.kascend.idea.jellyfish.config.util.ConfigFileGenerator;

/**
 * 项目初始化（结构补充）处理抽象类
 *
 * @author junhui.si
 */
public abstract class AbstractProjectFormat implements ProjectFormat {

    @Override
    public boolean format(String projectBasePath) throws Exception {
        File pomFile = PomUtil.getPomFile(projectBasePath);
        if (pomFile == null) {
            return false;
        }
        // 解析pom文件中的项目依赖结构
        ArtifactInfoGroup artifactInfoGroup = PomUtil.parse(pomFile);
        // 当前项目名
        String projectName = artifactInfoGroup.getCurrent().getArtifactId();
        // 获取项目包名
        String packageName = PomUtil.getPackageName(artifactInfoGroup);

        // 模板替换变量Map
        HashMap<String, String> variablesMap = new HashMap<>(4);
        variablesMap.put("$projectName", projectName);
        variablesMap.put("$packageName", packageName);
        // 当前项目依赖关系的xml字符串
        variablesMap.put("$artifactInfo", PomUtil.getArtifactInfoValue(artifactInfoGroup));

        // 创建（覆盖原文件）pom文件
        ConfigFileGenerator.generate(getPomTemplatePath(), variablesMap, projectBasePath + "/pom.xml", true);

        // 创建包结构
        buildPackage(projectBasePath, packageName);

        //是否需要创建 Resources 相关文件
        if (needBuildResources()) {
            buildResources(projectBasePath, packageName, projectName);
        }

        buildSpecificResources(projectBasePath, packageName, projectName);
        return true;
    }

    /**
     * 获取pom文件模块地址
     *
     * @return pom文件模块地址
     */
    protected abstract String getPomTemplatePath();

    protected abstract void buildSpecificResources(String projectBasePath, String packageName, String projectName) throws Exception;

    protected boolean needBuildResources() {
        return true;
    }

    /**
     * 创建包结构
     *
     * @param projectBasePath 项目根路径
     * @param packageName     项目包名
     */
    private void buildPackage(String projectBasePath, String packageName) {
        String packageDirectoryPath = projectBasePath + "/src/main/java/" + packageName.replace(".", "/");
        File packageDirectory = new File(packageDirectoryPath);
        packageDirectory.mkdirs();
    }

    private void buildResources(String projectBasePath, String packageName, String projectName) throws Exception {
        String commonFilePath = projectBasePath + "/src/main/resources/common/context/common.xml";
        HashMap<String, String> variablesMap = new HashMap<>(4);
        variablesMap.put("$packageName", packageName);
        variablesMap.put("$projectName", projectName);
        variablesMap.put("$projectNamePropertyPrefix", projectName.replace("-", "."));
        ConfigFileGenerator.generate("/project/resources/common_template.xml", variablesMap, commonFilePath, true);

        String naiveNotifierPath = projectBasePath + "/src/main/resources/common/context/naive-notifier.xml";
        ConfigFileGenerator.generate("/project/resources/naive_notifier_template.xml", variablesMap, naiveNotifierPath, true);

        String log4jPath = projectBasePath + "/src/main/resources/common/log4j.properties";
        ConfigFileGenerator.generate("/project/resources/log4j_template.properties", variablesMap, log4jPath, true);

        String[] environments = new String[]{"beta", "dev", "local", "rc"};
        for (String environment : environments) {
            variablesMap.put("$environment", environment);
            String hostFilePath = projectBasePath + "/src/main/resources/" + environment + "/properties/host.properties";
            ConfigFileGenerator.generate("/project/resources/properties/host_template.properties", null, hostFilePath, true);

            String environmentFilePath = projectBasePath + "/src/main/resources/" + environment + "/properties/environment.properties";
            ConfigFileGenerator.generate("/project/resources/properties/environment_template.properties", variablesMap, environmentFilePath, true);
        }

        String environmentJavaFilePath = projectBasePath + "/src/main/java/" + packageName.replace(".", "/") + "/environment/EnvironmentDetector.java";
        ConfigFileGenerator.generate("/project/resources/EnvironmentDetector.java", variablesMap, environmentJavaFilePath, true);
    }

    /**
     * 创建web所需的资源文件
     *
     * @param projectBasePath 项目根路径
     * @param packageName     项目包名
     * @param projectName     项目名
     * @throws Exception
     */
    protected void buildWebResources(String projectBasePath, String packageName, String projectName) throws Exception {
        HashMap<String, String> variablesMap = new HashMap<>(4);
        variablesMap.put("$packageName", packageName);
        variablesMap.put("$projectName", projectName);

        String indexJspPath = projectBasePath + "/src/main/webapp/index.jsp";
        ConfigFileGenerator.generate("/project/web/index_template.jsp", variablesMap, indexJspPath, true);

        String webXmlPath = projectBasePath + "/src/main/webapp/WEB-INF/web.xml";
        ConfigFileGenerator.generate("/project/web/web_template.xml", variablesMap, webXmlPath, true);

        String dispatcherServletXmlPath = projectBasePath + "/src/main/webapp/WEB-INF/dispatcher-servlet.xml";
        ConfigFileGenerator.generate("/project/web/dispatcher_servlet_template.xml", variablesMap, dispatcherServletXmlPath, true);

        String velocityConfigFilePath = projectBasePath + "/src/main/webapp/WEB-INF/freemarker.properties";
        ConfigFileGenerator.generate("/project/web/freemarker_template.properties", variablesMap, velocityConfigFilePath, true);

        String velocityDirectoryPath = projectBasePath + "/src/main/webapp/WEB-INF/freemarker";
        new File(velocityDirectoryPath).mkdirs();

        String responseJavaFilePath = projectBasePath + "/src/main/java/" + packageName.replace(".", "/") + "/web/Response.java";
        ConfigFileGenerator.generate("/project/web/Response.java", variablesMap, responseJavaFilePath, true);

        String webConfigJavaFilePath = projectBasePath + "/src/main/java/" + packageName.replace(".", "/") + "/web/WebConfig.java";
        ConfigFileGenerator.generate("/project/web/WebConfig.java", variablesMap, webConfigJavaFilePath, true);

        String controllerJavaDirectoryPath = projectBasePath + "/src/main/java/" + packageName.replace(".", "/") + "/web/controller";
        new File(controllerJavaDirectoryPath).mkdirs();
    }
}
