/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// INTRODUCING NEW DISAGREGATION BAND

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
        
        // graph1.addListener("clickGraphItem", graphClickHandler)
       
        chart.addGraph(graph1);
    }
    // LEGEND
    if(legendData.length > 0)
    {
        var legend = new AmCharts.AmLegend();
    
        legend.borderAlpha = 0.2;
        legend.labelWidth = 800;
        legend.labelWidth = 350;
        legend.data =  legendData//[{title: "One", color: "#3366CC"},{title: "Two", color: "#FFCC33"}];
        legend.horizontalGap = 2;
        legend.verticalGap = 20;
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

function myAjaxNew(myData,cohortAjaxUrl){
    var jq = jQuery;
    var response;
    jq.ajax({
        url: cohortAjaxUrl,
        dataType: 'json',
        async: true,
        data: myData,
        success: function(data) {          
          response = data;
        },
        error: function (xhr, ajaxOptions, thrownError) {
            alert(xhr.status);
            alert(thrownError);
          }
      });

      return response;
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
    // chart.addListener("clickSlice", pieClickHandler);
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
// 16894

function renderVlPatientDetails(vlCount, title)
{
    // console.table(vlData);
    
    jq("#detailsModal").modal("show");
    var html = "";
    var sn = 1;


    for(var i=0; i<vlData.length; i++){
        var patientData = vlData[i];
        if(vlCount == ">=1000 cp/ml" && parseFloat(vlData[i]["vlResult"]) >= 1000)
        {
            // html +=  buildVlPatientHtml(sn, vlData[i], "action");
            html += "<tr>"; 
            html += "<td >"+(i)+"</td>";
            html += "<td>"+patientData["pepfarId"]+"</td>";
            html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
            html += "<td>"+patientData["vlResult"]+"</td>";
            html += "<td>"+patientData["sampleCollectionDate"]+"</td>";
            //html += "<td>"+action+"</td>";        
            html += "</tr>";
            // sn++;
        }else if(vlCount == "51 - 999 cp/ml" && parseFloat(vlData[i]["vlResult"]) >= 51 && parseFloat(vlData[i]["vlResult"]) <1000)
        {
            // html +=  buildVlPatientHtml(sn, vlData[i], "action");
            // sn++;

            html += "<tr>"; 
            html += "<td >"+(i)+"</td>";
            html += "<td>"+patientData["pepfarId"]+"</td>";
            html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
            html += "<td>"+patientData["vlResult"]+"</td>";
            html += "<td>"+patientData["sampleCollectionDate"]+"</td>";
            //html += "<td>"+action+"</td>";        
            html += "</tr>";
        }
        else if(vlCount == "<51 cp/ml" && parseFloat(vlData[i]["vlResult"]) <=50)
        {
            // html +=  buildVlPatientHtml(sn, vlData[i], "action");
            // sn++;
            html += "<tr>"; 
            html += "<td >"+(i)+"</td>";
            html += "<td>"+patientData["pepfarId"]+"</td>";
            html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
            html += "<td>"+patientData["vlResult"]+"</td>";
            html += "<td>"+patientData["sampleCollectionDate"]+"</td>";
            //html += "<td>"+action+"</td>";        
            html += "</tr>";
        }  

        jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Current VL</th><th nowrap>Sample Collection Date</th></tr></thead><tbody id="detailsArea">'+html+'</tbody></table>');

        // 16911
        // oladoyin
        
    }
    
    // console.log(html);
    reInitializeDatatable(title);
    // jq("#detailsArea").html(html);
    
    //initialize datatable
    // reInitializeDatatable(title);
}

function graphClickHandler(event)
{
    var jq = jQuery;
    var isOptimum = event.graph.isOptimum;
    var chartType = event.graph.chartType;
    if(isOptimum == "vl"){
        renderVlPatientDetails(event.item.category, "Patient VL details");
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
            if(weight == "<3kg")
            {
                weightBand = "less_3";
            }
            else if(weight == "<20kg")
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
            if(weightBand == "less_3")
            {
                title += " < 3kg"
            }
            else if(weightBand == "less_20")
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
    if(weight == "less_3")
    {
        title += " <3kg";
    }else if(weight == "less_20")
    {
        title += " <20kg";
    }else if(weight == "bet_20_30"){
        title += " 20-30kg";
    }else{
        title += " >30kg";
    }
    renderPatientDetails(isOptimum, weight, regimen, title);
}

// NEW BAR CHART BUILDER
function barChartBuilder(data){
    AmCharts.makeChart("chartdiv",
				{
					"type": "serial",
					"categoryField": "category",
					"startDuration": 1,
					"handDrawScatter": 4,
					"theme": "light",
					"categoryAxis": {
						"gridPosition": "start"
					},
					"trendLines": [],
					"graphs": [
						{
							"balloonText": "[[title]] of [[category]]:[[value]]",
							"fillAlphas": 1,
							"fillColors": "#EC8C2A",
							"id": "AmGraph-1",
							"title": "Optimum",
							"type": "column",
							"valueField": "Optimum"
						},
						{
							"balloonText": "[[title]] of [[category]]:[[value]]",
							"fillAlphas": 1,
							"fillColors": "#008006",
							"id": "AmGraph-2",
							"title": "SubOptimum",
							"type": "column",
							"valueField": "SubOptimum"
						}
					],
					"guides": [],
					"valueAxes": [
						{
							"id": "ValueAxis-1",
							"title": "Values"
						}
					],
					"allLabels": [],
					"balloon": {},
					"legend": {
						"enabled": true,
						"useGraphSettings": true
					},
					"titles": [
						{
							"id": "Title-1",
							"size": 15,
							"text": "Optimum and SubOptimum ARV Chart"
						}
					],
					"dataProvider": data,
                    "zoomable":true,
                    "depth3D":10,
                    "angle":15
				}
			);
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

function PatientDetails(fullData, title)
{
    jq("#PatientDetails").html('<table class="dataTable table-responsive" style="width: 100% !important;"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Regimen</th><th nowrap>Regimen Line</th><th nowrap>Drug Strength</th><th nowrap>Drug Dosage</th> <th nowrap>Age</th> <th nowrap>Weight</th> <th nowrap>Weight band</th> <th nowrap>Weight Duration</th><th nowrap>Optimum Type</th></tr></thead><tbody id="detailsArea"></tbody></table>');
    
    var html = "";
    var sn = 1;
    for(var i=0; i<fullData.length; i++)
    {  
        
                html += buildPatientHtml2(sn, fullData[i],getWeightBandWithAge(fullData[i]["patientWeight"],fullData[i]["patientAge"]));
                sn++;
        
    }
    //reInitializeDatatable(title);
    jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

var setup_name = window.location.pathname.substring(1).split('/')[0];    

function buildPatientHtml2(i, patientData, weight)
{
    var optimumType = "SubOptimum";
    var style = 'style="background-color: #FFB6C1;"';
    var html = "";
    html += "<tr>"; 
    html += "<td >"+(i)+"</td>";
    html += "<td><a href='/"+setup_name+"/coreapps/clinicianfacing/patient.page?patientId="+patientData["patientId"]+"'>"+patientData["pepfarId"]+"</a></td>";    
    html += "<td>"+patientData["firstName"]+" "+patientData["lastName"]+"</td>";
    html += "<td>"+patientData["regimenLine"]+"</td>";
    html += "<td>"+patientData["regimenLine2"]+"</td>";
    html += "<td>"+patientData["drugStrength"]+"</td>";
    html += "<td>"+patientData["drugDosage"]+"</td>";
    html += "<td>"+patientData["patientAge"]+"</td>";
    html += "<td>"+patientData["patientWeight"]+"</td>";
    
    var weightBandHtml = "";
    if(weight == "less_3")
    {
        weightBandHtml = "< 3kg";
    }else if(weight == "less_20")
    {
        weightBandHtml = "< 20kg";
    }else if(weight == "bet_20_30"){
        weightBandHtml = "20-30kg"
    }else if(weight == "above_30"){
        weightBandHtml = ">30kg (ped)"
    }else{
         weightBandHtml = "Adult"
    }
    html += "<td>"+weightBandHtml+"</td>";
    html += "<td>"+getMonthsAgo(patientData["weightDate"])+" months</td>";
    if(isOptimumRegimen2(patientData["regimenConceptId"],patientData["regimenLine2"],patientData["patientWeight"],patientData["patientAge"]) === true)
    {optimumType = "Optimum"; style = 'style="background-color: #90EE90;"';}
    html += "<td "+style+">"+optimumType+"</td>";
    html += "</tr>";
    return html;
}

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

/*
function renderVlPatientDetails(vlCount, title)
{
    console.table(vlData);
    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Current VL</th><th nowrap>Sample Collection Date</th></tr></thead><tbody id="detailsArea"></tbody></table>');
    
    jq("#detailsModal").modal("show");
    var html = "";
    var sn = 1;
    
    
    for(var i=0; i<vlData.length; i++){
        if(parseFloat(vlData[i]["vlResult"]) >= 1000)
        {
            html +=  buildVlPatientHtml(sn, vlData[i], "action");
            sn++;
        }else if(parseFloat(vlData[i]["vlResult"]) >= 51 && parseFloat(vlData[i]["vlResult"]) <1000)
        {
            html +=  buildVlPatientHtml(sn, vlData[i], "action");
            sn++;
        }
        else if(parseFloat(vlData[i]["vlResult"]) <=50)
        {
            html +=  buildVlPatientHtml(sn, vlData[i], "action");
            sn++;
        }  
        // 16911
        // oladoyin
    }
    //reInitializeDatatable(title);
    jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}
*/

function renderEACPatientDetails(eacType, title)
{
    
    
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

    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Previous VL</th><th>Current VL</th></tr></thead><tbody id="detailsArea">'+html+'</tbody></table>');
    //reInitializeDatatable(title);
    // jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

function renderCD4PatientDetails(vlCount, title)
{
    
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

    jq("#tableContainer").html('<table class="dataTable"><thead><tr><th nowrap>S/N</th><th nowrap>Pepfar ID</th><th nowrap>Patient Name</th> <th nowrap>Current CD4+ Cell Count</th><th nowrap>Visit Date</th></tr></thead><tbody id="detailsArea">'+html+'</tbody></table>');
    
    //reInitializeDatatable(title);
    // jq("#detailsArea").html(html);
    
    //initialize datatable
   reInitializeDatatable(title);
}

function reInitializeDatatable(title)
{
   
    jq(".dataTable").DataTable({
             "bDestroy": true,
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
                            columns: [0,1,2,3,4,5,6,7,8,9,10]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    {
                        extend: 'excel',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4,5,6,7,8,9,10]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    {
                        extend: 'pdf',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4,5,6,7,8,9,10]
                        }
                        //messageTop: '<%= title; %>'
                    },
                    {
                        extend: 'print',
                        title:title,
                        exportOptions: {
                            columns: [0,1,2,3,4,5,6,7,8,9,10]
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

    alert(html);
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
    if(weight == "less_3")
    {
        weightBandHtml = "< 3kg";
    }else if(weight == "less_20")
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

// FLAWED BECAUSE THERE ARE CRITERIAS LIKE WEIGHT AND REGIMEN LINE TO DECIDE OPTIMUM OR SUBOPTIMUM
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

// FLAWED BECAUSE THERE ARE CRITERIAS LIKE WEIGHT AND REGIMEN LINE TO DECIDE OPTIMUM OR SUBOPTIMUM

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
    if (age <= 3) {
        ageBand = "less_3";
    } else if (age < 20) {
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
    if (age <= 3) {
        ageBand = "less_3";
    } else if (age < 20) {
        ageBand = "less_20";
    } else if (age >= 20 && age <= 30) {
        ageBand = "bet_20_30";
    } else if(age > 30){
        ageBand = "above_30";
    }
    return ageBand;
}

// NEW WEIGHT BAND

function  getWeightBandWithAge(weight,age) {
    var ageBand = "";
    if (weight < 3) {
        ageBand = "less_3";
    } else if ((age < 6) && (weight >= 3 && weight <20)) {
        ageBand = "less_20";
    } else if ((age < 19)  && (weight >= 20 && weight <30)) {
        ageBand = "bet_20_30";
    } else if((age < 19) && (weight >= 30)){
        ageBand = "above_30";
    }
    else if(age >= 19){
        ageBand = "adult";
    }
    return ageBand;
}

// NEW REGIMENS - FROM LATEST GUIDE - WITH NULLS
/*
var lessThan3 = [
    [165695,162561,1652,166187], // 1st Line Concepts
    ['AZT-3TC-RAL','AZT-3TC-LPV/r','AZT-3TC-NVP','AZT-3TC-DTG'], // First Line Associated Regimens
    [162561], // 2nd Line Concepts
    ['AZT-3TC-LPV/r'], // Second Line Associated Regimens            
]

var threeTo20 = [
    [165691,162200,162561,165696,165695,162563,162199,160124,1652,162561,165695], // 1st Line Concepts
    ['ABC-3TC-DTG','ABC-3TC-LPV/r','AZT-3TC-LPV/r','ABC-3TC-RAL','AZT-3TC-RAL','ABC-3TC-EFV','ABC-3TC-NVP','AZT-3TC-EFV','AZT-3TC-NVP','AZT-3TC-LPV/r','AZT-3TC-RAL'], // First Line Associated Regimens
    [162561,164511,162200,165696,165695], // 2nd Line Concepts
    ['AZT-3TC-LPV/r','AZT-3TC-ATV/r','ABC-3TC-LPV/r','ABC-3TC-RAL','AZT-3TC-RAL'], // Second Line Associated Regimens            
]

var twentyTo30 = [
    [165691,165681,,162200,165696,162563,162199,160124,1652,162561,165695], // 1st Line Concepts
    ['ABC-3TC-DTG','TDF-3TC-DTG','TAF-3TC-DTG','ABC-3TC-LPV/r','ABC-3TC-RAL','ABC-3TC-EFV','ABC-3TC-NVP','AZT-3TC-EFV','AZT-3TC-NVP','AZT-3TC-LPV/r','AZT-3TC-RAL'], // First Line Associated Regimens
    [162561,164511,162200,], // 2nd Line Concepts
    ['AZT-3TC-LPV/r','AZT-3TC-ATV/r','ABC-3TC-LPV/r','ABC-3TV-ATV/r',''], // Second Line Associated Regimens            
]

var thirtyAbove = [
    [165681,165682,165686,165687,,,165691,], // 1st Line Concepts
    ['TDF-3TC-DTG','TDF-FTC-DTG','TDF-3TC-EFV400','TDF-FTC-EFV400','TAF-3TC-DTG','TAF-FTC-DTG','ABC-3TC-DTG','AZT-3TC-EFV400'], // First Line Associated Regimens
    [164511,,162561,,164512,162201,165524,165523,166192,,165681,165682,166187,], // 2nd Line Concepts
    ['AZT-3TC-ATV/r','AZT-FTC-ATV/r','AZT-3TC-LPV/r','AZT-FTC-LPV/r','TDF-3TC-ATV/r','TDF-3TC-LPV/r','TDF-FTC-ATV/r','TDF-FTC-LPV/r','AZT-3TC-DRV/r','AZT-FTC-DRV/r','TDF-3TC-DTG','TDF-FTC-DTG','AZT-3TC-DTG','AZT-FTC-DTG'], // Second Line Associated Regimens            
]
*/


// NEW REGIMENS - FROM LATEST GUIDE - WITHOUT NULLS

var lessThan3 = [
    [165695,162561,1652,166187], // 1st Line Concepts
    ['AZT-3TC-RAL','AZT-3TC-LPV/r','AZT-3TC-NVP','AZT-3TC-DTG'], // First Line Associated Regimens
    [162561], // 2nd Line Concepts
    ['AZT-3TC-LPV/r'], // Second Line Associated Regimens            
]

var threeTo20 = [
    [165691,162200,162561,165696,165695,162563,162199,160124,1652,162561,165695], // 1st Line Concepts
    ['ABC-3TC-DTG','ABC-3TC-LPV/r','AZT-3TC-LPV/r','ABC-3TC-RAL','AZT-3TC-RAL','ABC-3TC-EFV','ABC-3TC-NVP','AZT-3TC-EFV','AZT-3TC-NVP','AZT-3TC-LPV/r','AZT-3TC-RAL'], // First Line Associated Regimens
    [162561,164511,162200,165696,165695], // 2nd Line Concepts
    ['AZT-3TC-LPV/r','AZT-3TC-ATV/r','ABC-3TC-LPV/r','ABC-3TC-RAL','AZT-3TC-RAL'], // Second Line Associated Regimens            
]

var twentyTo30 = [
    [165691,165681,162200,165696,162563,162199,160124,1652,162561,165695], // 1st Line Concepts
    ['ABC-3TC-DTG','TDF-3TC-DTG','ABC-3TC-LPV/r','ABC-3TC-RAL','ABC-3TC-EFV','ABC-3TC-NVP','AZT-3TC-EFV','AZT-3TC-NVP','AZT-3TC-LPV/r','AZT-3TC-RAL'], // First Line Associated Regimens
    [162561,164511,162200], // 2nd Line Concepts
    ['AZT-3TC-LPV/r','AZT-3TC-ATV/r','ABC-3TC-LPV/r'], // Second Line Associated Regimens            
]

var thirtyAbove = [
    [165681,165682,165686,165687,165691], // 1st Line Concepts
    ['TDF-3TC-DTG','TDF-FTC-DTG','TDF-3TC-EFV400','TDF-FTC-EFV400','ABC-3TC-DTG'], // First Line Associated Regimens
    [164511,162561,164512,162201,165524,165523,166192,165681,165682,166187], // 2nd Line Concepts
    ['AZT-3TC-ATV/r','AZT-3TC-LPV/r','TDF-3TC-ATV/r','TDF-3TC-LPV/r','TDF-FTC-ATV/r','TDF-FTC-LPV/r','AZT-3TC-DRV/r','TDF-3TC-DTG','TDF-FTC-DTG','AZT-3TC-DTG'], // Second Line Associated Regimens            
]

var allRegimen = [lessThan3, threeTo20, twentyTo30, thirtyAbove];

// GET REGIMEN ALL INFO FROM EXCEL (Updatable at anytime)
function getRegimens(){
   
    var allRegimen = [lessThan3, threeTo20, twentyTo30, thirtyAbove];
    
    return allRegimen;
}

// NEW isOPTIMUM CHECKS
function isOptimumRegimen2(regimenConceptId,regimenLine,weight,age){ // include dosage parameter for checks later
    // var allRegimen = [];
    // allRegimen = getRegimens();
    var ageBand = getWeightBandWithAge(weight,age);

    switch (ageBand) {
        case "less_3":
            if(lessThan3[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "less_3" ){
                return true;
            }else if(lessThan3[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "less_3"){
                return true;
            }else{
                return false;
            }
            
            break;
        
        case "less_20":
            if(threeTo20[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "less_20" ){
                return true;
            }else if(threeTo20[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "less_20"){
                return true;
            }else{
                return false;
            }
            
                break;
        case "bet_20_30":
                if(twentyTo30[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "less_20_30" ){
                    return true;
                }else if(twentyTo30[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "less_20_30"){
                    return true;
                }else{
                    return false;
                }
                
                break;
        case "above_30":
                if(thirtyAbove[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "above_30" ){
                    return true;
                }else if(thirtyAbove[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "above_30"){
                    return true;
                }else{
                    return false;
                }
                
                break;        
        case "adult":
                if(thirtyAbove[0].includes(regimenConceptId) && regimenLine == "Adult 1st line ARV regimen" && ageBand == "adult" ){
                    return true;
                }else if(thirtyAbove[2].includes(regimenConceptId) && regimenLine == "Adult 2nd line ARV regimen" && ageBand == "adult"){
                    return true;
                }else{
                    return false;
                }
                
                break;        
    
        
        default:
            return false;
            break;
    }

    
}

// NEW OPTIMUM TYPE CHECK
function getOptimalType2(regimenConceptId,regimenLine,weight,age){
    if(isOptimumRegimen2(regimenConceptId,regimenLine,weight,age) === true){
        return 1;
    }else{
        return -1;
    }
}


////////// NEW UNIMPLEMENTED CODES //////////
// NEW isOPTIMUM CHECKS WITH STRENGTH AND DOSAGE
function isOptimumRegimen3(regimenConceptId,regimenLine,weight,age,dosage,strength){ // include dosage parameter for checks later
    // var allRegimen = [];
    // allRegimen = getRegimens();
    var ageBand = getWeightBandWithAge(weight,age);

    switch (ageBand) {
        case "less_3":
            if(lessThan3[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "less_3" ){
                return true;
            }else if(lessThan3[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "less_3"){
                return true;
            }else{
                return false;
            }
            
            break;
        
        case "less_20":
            if(threeTo20[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "less_20" ){
                return true;
            }else if(threeTo20[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "less_20"){
                return true;
            }else{
                return false;
            }
            
                break;
        case "bet_20_30":
                if(twentyTo30[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "less_20_30" ){
                    return true;
                }else if(twentyTo30[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "less_20_30"){
                    return true;
                }else{
                    return false;
                }
                
                break;
        case "above_30":
                if(thirtyAbove[0].includes(regimenConceptId) && regimenLine == "Child 1st line ARV regimen" && ageBand == "above_30" ){
                    return true;
                }else if(thirtyAbove[2].includes(regimenConceptId) && regimenLine == "Child 2nd line ARV regimen" && ageBand == "above_30"){
                    return true;
                }else{
                    return false;
                }
                
                break;        
        case "adult":
                if(thirtyAbove[0].includes(regimenConceptId) && regimenLine == "Adult 1st line ARV regimen" && ageBand == "adult" ){
                    return true;
                }else if(thirtyAbove[2].includes(regimenConceptId) && regimenLine == "Adult 2nd line ARV regimen" && ageBand == "adult"){
                    return true;
                }else{
                    return false;
                }
                
                break;        
    
        
        default:
            return false;
            break;
    }

    
}

// NEW OPTIMUM TYPE CHECK WITH STRENGTH AND DOSAGE
function getOptimalType3(regimenConceptId,regimenLine,weight,age){
    if(isOptimumRegimen2(regimenConceptId,regimenLine,weight,age) === true){
        return 1;
    }else{
        return -1;
    }
}

// GET REGIMEN NAMES
function getRegimenNames(ageBand){
    
        // var allRegimen = [];
        // allRegimen = getRegimens();
        var allRegimenNames = "{";
        
        switch (ageBand) {
            case "less_3":    
                for(let i=0; i<=lessThan3[0].length-1; i++){
                    if(lessThan3[0][i]!=""){
                        if(i < lessThan3[0].length-1){
                            allRegimenNames += '"regimen_'+lessThan3[0][i]+'":{"title":"'+lessThan3[1][i] +'", "total":0}, ';
                        }
                    }

                    if(lessThan3[2][i]!=""){
                        if(i < lessThan3[2].length-1){
                            allRegimenNames += '"regimen_'+lessThan3[2][i]+'":{"title":"'+lessThan3[3][i] +'", "total":0}, ';
                        }
                    }    
                    
                    
                };
                allRegimenNames +='"regimen_0":{"title":"MISC", "total":0}}';
                return allRegimenNames;
            
            case "less_20":
    
                for(let i=0; i<=threeTo20[0].length-1; i++){
                    if(threeTo20[0][i]!=""){
                        if(i < threeTo20[0].length-1){
                            allRegimenNames += '"regimen_'+threeTo20[0][i]+'":{"title":"'+threeTo20[1][i] +'", "total":0}, ';
                        }
                    }

                    if(threeTo20[2][i]!=""){
                        if(i < threeTo20[2].length-1){
                            allRegimenNames += '"regimen_'+threeTo20[2][i]+'":{"title":"'+threeTo20[3][i] +'", "total":0}, ';
                        }
                    }                   
                };
                allRegimenNames +='"regimen_0":{"title":"MISC", "total":0}}';

                return allRegimenNames;
    
            case "bet_20_30":
    
                for(let i=0; i<=twentyTo30[0].length-1; i++){
                    if(twentyTo30[0][i]!=""){
                        if(i < twentyTo30[0].length-1){
                            allRegimenNames += '"regimen_'+twentyTo30[0][i]+'":{"title":"'+twentyTo30[1][i] +'", "total":0}, ';
                        }
                    }

                    if(twentyTo30[2][i]!=""){
                        if(i < twentyTo30[2].length-1){
                            allRegimenNames += '"regimen_'+twentyTo30[2][i]+'":{"title":"'+twentyTo30[3][i] +'", "total":0}, ';
                        }
                    }                   
                };
                allRegimenNames +='"regimen_0":{"title":"MISC", "total":0}}';
                return allRegimenNames;
    
            case "above_30":
    
                for(let i=0; i<=thirtyAbove[0].length-1; i++){
                    if(thirtyAbove[0][i]!=""){
                        if(i < thirtyAbove[0].length-1){
                            allRegimenNames += '"regimen_'+thirtyAbove[0][i]+'":{"title":"'+thirtyAbove[1][i] +'", "total":0}, ';
                        }
                    }

                    if(thirtyAbove[2][i]!=""){
                        if(i < thirtyAbove[2].length-1){
                            allRegimenNames += '"regimen_'+thirtyAbove[2][i]+'":{"title":"'+thirtyAbove[3][i] +'", "total":0}, ';
                        }
                    }                   
                };
                allRegimenNames +='"regimen_0":{"title":"MISC", "total":0}}';
                return allRegimenNames;
        
            case "adult":

                for(let i=0; i<=thirtyAbove[0].length-1; i++){
                    if(thirtyAbove[0][i]!=""){
                        if(i < thirtyAbove[0].length-1){
                            allRegimenNames += '"regimen_'+thirtyAbove[0][i]+'":{"title":"'+thirtyAbove[1][i] +'", "total":0}, ';
                        }
                    }

                    if(thirtyAbove[2][i]!=""){
                        if(i < thirtyAbove[2].length-1){
                            allRegimenNames += '"regimen_'+thirtyAbove[2][i]+'":{"title":"'+thirtyAbove[3][i] +'", "total":0}, ';
                        }
                    }                   
                };
                allRegimenNames +='"regimen_0":{"title":"MISC", "total":0}}';
                return allRegimenNames;
        
    
            default:
                allRegimenNames +='"regimen_0":{"title":"MISC", "total":0}}';
                return allRegimenNames;
        }

        
}
