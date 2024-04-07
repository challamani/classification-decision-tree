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
            size: 'medium', // Set layout size to large (default)
            allowCollapse: true,
            orientation :'vertical',
            allowHtml: true
         };

         // Create the chart.
         var chart = new google.visualization.OrgChart(document.getElementById('order_chart_div'));
         // Draw the chart, setting the allowHtml option to true for the tooltips.
         chart.draw(data, options);
      });
}