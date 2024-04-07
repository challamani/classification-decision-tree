package com.practice.dataclassification.multigroup.model;

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
