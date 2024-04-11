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
            data.addRow([{'v':item.name, 'f':item.label+'<div style="color:red; font-style:italic">'+item.records+'</div>'},item.parentNode, '']);
        });

         var options = {
            size: 'medium',
            allowCollapse: true,
            allowHtml: true
         };

         var chart = new google.visualization.OrgChart(document.getElementById('order_chart_div'));
         chart.draw(data, options);
      });
}