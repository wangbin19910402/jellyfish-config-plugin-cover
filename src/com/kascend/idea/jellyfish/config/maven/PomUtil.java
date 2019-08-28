package com.kascend.idea.jellyfish.config.maven;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * pom文件操作工具类
 *
 * @author junhui.si
 */
public class PomUtil {

    public static void addDependency(String projectBasePath, String comment, ArtifactInfo... dependencyArtifactInfoArray) throws Exception {
        if (dependencyArtifactInfoArray.length > 0) {
            LinkedHashMap<String, ArtifactInfo> dependencyInfoMap = new LinkedHashMap<>();
            for (ArtifactInfo dependencyInfo : dependencyArtifactInfoArray) {
                dependencyInfoMap.put(dependencyInfo.getArtifactId(), dependencyInfo);
            }

            File pomFile = getPomFile(projectBasePath);
            if (pomFile != null) {
                ArtifactInfoGroup artifactInfoGroup = parse(pomFile);
                for (String existedDependencyArtifactId : artifactInfoGroup.getDependencyMap().keySet()) {
                    dependencyInfoMap.remove(existedDependencyArtifactId);
                }

                List<String> lines = Files.readAllLines(pomFile.toPath(), StandardCharsets.UTF_8);

                if (!dependencyInfoMap.isEmpty()) {
                    boolean alreadyInserted = false;
                    try (BufferedWriter writer = Files.newBufferedWriter(pomFile.toPath(), StandardCharsets.UTF_8)) {
                        for (int i = 0; i < lines.size(); i++) {
                            String line = lines.get(i);
                            if (line.contains("</dependencies>")) {
                                writeDependencyInfoMap(writer, comment, dependencyInfoMap);
                                alreadyInserted = true;
                            } else if (line.contains("</project>") && !alreadyInserted) {
                                writer.newLine();
                                writer.write("    <dependencies>");
                                writeDependencyInfoMap(writer, comment, dependencyInfoMap);
                                writer.write("    </dependencies>");
                                writer.newLine();
                            }
                            writer.write(lines.get(i));
                            if (i < (lines.size() - 1)) {
                                writer.newLine();
                            }
                        }
                    }
                }
            }
        }
    }

    private static void writeDependencyInfoMap(BufferedWriter writer, String comment, Map<String, ArtifactInfo> dependencyInfoMap) throws IOException {
        if (comment != null && !comment.isEmpty()) {
            writer.newLine();
            writer.write("        <!--" + comment + "-->");
        }
        writer.newLine();
        for (String artifactId : dependencyInfoMap.keySet()) {
            ArtifactInfo info = dependencyInfoMap.get(artifactId);
            writer.write("        <dependency>");
            writer.newLine();
            writer.write("            <groupId>" + info.getGroupId() + "</groupId>");
            writer.newLine();
            writer.write("            <artifactId>" + info.getArtifactId() + "</artifactId>");
            writer.newLine();
            writer.write("            <version>" + info.getVersion() + "</version>");
            writer.newLine();
            writer.write("        </dependency>");
            writer.newLine();
        }
    }

    /**
     * 获取pom文件对象
     *
     * @param projectBasePath 项目地址
     * @return pom文件对象
     */
    public static File getPomFile(String projectBasePath) {
        String pomFilePath = projectBasePath + "/pom.xml";
        File pomFile = new File(pomFilePath);
        if (pomFile.exists()) {
            return pomFile;
        } else {
            return null;
        }
    }

    /**
     * 解析pom文件中的项目依赖结构
     *
     * @param pomFile pom文件对象
     * @return 项目依赖及当前项目坐标信息
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public static ArtifactInfoGroup parse(File pomFile) throws ParserConfigurationException, IOException, SAXException {
        Document pomDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pomFile);
        ArtifactInfoGroup group = new ArtifactInfoGroup();
        // 获取pom文件 project节点
        Node projectNode = pomDocument.getElementsByTagName("project").item(0);
        // 获取当前项目唯一标识信息
        group.setCurrent(parse(projectNode));

        NodeList childNodes = projectNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if ("parent".equals(childNode.getNodeName())) {
                group.setParent(parse(childNode));
            } else if ("dependencies".equals(childNode.getNodeName())) {
                group.setDependencyMap(parseDependencies(childNode));
            }
        }
        return group;
    }

    /**
     * 解析 jar包依赖坐标
     *
     * @param dependenciesNode pom文件中的依赖(dependencies)子节点列表
     * @return key:jar包artifactId;value:该jar包依赖坐标详情
     */
    private static Map<String, ArtifactInfo> parseDependencies(Node dependenciesNode) {
        Map<String, ArtifactInfo> dependencyMap = new LinkedHashMap<>();
        NodeList childNodes = dependenciesNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if ("dependency".equals(childNode.getNodeName())) {
                ArtifactInfo artifactInfo = parse(childNode);
                dependencyMap.put(artifactInfo.getArtifactId(), artifactInfo);
            }
        }
        return dependencyMap;
    }

    /**
     * 解析目标节点的坐标信息
     *
     * @param targetNode 待解析的目标节点
     * @return jar包或者项目的坐标信息
     */
    private static ArtifactInfo parse(Node targetNode) {
        ArtifactInfo artifactInfo = new ArtifactInfo();
        NodeList childNodes = targetNode.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if ("artifactId".equals(childNode.getNodeName())) {
                artifactInfo.setArtifactId(childNode.getTextContent());
            } else if ("groupId".equals(childNode.getNodeName())) {
                artifactInfo.setGroupId(childNode.getTextContent());
            } else if ("version".equals(childNode.getNodeName())) {
                artifactInfo.setVersion(childNode.getTextContent());
            }
        }
        return artifactInfo;
    }

    /**
     * 获取项目包名（根据pom文件中groupId和artifactId生成）
     *
     * @param artifactInfoGroup pom文件项目依赖结构
     * @return 项目包名
     */
    public static String getPackageName(ArtifactInfoGroup artifactInfoGroup) {
        String projectName = artifactInfoGroup.getCurrent().getArtifactId();
        projectName = projectName.replace("-server", "");
        projectName = projectName.replace("-rpc", "");

        String groupId = artifactInfoGroup.getCurrent().getGroupId();
        if (groupId == null || groupId.isEmpty()) {
            if (artifactInfoGroup.getParent() != null) {
                groupId = artifactInfoGroup.getParent().getGroupId();
            }
        }

        return groupId + "." + projectName.replace("-", ".");
    }

    /**
     * 将当前项目的依赖结构转化成xml字符串
     *
     * @param artifactInfoGroup pom文件依赖结构对象
     * @return xml字符串
     * @throws Exception
     */
    public static String getArtifactInfoValue(ArtifactInfoGroup artifactInfoGroup) throws Exception {
        StringBuilder buffer = new StringBuilder();
        ArtifactInfo parent = artifactInfoGroup.getParent();
        String lineSeparator = System.lineSeparator();
        if (parent != null) {
            buffer.append("    <parent>").append(lineSeparator);
            buffer.append("        <artifactId>").append(parent.getArtifactId()).append("</artifactId>").append(lineSeparator);
            buffer.append("        <groupId>").append(parent.getGroupId()).append("</groupId>").append(lineSeparator);
            buffer.append("        <version>").append(parent.getVersion()).append("</version>").append(lineSeparator);
            buffer.append("    </parent>").append(lineSeparator).append(lineSeparator);
        }

        ArtifactInfo current = artifactInfoGroup.getCurrent();
        buffer.append("    <artifactId>").append(current.getArtifactId()).append("</artifactId>").append(lineSeparator);
        if (current.getGroupId() != null && !current.getGroupId().isEmpty()) {
            buffer.append("    <groupId>").append(current.getGroupId()).append("</groupId>").append(lineSeparator);
        }
        if (current.getVersion() != null && !current.getVersion().isEmpty()) {
            buffer.append("    <version>").append(current.getVersion()).append("</version>").append(lineSeparator);
        }
        return buffer.toString();
    }
}
