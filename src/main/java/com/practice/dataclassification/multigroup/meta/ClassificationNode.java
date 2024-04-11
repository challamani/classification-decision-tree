package com.practice.dataclassification.multigroup.meta;

import java.util.Set;

public class ClassificationNode {
    private Set<Decision> decisions;
    private String name;
    private String type;
    private String dataType;
    private String attribute;

    public Set<Decision> getDecisions() {
        return decisions;
    }

    public void setDecisions(Set<Decision> decisions) {
        this.decisions = decisions;
    }

    @Override
    public String toString() {
        return "ClassificationAttribute{" +
                "decisions=" + decisions +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDataType() {
        return dataType;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
