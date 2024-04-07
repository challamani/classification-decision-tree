package com.practice.dataclassification.multigroup.repository;

import com.google.gson.JsonObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommonDatasetRepository {

    private final Map<String, List<JsonObject>> datasetMap = new HashMap<>();

    public void save(String entityName, JsonObject record) {
        if (!datasetMap.containsKey(entityName)) {
            datasetMap.put(entityName, new ArrayList<>());
        }
        datasetMap.get(entityName).add(record);
    }

    public void saveAll(String entityName, List<JsonObject> records) {
        if (!datasetMap.containsKey(entityName)) {
            datasetMap.put(entityName, new ArrayList<>());
        }
        datasetMap.get(entityName).addAll(records);
    }

    public List<JsonObject> getDatasetByName(String datasetName) {
        return datasetMap.get(datasetName);
    }
}
