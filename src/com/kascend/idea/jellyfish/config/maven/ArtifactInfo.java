package com.kascend.idea.jellyfish.config.maven;

/**
 * pom文件中jar包或者当前项目的坐标信息对象
 *
 * @author junhui.si
 */
public class ArtifactInfo {

    /**
     * 项目/jar包组织的唯一坐标
     */
    private String groupId = "";

    /**
     * 项目/jar包的唯一坐标
     */
    private String artifactId = "";

    /**
     * 项目/jar包版本号
     */
    private String version = "";

    public ArtifactInfo() {
    }

    public ArtifactInfo(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "ArtifactInfo{" +
                "groupId='" + groupId + '\'' +
                ", artifactId='" + artifactId + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
