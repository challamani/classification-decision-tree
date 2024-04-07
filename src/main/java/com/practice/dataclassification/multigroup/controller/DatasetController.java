package com.practice.dataclassification.multigroup.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.practice.dataclassification.multigroup.repository.CommonDatasetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DatasetController {

    private final CommonDatasetRepository commonDatasetRepository;

    @PostMapping("/api/dataset/{datasetName}/record")
    public ResponseEntity<String> handlePostRequest(@PathVariable String datasetName, @RequestBody String jsonString) {
        JsonObject jsonObject =  new Gson().fromJson(jsonString, JsonObject.class);
        commonDatasetRepository.save(datasetName, jsonObject);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    @PostMapping("/api/dataset/{datasetName}/records")
    public ResponseEntity<String> saveRecords(@PathVariable String datasetName, @RequestBody String jsonString) {
        Type jsonObjectType = new TypeToken<List<JsonObject>>(){}.getType();
        List<JsonObject> jsonObjects =  new Gson().fromJson(jsonString, jsonObjectType);

        commonDatasetRepository.saveAll(datasetName, jsonObjects);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }
}
