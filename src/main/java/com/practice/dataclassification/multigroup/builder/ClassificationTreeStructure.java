package com.practice.dataclassification.multigroup.builder;

import com.google.gson.JsonObject;
import com.practice.dataclassification.multigroup.domain.ClassificationTree;
import com.practice.dataclassification.multigroup.meta.ClassificationNode;
import com.practice.dataclassification.multigroup.meta.Decision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

public class ClassificationTreeStructure {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationTreeStructure.class);

    private Node<JsonObject> root;

    private ClassificationTreeStructure(ClassificationTreeBuilder decisionTreeBuilder) {
        initClassificationTree(decisionTreeBuilder.nodes);
    }

    private void initClassificationTree(Map<String, ClassificationNode> nodeMap){
         initRootNode(nodeMap);
         initChildNodes(root,nodeMap);
    }

    private void initRootNode(Map<String, ClassificationNode> nodeMap) {
        ClassificationNode rootClassificationNode = nodeMap.get("ROOT");
        root = new Node<>("ROOT");
        Map<String, Condition> predicateNode = new HashMap<>();
        Map<String, Node<JsonObject>> referenceNode = new HashMap<>();
        for (Decision decision : rootClassificationNode.getDecisions()) {
            predicateNode.put(decision.getReferenceNode(), new Condition.ConditionBuilder()
                    .setType(decision.getType())
                    .setValue(decision.getValue()).build());
            referenceNode.put(decision.getReferenceNode(), new Node<>(decision.getReferenceNode()));
        }
        root.setPredicateNode(predicateNode);
        root.setReferenceNode(referenceNode);
    }

    private void initChildNodes(Node<JsonObject> node, Map<String, ClassificationNode> nodeMap) {
        if(!CollectionUtils.isEmpty(node.getReferenceNode())) {
            node.getReferenceNode().forEach((k, v) -> {
                ClassificationNode decisionNode = nodeMap.get(v.getNodeName());

                if (Objects.nonNull(decisionNode) && !CollectionUtils.isEmpty(decisionNode.getDecisions())) {
                    Map<String, Condition> predicateNode = new HashMap<>();
                    Map<String, Node<JsonObject>> referenceNode = new HashMap<>();
                    for (Decision decision : decisionNode.getDecisions()) {
                        predicateNode.put(decision.getReferenceNode(), new Condition.ConditionBuilder()
                                .setType(decision.getType()).setValue(decision.getValue()).build());
                        referenceNode.put(decision.getReferenceNode(), new Node<>(decision.getReferenceNode()));
                    }
                    v.setPredicateNode(predicateNode);
                    v.setReferenceNode(referenceNode);
                }else if(Objects.nonNull(decisionNode) && Objects.isNull(decisionNode.getDecisions())){
                    decisionNode.setType("data");
                }
            });

            node.getReferenceNode().forEach((k, v) -> {
                initChildNodes(v, nodeMap);
            });
        }
    }

    public void renderDecisionTree(){
        logger.info("node [{}] instances [{}] ",root.getNodeName(),root.getInstanceCount());
        String tab="|_____";
        logger.info("{}",tab);
        root.getReferenceNode().forEach((k,v) -> {
            recursiveRender(2,v);
        });
    }

    private void recursiveRender(int noOfTabs, Node<JsonObject> node) {
        String appender = "";
        for (int i = 0; i < noOfTabs; i++) {
            appender += "   ";
        }
        String[] targetNames = node.getNodeName().split("\\.");
        String displayName = targetNames[targetNames.length - 1];
        logger.info("{} |",appender);
        logger.info("{} |",appender);
        logger.info("{} |___Node [{}] instances [{}]", appender, displayName, node.getInstanceCount());

        if (!CollectionUtils.isEmpty(node.getReferenceNode())) {
            for (Map.Entry<String, Node<JsonObject>> entrySet : node.getReferenceNode().entrySet()) {
                if (entrySet.getValue().getInstanceCount() != null)
                    recursiveRender(noOfTabs + 1, entrySet.getValue());
            }
        }
    }

    public ClassificationTree getDecisionTree() {

        ClassificationTree classificationTree = new ClassificationTree();
        classificationTree.setLabel("ROOT");
        classificationTree.setName("ROOT");
        classificationTree.setLevel(0);
        classificationTree.setChildren(new ArrayList<>());
        root.getReferenceNode().forEach((k, v) -> {
            recursiveChildNode(1, v, classificationTree.getChildren());
        });
        return classificationTree;
    }

    public ClassificationTree getDecisionTree(Map<String,String> displayNamesMap) {

        ClassificationTree classificationTree = new ClassificationTree();
        classificationTree.setLabel("ROOT");
        classificationTree.setLevel(0);
        classificationTree.setChildren(new ArrayList<>());
        root.getReferenceNode().forEach((k, v) -> {
            recursiveChildNode(1, v, classificationTree.getChildren(),displayNamesMap);
        });
        return classificationTree;
    }

    private void recursiveChildNode(int index, Node<JsonObject> node, List<ClassificationTree> children) {

        if(Objects.isNull(node.getInstanceCount())) {
            return;
        }

        Integer lastIndexOfSeparator = node.getNodeName().lastIndexOf('.');
        String[] targetNames = node.getNodeName().split("\\.");
        String dataNodeName = targetNames[targetNames.length - 1];
        ClassificationTree classificationTree = new ClassificationTree();

        String name = dataNodeName+" ("+(node.getInstanceCount()!=null?node.getInstanceCount():0)+")";
        classificationTree.setLabel(name);
        classificationTree.setLevel(index);
        classificationTree.setName(node.getNodeName());
        classificationTree.setParent(targetNames.length==1?"ROOT":node.getNodeName().toString().substring(0,lastIndexOfSeparator));
        classificationTree.setRecordsCount((node.getInstanceCount()!=null?node.getInstanceCount():0));
        children.add(classificationTree);
        classificationTree.setChildren(new ArrayList<>());
        if (!CollectionUtils.isEmpty(node.getReferenceNode())) {
            for (Map.Entry<String, Node<JsonObject>> entrySet : node.getReferenceNode().entrySet()) {
                if (entrySet.getValue().getInstanceCount() != null) {
                    recursiveChildNode(index + 1, entrySet.getValue(), classificationTree.getChildren());
                }
            }
        }else {
            String fileName = node.getNodeName().replaceAll("\\.", "_") + ".csv";
            classificationTree.setFileName(fileName);
        }
    }

    private void recursiveChildNode(int index, Node<JsonObject> node, List<ClassificationTree> children, Map<String,String> displayNamesMap) {

        String[] targetNames = node.getNodeName().split("\\.");
        String dataNodeName = targetNames[targetNames.length - 1];
        ClassificationTree classificationTree = new ClassificationTree();

        String name = (displayNamesMap.containsKey(dataNodeName)?displayNamesMap.get(dataNodeName):dataNodeName)+" ("+(node.getInstanceCount()!=null?node.getInstanceCount():0)+")";

        classificationTree.setLabel(name);
        classificationTree.setLevel(index);
        classificationTree.setParent(targetNames.length==1?"ROOT":targetNames[targetNames.length-2]);
        classificationTree.setRecordsCount((node.getInstanceCount()!=null?node.getInstanceCount():0));
        children.add(classificationTree);
        classificationTree.setChildren(new ArrayList<>());
        if (!CollectionUtils.isEmpty(node.getReferenceNode())) {
            for (Map.Entry<String, Node<JsonObject>> entrySet : node.getReferenceNode().entrySet()) {
                if (entrySet.getValue().getInstanceCount() != null) {
                    recursiveChildNode(index + 1, entrySet.getValue(), classificationTree.getChildren(),displayNamesMap);
                }
            }
        }else {
            String fileName = node.getNodeName().replaceAll("\\.", "_") + ".csv";
            classificationTree.setFileName(fileName);
        }

    }

    public Node<JsonObject> getRoot() {
        return root;
    }


    public static class ClassificationTreeBuilder {

        private Integer level;
        private Map<String, ClassificationNode> nodes;

        public ClassificationTreeBuilder() {
        }

        public ClassificationTreeBuilder setLevel(Integer level) {
            this.level = level;
            return this;
        }

        public ClassificationTreeBuilder setNodes(Map<String, ClassificationNode> nodes) {
            this.nodes = nodes;
            return this;
        }

        public ClassificationTreeStructure build(){
            return new ClassificationTreeStructure(this);
        }

    }

}
