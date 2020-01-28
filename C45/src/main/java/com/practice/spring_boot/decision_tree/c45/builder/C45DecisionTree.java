package com.practice.spring_boot.decision_tree.c45.builder;

import com.practice.spring_boot.decision_tree.c45.domain.Decision;
import com.practice.spring_boot.decision_tree.c45.domain.DecisionTree;
import com.practice.spring_boot.decision_tree.c45.model.UserOrderHistory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class C45DecisionTree {

    private static final Logger logger = LoggerFactory.getLogger(C45DecisionTree.class);

    private Node<UserOrderHistory> root;

    private C45DecisionTree(DecisionTreeBuilder decisionTreeBuilder) {

        initDecisionTree(decisionTreeBuilder.nodes);
    }

    private void initDecisionTree(Map<String, com.practice.spring_boot.decision_tree.c45.domain.Node> nodeMap){
         initRootNode(nodeMap);
         initChildNodes(root,nodeMap);
    }

    private void initRootNode(Map<String, com.practice.spring_boot.decision_tree.c45.domain.Node> nodeMap) {
        com.practice.spring_boot.decision_tree.c45.domain.Node rootNode = nodeMap.get("root");
        root = new Node<>("root");
        Map<String, Condition> predicateNode = new HashMap<>();
        Map<String, Node<UserOrderHistory>> referenceNode = new HashMap<>();
        for (Decision decision : rootNode.getDecisions()) {
            predicateNode.put(decision.getReferenceNode(), new Condition.ConditionBuilder().setType(decision.getType()).setValue(decision.getValue()).build());
            referenceNode.put(decision.getReferenceNode(), new Node<>(decision.getReferenceNode()));
        }
        root.setPredicateNode(predicateNode);
        root.setReferenceNode(referenceNode);
    }

    private void initChildNodes(Node<UserOrderHistory> node, Map<String, com.practice.spring_boot.decision_tree.c45.domain.Node> nodeMap) {
        if(!CollectionUtils.isEmpty(node.getReferenceNode())) {
            node.getReferenceNode().forEach((k, v) -> {
                com.practice.spring_boot.decision_tree.c45.domain.Node decisionNode = nodeMap.get(v.getNodeName());
                if (!ObjectUtils.isEmpty(decisionNode) && decisionNode.getDecisions() !=null) {
                    Map<String, Condition> predicateNode = new HashMap<>();
                    Map<String, Node<UserOrderHistory>> referenceNode = new HashMap<>();
                    for (Decision decision : decisionNode.getDecisions()) {
                        predicateNode.put(decision.getReferenceNode(), new Condition.ConditionBuilder().setType(decision.getType()).setValue(decision.getValue()).build());
                        referenceNode.put(decision.getReferenceNode(), new Node<>(decision.getReferenceNode()));
                    }
                    v.setPredicateNode(predicateNode);
                    v.setReferenceNode(referenceNode);
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

    private void recursiveRender(int noOfTabs, Node<UserOrderHistory> node) {
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
            for (Map.Entry<String, Node<UserOrderHistory>> entrySet : node.getReferenceNode().entrySet()) {
                if (entrySet.getValue().getInstanceCount() != null)
                    recursiveRender(noOfTabs + 1, entrySet.getValue());
            }
        }
    }

    public DecisionTree getDecisionTree() {

        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setLabel("root");
        decisionTree.setLevel(0);
        decisionTree.setChildren(new ArrayList<>());
        root.getReferenceNode().forEach((k, v) -> {
            recursiveChildNode(1, v, decisionTree.getChildren());
        });
        return decisionTree;
    }

    public DecisionTree getDecisionTree(Map<String,String> displayNamesMap) {

        DecisionTree decisionTree = new DecisionTree();
        decisionTree.setLabel("root");
        decisionTree.setLevel(0);
        decisionTree.setChildren(new ArrayList<>());
        root.getReferenceNode().forEach((k, v) -> {
            recursiveChildNode(1, v, decisionTree.getChildren(),displayNamesMap);
        });
        return decisionTree;
    }

    private void recursiveChildNode(int index, Node<UserOrderHistory> node, List<DecisionTree> children) {

        if(node.getInstanceCount()==null) {
            return;
        }

        String[] targetNames = node.getNodeName().split("\\.");
        String dataNodeName = targetNames[targetNames.length - 1];
        DecisionTree decisionTree = new DecisionTree();

        String name = dataNodeName+" ("+(node.getInstanceCount()!=null?node.getInstanceCount():0)+")";

        decisionTree.setLabel(name);
        decisionTree.setLevel(index);
        decisionTree.setParent(targetNames.length==1?"root":targetNames[targetNames.length-2]);
        decisionTree.setRecordsCount((node.getInstanceCount()!=null?node.getInstanceCount():0));
        children.add(decisionTree);
        decisionTree.setChildren(new ArrayList<>());
        if (!CollectionUtils.isEmpty(node.getReferenceNode())) {
            for (Map.Entry<String, Node<UserOrderHistory>> entrySet : node.getReferenceNode().entrySet()) {
                if (entrySet.getValue().getInstanceCount() != null) {
                    recursiveChildNode(index + 1, entrySet.getValue(), decisionTree.getChildren());
                }
            }
        }else {
            String fileName = node.getNodeName().replaceAll("\\.", "_") + ".csv";
            decisionTree.setFileName(fileName);
        }

    }

    private void recursiveChildNode(int index, Node<UserOrderHistory> node, List<DecisionTree> children, Map<String,String> displayNamesMap) {

        String[] targetNames = node.getNodeName().split("\\.");
        String dataNodeName = targetNames[targetNames.length - 1];
        DecisionTree decisionTree = new DecisionTree();

        String name = (displayNamesMap.containsKey(dataNodeName)?displayNamesMap.get(dataNodeName):dataNodeName)+" ("+(node.getInstanceCount()!=null?node.getInstanceCount():0)+")";

        decisionTree.setLabel(name);
        decisionTree.setLevel(index);
        decisionTree.setParent(targetNames.length==1?"root":targetNames[targetNames.length-2]);
        decisionTree.setRecordsCount((node.getInstanceCount()!=null?node.getInstanceCount():0));
        children.add(decisionTree);
        decisionTree.setChildren(new ArrayList<>());
        if (!CollectionUtils.isEmpty(node.getReferenceNode())) {
            for (Map.Entry<String, Node<UserOrderHistory>> entrySet : node.getReferenceNode().entrySet()) {
                if (entrySet.getValue().getInstanceCount() != null) {
                    recursiveChildNode(index + 1, entrySet.getValue(), decisionTree.getChildren(),displayNamesMap);
                }
            }
        }else {
            String fileName = node.getNodeName().replaceAll("\\.", "_") + ".csv";
            decisionTree.setFileName(fileName);
        }

    }

    public Node<UserOrderHistory> getRoot() {
        return root;
    }


    public static class DecisionTreeBuilder {

        private Integer level;
        private Map<String, com.practice.spring_boot.decision_tree.c45.domain.Node> nodes;

        public DecisionTreeBuilder() {
        }

        public DecisionTreeBuilder setLevel(Integer level) {
            this.level = level;
            return this;
        }

        public DecisionTreeBuilder setNodes(Map<String, com.practice.spring_boot.decision_tree.c45.domain.Node> nodes) {
            this.nodes = nodes;
            return this;
        }

        public C45DecisionTree build(){
            return new C45DecisionTree(this);
        }

    }

}
