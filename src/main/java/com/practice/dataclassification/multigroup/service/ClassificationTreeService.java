package com.practice.dataclassification.multigroup.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.practice.dataclassification.multigroup.builder.Node;
import com.practice.dataclassification.multigroup.builder.ClassificationTreeStructure;
import com.practice.dataclassification.multigroup.builder.Condition;
import com.practice.dataclassification.multigroup.domain.ClassificationTree;
import com.practice.dataclassification.multigroup.meta.ClassificationNode;
import com.practice.dataclassification.multigroup.meta.ClassificationOption;
import com.practice.dataclassification.multigroup.meta.DatasetClassificationMeta;
import com.practice.dataclassification.multigroup.model.ClassificationTreeRequest;
import com.practice.dataclassification.multigroup.repository.CommonDatasetRepository;
import com.practice.dataclassification.multigroup.util.CommonUtil;
import com.practice.dataclassification.multigroup.util.DataSetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class ClassificationTreeService {

    private final ClassificationTreeNodeService classificationTreeNodeService;
    private final DatasetClassificationMetaReader datasetClassificationMetaReader;
    private final CommonDatasetRepository commonDatasetRepository;
    private ClassificationTreeStructure classificationTreeStructure;

    private Map<String, List<JsonObject>> createClassificationTreeNodes(ClassificationTreeRequest request) {

        List<JsonObject> dataset = commonDatasetRepository.getDatasetByName(request.datasetName());
        Map<String, ClassificationNode> stringClassificationNodeMap = classificationTreeNodeService.createClassificationTreeNodes(dataset,
                request.classificationOption(), request.datasetName());

        try {
            log.info("Classification tree nodes {}",new ObjectMapper().writeValueAsString(stringClassificationNodeMap));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        classificationTreeStructure = new ClassificationTreeStructure.ClassificationTreeBuilder()
                .setLevel(3)
                .setNodes(stringClassificationNodeMap)
                .build();

        DatasetClassificationMeta metadata = datasetClassificationMetaReader.getDatasetClassificationMetadata(request.datasetName());
        ClassificationOption classificationOption = metadata.classificationOptions().stream()
                .filter(option1 -> option1.name().equalsIgnoreCase(request.classificationOption()))
                .findFirst().orElse(null);

        Map<String, List<JsonObject>> dataNodeDataSet = new HashMap<>();
        dataset.forEach(record -> {
            String evaluationString = DataSetUtil.getEvalString(record, classificationOption.nodeLevels());
            String[] values = evaluationString.split("\\|");
            updateTreeWithInstanceValue(record, values, classificationTreeStructure, dataNodeDataSet);
        });
        return dataNodeDataSet;
    }

    private void  updateTreeWithInstanceValue(JsonObject record, String[] values,
                                              ClassificationTreeStructure classificationTreeStructure,
                                              Map<String,List<JsonObject>> dataNodeDataSet) {
        int index = 0;
        Node<JsonObject> rootNode = classificationTreeStructure.getRoot();
        Integer instanceCount = rootNode.getInstanceCount() == null ? 0 : rootNode.getInstanceCount() + 1;
        rootNode.setInstanceCount(instanceCount);
        rootNode.getPredicateNode().forEach((k, v) -> {
            String value = values[index];

            if (v.getPredicate().test(value)) {
                Node<JsonObject> targetNode = classificationTreeStructure.getRoot().getReferenceNode().get(k);
                updateLinkedTargetNodes(record, values, index + 1, targetNode, dataNodeDataSet);
            }
        });
    }


    private void updateLinkedTargetNodes(JsonObject record, String[] values,
                                         int index, Node<JsonObject> node,
                                         Map<String,List<JsonObject>> dataNodeDataSet) {

        Integer instanceCount = node.getInstanceCount() == null ? 1 : node.getInstanceCount() + 1;
        node.setInstanceCount(instanceCount);

        if (node.getNodeType(node).equalsIgnoreCase("data")) {
            if (!dataNodeDataSet.containsKey(node.getNodeName())) {
                dataNodeDataSet.put(node.getNodeName(), new ArrayList<>());
            }
            dataNodeDataSet.get(node.getNodeName()).add(record);
            return;
        }

        boolean noTargetNodeFound = true;
        for (Map.Entry<String, Condition> entry : node.getPredicateNode().entrySet()) {
            String value = values[index];
            Object convertValue = CommonUtil.convertValueType(entry.getValue().getType(), value);

            if (entry.getValue().getPredicate().test(convertValue)) {
                Node<JsonObject> targetNode = node.getReferenceNode().get(entry.getKey());
                updateLinkedTargetNodes(record, values, index + 1, targetNode, dataNodeDataSet);
                return;
            }
        }

        if (noTargetNodeFound) {
            log.info("no target nodes {} values {}, index {}", record, values, index);
        }
    }

    public ClassificationTree getClassificationTree(ClassificationTreeRequest classificationTreeRequest){
        createClassificationTreeNodes(classificationTreeRequest);
        return classificationTreeStructure.getDecisionTree();
    }
}
