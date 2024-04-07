package com.practice.dataclassification.multigroup.domain;

public class ClassificationTreeResult {
    private ClassificationTree classificationTree;
    private String responseCode;
    private String responseDescription;

    public ClassificationTreeResult() {
    }

    public ClassificationTreeResult(String responseCode, String responseDescription) {
        this.responseCode = responseCode;
        this.responseDescription = responseDescription;
    }

    public ClassificationTree getDecisionTree() {
        return classificationTree;
    }

    public void setDecisionTree(ClassificationTree classificationTree) {
        this.classificationTree = classificationTree;
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
