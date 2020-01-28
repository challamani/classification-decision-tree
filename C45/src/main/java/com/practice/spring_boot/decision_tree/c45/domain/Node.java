package com.practice.spring_boot.decision_tree.c45.domain;

import java.util.Arrays;

public class Node {

    private Decision[] decisions;

    public Decision[] getDecisions() {
        return decisions;
    }

    public void setDecisions(Decision[] decisions) {
        this.decisions = decisions;
    }

    @Override
    public String toString() {
        return "Node{" +
                "decisions=" + Arrays.toString(decisions) +
                '}';
    }
}
