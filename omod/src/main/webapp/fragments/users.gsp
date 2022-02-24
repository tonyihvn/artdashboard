<style>
#PatientDetails table {  
    border-collapse: collapse;
}
#PatientDetails th,td {
    padding: 0;
}
</style>
<%
ui.includeJavascript("artdashboard", "jquery-3.3.1.js")
ui.includeJavascript("artdashboard", "jquery.dataTables.min.js")
ui.includeJavascript("artdashboard", "datatables.min.js")
ui.includeJavascript("artdashboard", "buttons.flash.min.js")
ui.includeJavascript("artdashboard", "jquery-ui.js")
ui.includeJavascript("artdashboard", "jszip.min.js")
ui.includeJavascript("artdashboard", "myAjax.js")
ui.includeJavascript("artdashboard", "pdfmake.min.js")
ui.includeJavascript("artdashboard", "vfs_fonts.js")
ui.includeCss("artdashboard", "bootstrap.min.css")
ui.includeJavascript("artdashboard", "bootstrap.min.js")
ui.includeJavascript("artdashboard", "buttons.html5.min.js")
ui.includeJavascript("artdashboard", "buttons.print.min.js")
ui.includeJavascript("artdashboard", "datatable.button.min.js")
ui.includeCss("artdashboard", "buttons.dataTables.min.css")
ui.includeCss("artdashboard", "jquery.dataTables.min.css")
ui.includeCss("artdashboard", "myStyle.css")
def id = config.id
%>


<h3>ART Optimization Dashboard</h3>

<fieldset>
<legend>Filters</legend>
<div class="row">
    <div class="col-sm-3 col-md-2"><label class="pull-right">Start date</label></div>
    <div class="col-sm-3 col-md-3" >
        <input type="text" name="startDate" id="startDate" class="form-control date" readonly="readonly"  />
    
    </div>
    
    <div class="col-sm-3 col-md-2"><label class="pull-right">End date</label></div>
    <div class="col-sm-3 col-md-3">
        <input type="text" name="endDate" id="endDate" class="form-control date" readonly="readonly"  />
    
    </div>
    <div class="col-sm-3 col-md-2">
        <button class="btn btn-default" id="filterChart">Filter</button>
    </div>
</div>
</fieldset>
<br/>
<br/>
<div id="graphArea">
    <div class="row">
    
        <div class="col-sm-12">
            <nav>
                <div class="nav nav-tabs" id="nav-tab" role="tablist">
                  <a class="nav-item nav-link active" id="nav-summary-tab" data-toggle="tab" href="#nav-summary" role="tab" aria-controls="nav-summary" aria-selected="true">ARV Analysis</a>
                  <a class="nav-item nav-link" id="nav-optimal-tab" data-toggle="tab" href="#nav-optimal" role="tab" aria-controls="nav-optimal" aria-selected="false">Tx Monitoring</a>
                  <a class="nav-item nav-link" id="nav-all-qoc" data-toggle="tab" href="#nav-qoc" role="tab" aria-controls="nav-qoc" aria-selected="false">Quality of Care</a>
                  <!--
                  <a class="nav-item nav-link" id="nav-all-adr" data-toggle="tab" href="#nav-adr" role="tab" aria-controls="nav-adr" aria-selected="false">ADR and Pharmacovigilance</a>
                  <a class="nav-item nav-link" id="nav-all-ret" data-toggle="tab" href="#nav-ret" role="tab" aria-controls="nav-ret" aria-selected="false">Retention Analysis</a>
                  -->
                </div>
            </nav>

            <div class="tab-content" id="nav-tabContent">
                <div class="tab-pane fade show active" id="nav-summary" role="tabpanel" aria-labelledby="nav-summary-tab">${ ui.includeFragment("artdashboard", "summary") }</div>
                <div class="tab-pane fade" id="nav-optimal" role="tabpanel" aria-labelledby="nav-optimal-tab">${ ui.includeFragment("artdashboard", "treatment_monit") }</div>
                <!--
                    <div class="tab-pane fade" id="nav-eac" role="tabpanel" aria-labelledby="nav-eac-tab">${ ui.includeFragment("artdashboard", "eac") }</div>
                    <div class="tab-pane fade" id="nav-all" role="tabpanel" aria-labelledby="nav-all-tab">${ ui.includeFragment("artdashboard", "all") }</div>
                -->
                    <div class="tab-pane fade" id="nav-qoc" role="tabpanel" aria-labelledby="nav-all-qoc">${ ui.includeFragment("artdashboard", "qoc") }</div>
            
            </div>
              
        </div>
    </div>
    
    <div id="loadingBg">
        <div id="loader" class="center"></div>
    </div>
</div>


<!-- Modal <div class="col-sm-6"></div> --> 
<div class="modal fade" id="detailsModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog" role="document" >
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="modalTitle">Details</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
          <div class="row">
              <div class="col-sm-12 " id="tableContainer">
                <div class="table-responsive  box-body">
                    <table class="dataTable ">
                        <thead>
                            <tr>
                                <th>S/N</th><th>Patient Name</th> <th>Age</th> <th>Weight</th> <th>Weight band</th>
                            </tr>
                        </thead>
                        <tbody id="detailsArea">

                        </tbody>
                    </table>
                </div>
              </div>
              
          </div>
          
         
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
      </div>
    </div>
  </div>
</div>

<script src="../ms/uiframework/resource/artdashboard/amcharts/amcharts/amcharts.js"></script>
<script src="../ms/uiframework/resource/artdashboard/amcharts/amcharts/serial.js" type="text/javascript"></script>
<script src="../ms/uiframework/resource/artdashboard/amcharts/amcharts/light.js" type="text/javascript"></script>
<script src="../ms/uiframework/resource/artdashboard/amcharts/amcharts/pie.js" type="text/javascript"></script>

 
<script type="text/javascript">
 var jq = jQuery;
 
     jq('.date').datepicker({
            dateFormat: 'yy-mm-dd',
            changeYear: true,
            changeMonth:true,
            yearRange: "-30:+0",
            autoclose: true
    });
    jq(document).ready(function(e){
      // jq("#detailsModal").modal("show");
      
      jq("#filterChart").click(function(e){
           jq("#loadingBg").removeClass("hidden");
           buildCharts();
           // getQoc();
      });
        buildCharts();  
    });
    //jq("#detailsModal").modal("show");
    //var fullData ;
    var vlData ;
    var eacData;
    var cd4Data;

    var totalOnTreatment = 0;
    var totaltxOptimal = 0;

    var colors = new Array("#249e4d", "#6ef09a",  "#b57e1f", "#1a6e37", "#d6a724",  "#c45123","#2e85bf","#366585","#46a6e8"  );
    function buildCharts(){

        var startDate = jq("#startDate").val();
        var endDate = jq("#endDate").val();
        
        var less3Optimaltotal = 0;
        var threeto20Optimaltotal = 0;
        var twentyto30Optimaltotal = 0;
        var above30Optimaltotal = 0;
        var adultOptimaltotal = 0;
        
        var less3SubOptimaltotal = 0;
        var threeto20SubOptimaltotal = 0;
        var twentyto30SubOptimaltotal = 0;
        var above30SubOptimaltotal = 0;
        var adultSubOptimaltotal = 0;
        
        myAjax({startDate:startDate, endDate:endDate}, '${ ui.actionLink("getRegimenGraphData") }').then(function(response){
            
            var data = JSON.parse(response);
            fullData = data;
            // console.table(fullData);
            
            jq("#loadingBg").addClass("hidden");
            // Generate the Entire Data Table
            
            PatientDetails(data, "All Regimen Data");

            for(var i=0; i<fullData.length; i++)
            {     
        
                var weightBand = getWeightBandWithAge(fullData[i]["patientWeight"],fullData[i]["patientAge"]);
                
                if(isOptimumRegimen2(fullData[i]["regimenConceptId"],fullData[i]["regimenLine2"],fullData[i]["patientWeight"],fullData[i]["patientAge"]) === true){
                    
                    if(weightBand=="less_3"){
                        less3Optimaltotal++;
                    }else if(weightBand=="less_20"){
                        threeto20Optimaltotal++;
                    }else if(weightBand=="bet_20_30"){
                        twentyto30Optimaltotal++;
                    }else if(weightBand=="above_30"){
                        above30Optimaltotal++;
                    }else if(weightBand=="adult"){
                        adultOptimaltotal++;
                    }
                }else{
                    if(weightBand=="less_3"){
                        less3SubOptimaltotal++;
                    }else if(weightBand=="less_20"){
                        threeto20SubOptimaltotal++;
                    }else if(weightBand=="bet_20_30"){
                        twentyto30SubOptimaltotal++;
                    }else if(weightBand=="above_30"){
                        above30SubOptimaltotal++;
                    }else if(weightBand=="adult"){
                        adultSubOptimaltotal++;
                    }
                }
            
            }


            totalOnTreatment = fullData.length;

            totaltxOptimal = less3Optimaltotal+threeto20Optimaltotal+twentyto30Optimaltotal+above30Optimaltotal+adultOptimaltotal;


            jq('#less3_optimal').html(less3Optimaltotal);
            jq('#3_20_optimal').html(threeto20Optimaltotal);
            jq('#20_30_optimal').html(twentyto30Optimaltotal);
            jq('#30above_optimal').html(above30Optimaltotal);
            jq('#adults_optimal').html(adultOptimaltotal);

            jq('#less3_suboptimal').html(less3SubOptimaltotal);
            jq('#3_20_suboptimal').html(threeto20SubOptimaltotal);
            jq('#20_30_suboptimal').html(twentyto30SubOptimaltotal);
            jq('#30above_suboptimal').html(above30SubOptimaltotal);
            jq('#adults_suboptimal').html(adultSubOptimaltotal);


            // BUILD REGIMEN ANALYSIS BAR CHART
            // Define data
            var data = [{ 
            category: "<3Kg", 
            Optimum: less3Optimaltotal, 
            SubOptimum: less3SubOptimaltotal 
            }, { 
            category: "3-20Kg", 
            Optimum: threeto20Optimaltotal, 
            SubOptimum: threeto20SubOptimaltotal 
            }, { 
            category: "20-30Kg", 
            Optimum: twentyto30Optimaltotal, 
            SubOptimum: twentyto30SubOptimaltotal 
            }, { 
            category: ">30Kg", 
            Optimum: above30Optimaltotal, 
            SubOptimum: above30SubOptimaltotal 
            }, { 
            category: "Adults", 
            Optimum: adultOptimaltotal, 
            SubOptimum: adultSubOptimaltotal 
            }];

            barChartBuilder(data);


            // GET TX OPTIMAL CHART

             var percentagelost = (totaltxOptimal/totalOnTreatment)*100;

            var txOptimalChartData = new Array();
                
                txOptimalChartData.push({
                    title:"Tx_Optimal",
                    totalOnTreatment: totalOnTreatment,
                    txOptimal : totaltxOptimal,
                    // percentage:percentagelost
                    
                })
                
            
             var legendData = [{color:"#589BD4", title:"Total Patient on treatment(Tx Curr)"},
                       {color:"#E2932B", title:"Tx_Optimal"},
                       // {color:"#bf6c19", title:"Percentage Lost"}      
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "totalOnTreatment", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total patients on treatment", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txOptimal", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"Tx_Optimal", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                   // {field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                  ]
              
               //create chart
                artDashboard_buildBarCharts("VL Access", txOptimalChartData, graphArray, "title", "txOptimal", "", "none", legendData, true, true, 0, 0);

        
        });

        // GET TX ML
        myAjax({startDate:startDate, endDate:endDate}, "users/getTxMl.action").then(function(response){
                var data = JSON.parse(response);

                // var previousTxCurrno = data["previousTxCurrno"];
                var previousTxCurrno = totalOnTreatment;
                var txMlno = data["txMlno"];
 
                var txMlData = new Array();
                txMlData.push({
                    title:"Tx_ML",
                    txCurr: previousTxCurrno,
                    txML : txMlno,
                    // txCurr: Object.keys(data.previousTxCurr).length,
                    // txML : Object.keys(data.txMl).length,
                    // percentage:100                    
                });
                
                var legendData = [{color:"#589BD4", title:"Denominator (Tx Curr at the start of Quarter)"},
                       {color:"#E2932B", title:"Numerator(Tx ML)"},
                       //{color:"#bf6c19", title:"Percentage Lost"}      
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "txCurr", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"TX Curr for Previous Quarter", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txML", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"TX ML", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                   //{field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                  ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", txMlData, graphArray, "title", "txMl", "", "none", legendData, true, true, 0, 0);
               
                
               
            }).catch(function(xhr, status, error){
                console.log(xhr);
        });

        // FOR VIRAL LOAD ANALYSIS
        myAjax({startDate:startDate, endDate:endDate}, '${ ui.actionLink("getLabGraphData") }').then(function(response){
            
            var vlChartData = new Array();
           /*
            var aboveOr1000 = 0;
            var bet200_1000 = 0;
            var less200 = 0;
            */
            var aboveOr1000 = 0;
            var bet51_999 = 0;
            var less50 = 0;
            
            var data = JSON.parse(response);
            // console.table(data);
            vlData = data;
            for(var i=0; i<data.length; i++){
                if(parseFloat(data[i]["vlResult"]) >= 1000)
                {
                    aboveOr1000++;
                }else if(parseFloat(data[i]["vlResult"]) >= 51 && parseFloat(data[i]["vlResult"]) <1000)
                {
                    bet51_999++;
                }
                else if(parseFloat(data[i]["vlResult"]) <=50)
                {
                    less50++;
                }  
                // 16911
                // oladoyin
            }
              
              vlChartData.push(
                    {
                        vlRange:">=1000 cp/ml",
                        vlCount:aboveOr1000,
                        weightBand:"#60aaaa",
                        color:"#e35b17"
                     },
                      {
                        vlRange:"51 - 999 cp/ml",
                        vlCount:bet51_999,
                        color:"#edc009"

                     },
                     {
                        vlRange:"<51 cp/ml",
                        vlCount:less50,
                        color:"#23ba58"

                     },
                      
                 );
                 var legendData = [{color:"#e35b17", title:"Patient with VL >=1000 cp/ml"}, {color:"#edc009", title:"Patient with VL between 51 - 999 cp/ml"},
                        {color:"#23ba58", title:"Patient with VL <51 cp/ml"},      
                 ]                 
              

                     var graphArray = [
                        {field: "vlCount", description:"Total Patients ", title:"Total Patients", isOptimum:"vl", chartType:"vlAnalysis"},
                       ]
                   //create chart
                   buildBarCharts("Total patients", vlChartData, graphArray, "vlRange", "vl_analysis", "", "none", legendData, false);
             
        });

        // FOR EAC DATA
        myAjax({startDate:startDate, endDate:endDate}, '${ ui.actionLink("getEACGraphData") }').then(function(response){
            var data = JSON.parse(response);
            eacData = data;
            // console.table(eacData);
           
            var receivingEAC = 0;
            var receivedEACVLS = 0;
            var receivedEACVLE = 0;
            for(var i=0; i<eacData.length; i++)
            {
                var eacDate = Date.parse(eacData[i]["encounterDate"]);
                
                if((eacDate + (30 * 24 * 60 * 60 * 1000) < new Date().getTime()))
                {
                      receivingEAC++;
                }
                
                if(eacData[i]["secondToLastVl"]<1000)
                {
                    receivedEACVLS++;
                }
                
                if(typeof eacData[i]["vlResult"] != "undefined" )
                {
                    if(eacData[i]["secondToLastVl"] >  eacData[i]["vlResult"])
                    {
                        receivedEACVLE++
                    }
                }
                
               
            }
            
            var eacChartData = new Array();
            
            eacChartData.push(
                 { category:"receivingEAC",receivingEAC:receivingEAC, color:"#ffe699" },
                 { category:"receivedEACVLS",receivedEACVLS:receivedEACVLS, color:"#ffd966"},
                 { category:"receivedEACVLE",receivedEACVLE:receivedEACVLE, color:"#bf9000"},
                  
               );
                 var graphArray = [
                    {field: "receivingEAC", description:" # receiving EAC", title:"# receiving EAC", color:"#ffe699",  isOptimum:"eac"},
                    {field: "receivedEACVLS", description:"  #", title:"# received EAC with VLS", color:"#ffd966",  isOptimum:"eac"},
                    {field: "receivedEACVLE", description:"  #", title:"# received EAC on 1L with VL Elevation", color:"#bf9000",  isOptimum:"eac"}
                   ]
               //create chart
               
            var legendData = [{color:"#ffe699", title:"# receiving EAC"}, {color:"#ffd966", title:"#received EAC with VLS"},
                        {color:"#bf9000", title:"#received EAC on 1L with VL Elevation"},      
                 ]
               buildBarCharts("Total patients", eacChartData, graphArray, "category", "eacChart", "", "none", legendData, false);
            
        });
         
        // CD4 COUNT ANALYSIS
        myAjax({startDate:startDate, endDate:endDate}, '${ ui.actionLink("getCD4GraphData") }').then(function(response){
            var data = JSON.parse(response);
            cd4Data = data;
            // console.log(JSON.stringify(data));
           
            var goodCd4 = 0;
            var badCd4 = 0;
           
            for(var i=0; i<cd4Data.length; i++)
            {
                if(cd4Data[i]["cd4Count"] < 200)
                {
                    badCd4++;
                }
                
               else{
                    goodCd4++;
               }
               
            }
            
            var cd4ChartData = new Array();
            
            var legendData = [{color:"#AF5122", title:"# of patients with CD4+ cell count <200"}, {color:"#27A038", title:"# of patients with CD4+ cell count >200"},      
                 ]
            
            cd4ChartData.push(
                 { category:"# of patients with CD4+ cell count <200",badCd4:badCd4, color:"#AF5122"},
                 { category:"# of patients with CD4+ cell count >200",goodCd4:goodCd4, color:"#27A038"},
                
               );
                 var graphArray = [
                    {field: "badCd4", description:"  ", title:"# of patients with CD4+ cell count <200", color:"#AF5122",  isOptimum:"cd4"},
                    {field: "goodCd4", description:"  ", title:"# of patients with CD4+ cell count >200", color:"#27A038",  isOptimum:"cd4"}
                   ]
               //create chart
               buildBarCharts("Total patients", cd4ChartData, graphArray, "category", "cd4Analysis", "", "none", legendData, false);
            
        });
        
    }
           
   // buildCharts();  

 </script>