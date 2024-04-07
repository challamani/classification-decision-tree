package com.practice.dataclassification.multigroup.util;

import com.google.gson.JsonObject;
import com.practice.dataclassification.multigroup.meta.NodeLevel;

import java.util.List;

public class DataSetUtil {

    public static String getEvalString(JsonObject jsonObject, List<NodeLevel> nodeLevels){
        StringBuilder evalString = new StringBuilder();
        nodeLevels.forEach(nodeLevel -> evalString.append(jsonObject.get(nodeLevel.name())
                .getAsJsonPrimitive().getAsString()).append("|"));
        return evalString.toString().substring(0, evalString.length() - 1);
    }
}
