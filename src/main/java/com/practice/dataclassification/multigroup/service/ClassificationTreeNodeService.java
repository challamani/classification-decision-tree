package com.practice.dataclassification.multigroup.service;

import com.google.gson.JsonObject;
import com.practice.dataclassification.multigroup.meta.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Slf4j
public class ClassificationTreeNodeService {

    private Map<String, ClassificationNode> classificationTreeNodes;
    private final DatasetClassificationMetaReader datasetClassificationMetaReader;

    @Autowired
    public ClassificationTreeNodeService(DatasetClassificationMetaReader datasetClassificationMetaReader){
        this.datasetClassificationMetaReader = datasetClassificationMetaReader;
        classificationTreeNodes = new HashMap<>();
    }

    private ClassificationNode createRootClassification() {
        ClassificationNode rootClassificationNode = new ClassificationNode();
        rootClassificationNode.setDecisions(new HashSet<>());
        rootClassificationNode.setDataType("string");
        rootClassificationNode.setType("ROOT");
        rootClassificationNode.setName("ROOT");
        classificationTreeNodes.put("ROOT",rootClassificationNode);
        return rootClassificationNode;
    }

    private void createClassificationTreeNodes(JsonObject record,
                                               ClassificationNode runningNode,
                                               DatasetClassificationMeta metadata,
                                               ClassificationOption classificationOption) {


        NodeLevel runningNodeLevel = classificationOption.nodeLevels().stream()
                .filter(node -> node.name().equalsIgnoreCase(runningNode.getName()))
                .findFirst().orElse(null);

        if (Objects.nonNull(runningNode)) {
            StringBuilder nodeName = new StringBuilder();
            AtomicReference<String> nextNodeAttribute = new AtomicReference<>();

            classificationOption.nodeLevels().stream()
                    .forEach(node -> {
                        if (node.executionId() <= runningNodeLevel.executionId()) {
                            nodeName.append(record.get(node.name()).getAsJsonPrimitive().getAsString()).append(".");
                        }
                        if (node.executionId() == runningNodeLevel.executionId() + 1) {
                            nextNodeAttribute.set(node.name());
                        }
                    });
            String nodeReference = nodeName.toString().substring(0, nodeName.toString().length() - 1);
            ClassificationNode treeNode = classificationTreeNodes.getOrDefault(nodeReference, new ClassificationNode());
            treeNode.setName(nodeReference);
            if (Objects.isNull(treeNode.getDecisions())) {
                treeNode.setDecisions(new HashSet<>());
            }
            classificationTreeNodes.putIfAbsent(treeNode.getName(), treeNode);

            ClassificationNode nextClassificationNode = metadata.classificationNodes()
                    .stream().filter(attribute1 -> attribute1.getName().equalsIgnoreCase(nextNodeAttribute.get()))
                    .findFirst().orElse(new ClassificationNode());

            if (Objects.nonNull(nextClassificationNode.getDecisions())) {
                treeNode.setType("parent");
                nextClassificationNode.getDecisions().stream().forEach(decision -> {
                    Decision treeNodeDecision = new Decision();
                    String suffixLabel = Objects.isNull(decision.getNodeLabel()) ? record.get(nextClassificationNode.getName()).getAsJsonPrimitive().getAsString() :
                            decision.getNodeLabel();

                    treeNodeDecision.setReferenceNode(nodeName.toString().concat(suffixLabel));
                    treeNodeDecision.setType(decision.getType());
                    String decisionValue = Objects.isNull(decision.getValue()) ? record.get(nextClassificationNode.getName()).getAsJsonPrimitive().getAsString() :
                            decision.getValue();
                    treeNodeDecision.setValue(decisionValue);
                    treeNode.getDecisions().add(treeNodeDecision);
                });
            }
        }

        if (runningNodeLevel.executionId() == 0) {
            runningNode.getDecisions().stream().forEach(decision -> {
                Decision treeNodeDecision = new Decision();
                treeNodeDecision.setReferenceNode(record.get(runningNode.getName()).getAsJsonPrimitive().getAsString());
                treeNodeDecision.setType(decision.getType());
                treeNodeDecision.setValue(record.get(runningNode.getName()).getAsJsonPrimitive().getAsString());
                classificationTreeNodes.get("ROOT").getDecisions().add(treeNodeDecision);
            });
        }
    }

    public Map<String, ClassificationNode> createClassificationTreeNodes(List<JsonObject> dataset,
                                                                         String datasetClassificationOption,
                                                                         String datasetName) {
        createRootClassification();
        DatasetClassificationMeta metadata = datasetClassificationMetaReader.getDatasetClassificationMetadata(datasetName);
        ClassificationOption classificationOption = metadata.classificationOptions().stream()
                .filter(option1 -> option1.name().equalsIgnoreCase(datasetClassificationOption))
                .findFirst().orElse(null);

        dataset.forEach(jsonObject -> {
            classificationOption.nodeLevels().forEach(nodeLevel -> {
                ClassificationNode runningNode = metadata.classificationNodes()
                        .stream().filter(node -> node.getName().equalsIgnoreCase(nodeLevel.name()))
                        .findFirst().orElse(null);
                createClassificationTreeNodes(jsonObject, runningNode, metadata, classificationOption);
            });
        });
        return classificationTreeNodes;
    }
}
