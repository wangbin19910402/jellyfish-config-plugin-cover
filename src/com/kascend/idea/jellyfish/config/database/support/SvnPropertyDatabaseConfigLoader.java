package com.kascend.idea.jellyfish.config.database.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kascend.idea.jellyfish.config.database.DatabaseConfig;
import com.kascend.idea.jellyfish.config.database.DatabaseConfigLoader;

/**
 * 从 properties 文件中读取数据库配置信息。
 *
 * @author chenwei & hiyuno
 */
public class SvnPropertyDatabaseConfigLoader implements DatabaseConfigLoader {

    //    private static final String JDBC_PROPERTY_SVN_URL = "http://svn.kascend-inc.com/svn/jellyfish-config/trunk/src/main/resources/dev/properties/jdbc.properties";
    private static final String JDBC_PROPERTY_SVN_URL = "http://svn.kascend-inc.com/svn/jellyfish-tools/trunk/jellyfish-tools-console/src/main/resources/dev/properties/jdbc-demo.properties";

    private static final Logger LOGGER = LoggerFactory.getLogger(SvnPropertyDatabaseConfigLoader.class);

    private final Map<String, DatabaseConfig> databaseConfigMap;

    /**
     * 读取数据库配置信息
     *
     * @throws IOException
     * @throws IllegalArgumentException
     */
    public SvnPropertyDatabaseConfigLoader() throws IOException, IllegalArgumentException {
        databaseConfigMap = new LinkedHashMap<>();
        Process process = null;
        InputStream in = null;
        try {
            process = Runtime.getRuntime().exec("svn cat " + JDBC_PROPERTY_SVN_URL);
            in = process.getInputStream();
            Properties po = new Properties();
            po.load(in);
            Enumeration<String> keys = (Enumeration<String>) po.propertyNames();
            while (keys.hasMoreElements()) {
                String key = keys.nextElement();
                String databaseName = getDatabaseName(key);
                DatabaseConfig config = databaseConfigMap.get(databaseName);
                if (config == null) {
                    config = new DatabaseConfig();
                    config.setName(databaseName);
                    databaseConfigMap.put(databaseName, config);
                }

                String value = po.getProperty(key).trim();
                if (key.endsWith(".jdbc.url")) {
                    config.setJdbcUrl(value);
                } else if (key.endsWith(".jdbc.user")) {
                    config.setUser(value);
                } else if (key.endsWith(".jdbc.password")) {
                    config.setPassword(value);
                } else {
                    LOGGER.error("Unrecognized jdbc property: `" + key + "`.");
                    throw new IllegalArgumentException("Unrecognized jdbc property: `" + key + "`.");
                }
            }
        } finally {
            if (process != null) {
                process.destroy();
            }
            if (in != null) {
                in.close();
            }
        }

    }

    @Override
    public String[] getDatabaseNames() {
        ArrayList<String> databaseNames = new ArrayList<>(databaseConfigMap.keySet());
        Collections.sort(databaseNames);
        return databaseNames.toArray(new String[0]);
    }

    @Override
    public DatabaseConfig get(String databaseName) {
        return databaseConfigMap.get(databaseName);
    }

    @Override
    public List<DatabaseConfig> load() {
        return new ArrayList<>(databaseConfigMap.values());
    }

    /**
     * 通过properties中的key获取数据库名
     *
     * @param key
     * @return 数据库名
     */
    private String getDatabaseName(String key) {
        String databaseName = "";
        int index = key.indexOf(".jdbc.");
        if (index > 0) {
            databaseName = key.substring(0, index).replace(".", "_");
        }
        if (databaseName.isEmpty()) {
            LOGGER.error("Could not parse database name from jdbc property: `" + key + "`.");
            throw new IllegalArgumentException("Could not parse database name from jdbc property: `" + key + "`.");
        }
        return databaseName;
    }
}
