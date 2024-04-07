package com.practice.dataclassification.multigroup.meta;

import java.util.List;

public record DatasetClassificationMeta(String name, List<ClassificationNode> classificationNodes, List<ClassificationOption> classificationOptions) {
}
