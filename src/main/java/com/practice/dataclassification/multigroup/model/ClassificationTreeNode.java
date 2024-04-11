package com.practice.dataclassification.multigroup.model;

import java.util.List;

public class ClassificationTreeNode {
    private Integer level;
    private String name;
    private String label;
    private String parent;
    private List<ClassificationTreeNode> children;
    private Integer recordsCount;
    private String fileName;
    private String expandedIcon;
    private String collapsedIcon;
    private String attribute;

    public ClassificationTreeNode() {
        expandedIcon = "fa fa-folder-open";
        collapsedIcon="fa fa-folder";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getRecordsCount() {
        return recordsCount;
    }

    public void setRecordsCount(Integer recordsCount) {
        this.recordsCount = recordsCount;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<ClassificationTreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<ClassificationTreeNode> children) {
        this.children = children;
    }

    public String getExpandedIcon() {
        return expandedIcon;
    }

    public void setExpandedIcon(String expandedIcon) {
        this.expandedIcon = expandedIcon;
    }

    public String getCollapsedIcon() {
        return collapsedIcon;
    }

    public void setCollapsedIcon(String collapsedIcon) {
        this.collapsedIcon = collapsedIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
