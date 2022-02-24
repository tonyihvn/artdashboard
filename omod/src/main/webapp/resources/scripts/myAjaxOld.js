/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



//for some reason, the date picker does not appear directly below the input box. 
//This function will reposition it.
var fullData = [];
function setupDatePickerPositioner()
{
    /*jq(".date").unbind("click");
    jq(".date").click(function(e){
        var leftPosition = jq(this).offset().left;
        jq(".datepicker-dropdown").css("left", leftPosition+"px");
    })*/
   
}

function getCohorts(type, cohortAjaxUrl, callback)
{
    jq = jQuery;
   /* jq.ajax({
        url:cohortAjaxUrl,
        type:"GET",
        data:{state:"start"},
        success:function(response){
            console.log(response);
        },
        error:function(xhr, status, error){
             console.log(error);
        }
    })*/
    jq.getJSON(cohortAjaxUrl,
            {
              type: type,
             
            }, function(data){
                callback(data);
            }, function(xhr, status, error){
                console.log(error);
            })
        
}




function getCohorts2(type, cohortAjaxUrl)
{
    let myPromise = new Promise(function(resolve, reject) {
    // "Producing Code" (May take some time)

      jq = jQuery;
   
        jq.getJSON(cohortAjaxUrl,
            {
              type: type,
             
            }, function(data){
                resolve(data)
            }, function(xhr, status, error){
                console.log(error);
                reject(error)
         })
         
    });
    
    return myPromise;
          
}

function artDashboard_buildBarCharts(title, chartData, graphArray, categoryField, chartDiv, postFix, stackType, legendData, enableCategoryLabels=true, is3d=false, min=0, max=0)
{
    var chart = new AmCharts.AmSerialChart();
    chart.dataProvider = chartData;
    chart.categoryField = categoryField;
    //chart.color = "#333333";
    //chart.fontSize = 14;
    //chart.startDuration = 1;
    chart.plotAreaBorderAlpha = 0.2;
    // the following two lines makes chart 3D
    
    if(is3d == true){
        chart.angle = 30;
        chart.depth3D = 20;
    }
    
    chart.startDuration = 1;
    // AXESchartData2chartDiv
    // category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.gridAlpha = 0.2;
    categoryAxis.gridPosition = "start";
    categoryAxis.gridColor = "#ffffff";
    categoryAxis.axisColor = "#333333";
    categoryAxis.axisAlpha = 0;
    categoryAxis.dashLength = 5;
    categoryAxis.labelsEnabled = enableCategoryLabels;

    // value
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.stackType = stackType; // This line makes chart 3D stacked (columns are placed one behind another)
    valueAxis.gridAlpha = 0.2;
    valueAxis.gridColor = "#333333";
    valueAxis.axisColor = "#333333";
    valueAxis.axisAlpha = 0;
    valueAxis.dashLength = 5;
    valueAxis.title = title;
    valueAxis.position = "left"
    valueAxis.titleColor = "#333333";
    valueAxis.unit = "";
    
    if(max >0){
        valueAxis.minimum = min;
        valueAxis.maximum = max;
        valueAxis.strictMinMax = true;
    }
    chart.addValueAxis(valueAxis);
    
    var valueAxis2 = new AmCharts.ValueAxis();
    valueAxis2.stackType = stackType; // This line makes chart 3D stacked (columns are placed one behind another)
    valueAxis2.gridAlpha = 0.2;
    valueAxis2.gridColor = "#333333";
    valueAxis2.axisColor = "#333333";
    valueAxis2.axisAlpha = 0;
    valueAxis2.dashLength = 5;
    valueAxis2.position = "right"
    valueAxis2.title = "Percentage";
    valueAxis2.titleColor = "#333333";
    valueAxis2.unit = "";
    valueAxis2.minimum = 0;
    valueAxis2.maximum = 120;
    valueAxis2.strictMinMax = true;
    
    chart.addValueAxis(valueAxis2);
    

    // CURSOR
    var chartCursor = new AmCharts.ChartCursor();
    chartCursor.cursorAlpha = 0;
    chartCursor.zoomable = true;
    chartCursor.categoryBalloonEnabled = false;
    chart.addChartCursor(chartCursor);

    chart.categoryAxis.labelRotation = 60;
    
    for(var i=0; i<graphArray.length; i++)
    {
        
                        
        var graph1 = new AmCharts.AmGraph();
        
        graph1.title = graphArray[i]["title"];
        graph1.weightBand = graphArray[i]["weightBand"];
        graph1.regimen = graphArray[i]["regimen"];
        graph1.isOptimum = graphArray[i]["isOptimum"];
        graph1.chartType = graphArray[i]["chartType"];
        graph1.valueField = graphArray[i]["field"];
        graph1.colorField = "color";
        var description = graphArray[i]["description"];
        graph1.type = graphArray[i]["type"];
        
        
        if(graphArray[i]["type"] == "column")
        {
            graph1.fillAlphas = 1;
            graph1.lineAlpha = 0;
            graph1.labelText = "[[value]]"+postFix;
             graph1.balloonText = graphArray[i]["title"]+" [["+description+"]]<br><span style='font-size:14px;'>[[value]]</span>"//graphArray[i]["description"]+" [[category]]: <b>[[value]]</b>"+postFix;
        }
        else{
            graph1.bulletBorderAlpha = 1;
            graph1.bulletBorderThickness = 2;
            graph1.lineThickness = 2;
            graph1.bullet = "round";
            graph1.bulletSize = 8;
            graph1.labelText = "[[value]] %";
             graph1.balloonText = graphArray[i]["title"]+" [["+description+"]]<br><span style='font-size:14px;'>[[value]]%</span>"//graphArray[i]["description"]+" [[category]]: <b>[[value]]</b>"+postFix;
        }
        //graph1.periodValue = graphArray[i]["title"]+" [["+description+"]]<br><span style='font-size:14px;'>[[value]]</span>";
        
         
        
        
        graph1.regimenId = 1;
        if(typeof graphArray[i]["color"] == "undefined"){
            graph1.colorField = "color";
        }else{
            graph1.lineColor = graphArray[i]["color"];
        }
        
        
        if(graphArray[i]["valueAxis"] == 1){
            graph1.valueAxis = valueAxis;
        }
        else{
            graph1.valueAxis = valueAxis2;
        }
        
        
        
        //
        
        graph1.addListener("clickGraphItem", graphClickHandler)
       
        chart.addGraph(graph1);
    }
    // LEGEND
    if(legendData.length > 0)
    {
        var legend = new AmCharts.AmLegend();
    
        legend.borderAlpha = 0.2;
        legend.labelWidth = 800;
        //legend.labelWidth = 350;
        legend.data =  legendData//[{title: "One", color: "#3366CC"},{title: "Two", color: "#FFCC33"}];
        legend.horizontalGap = 2;
        legend.verticalGap = 20;
        legend.equalWidths = false;
        legend.forceWidth = true;
        chart.addLegend(legend);
    }

   chart.addListener("clickGraphItem", graphClickHandler)
   chart.write(chartDiv);
}

function myAjax(data, cohortAjaxUrl)
{
    let myPromise = new Promise(function(resolve, reject) {
    // "Producing Code" (May take some time)

      jq = jQuery;
   
        jq.getJSON(cohortAjaxUrl,
            
        data, function(data){
                resolve(data)
            }, function(xhr, status, error){
                console.log(error);
                reject(error)
         })
         
    });
    
    return myPromise;
          
}


function buildBarCharts(title, chartData, graphArray, categoryField, chartDiv, postFix, stackType, legendData, enableCategoryLabels=true)
{
    var chart = new AmCharts.AmSerialChart();
    chart.dataProvider = chartData;
    chart.categoryField = categoryField;
    //chart.color = "#333333";
    //chart.fontSize = 14;
    //chart.startDuration = 1;
    chart.plotAreaBorderAlpha = 0.2;
    // the following two lines makes chart 3D
    chart.angle = 30;
    chart.depth3D = 20;
  

    chart.startDuration = 1;

    // AXESchartData2chartDiv
    // category
    var categoryAxis = chart.categoryAxis;
    categoryAxis.gridAlpha = 0.2;
    categoryAxis.gridPosition = "start";
    categoryAxis.gridColor = "#ffffff";
    categoryAxis.axisColor = "#333333";
    categoryAxis.axisAlpha = 0;
    categoryAxis.dashLength = 5;
    categoryAxis.labelsEnabled = enableCategoryLabels;

    // value
    var valueAxis = new AmCharts.ValueAxis();
    valueAxis.stackType = stackType; // This line makes chart 3D stacked (columns are placed one behind another)
    valueAxis.gridAlpha = 0.2;
    valueAxis.gridColor = "#333333";
    valueAxis.axisColor = "#333333";
    valueAxis.axisAlpha = 0;
    valueAxis.dashLength = 5;
    valueAxis.title = title;
    valueAxis.titleColor = "#333333";
    valueAxis.unit = "";
    
    chart.addValueAxis(valueAxis);


    // CURSOR
    var chartCursor = new AmCharts.ChartCursor();
    chartCursor.cursorAlpha = 0;
    chartCursor.zoomable = true;
    chartCursor.categoryBalloonEnabled = false;
    chart.addChartCursor(chartCursor);

    chart.categoryAxis.labelRotation = 60;
    
    for(var i=0; i<graphArray.length; i++)
    {
                        
        var graph1 = new AmCharts.AmGraph();
        graph1.title = graphArray[i]["title"];
        graph1.weightBand = graphArray[i]["weightBand"];
        graph1.regimen = graphArray[i]["regimen"];
        graph1.isOptimum = graphArray[i]["isOptimum"];
        graph1.valueField = graphArray[i]["field"];
        graph1.type = "column";
        
        graph1.lineAlpha = 0;
        graph1.labelText = "[[value]]"+postFix;
        graph1.regimenId = 1;
        if(typeof graphArray[i]["color"] == "undefined"){
            graph1.colorField = "color";
        }else{
            graph1.lineColor = graphArray[i]["color"];
        }
        
        //
        graph1.fillAlphas = 1;
        graph1.addListener("clickGraphItem", graphClickHandler)
        graph1.balloonText = graphArray[i]["title"]+"<br><span style='font-size:14px;'>[[value]]"+postFix+"</span>"//graphArray[i]["description"]+" [[category]]: <b>[[value]]</b>"+postFix;
        chart.addGraph(graph1);
    }
    // LEGEND
    var legend = new AmCharts.AmLegend();
    legend.borderAlpha = 0.2;
    legend.data =  legendData//[{title: "One", color: "#3366CC"},{title: "Two", color: "#FFCC33"}];
    legend.horizontalGap = 10;
    chart.addLegend(legend);

   //chart.addListener("clickGraphItem", graphClickHandler)
    chart.write(chartDiv);
}

function buildPieCharts(chartData, valueField, titleField, divId, legendData=new Array())
{
    var chart = new AmCharts.AmPieChart();
    chart.dataProvider = chartData;
    chart.titleField = titleField;
    chart.valueField = valueField;
    chart.outlineColor = "#FFFFFF";
    chart.outlineAlpha = 0.8;
    chart.outlineThickness = 2;
    chart.radius = 125;
    chart.balloonText = "[[title]]<br><span style='font-size:14px'><b>[[value]]</b> ([[percents]]%)</span>";
    // this makes the chart 3D
    chart.depth3D = 15;
    chart.angle = 30;
    chart.colors = ["#FF0F00", "#FF6600", "#FF9E01", "#FCD202", "#F8FF01", "#B0DE09", "#04D215", "#0D8ECF", "#0D52D1", "#2A0CD0", "#8A0CCF", "#CD0D74", "#754DEB", "#DDDDDD", "#999999", "#333333", "#000000", "#57032A", "#CA9726", "#990000", "#4B0C25"]
    chart.addListener("clickSlice", pieClickHandler);
    // WRITE
    var legend = new AmCharts.AmLegend();
    legend.position = "left";
    legend.horizontalGap = 20;
    legend.bottom = 10;
    legend.marginRight = 30
    //legend.data = legendData;
    chart.addLegend(legend);
    chart.write(divId);
}


function graphClickHandler(event)
{
     var jq = jQuery;
    var isOptimum = event.graph.isOptimum;
    var chartType = event.graph.chartType;
    if(isOptimum == "vl"){
        renderVlPatientDetails(event.item.category, "Patient details");
    }
    else if(isOptimum == "eac")
    {
        renderEACPatientDetails(event.item.category, "EAC");
    }
    else if(isOptimum == "cd4")
    {
        //console.log(event.item.category);
        renderCD4PatientDetails(event.item.category, "CD4+ Cell Count");
    }
    else{
        var weightBand = event.graph.weightBand;
        var regimen = event.graph.regimen;
        var title = "PPR "+ (isOptimum == "true") ? "Optimal Regimen " : "Sub-optimal Regimen ";
        if(weightBand == null)
        {
           var weight = event.item.category;
           title += " "+weight;
            if(weight == "<20kg")
            {
                weightBand = "less_20";
            }
            else if(weight == "20-30kg"){
                weightBand = "bet_20_30";
            }
            else{
                weightBand = "above_30";
            }
        }
        else{
            if(weightBand == "less_20")
            {
                title += " < 20kg"
            }
            else if(weight == "bet_20_30"){
                title += " 20-30kg"
            }
            else{
                title += "< 20kg"
            }
        }



        renderPatientDetails(isOptimum, weightBand, regimen, title);

        /*jq('#detailsModal').modal({
            keyboard: false
        })*/
    }
}

function pieClickHandler(event)
{
    var jq = jQuery;
   
    var isOptimum = event.dataItem.dataContext.isOptimum
    var weight = event.dataItem.dataContext.weightBand
    var regimen = event.dataItem.dataContext.regimen;
    var title = ((isOptimum == "true") ? "PPR Optimal regimen: " : "PPR Sub-optimal regimen")+" "+event.dataItem.dataContext.title;
    if(weight == "less_20")
    {
        title += " <20kg";
    }else if(weight == "bet_20_30"){
        title += " 20-30kg";
    }else{
        title += " >30kg";
    }
    renderPatientDetails(isOptimum, weight, regimen, title);
}

var jq = jQuery;
jq(document).ready(function(){
    jq(".showDetails").click(function(){
        var isOptimum = jq(this).attr("data-isOptimum");
        var weight = jq(this).attr("data-weight");
        var regimen = jq(this).attr("data-regimen");
        var title = jq(this).attr("data-title");

        renderPatientDetails(isOptimum, weight, regimen, title);
        
    });
})

function renderPatientDetails(isOptimum, weight, regimen, title)
{
    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Regimen</th><th nowrap>Regimen Line</th><th nowrap>Drug Strength</th><th nowrap>Drug Dosage</th> <th nowrap>Age</th> <th nowrap>Weight</th> <th nowrap>Weight band</th> <th nowrap>Weight Duration</th></tr></thead><tbody id="detailsArea"></tbody></table>');
    
    jq("#detailsModal").modal("show");
    var html = "";
    var sn = 1;
    for(var i=0; i<fullData.length; i++)
    {  
        var isOptimumTrue = (isOptimum == "true") ? true : false;
        if(isOptimumRegimen(fullData[i]["regimenConceptId"]) === isOptimumTrue && getWeightBand(fullData[i]["patientWeight"]) == weight)
        {//there is a match
            if(regimen == "" || regimen == null){
                html += buildPatientHtml(sn, fullData[i], weight);
                sn++;
            }
            else if(regimen == "regimen_"+fullData[i]["regimenConceptId"])
            {
                html += buildPatientHtml(sn, fullData[i], weight);
                sn++;
            }else if(regimen == "regimen_0" && getOptimalType(fullData[i]["regimenConceptId"]) == -1)
            {
                 html += buildPatientHtml(sn, fullData[i], weight);
                 sn++;
            }
            
            
        } 
        else{
            
        }
    }
    //reInitializeDatatable(title);
    jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

function renderVlPatientDetails(vlCount, title)
{
    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Current VL</th><th nowrap>Sample Collection Date</th></tr></thead><tbody id="detailsArea"></tbody></table>');
    
    jq("#detailsModal").modal("show");
    var html = "";
    var sn = 1;
    for(var i=0; i<vlData.length; i++)
    {  
        if(vlCount == ">=1000 cp/ml" && parseFloat(vlData[i]["vlResult"]) >= 1000 )
        {//there is a match
           html +=  buildVlPatientHtml(sn, vlData[i], "action");
           sn++;
        } else if(vlCount == "200 - 999 cp/ml" && parseFloat(vlData[i]["vlResult"]) > 200  && parseFloat(vlData[i]["vlResult"]) < 1000 )
        {
           html +=  buildVlPatientHtml(sn, vlData[i], "action");
           sn++;
        }
        else if(vlCount == "<200 cp/ml" && parseFloat(vlData[i]["vlResult"]) < 200)
        {
            html += buildVlPatientHtml(sn, vlData[i], "action");
            sn++;
        }
        
        
    }
    //reInitializeDatatable(title);
    jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

function renderEACPatientDetails(eacType, title)
{
    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Previous VL</th><th>Current VL</th></tr></thead><tbody id="detailsArea"></tbody></table>');
    
    jq("#detailsModal").modal("show");
    var html = "";
    var sn = 1;
    //console.log(eacData);
    for(var i=0; i<eacData.length; i++)
    {  
        
        var eacDate = Date.parse(eacData[i]["encounterDate"]);
        if(eacType == "receivingEAC" && (eacDate + (30 * 24 * 60 * 60 * 1000) < new Date().getTime()) )
        {//there is a match
           
           html +=  buildEACPatientHtml(sn, eacData[i], "action1");
           sn++;
        } else if(eacType == "receivedEACVLS" && eacData[i]["secondToLastVl"]<1000)
        {
           html +=  buildEACPatientHtml(sn, eacData[i], "action2");
           sn++;
        }
        else if(eacType == "receivedEACVLE" && eacData[i]["secondToLastVl"] >  eacData[i]["vlResult"])
        {
           html +=  buildEACPatientHtml(sn, eacData[i], "action2");
           sn++;
        }
        
    }
    //reInitializeDatatable(title);
    jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

function renderCD4PatientDetails(vlCount, title)
{
    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Current CD4+ Cell Count</th><th nowrap>Visit Date</th></tr></thead><tbody id="detailsArea"></tbody></table>');
    
    jq("#detailsModal").modal("show");
    var html = "";
    var sn = 1;
    console.log(vlCount);
    for(var i=0; i<cd4Data.length; i++)
    {  
        if(vlCount == "# of patients with CD4+ cell count <200" && parseFloat(cd4Data[i]["cd4Count"]) < 200 )
        {//there is a match
           console.log(cd4Data[i]);
           html +=  buildCD4PatientHtml(sn, cd4Data[i], "action1");
           sn++;
        } else if(vlCount == "# of patients with CD4+ cell count >200" && parseFloat(cd4Data[i]["cd4Count"]) > 200)
        {
           html +=  buildCD4PatientHtml(sn, cd4Data[i], "action2");
           sn++;
        }
        
        
        
    }
    //reInitializeDatatable(title);
    jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

function reInitializeDatatable(title)
{
   
    jq(".dataTable").DataTable({
        
             pageLength: 100,
             "lengthMenu": [[50, 100, 250, 500, -1], [50, 100, 250, 500, "All"]],
             "columnDefs": [
                { "searchable": false, "targets": [0] }  // Disable search on first and last columns
              ],
              dom: 'lfBrtip',
                buttons: [
                    'copy', 
                   // 'csv', 
                    {
                        extend: 'csv',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    {
                        extend: 'excel',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    {
                        extend: 'pdf',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    {
                        extend: 'print',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    //'pdf', 
                    //'print'
                ]

                });
}


 
function buildVlPatientHtml(i, patientData, action)
{
    var html = "";
    html += "<tr>"; 
    html += "<td >"+(i)+"</td>";
    html += "<td>"+patientData["pepfarId"]+"</td>";
    html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
    html += "<td>"+patientData["vlResult"]+"</td>";
    html += "<td>"+patientData["sampleCollectionDate"]+"</td>";
    //html += "<td>"+action+"</td>";
   
    html += "</tr>";
    return html;
}

function buildEACPatientHtml(i, patientData, action)
{
    var html = "";
    html += "<tr>"; 
    html += "<td >"+(i)+"</td>";
    html += "<td>"+patientData["pepfarId"]+"</td>";
    html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
    html += "<td>"+patientData["vlResult"]+"</td>";
    html += "<td>"+patientData["secondToLastVl"]+"</td>";
    //html += "<td>"+action+"</td>";
   
    html += "</tr>";
    return html;
}

function buildCD4PatientHtml(i, patientData, action)
{
    var html = "";
    html += "<tr>"; 
    html += "<td >"+(i)+"</td>";
    html += "<td>"+patientData["pepfarId"]+"</td>";
    html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
    html += "<td>"+patientData["cd4Count"]+"</td>";
    html += "<td>"+patientData["encounterDate"]+"</td>";
    //html += "<td>"+action+"</td>";
   
    html += "</tr>";
    return html;
}

function buildPatientHtml(i, patientData, weight)
{
    
    var html = "";
    html += "<tr>"; 
    html += "<td >"+(i)+"</td>";
    html += "<td>"+patientData["pepfarId"]+"</td>";
    html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
    html += "<td>"+patientData["regimenLine"]+"</td>";
    html += "<td>"+patientData["regimenLine2"]+"</td>";
    html += "<td>"+patientData["drugStrength"]+"</td>";
    html += "<td>"+patientData["drugDosage"]+"</td>";
    html += "<td>"+patientData["patientAge"]+"</td>";
    html += "<td>"+patientData["patientWeight"]+"</td>";
    var weightBandHtml = "";
    if(weight == "less_20")
    {
        weightBandHtml = "< 20kg";
    }else if(weight == "bet_20_30"){
        weightBandHtml = "20-30kg"
    }else{
         weightBandHtml = ">30kg"
    }
    html += "<td>"+weightBandHtml+"</td>";
    html += "<td>"+getMonthsAgo(patientData["weightDate"])+" months</td>";
    html += "</tr>";
    return html;
}

function getMonthsAgo(date)
{
    var d = new Date(Date.parse(date));
    var now = new Date();
   
    var months = monthDiff(d, now);
    return months;
    
}

function monthDiff(d1, d2) {
    var months;
    months = (d2.getFullYear() - d1.getFullYear()) * 12;
    months -= d1.getMonth();
    months += d2.getMonth();
    return months <= 0 ? 0 : months;
}

function isOptimumRegimen(conceptId)
{
    if([162200, 165691, 165681, 166092, 164512, 162201].includes(conceptId))
    {
        return true;
    }
    else{
        return false;
    }
}


function getOptimalType(conceptId)
{
    if([162200, 165691, 165681, 166092, 164512, 162201].includes(conceptId))
    {
        return 1;
    }
    else if([162561, 164505, 162199, 162563, 1652].includes(conceptId)){
        return 0;
    }
    else {
        return -1;
    }
}

function  getAgeBand(age) {//this is actually weight band
    var ageBand = "";
    if (age < 20) {
        ageBand = "less_20";
    } else if (age >= 20 && age <= 30) {
        ageBand = "bet_20_30";
    } else if(age > 30){
        ageBand = "above_30";
    }
    return ageBand;
}
function  getWeightBand(age) {//this is actually weight band
    var ageBand = "";
    if (age < 20) {
        ageBand = "less_20";
    } else if (age >= 20 && age <= 30) {
        ageBand = "bet_20_30";
    } else if(age > 30){
        ageBand = "above_30";
    }
    return ageBand;
}
