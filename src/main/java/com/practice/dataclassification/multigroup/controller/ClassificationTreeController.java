package com.practice.dataclassification.multigroup.controller;

import com.practice.dataclassification.multigroup.model.ClassificationTreeNode;
import com.practice.dataclassification.multigroup.model.ClassificationTreeResult;
import com.practice.dataclassification.multigroup.model.ClassificationTreeRequest;
import com.practice.dataclassification.multigroup.model.FlatNode;
import com.practice.dataclassification.multigroup.service.ClassificationTreeService;
import com.practice.dataclassification.multigroup.util.ScriptConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ClassificationTreeController {

    private final ClassificationTreeService classificationTreeService;

    @GetMapping("/api/classificationTree/{datasetName}/{option}")
    public ResponseEntity<ClassificationTreeResult> handlePostRequest(@PathVariable String datasetName, @PathVariable String option) {

        ClassificationTreeRequest classificationTreeRequest = new ClassificationTreeRequest(datasetName,option);
        ClassificationTreeNode classificationTreeNode = classificationTreeService.getClassificationTree(classificationTreeRequest);
        ClassificationTreeResult classificationTreeResult = new ClassificationTreeResult();
        classificationTreeResult.setDecisionTree(classificationTreeNode);
        classificationTreeResult.setResponseCode("0");
        classificationTreeResult.setResponseDescription("Success");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(classificationTreeResult);
    }

    @GetMapping(value = "/api/classificationTree/{datasetName}/{option}/tree-view", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getClassificationTreeView(@PathVariable String datasetName, @PathVariable String option) {

        ClassificationTreeRequest classificationTreeRequest = new ClassificationTreeRequest(datasetName,option);
        ClassificationTreeNode classificationTreeNode = classificationTreeService.getClassificationTree(classificationTreeRequest);
        ClassificationTreeResult classificationTreeResult = new ClassificationTreeResult();
        classificationTreeResult.setDecisionTree(classificationTreeNode);


        StringBuilder stringBuilder = new StringBuilder("<ul id=\"myUL\">");
        stringBuilder.append("<li><span class=\"caret\">root</span>");
        stringBuilder.append("<ul class=\"nested\">");

        for (ClassificationTreeNode children : classificationTreeNode.getChildren()) {
            recursiveHtmlStringBuilder(children, stringBuilder);
        }
        stringBuilder.append("</ul></li></ul>");

        String htmlString =
                ScriptConstants.head.concat(stringBuilder.toString())
                .concat(ScriptConstants.script).concat(ScriptConstants.footer);
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlString);
    }

    private void recursiveHtmlStringBuilder(ClassificationTreeNode decisionTree, StringBuilder stringBuilder) {

        if (!CollectionUtils.isEmpty(decisionTree.getChildren()) && decisionTree.getChildren().size() > 0) {
            stringBuilder.append("<li><span class=\"caret\">" + decisionTree.getLabel() + "</span>");
            stringBuilder.append("<ul class=\"nested\">");
            for (ClassificationTreeNode child : decisionTree.getChildren()) {
                recursiveHtmlStringBuilder(child, stringBuilder);
            }
            stringBuilder.append("</ul></li>");
        } else {
            if (decisionTree.getFileName() != null) {
                String str = "<a href=\"http://localhost:8080//api/classification-data/1.0.0/download-dataset/" + decisionTree.getFileName() + "\">" + decisionTree.getLabel() + "</a>";
                stringBuilder.append("<li>" + str + "</li>");
            } else {
                stringBuilder.append("<li>" + decisionTree.getLabel() + "</li>");
            }
        }
    }

    @GetMapping("/api/classificationNodeRelation/{datasetName}/{option}")
    public ResponseEntity<List<FlatNode>> classificationNodeRelation(@PathVariable String datasetName, @PathVariable String option) {

        ClassificationTreeRequest classificationTreeRequest = new ClassificationTreeRequest(datasetName,option);
        List<FlatNode> flatClassificationNodes = classificationTreeService.getFlatClassificationNodeRelations(classificationTreeRequest);

       /* List<List<String>> resultList = flatClassificationNodes.stream()
                .map(item -> {
                    // Convert each item to a list of strings
                    List<String> stringList = Arrays.asList(item.name(), Objects.isNull(item.parentNode())?"": item.parentNode(),"");
                    return stringList;
                })
                .collect(Collectors.toList());*/

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(flatClassificationNodes);
    }
}
