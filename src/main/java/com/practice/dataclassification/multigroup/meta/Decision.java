package com.practice.dataclassification.multigroup.meta;

import java.util.Objects;

public class Decision {
    private String type;
    private String value;
    private String referenceNode;
    private String nodeLabel;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Decision decision = (Decision) o;
        return Objects.equals(type, decision.type) && Objects.equals(value, decision.value) && Objects.equals(referenceNode, decision.referenceNode) && Objects.equals(nodeLabel, decision.nodeLabel);
    }

    @Override
    public String toString() {
        return "Decision{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                ", referenceNode='" + referenceNode + '\'' +
                ", nodeLabel='" + nodeLabel + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, value, referenceNode, nodeLabel);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getReferenceNode() {
        return referenceNode;
    }

    public void setReferenceNode(String referenceNode) {
        this.referenceNode = referenceNode;
    }

    public String getNodeLabel() {
        return nodeLabel;
    }

    public void setNodeLabel(String nodeLabel) {
        this.nodeLabel = nodeLabel;
    }
}
