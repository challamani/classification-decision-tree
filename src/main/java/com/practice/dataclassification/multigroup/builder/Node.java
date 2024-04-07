package com.practice.dataclassification.multigroup.builder;

import java.util.List;
import java.util.Map;

public class Node<T> implements NodeType {

    private Map<String, Condition> predicateNode;
    private Map<String, Node<T>> referenceNode;
    private List<T> data;
    private Integer instanceCount;
    private String nodeName;
    private String dataSetLink;

    public String getDataSetLink() {
        return dataSetLink;
    }

    public void setDataSetLink(String dataSetLink) {
        this.dataSetLink = dataSetLink;
    }

    public Node(String name) {
        this.nodeName = name;
    }
    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Integer getInstanceCount() {
        return instanceCount;
    }

    public void setInstanceCount(Integer instanceCount) {
        this.instanceCount = instanceCount;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Map<String, Condition> getPredicateNode() {
        return predicateNode;
    }

    public void setPredicateNode(Map<String, Condition> predicateNode) {
        this.predicateNode = predicateNode;
    }

    public Map<String, Node<T>> getReferenceNode() {
        return referenceNode;
    }

    public void setReferenceNode(Map<String, Node<T>> referenceNode) {
        this.referenceNode = referenceNode;
    }

    @Override
    public String toString() {
        return "Node{" +
                "predicateNode=" + predicateNode +
                ", referenceNode=" + referenceNode +
                ", data=" + data +
                ", instanceCount=" + instanceCount +
                ", nodeName='" + nodeName + '\'' +
                '}';
    }
}
