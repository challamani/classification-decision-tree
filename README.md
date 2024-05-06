# Dataset Classification
The `dataset-classification` service generates a Dynamic dataset classification tree based on the dataset metadata.

## Table of Contents

- [About Dataset Metadata](#dataset-metadata)
- [Classification Options](#classification-options)
- [Usage](#usage)
  - [Create dataset records](#datasetentity)
  - [Generate classification tree](#generate-classification-tree)
  - [Render classification tree view](#classification-tree-view)
- [Org-Chart View](#googles-org-chart-integration)

### Dataset Metadata
The dataset metadata is a json input to dataset-classification service, it describes about the node/attribute types (root, parent, split, and data) of a dataset, each associated with specific positions within the classification options list, based on the hierarchical position aggregation (records grouping) will get applied.

Example metadata with a two-classification option for employee dataset.
```json
{
  "name": "employee",
  "classificationNodes": [
    {
      "name": "superDepartment",
      "type": "parent",
      "dataType": "string",
      "decisions": [{"type": "STRING_EQ", "value": null,"nodeLabel": null}]
    },
    {
      "name": "department",
      "type": "parent",
      "dataType": "string",
      "decisions": [{"type": "STRING_EQ","value": null,"nodeLabel": null}]
    },
    {
      "name": "project",
      "type": "parent",
      "dataType": "string",
      "decisions": [{"type": "STRING_EQ","value": null,"nodeLabel": null}]
    },
    {
      "name": "team",
      "type": "parent",
      "dataType": "string",
      "decisions": [{"type": "STRING_EQ","value": null,"nodeLabel": null}]
    },
    {
      "name": "skill",
      "type": "data",
      "dataType": "string",
      "decisions": [{"type": "STRING_EQ","value": null,"nodeLabel": null}]
    }
  ],
  "classificationOptions": [
    {
      "name": "superDepartment.department.project.team",
      "nodeLevels": [
        {"name": "superDepartment","executionId": 0},
        {"name": "department","executionId": 1},
        {"name": "project","executionId": 2},
        {"name": "team","executionId": 3}
      ]
    },
    {
      "name": "superDepartment.department.skill",
      "nodeLevels": [
        {"name": "superDepartment","executionId": 0},
        {"name": "department","executionId": 1},
        {"name": "skill","executionId": 2}
      ]
    }
  ]
}
```

### Classification Options

The classification option is a mandatory input to perform the multi-grouping on the given dataset. Subsequently,the classification tree is constructed in alignment with this input

Note: 
1. We can generate multiple classification options for a dataset by considering the cardinality of its attributes and the interrelationships between them.
2. The current classification-tree project supports "atomic attribute" values, representing flat dataset records. However, it lacks support for datasets containing complex attribute values, such as object types
3. In the future, the `dataset-classification` service will extend support to dataset attributes characterized by `high cardinality`, `primitive` list types, and `comma-separated` values. This expansion necessitates the creation of `logical reference nodes` to ensure efficient classification.   

### Usage

#### Dataset/Entity
Since the project exclusively supports the classification of `flat datasets`, that means no complex attribute structures, so there's no need to uphold `Java POJOs` or `domain models` for deserializing dataset records. Instead, the `Gson's JsonObject` serves as the intermediary for storing and retrieving these records efficiently.

An example rest-api call to store dataset records for `employee dataset classification`
```curl
#http://localhost:8080/api/dataset/{datasetName}/records
curl --location 'http://localhost:8080/api/dataset/employee/records' \
--header 'Content-Type: application/json' \
--data '[
        {
            "superDepartment": "Finance",
            "department": "Accounting",
            "project": "ReportingSystem",
            "team": "Accounting",
            "skill": "Analyst",
            "experience": 2,
            "name":"bob"
        },
        {
            "superDepartment": "SuperSpecial2024",
            "department": "GenAI",
            "project": "NoCodeProj",
            "team": "GenAIDevTeam",
            "skill": "Python",
            "experience": 4,
            "name":"bob"
        }
    ]'
```
#### Generate Classification Tree

An example request to create a classification tree on an employee dataset
##### Request
```curl
### http://localhost:8080/api/classificationTree/{datasetName}/{classificationOption}
curl --location 'http://localhost:8080/api/classificationTree/employee/superDepartment.department.skill' 
```

##### Response
```json
{
    "responseCode": "0",
    "responseDescription": "Success",
    "decisionTree": {
        "level": 0,
        "name": "ROOT",
        "label": "ROOT",
        "children": [
            {
                "level": 1,
                "name": "Engineering",
                "label": "Engineering (3)",
                "parent": "ROOT",
                "children": [
                    {
                        "level": 2,
                        "name": "Engineering.Ecom",
                        "label": "Ecom (3)",
                        "parent": "Engineering",
                        "children": [
                            {
                                "level": 3,
                                "name": "Engineering.Ecom.BackendDev",
                                "label": "BackendDev (1)",
                                "parent": "Engineering.Ecom",
                                "recordsCount": 1,
                                "fileName": "Engineering_Ecom_BackendDev.csv"
                            },
                            {
                                "level": 3,
                                "name": "Engineering.Ecom.WebDev",
                                "label": "WebDev (2)",
                                "parent": "Engineering.Ecom",
                                "recordsCount": 2,
                                "fileName": "Engineering_Ecom_WebDev.csv"
                            }
                        ],
                        "recordsCount": 3
                    }
                ],
                "recordsCount": 3
            },
            {
                "level": 1,
                "name": "superSpecial",
                "label": "superSpecial (7)",
                "parent": "ROOT",
                "children": [
                    {
                        "level": 2,
                        "name": "superSpecial.GenAI",
                        "label": "GenAI (3)",
                        "parent": "superSpecial",
                        "children": [
                            {
                                "level": 3,
                                "name": "superSpecial.GenAI.PythonDev",
                                "label": "PythonDev (3)",
                                "parent": "superSpecial.GenAI",
                                "recordsCount": 3,
                                "fileName": "superSpecial_GenAI_PythonDev.csv"
                            }
                        ],
                        "recordsCount": 3
                    },
                    {
                        "level": 2,
                        "name": "superSpecial.ML",
                        "label": "ML (4)",
                        "parent": "superSpecial",
                        "children": [
                            {
                                "level": 3,
                                "name": "superSpecial.ML.WebDev",
                                "label": "WebDev (2)",
                                "parent": "superSpecial.ML",
                                "recordsCount": 2,
                                "fileName": "superSpecial_ML_WebDev.csv"
                            },
                            {
                                "level": 3,
                                "name": "superSpecial.ML.Backend",
                                "label": "Backend (2)",
                                "parent": "superSpecial.ML",
                                "recordsCount": 2,
                                "fileName": "superSpecial_ML_Backend.csv"
                            }
                        ],
                        "recordsCount": 4
                    }
                ],
                "recordsCount": 7
            }
        ]
    }
}
```

#### Classification-tree view
```curl
#### To render the classification-tree view, we can invoke the GET endpoint on the browser.
### http://localhost:8080/api/classificationTree/{datasetName}/{classificationOption}/tree-view

http://localhost:8080/api/classificationTree/employee/superDepartment.department.skill/tree-view
```

### Google's Org Chart integration

#### Example
```javascript
google.charts.load('current', {packages:["orgchart"]});
google.charts.setOnLoadCallback(drawChart);

function drawChart(){
    var data = new google.visualization.DataTable();
    data.addColumn('string', 'Name');
    data.addColumn('string', 'Parent');
    data.addColumn('string', 'ToolTip');

    fetch("http://localhost:8080/api/classificationNodeRelation/order/state.category.zipCode")
    .then(response => response.json())
    .then(apiData => {
    
        apiData.forEach(item => {
            data.addRow([{'v':item.name, 'f':item.name+'<div style="color:red; font-style:italic">'+item.label+'</div>'},item.parentNode, '']);
        });
        
         // chart options
         var options = {
            size: 'medium', 
            allowCollapse: true,
            allowHtml: true
         };

         var chart = new google.visualization.OrgChart(document.getElementById('order_chart_div'));
         chart.draw(data, options);
      });
}
```

![Classification Chart View](https://github.com/challamani/dataset-classification/blob/master/src/main/resources/images/googles_org_chart_screenshot.png)

