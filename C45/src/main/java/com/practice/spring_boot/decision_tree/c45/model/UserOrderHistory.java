package com.practice.spring_boot.decision_tree.c45.model;

import lombok.Data;

import java.util.List;

@Data
public class UserOrderHistory {

    private String id;
    private String pin;
    private String state;
    private String itemCode;
    private Integer price;
    private String category;
    private String transactionId;
    private String dateOfPurchase;
    private Integer quantity;
    private List<String> productTags;
    private String contactNo;
    private String email;


    public String getEvalStr(){
        //delimiter separate string, ordered as per the decision-tree
        return "";
    }

}
