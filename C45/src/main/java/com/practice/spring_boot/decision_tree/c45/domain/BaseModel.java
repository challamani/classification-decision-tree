package com.practice.spring_boot.decision_tree.c45.domain;

import java.io.Serializable;

public class BaseModel implements Serializable {
    private String instanceId;

    public BaseModel() {
    }

    public BaseModel(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
