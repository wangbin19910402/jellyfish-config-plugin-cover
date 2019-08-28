package com.kascend.idea.jellyfish.config.maven;

import java.util.HashMap;
import java.util.Map;

/**
 * pom文件项目依赖结构
 *
 * @author junhui.si
 */
public class ArtifactInfoGroup {

    /**
     * 当前项目（model）的坐标
     */
    private ArtifactInfo current = null;

    /**
     * 当前项目（project）的坐标
     */
    private ArtifactInfo parent = null;

    /**
     * maven依赖信息
     */
    private Map<String, ArtifactInfo> dependencyMap = new HashMap<>();

    public ArtifactInfo getCurrent() {
        return current;
    }

    public void setCurrent(ArtifactInfo current) {
        this.current = current;
    }

    public ArtifactInfo getParent() {
        return parent;
    }

    public void setParent(ArtifactInfo parent) {
        this.parent = parent;
    }

    public Map<String, ArtifactInfo> getDependencyMap() {
        return dependencyMap;
    }

    public void setDependencyMap(Map<String, ArtifactInfo> dependencyMap) {
        this.dependencyMap = dependencyMap;
    }

    @Override
    public String toString() {
        return "ArtifactInfoGroup{" +
                "current=" + current +
                ", parent=" + parent +
                ", dependencyMap=" + dependencyMap +
                '}';
    }
}
