package com.kascend.idea.jellyfish.config.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 监控配置文件生成器
 *
 * @author junhui.si
 */
public class FalconConfigFileGenerator {

    private static final String FALCON_REPORTER_TEMPLATE_PATH = "/falcon/falconReporter_template.xml";

    /**
     * 添加监控配置
     *
     * @param projectBasePath module绝对路径
     * @param comment         注释
     * @param className       采集器className
     * @param constructorArgs 构造参数
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void addFalconDataCollector(String projectBasePath, String comment, String className, String... constructorArgs) throws IOException, URISyntaxException {
        List<String> beanConfigContents = getBeanConfigContents(className, constructorArgs);
        addFalconDataCollector(projectBasePath, comment, beanConfigContents);
    }

    /**
     * @param projectBasePath    module绝对路径
     * @param comment            注释
     * @param beanConfigContents bean配置列表
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void addFalconDataCollector(String projectBasePath, String comment, List<String> beanConfigContents) throws IOException, URISyntaxException {
        File configFile = getFalconConfigFile(projectBasePath);
        List<String> lines = Files.readAllLines(configFile.toPath(), StandardCharsets.UTF_8);
        int insertLineIndex = -1;
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("</util:list>")) {
                insertLineIndex = i;
            } else if (!comment.isEmpty() && line.contains(comment)) {
                insertLineIndex = -1;
                break;
            }
        }
        if (insertLineIndex > 0) {
            try (BufferedWriter writer = Files.newBufferedWriter(configFile.toPath(), StandardCharsets.UTF_8)) {
                for (int i = 0; i < lines.size(); i++) {
                    if (i == insertLineIndex) {
                        if (comment != null && !comment.isEmpty()) {
                            writer.newLine();
                            writer.write("        <!--" + comment + "-->");
                        }
                        writer.newLine();
                        for (String beanConfigContent : beanConfigContents) {
                            writer.write("        " + beanConfigContent);
                            writer.newLine();
                        }
                    }
                    writer.write(lines.get(i));
                    if (i < (lines.size() - 1)) {
                        writer.newLine();
                    }
                }
            }
        }
    }

    /**
     * 获取监控配置bean
     *
     * @param className       采集器className
     * @param constructorArgs 构造参数
     * @return
     */
    private static List<String> getBeanConfigContents(String className, String... constructorArgs) {
        if (constructorArgs.length == 0) {
            return Collections.singletonList("<bean class=\"" + className + "\" />");
        } else {
            List<String> contents = new ArrayList<>();
            contents.add("<bean class=\"" + className + "\">");
            for (int i = 0; i < constructorArgs.length; i++) {
                contents.add("    <constructor-arg index=\"" + i + "\" value=\"" + constructorArgs[i] + "\" />");
            }
            contents.add("</bean>");
            return contents;
        }
    }

    /**
     * 获取监控配置文件
     *
     * @param projectBasePath module绝对路径
     * @return 配置文件
     * @throws IOException
     * @throws URISyntaxException
     */
    private static File getFalconConfigFile(String projectBasePath) throws IOException, URISyntaxException {
        String falconFilePath = projectBasePath + "/src/main/resources/rc/context/falcon-reporter.xml";
        File falconFile = new File(falconFilePath);
        if (falconFile.exists()) {
            return falconFile;
        } else {
            ConfigFileGenerator.generate(FALCON_REPORTER_TEMPLATE_PATH, null, falconFilePath);
            return falconFile;
        }

    }
}
