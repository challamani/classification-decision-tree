package com.practice.spring_boot.decision_tree.c45.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.practice.spring_boot.decision_tree.c45.domain.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

@Configuration
public class DecisionTreeParser {


    private static final Logger logger = LoggerFactory.getLogger(DecisionTreeParser.class);

    private Map<String, Node> nodeMap;


    public DecisionTreeParser() {

        try {
            StringBuffer jsonStr = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/OrderClassification.json")));
            String oneLine;
            while ((oneLine = reader.readLine()) != null) {
                jsonStr.append(oneLine);
            }

            Type type = new TypeToken<Map<String, Node>>() {
            }.getType();
            nodeMap = new Gson().fromJson(jsonStr.toString(), type);

        } catch (IOException e) {
            logger.info(e.getMessage());
        }
    }

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

}
