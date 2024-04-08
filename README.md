# Dataset Classification
Dynamic dataset classification tree based on the given dataset metadata.


## Dataset Metadata
The dataset metadata describes the individual node/attribute type (root, parent, split, and data) that are associated with the dataset and its position in the list of classification options

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

The classification option is mandatory input to perform the multi-group on the given dataset and the classification tree will be constructed accordingly.
Note: 
1. We can create multiple classification options on a dataset, based on the cardinality of the dataset attributes and relationships
2. Currently, this classification-tree project does support `atomic attribute` values (a flat dataset record), but it doesn't support dataset(s) that contain a complex attribute values (object type)
3. Dataset attribute that belongs to `High cardinal number type, primitive list type, and comma separated values` will support in the future for classification, this demands a logical reference nodes creation.   

## Dataset/Entity
As this project supports any `flat dataset` classification (where all runtime attribute values are atomic), so not maintain `Java pojo(s) or domain models` for deserializing the dataset records, using the Gson's JsonObject to store and retrieve the records.

An example rest-api call to create dataset records
```curl
#http://localhost:8080/api/dataset/{datasetName}/records
curl --location 'http://localhost:8080/api/dataset/order/records' \
--header 'Content-Type: application/json' \
--data '[
    {
        "category": "Books",
        "state": "Manchester",
        "price": "1000",
        "zipCode": "M00002",
        "user": "bob",
        "orderId":67900
    },
    {
        "category": "Books",
        "state": "Glasgow",
        "price": "2000",
        "zipCode": "G00002",
        "user": "bob",
        "orderId":67901
    }
]'
```
## Create Classification Tree

An example request to create a classification tree on an employee dataset
### Request
```curl
### http://localhost:8080/api/classificationTree/{datasetName}/{classificationOption}
curl --location 'http://localhost:8080/api/classificationTree/employee/superDepartment.department.skill' 
```

### Response
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

### To render the classification-tree view, we can invoke the GET endpoint on the browser.
```curl
### http://localhost:8080/api/classificationTree/{datasetName}/{classificationOption}/tree-view

http://localhost:8080/api/classificationTree/employee/superDepartment.department.skill/tree-view
```

## Google's Org Chart integration

### Example
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

