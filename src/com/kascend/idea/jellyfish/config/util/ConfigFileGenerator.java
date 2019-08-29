package com.kascend.idea.jellyfish.config.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目文件生成器
 * 根据模板即替换变量生成文件
 *
 * @author junhui.si
 */
public class ConfigFileGenerator {

    /**
     * 根据模板文件和替换变量生成项目配置文件
     *
     * @param templateFilePath 模板文件地址
     * @param variablesMap     替换变量map
     * @param targetFilePath   配置文件存储地址
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void generate(String templateFilePath, Map<String, String> variablesMap, String targetFilePath) throws IOException, URISyntaxException {
        generate(templateFilePath, variablesMap, targetFilePath, false);
    }

    /**
     * 根据模板文件和替换变量生成项目配置文件
     *
     * @param templateFilePath 模板文件地址
     * @param variablesMap     替换变量map
     * @param targetFilePath   配置文件存储地址
     * @param isOverride       是否覆盖已存在的配置文件
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void generate(String templateFilePath, Map<String, String> variablesMap, String targetFilePath, boolean isOverride) throws IOException {
        File targetFile = new File(targetFilePath);
        boolean isTargetFileExist = targetFile.exists();
        // 不覆盖文件
        if (isTargetFileExist && !isOverride) {
            return;
        }
        if (!isTargetFileExist) {
            File parent = targetFile.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }
            targetFile.createNewFile();
        }
        try (
                // 从ClassPath根路径下获取文件流
                InputStream in = ConfigFileGenerator.class.getResourceAsStream(templateFilePath);
                // 字符缓冲输出流
                BufferedWriter writer = Files.newBufferedWriter(targetFile.toPath(), StandardCharsets.UTF_8);
                // 字符缓冲读取流
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))
        ) {
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                lines.add(replaceVariables(line, variablesMap));
            }

            for (int i = 0; i < lines.size(); i++) {
                writer.write(lines.get(i));
                if (i < (lines.size() - 1)) {
                    writer.newLine();
                }
            }
        }
    }

    /**
     * 替换模板中的变量，并返回替换后的字符串
     *
     * @param lineContent  文件行内容
     * @param variablesMap 替换变量map
     * @return 替换后的字符串
     */
    private static String replaceVariables(String lineContent, Map<String, String> variablesMap) {
        if (variablesMap != null) {
            for (String variableName : variablesMap.keySet()) {
                String variableValue = variablesMap.get(variableName);
                lineContent = lineContent.replace(variableName, variableValue);
            }
        }
        return lineContent;
    }
}
