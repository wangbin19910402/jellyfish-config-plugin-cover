package com.kascend.idea.jellyfish.config.database;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kascend.idea.jellyfish.config.maven.ArtifactInfo;
import com.kascend.idea.jellyfish.config.maven.PomUtil;
import com.kascend.idea.jellyfish.config.util.ConfigFileGenerator;
import com.kascend.idea.jellyfish.config.util.FalconConfigFileGenerator;
import com.kascend.idea.jellyfish.config.util.LogUtil;

/**
 * 数据库配置文件生成器
 *
 * @author junhui.si
 */
public class DatabaseConfigFileGenerator {

    private static final String SPRING_BEAN_CONFIG_TEMPLATE_PATH = "/database/datasourceBeanConfig_template.xml";

    private static final String COMMON_SPRING_BEAN_CONFIG_TEMPLATE_PATH = "/database/datasource_common_template.xml";

    private static final String SQL_MAP_CONFIG_TEMPLATE_PATH = "/database/sqlMapConfig_template.xml";

    private static final ArtifactInfo[] ORM_DEPENDENCY_ARRAY;

    private static final String LOG_KEYWORD = "MYSQL_JDBC_LOG";

    private static final List<String> SQL_LOG_CONTENT_LIST;

    static {
        ORM_DEPENDENCY_ARRAY = new ArtifactInfo[4];
        ORM_DEPENDENCY_ARRAY[0] = new ArtifactInfo("org.springframework", "spring-orm", "5.1.5.RELEASE");
        ORM_DEPENDENCY_ARRAY[1] = new ArtifactInfo("org.mybatis", "mybatis-spring", "2.0.2");
        ORM_DEPENDENCY_ARRAY[2] = new ArtifactInfo("org.mybatis", "mybatis", "3.5.2");
        ORM_DEPENDENCY_ARRAY[3] = new ArtifactInfo("com.heimuheimu", "mysql-jdbc", "1.0-SNAPSHOT");

        SQL_LOG_CONTENT_LIST = new ArrayList<>();
        SQL_LOG_CONTENT_LIST.add("#MySQLJDBC根日志");
        SQL_LOG_CONTENT_LIST.add("log4j.logger.com.heimuheimu.mysql=WARN,MYSQL_JDBC_LOG");
        SQL_LOG_CONTENT_LIST.add("log4j.additivity.com.heimuheimu.mysql=false");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_JDBC_LOG=org.apache.log4j.DailyRollingFileAppender");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_JDBC_LOG.file=${log.output.directory}/mysql-jdbc/mysql-jdbc.log");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_JDBC_LOG.encoding=UTF-8");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_JDBC_LOG.DatePattern=_yyyy-MM-dd");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_JDBC_LOG.layout=org.apache.log4j.PatternLayout");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_JDBC_LOG.layout.ConversionPattern=%d{ISO8601}%-5p[%F:%L]:%m%n");
        SQL_LOG_CONTENT_LIST.add("");
        SQL_LOG_CONTENT_LIST.add("#MySQL连接信息日志");
        SQL_LOG_CONTENT_LIST.add("log4j.logger.MYSQL_CONNECTION_LOG=INFO,MYSQL_CONNECTION_LOG");
        SQL_LOG_CONTENT_LIST.add("log4j.additivity.MYSQL_CONNECTION_LOG=false");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_CONNECTION_LOG=org.apache.log4j.DailyRollingFileAppender");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_CONNECTION_LOG.file=${log.output.directory}/mysql-jdbc/connection.log");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_CONNECTION_LOG.encoding=UTF-8");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_CONNECTION_LOG.DatePattern=_yyyy-MM-dd");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_CONNECTION_LOG.layout=org.apache.log4j.PatternLayout");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_CONNECTION_LOG.layout.ConversionPattern=%d{ISO8601}%-5p:%m%n");
        SQL_LOG_CONTENT_LIST.add("");
        SQL_LOG_CONTENT_LIST.add("#MySQL慢查日志，打印执行时间过慢的操作");
        SQL_LOG_CONTENT_LIST.add("log4j.logger.MYSQL_SLOW_EXECUTION_LOG=INFO,MYSQL_SLOW_EXECUTION_LOG");
        SQL_LOG_CONTENT_LIST.add("log4j.additivity.MYSQL_SLOW_EXECUTION_LOG=false");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_SLOW_EXECUTION_LOG=org.apache.log4j.DailyRollingFileAppender");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_SLOW_EXECUTION_LOG.file=${log.output.directory}/mysql-jdbc/slow_execution.log");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_SLOW_EXECUTION_LOG.encoding=UTF-8");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_SLOW_EXECUTION_LOG.DatePattern=_yyyy-MM-dd");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_SLOW_EXECUTION_LOG.layout=org.apache.log4j.PatternLayout");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_SLOW_EXECUTION_LOG.layout.ConversionPattern=%d{ISO8601}:%m%n");
        SQL_LOG_CONTENT_LIST.add("");
        SQL_LOG_CONTENT_LIST.add("#调用MySQLJDBC不支持的方法错误日志");
        SQL_LOG_CONTENT_LIST.add("log4j.logger.SQL_FEATURE_NOT_SUPPORTED_LOG=ERROR,SQL_FEATURE_NOT_SUPPORTED_LOG");
        SQL_LOG_CONTENT_LIST.add("log4j.additivity.SQL_FEATURE_NOT_SUPPORTED_LOG=false");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.SQL_FEATURE_NOT_SUPPORTED_LOG=org.apache.log4j.DailyRollingFileAppender");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.SQL_FEATURE_NOT_SUPPORTED_LOG.file=${log.output.directory}/mysql-jdbc/sql_feature_not_supported.log");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.SQL_FEATURE_NOT_SUPPORTED_LOG.encoding=UTF-8");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.SQL_FEATURE_NOT_SUPPORTED_LOG.DatePattern=_yyyy-MM-dd");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.SQL_FEATURE_NOT_SUPPORTED_LOG.layout=org.apache.log4j.PatternLayout");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.SQL_FEATURE_NOT_SUPPORTED_LOG.layout.ConversionPattern=%d{ISO8601}:%m%n");
        SQL_LOG_CONTENT_LIST.add("");
        SQL_LOG_CONTENT_LIST.add("#SQL执行DEBUG日志（请勿在生产环境中开启）");
        SQL_LOG_CONTENT_LIST.add("log4j.logger.MYSQL_EXECUTION_DEBUG_LOG=DEBUG,MYSQL_EXECUTION_DEBUG_LOG");
        SQL_LOG_CONTENT_LIST.add("log4j.additivity.MYSQL_EXECUTION_DEBUG_LOG=false");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_EXECUTION_DEBUG_LOG=org.apache.log4j.DailyRollingFileAppender");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_EXECUTION_DEBUG_LOG.file=${log.output.directory}/mysql-jdbc/sql_execution_debug.log");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_EXECUTION_DEBUG_LOG.encoding=UTF-8");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_EXECUTION_DEBUG_LOG.DatePattern=_yyyy-MM-dd");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_EXECUTION_DEBUG_LOG.layout=org.apache.log4j.PatternLayout");
        SQL_LOG_CONTENT_LIST.add("log4j.appender.MYSQL_EXECUTION_DEBUG_LOG.layout.ConversionPattern=%d{ISO8601}:%m%n");
    }

    /**
     * 生成数据库配置文件
     *
     * @param projectBasePath     项目跟路径（绝对路径）
     * @param config              数据库配置
     * @param isFrequently        是否调用频繁
     * @param includeFalconConfig 是否生成 Falcon 配置文件
     * @param projectName         项目名
     * @throws Exception
     */
    public static void generate(String projectBasePath, DatabaseConfig config, boolean isFrequently, boolean includeFalconConfig, String projectName) throws Exception {
        // 生成数据库操作bean
        generateSpringBeanConfigFile(projectBasePath, config, isFrequently, projectName);
        // 生成sqlMap配置文件
        generateSqlMapConfigFile(projectBasePath, config);

        if (includeFalconConfig) {
            String comment = "数据库监控：" + config.getName();
            FalconConfigFileGenerator.addFalconDataCollector(projectBasePath, comment,
                    "com.heimuheimu.mysql.jdbc.monitor.falcon.DatabaseDataCollector",
                    config.getJdbcUrl().substring(0, config.getJdbcUrl().indexOf("?")), getMonitorDbName(config.getName()));
        }

        // 添加数据库对应的maven依赖
        PomUtil.addDependency(projectBasePath, "数据库访问依赖", ORM_DEPENDENCY_ARRAY);
        // 添加com.heimuheimu/mysql-jdbc的相关日志
        LogUtil.addLogConfiguration(projectBasePath, LOG_KEYWORD, SQL_LOG_CONTENT_LIST);
    }

    /**
     * 生成数据库操作bean xml
     *
     * @param projectBasePath 项目跟路径（绝对路径）
     * @param config          数据库配置
     * @param isFrequently    是否调用频繁
     * @param projectName     项目名
     * @throws IOException
     * @throws URISyntaxException
     */
    private static void generateSpringBeanConfigFile(String projectBasePath, DatabaseConfig config, boolean isFrequently, String projectName) throws IOException, URISyntaxException {
        // 生成通用数据库配置bean
        Map<String, String> variablesMap = new HashMap<>(2);
        variablesMap.put("$yourProjectName", projectName);

        String commonConfigFilePath = projectBasePath + "/src/main/resources/common/context/datasource/datasource-common.xml";
        ConfigFileGenerator.generate(COMMON_SPRING_BEAN_CONFIG_TEMPLATE_PATH, variablesMap, commonConfigFilePath);

        // 生成相应数据库的配置bean
        String dbName = config.getName();
        // 驼峰规则的数据库名
        String dbVariableName = getDatabaseVariableName(dbName);
        variablesMap = new HashMap<>(10);
        variablesMap.put("$dataSourceBeanName", dbVariableName + "DataSource");
        variablesMap.put("$dataSourceConfiguration", isFrequently ? "highFrequentlyDataSourceConfiguration" : "lowFrequentlyDataSourceConfiguration");
        variablesMap.put("$dbPropertyPrefix", dbName.replace("_", "."));
        variablesMap.put("$sqlSessionFactoryBeanName", dbVariableName + "SqlSessionFactory");
        variablesMap.put("$dbName", dbName);
        variablesMap.put("$sqlSessionTemplateBeanName", dbVariableName + "SqlSessionTemplate");
        variablesMap.put("$monitorDbName", getMonitorDbName(dbName));

        String configFilePath = projectBasePath + "/src/main/resources/common/context/datasource/" + dbName + ".xml";
        ConfigFileGenerator.generate(SPRING_BEAN_CONFIG_TEMPLATE_PATH, variablesMap, configFilePath);
    }

    /**
     * 生成sqlMap配置文件
     *
     * @param projectBasePath 项目跟路径（绝对路径）
     * @param config          数据库配置
     * @throws IOException
     * @throws URISyntaxException
     */
    private static void generateSqlMapConfigFile(String projectBasePath, DatabaseConfig config) throws IOException, URISyntaxException {
        String dbName = config.getName();
        String targetFilePath = projectBasePath + "/src/main/resources/common/sqlmaps/" + dbName + "/sqlMapConfig.xml";
        ConfigFileGenerator.generate(SQL_MAP_CONFIG_TEMPLATE_PATH, null, targetFilePath);
    }

    /**
     * 将数据库名称转换为驼峰形式的变量名。
     *
     * @param databaseName 数据库名
     * @return 驼峰形式的变量名
     */
    private static String getDatabaseVariableName(String databaseName) {
        String[] dbNameParts = databaseName.split("_");
        String dbVariableName = dbNameParts[0];
        for (int i = 1; i < dbNameParts.length; i++) {
            dbVariableName += Character.toUpperCase(dbNameParts[i].charAt(0)) + dbNameParts[i].substring(1);
        }
        return dbVariableName;
    }

    /**
     * 获得用于监控的数据库名称。
     *
     * @return 用于监控的数据库名称
     */
    private static String getMonitorDbName(String databaseName) {
        databaseName = databaseName.replace("jellyfish_", "");
        if (!databaseName.contains("slave")) {
            databaseName += "_master";
        }
        return databaseName;
    }
}
