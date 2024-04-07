package com.practice.dataclassification.multigroup.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.dataclassification.multigroup.meta.DatasetClassificationMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class DatasetClassificationMetaReader {

    private final List<DatasetClassificationMeta> datasetClassificationMetadataList;

    @Autowired
    public DatasetClassificationMetaReader() {
        try (
                InputStream inputStream = Thread.currentThread()
                        .getContextClassLoader().getResourceAsStream("dataset-classification-meta.json");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {

            StringBuilder classificationMetadata = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                classificationMetadata.append(line);
            }
            datasetClassificationMetadataList = new ObjectMapper().readValue(classificationMetadata.toString(),
                    new TypeReference<List<DatasetClassificationMeta>>() {
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DatasetClassificationMeta> getDatasetClassificationMetadataList(){
        return datasetClassificationMetadataList;
    }

    public DatasetClassificationMeta getDatasetClassificationMetadata(String datasetName){
        return datasetClassificationMetadataList.stream()
                .filter(datasetClassificationMeta -> datasetClassificationMeta.name().equalsIgnoreCase(datasetName))
                .findFirst().orElse(null);
    }
}
