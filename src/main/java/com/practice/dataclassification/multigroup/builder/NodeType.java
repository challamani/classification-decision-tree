package com.practice.dataclassification.multigroup.builder;

import java.util.Objects;

public interface NodeType {

    String CONDITION_NODE="Condition";
    String ROOT_NODE="Root";
    String DATA_NODE="Data";
    default String getNodeType(Node node) {
          if(Objects.nonNull(node.getPredicateNode())){
              return CONDITION_NODE;
          }
          return DATA_NODE;
    }
}
