package com.practice.dataclassification.multigroup.controller;

import com.google.gson.JsonObject;
import com.practice.dataclassification.multigroup.domain.ClassificationTree;
import com.practice.dataclassification.multigroup.domain.ClassificationTreeResult;
import com.practice.dataclassification.multigroup.model.ClassificationTreeRequest;
import com.practice.dataclassification.multigroup.repository.CommonDatasetRepository;
import com.practice.dataclassification.multigroup.service.ClassificationTreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ClassificationTreeController {

    private final ClassificationTreeService classificationTreeService;

    @GetMapping("/api/classificationTree/{datasetName}/{option}")
    public ResponseEntity<ClassificationTreeResult> handlePostRequest(@PathVariable String datasetName, @PathVariable String option) {

        ClassificationTreeRequest classificationTreeRequest = new ClassificationTreeRequest(datasetName,option);
        ClassificationTree classificationTree = classificationTreeService.getClassificationTree(classificationTreeRequest);
        ClassificationTreeResult classificationTreeResult = new ClassificationTreeResult();
        classificationTreeResult.setDecisionTree(classificationTree);
        classificationTreeResult.setResponseCode("0");
        classificationTreeResult.setResponseDescription("Success");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(classificationTreeResult);
    }
}
