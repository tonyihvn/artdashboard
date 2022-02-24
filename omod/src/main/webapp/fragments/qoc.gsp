

</br>
    <div class="row">
        <div class="col-sm-6">
            <h4>TX_ML</h4>
            <div id="txMl" style="width: 100%; height: 550px;"></div>
        </div>
         
         <div class="col-sm-6">
             <h3>TX RTT</h3>
            <div id="txRTT" class="chart" style="width: 100%; height: 550px;"></div>
        </div>
        <div class="col-sm-6">
             <h3>Tx PVLS</h3>
            <div id="txPvls" class="chart" style="width: 100%; height: 550px;"></div>
        </div>
        <div class="col-sm-6">
            <h3>TX Optimal</h3>
            <div id="txOptimal" class="chart" style="width: 100%; height: 550px;"></div>
        </div>
    </div>
     <br/>
    <div class="row">
        <div class="col-sm-6">
            <h4>TX_VL_UND</h4>
            <div id="txVLUND" style="width: 100%; height: 550px;"></div>
        </div>

        <div class="col-sm-6">
            <h4>TX_Curr</h4>
            <div id="txCurr" style="width: 100%; height: 350px;"></div>
        </div>        
        
    </div>
    <div class="row">
        
        <div class="col-sm-6">
            <h3>TX New</h3>
            <div id="txNew" class="chart" style="width: 100%; height: 350px;"></div>
        </div>
        <div class="col-sm-6">
            <h4>TX ADR</h4>
            <div id="txADR" style="width: 100%; height: 550px;"></div>
        </div>
        
    </div>
   
    
  <script type="text/javascript">
      var jq = jQuery;
       var txCurrData = new Array();
        var txNewData = new Array();
      function getQoc()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            myAjax({startDate:startDate, endDate:endDate}, "users/getActivePts.action").then(function(response){
                var data = JSON.parse(response);
                var less1=0;
                var age_1_4 = 0;
                var age_5_9 = 0;
                var age_10_14 = 0;
                var age_15_19 = 0;
                var age_20_above = 0;

                // console.table(data);
                for(var i=0; i<data.length; i++)
                {
                    if(data[i]["patientAge"] < 1)
                    {
                      less1++;
                    }
                    else if(data[i]["patientAge"] >= 1 && data[i]["patientAge"] <=4)
                    {
                      age_1_4++;
                    }
                    else if(data[i]["patientAge"] >= 5 && data[i]["patientAge"] <=9)
                    {
                      age_5_9++;
                    }
                    else if(data[i]["patientAge"] >= 10 && data[i]["patientAge"] <=14)
                    {
                      age_10_14++;
                    }
                    else if(data[i]["patientAge"] >= 15 && data[i]["patientAge"] <=19)
                    {
                      age_15_19++;
                    }
                    else if(data[i]["patientAge"] >= 20)
                    {
                      age_20_above++;
                    }
                    
                }
                
                
                txCurrData.push({title: "<1", value: less1, weightBand:"less_1", regimen:"less_1", isOptimum:"true" })
                txCurrData.push({title: "1-4", value: age_1_4, weightBand:"1_4", regimen:"1_4", isOptimum:"true" })
                txCurrData.push({title: "5-9", value: age_5_9, weightBand:"5_9", regimen:"5_9", isOptimum:"true" })
                txCurrData.push({title: "10-14", value: age_10_14, weightBand:"10_14", regimen:"10_14", isOptimum:"true" })
                txCurrData.push({title: "15-19", value: age_15_19, weightBand:"15_19", regimen:"15_19", isOptimum:"true" })
                txCurrData.push({title: ">20", value: age_20_above, weightBand:">20", regimen:">20", isOptimum:"true" })

                
                
                var legendData = [{color:"#589BD4", title:"Denominator (Tx Curr at the start of Quarter)"},
                       {color:"#E2932B", title:"Numerator(Tx ML)"},
                        {color:"#E2932B", title:"Numerator(Tx ML)"},
                         {color:"#E2932B", title:"Numerator(Tx ML)"},
                       //{color:"#bf6c19", title:"Percentage Lost"}      
                ]
                
                
                buildPieCharts(txCurrData, "value", "title", "txCurr", legendData);
            }).catch(function(xhr, status, error){
                console.log(xhr);
            });
      }
      
      function getTxNew()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            myAjax({startDate:startDate, endDate:endDate}, "users/getTxNew.action").then(function(response){
                var data = JSON.parse(response);
                var less1 = 0;
                var age_1_4 = 0;
                var age_5_9 = 0;
                var age_10_14 = 0;
                var age_15_19 = 0;
                var age_20_above = 0;
                for(var i=0; i<data.length; i++)
                {
                    if(data[i]["patientAge"] < 1)
                    {
                      less1++;
                    }
                    else if(data[i]["patientAge"] >= 1 && data[i]["patientAge"] <=4)
                    {
                      age_1_4++;
                    }
                    else if(data[i]["patientAge"] >= 5 && data[i]["patientAge"] <=9)
                    {
                      age_5_9++;
                    }
                    else if(data[i]["patientAge"] >= 10 && data[i]["patientAge"] <=14)
                    {
                      age_10_14++;
                    }
                    else if(data[i]["patientAge"] >= 15 && data[i]["patientAge"] <=19)
                    {
                      age_15_19++;
                    }
                    else if(data[i]["patientAge"] >= 20)
                    {
                      age_20_above++;
                    }
                    
                }
                
                
                txNewData.push({title: "<1", value: less1, weightBand:"less_1", regimen:"less_1", isOptimum:"true" })
                txNewData.push({title: "1-4", value: age_1_4, weightBand:"1_4", regimen:"1_4", isOptimum:"true" })
                txNewData.push({title: "5-9", value: age_5_9, weightBand:"5_9", regimen:"5_9", isOptimum:"true" })
                txNewData.push({title: "10-14", value: age_10_14, weightBand:"10_14", regimen:"10_14", isOptimum:"true" })
                txNewData.push({title: "15-19", value: age_15_19, weightBand:"15_19", regimen:"15_19", isOptimum:"true" })
                txNewData.push({title: ">20", value: age_20_above, weightBand:">20", regimen:">20", isOptimum:"true" })

                buildPieCharts(txNewData, "value", "title", "txNew");
            }).catch(function(xhr, status, error){
                console.log(xhr);
            });
      }
      
      /*
      function getTxMl()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
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
      }
      */
      
      function getTxRTT()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            myAjax({startDate:startDate, endDate:endDate}, "users/getTxRTT.action").then(function(response){
                var data = JSON.parse(response);
                
                var txRTTData = new Array();
                txRTTData.push({
                    title:"Tx_RTT",
                    previousLTFU: data["previousLTFUno"],
                    txRTT :  data["txRTTno"],
                    // percdentage:100
                    
                })
                
                
                
                var legendData = [{color:"#589BD4", title:"Denominator (LTFU)"},
                       {color:"#E2932B", title:"Numerator(Tx RTT)"},
                       //{color:"#bf6c19", title:"Percentage Lost"}      
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "previousLTFU", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"LTFU", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txRTT", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"TX RTT", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                   //{field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                  ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", txRTTData, graphArray, "title", "txRTT", "", "none", legendData, true, true, 0, 0);
               
                
               
            }).catch(function(xhr, status, error){
                console.log(xhr);
            });
      }
      
      function getTxPVLS()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            myAjax({startDate:startDate, endDate:endDate}, "users/getTxPVLS.action").then(function(response){
                var data = JSON.parse(response);
               
               if(data != null){
                    var totalPVLS = 0;
                    var txPVLSData = JSON.parse(data["txPVLSDataJSON"]);
                    var totalUndetected = 0;
                    console.log(txPVLSData);
                    var totalWithResult = txPVLSData.length;
                    for(var i=0; i<txPVLSData.length; i++)
                    {
                        
                        if(parseInt(txPVLSData[i]["viralLoad"]) < 1000 && parseInt(txPVLSData[i]["viralLoad"]) != -1)
                        {
                        totalPVLS++;
                        if(parseInt(txPVLSData[i]["viralLoad"]) < 20)
                        {
                            totalUndetected++;
                        }
                        }
                    }
                    
                    var txPVLSChartData = new Array();
                    
                    txPVLSChartData.push({
                        title:"Tx_PVLS",
                        totalWithResult: totalWithResult,
                        txPVLS : totalPVLS,
                        percdentage:100
                        
                    })
                    
                    
                    
                    var legendData = [{color:"#589BD4", title:"Denominator (Total with result)"},
                        {color:"#E2932B", title:"Numerator(Tx PVLS)"},
                        //{color:"#bf6c19", title:"Percentage Lost"}      
                    ]
                    //var legendData = [];
                    var graphArray = [
                    {field: "totalWithResult", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total with result", isOptimum:"eligible6mts", chartType:"vlotz"},
                    {field: "txPVLS", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"TX PVLS", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                    //{field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                    ]
                
                    //create chart
                    artDashboard_buildBarCharts("Total Patients", txPVLSChartData, graphArray, "title", "txPvls", "", "none", legendData, true, true, 0, 0);
                                
                    var txVLUNDChartData = new Array();
                    
                    txVLUNDChartData.push({
                        title:"Tx_VL_UND",
                        totalSuppressed: totalPVLS,
                        txVLUND : totalUndetected,
                        // percdentage:100
                        
                    })
                    
                    
                    
                    var legendData = [{color:"#589BD4", title:"Denominator (Total with result)"},
                        {color:"#E2932B", title:"Numerator(Tx PVLS)"},
                        //{color:"#bf6c19", title:"Percentage Lost"}      
                    ]
                    //var legendData = [];
                    var graphArray = [
                    {field: "totalSuppressed", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total suppressed", isOptimum:"eligible6mts", chartType:"vlotz"},
                    {field: "txVLUND", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"TX VL UND", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                    //{field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                    ]
                
                    //create chart
                    artDashboard_buildBarCharts("Total Patients", txVLUNDChartData, graphArray, "title", "txVLUND", "", "none", legendData, true, true, 0, 0);
                }
            }).catch(function(xhr, status, error){
                console.log(xhr);
            });
      }
      /*
      function getTxOptimal()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }

            
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

            
            });
            
                
            alert(totalOnTreatment);

            var txOptimalChartData = new Array();
                
                txOptimalChartData.push({
                    title:"Tx_Optimal",
                    totalOnTreatment: totalOnTreatment,
                    txOptimal : totaltxOptimal,
                    percentage:100
                    
                })
                
            
             var legendData = [{color:"#589BD4", title:"Total Patient on treatment(Tx Curr)"},
                       {color:"#E2932B", title:"Tx_Optimal"},
                       {color:"#bf6c19", title:"Percentage Lost"}      
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "totalOnTreatment", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total patients on treatment", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txOptimal", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"Tx_Optimal", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                   {field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                  ]
              
               //create chart
                artDashboard_buildBarCharts("VL Access", txOptimalChartData, graphArray, "title", "txOptimal", "", "none", legendData, true, true, 0, 0);
            
            
      }
      
      
      function getTxTT()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            var txTTChartData = new Array();
                
                txTTChartData.push({
                    title:"Tx_T&T",
                    totalEnrolled: 3,
                    txTT : 2,
                    percdentage:100
                    
                })
                
            
             var legendData = [{color:"#589BD4", title:"Total Patient on treatment(Tx Curr)"},
                       {color:"#E2932B", title:"Tx_Optimal"},
                       //{color:"#bf6c19", title:"Percentage Lost"}      
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "totalEnrolled", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total Enrolled", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txTT", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"Tx_T&T", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                   //{field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                  ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", txTTChartData, graphArray, "title", "txTT", "", "none", legendData, true, true, 0, 0);
            
            
      }
      
      function getTxADR()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            var txADRData = new Array();
            var less1 = 0;
            var age_1_4 = 0;
            var age_5_9 = 2;
            var age_10_14 = 1;
            var age_15_19 = 0;

            txADRData.push({title: "<1", value: less1, weightBand:"less_1", regimen:"less_1", isOptimum:"true" })
            txADRData.push({title: "1-4", value: age_1_4, weightBand:"1_4", regimen:"1_4", isOptimum:"true" })
            txADRData.push({title: "5-9", value: age_5_9, weightBand:"5_9", regimen:"5_9", isOptimum:"true" })
            txADRData.push({title: "10-14", value: age_10_14, weightBand:"10_14", regimen:"10_14", isOptimum:"true" })
            txADRData.push({title: "15-19", value: age_15_19, weightBand:"15_19", regimen:"15_19", isOptimum:"true" })
            buildPieCharts(txADRData, "value", "title", "txADR");
            
            
      }
      
      function getTxPTS()
      {
            var startDate = jq("#startDate").val();
            var endDate = jq("#endDate").val();
            
            if(endDate == "")
            {
                //end date is today then
                var dateObj = new Date();
                var month = dateObj.getMonth()+1;
                month = (month <10) ? "0"+month : month;
                endDate = dateObj.getFullYear()+"-"+month+"-"+dateObj.getDate();
                
            }
            
            var txTTChartData = new Array();
                
                txTTChartData.push({
                    title:"Tx_T&T",
                    totalTracked: 6,
                    txTT : 4,
                    percdentage:100
                    
                })
                
            
             var legendData = [{color:"#589BD4", title:"Total Patients tracked"},
                       {color:"#E2932B", title:"Tx_Optimal"},
                       //{color:"#bf6c19", title:"Percentage Lost"}      
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "totalTracked", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total Tracked", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txTT", type:"column", valueAxis:1,  color:"#E2932B", description:"title", title:"Tx_T&T", isOptimum:"sampleTaken6t", chartType:"vlotz"},
                   //{field: "percentage", type:"line", valueAxis:2,  color:"#bf6c19", description:"title", title:"Percent Lost", isOptimum:"percentage", chartType:"vlotz"},
                  ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", txTTChartData, graphArray, "title", "txPTS", "", "none", legendData, true, true, 0, 0);
            
            
      }
      */
      jq(document).ready(function(){
          

          
           getQoc();
           getTxNew();
           // getTxMl();
           getTxRTT();
           getTxPVLS();
           
           /*
           getTxOptimal();
           
           getTxTT();
           getTxADR();
           getTxTT();
           getTxPTS();
           */
             
      })
      
      
  </script>