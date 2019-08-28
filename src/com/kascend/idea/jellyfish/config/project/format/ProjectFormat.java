package com.kascend.idea.jellyfish.config.project.format;

/**
 * 项目格式化（结构补充）接口
 *
 * @author junhui.si
 */
public interface ProjectFormat {

    /**
     * 格式化（结构补充）接口
     *
     * @param projectBasePath 项目地址
     * @return 结果。true-成功；false-失败
     * @throws Exception
     */
    boolean format(String projectBasePath) throws Exception;
}
