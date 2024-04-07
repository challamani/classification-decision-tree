google.charts.load('current', {packages:["orgchart"]});
google.charts.setOnLoadCallback(fetchAndDrawCharts);

async function fetchAndDrawCharts(){
    try {
        const data1 = await fetch("http://localhost:8080/api/classificationNodeRelation/employee/superDepartment.department.project.team")
        .then(response => response.json());
        drawChart(data1, 'org_team_chart_div');

        const data2 = await fetch("http://localhost:8080/api/classificationNodeRelation/employee/superDepartment.department.skill")
        .then(response => response.json());
        drawChart(data2, 'org_skill_chart_div');

    } catch (error) {
        console.error('Error:', error);
    }
}

function drawChart(apiData, divId) {
        // Create a DataTable from the fetched data
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Name');
        data.addColumn('string', 'Parent');
        data.addColumn('string', 'ToolTip');

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
        // Create and draw the chart
        var chart = new google.visualization.OrgChart(document.getElementById(divId));
        chart.draw(data, options);
}