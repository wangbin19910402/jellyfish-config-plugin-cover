package com.kascend.idea.jellyfish.config.util;

import java.io.BufferedWriter;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * 日志配置文件工具
 *
 * @author junhui.si
 */
public class LogUtil {

    /**
     * 添加日志配置
     *
     * @param projectBasePath         module绝对路径
     * @param keyword                 log名
     * @param logConfigurationContent 配置内容
     * @throws Exception
     */
    public static void addLogConfiguration(String projectBasePath, String keyword, List<String> logConfigurationContent) throws Exception {
        String logFilePath = projectBasePath + "/src/main/resources/common/log4j.properties";
        File logFile = new File(logFilePath);
        if (logFile.exists()) {
            List<String> lines = Files.readAllLines(logFile.toPath(), StandardCharsets.UTF_8);
            boolean isConfigurationExisted = false;
            boolean needNewLine = false;
            try (BufferedWriter writer = Files.newBufferedWriter(logFile.toPath(), StandardCharsets.UTF_8)) {
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    if (line.contains(keyword)) {
                        // 配置是否已存在
                        isConfigurationExisted = true;
                    }
                    writer.write(line);
                    // 若最后一行为换行或空，那么在新建一行
                    // 若最后一行不为空，那么在新建两行
                    if (i < (lines.size() - 1)) {
                        writer.newLine();
                    } else {
                        needNewLine = !line.replace(" ", "").replace("\t", "").isEmpty();
                    }
                }

                if (!isConfigurationExisted) {
                    if (needNewLine) {
                        writer.newLine();
                    }
                    writer.newLine();
                    for (int i = 0; i < logConfigurationContent.size(); i++) {
                        writer.write(logConfigurationContent.get(i));
                        if (i < (logConfigurationContent.size() - 1)) {
                            writer.newLine();
                        }
                    }
                }
            }
        }
    }
}
