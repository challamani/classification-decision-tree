package com.practice.spring_boot.decision_tree.c45.domain;

import java.util.List;

public class DecisionTree {
    private Integer level;
    private String label;
    private String parent;
    private List<DecisionTree> children;
    private Integer recordsCount;
    private String fileName;
    private String expandedIcon;
    private String collapsedIcon;

    public DecisionTree() {
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

    public List<DecisionTree> getChildren() {
        return children;
    }

    public void setChildren(List<DecisionTree> children) {
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
}
