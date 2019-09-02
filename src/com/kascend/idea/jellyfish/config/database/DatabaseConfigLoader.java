package com.kascend.idea.jellyfish.config.database;

import java.util.List;

/**
 * 数据库配置读取器。
 *
 * @author chenwei & hiyuno
 */
public interface DatabaseConfigLoader {

    /**
     * 获取所有已配置的数据库名称数组。
     *
     * @return 数据库名称数组
     */
    String[] getDatabaseNames();

    /**
     * 根据数据库名称获取对应的数据库配置信息，如果不存在，则返回 {@code null}
     *
     * @param databaseName 数据库名称
     * @return 数据库配置信息
     */
    DatabaseConfig get(String databaseName);

    /**
     * 获得所有的数据库配置信息列表，该方法不会返回 {@code null}。
     *
     * @return 数据库配置信息列表
     */
    List<DatabaseConfig> load();
}
