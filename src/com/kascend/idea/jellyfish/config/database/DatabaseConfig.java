package com.kascend.idea.jellyfish.config.database;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 数据库配置信息。
 *
 * @author chenwei & hiyuno
 */
public class DatabaseConfig {

    /**
     * 数据库名称
     */
    private String name = "";

    /**
     * jdbc URL 全路径地址，例如：jdbc:mysql://192.168.16.176:3308/jellyfish_stat?useUnicode=true
     */
    private String jdbcUrl = "";

    /**
     * 数据库用户名
     */
    private String user = "";

    /**
     * 数据库密码
     */
    private String password = "";

    /**
     * 获得数据库名称。
     *
     * @return 数据库名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置数据库名称。
     *
     * @param name 数据库名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得 jdbc URL 全路径地址，例如：jdbc:mysql://192.168.16.176:3308/jellyfish_stat?useUnicode=true。
     *
     * @return jdbc URL 全路径地址
     */
    public String getJdbcUrl() {
        return jdbcUrl;
    }

    /**
     * 设置 jdbc URL 全路径地址。
     *
     * @param jdbcUrl jdbc URL 全路径地址
     */
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    /**
     * 获得数据库用户名。
     *
     * @return 数据库用户名
     */
    public String getUser() {
        return user;
    }

    /**
     * 设置数据库用户名。
     *
     * @param user 数据库用户名
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * 获得数据库密码。
     *
     * @return 数据库密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置数据库密码。
     *
     * @param password 数据库密码
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获得当前数据库配置信息对应的 jdbc property 属性 Map。
     *
     * @return jdbc property 属性 Map
     */
    public Map<String, String> getProperties() {
        Map<String, String> properties = new LinkedHashMap<>();
        String propertyPrefix = getName().replace("_", ".");
        properties.put(propertyPrefix + ".jdbc.url", getJdbcUrl());
        properties.put(propertyPrefix + ".jdbc.user", getUser());
        properties.put(propertyPrefix + ".jdbc.password", getPassword());
        return properties;
    }

    @Override
    public String toString() {
        return "DatabaseConfig{" +
                "name='" + name + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
