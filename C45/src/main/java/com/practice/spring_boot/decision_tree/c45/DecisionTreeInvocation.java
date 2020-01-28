package com.practice.spring_boot.decision_tree.c45;

import com.practice.spring_boot.decision_tree.c45.builder.C45DecisionTree;
import com.practice.spring_boot.decision_tree.c45.builder.Condition;
import com.practice.spring_boot.decision_tree.c45.config.DecisionTreeParser;
import com.practice.spring_boot.decision_tree.c45.domain.Node;
import com.practice.spring_boot.decision_tree.c45.model.UserOrderHistory;
import com.practice.spring_boot.decision_tree.c45.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DecisionTreeInvocation {

    @Autowired
    DecisionTreeParser decisionTreeParser;

    private C45DecisionTree decisionTree;

    Logger logger = LoggerFactory.getLogger(DecisionTreeInvocation.class);

    @PostConstruct
    public void init(){

        List<UserOrderHistory> userOrderHistories =new ArrayList<>();
        Map<String, List<UserOrderHistory>> dataNodeDataSet = populateDecisionTree(userOrderHistories);
        logger.info("C45DecisionTree {}",dataNodeDataSet);

    }


    private Map<String, List<UserOrderHistory>> populateDecisionTree(List<UserOrderHistory> userOrderHistories) {

        Map<String, Node> stringNodeMap = decisionTreeParser.getNodeMap();
        decisionTree = new C45DecisionTree.DecisionTreeBuilder().setLevel(3).setNodes(stringNodeMap).build();

        Map<String, List<UserOrderHistory>> dataNodeDataSet = new HashMap<>();
        userOrderHistories.forEach(userOrderHistory -> {
            String evaluationString = userOrderHistory.getEvalStr();
            String[] values = evaluationString.split("\\|");
            updateTreeWithInstanceValue(userOrderHistory, values, decisionTree, dataNodeDataSet);
        });


        return dataNodeDataSet;
    }

    private void  updateTreeWithInstanceValue(UserOrderHistory userOrderHistory,String[] values, C45DecisionTree decisionTree, Map<String,List<UserOrderHistory>> dataNodeDataSet) {
        int index = 0;
        com.practice.spring_boot.decision_tree.c45.builder.Node<UserOrderHistory> rootNode = decisionTree.getRoot();
        Integer instanceCount = rootNode.getInstanceCount() == null ? 0 : rootNode.getInstanceCount() + 1;
        rootNode.setInstanceCount(instanceCount);
        rootNode.getPredicateNode().forEach((k, v) -> {
            String value = values[index];
            if (v.getPredicate().test(value)) {
                com.practice.spring_boot.decision_tree.c45.builder.Node<UserOrderHistory> targetNode = decisionTree.getRoot().getReferenceNode().get(k);
                updateLinkedTargetNodes(userOrderHistory, values, index + 1, targetNode,dataNodeDataSet);
                return;
            }
        });
    }


    private void updateLinkedTargetNodes(UserOrderHistory userOrderHistory, String[] values, int index, com.practice.spring_boot.decision_tree.c45.builder.Node<UserOrderHistory> node, Map<String,List<UserOrderHistory>> dataNodeDataSet) {

        Integer instanceCount = node.getInstanceCount() == null ? 1 : node.getInstanceCount() + 1;
        node.setInstanceCount(instanceCount);

        if (node.getNodeType(node).equalsIgnoreCase("data")) {

            if (!dataNodeDataSet.containsKey(node.getNodeName())) {
                dataNodeDataSet.put(node.getNodeName(), new ArrayList<>());
            }
            dataNodeDataSet.get(node.getNodeName()).add(userOrderHistory);
            return;
        }

        boolean noTargetNodeFound = true;
        for (Map.Entry<String, Condition> entry : node.getPredicateNode().entrySet()) {
            String value = values[index];
            Object convertValue = CommonUtil.convertValueType(entry.getValue().getType(), value);

            if (entry.getValue().getPredicate().test(convertValue)) {
                com.practice.spring_boot.decision_tree.c45.builder.Node<UserOrderHistory> targetNode = node.getReferenceNode().get(entry.getKey());
                updateLinkedTargetNodes(userOrderHistory, values, index + 1, targetNode, dataNodeDataSet);
                return;
            }
        }

        if (noTargetNodeFound) {
            logger.info("no target nodes {} value {}", userOrderHistory, values[index]);
        }

    }
}
