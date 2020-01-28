package com.practice.spring_boot.decision_tree.c45.builder;

public interface NodeType {

    String CONDITION_NODE="Condition";
    String ROOT_NODE="Root";
    String DATA_NODE="Data";
    default String getNodeType(Node node) {
          if(node.getPredicateNode()!=null){
              return CONDITION_NODE;
          }

          return DATA_NODE;
    }
}
