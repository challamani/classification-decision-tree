package com.practice.dataclassification.multigroup.meta;

import java.util.List;

public record ClassificationOption(String name, List<NodeLevel> nodeLevels) {
}
