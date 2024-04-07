package com.practice.dataclassification.multigroup.model;

public class ClassificationTreeResult {
    private ClassificationTreeNode classificationTreeNode;
    private String responseCode;
    private String responseDescription;

    public ClassificationTreeResult() {
    }

    public ClassificationTreeResult(String responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public ClassificationTreeNode getDecisionTree() {
        return classificationTreeNode;
    }

    public void setDecisionTree(ClassificationTreeNode classificationTreeNode) {
        this.classificationTreeNode = classificationTreeNode;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }
}
