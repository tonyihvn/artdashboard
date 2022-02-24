
<div class="row">
        <div class="col-sm-12">
            <h4>ADR and Pharmacovigilance Analysis (0-19 years)</h4>
            <div id="adr19" style="width: 100%; height: 650px;"></div>
        </div>
        <div class="col-sm-12">
            <h3>ADR and Pharmacovigilance Analysis (>=20 years)</h3>
            <div id="adr20" class="chart" style="width: 100%; height: 650px;"></div>
        </div>
        
    </div>
    

    
  <script type="text/javascript">
      var jq = jQuery;
       var txCurrData = new Array();
        var txNewData = new Array();
      
      
      function getAdr19Trend()
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
            
            var adr19Data = new Array();
                
            adr19Data.push({
                title:"Total CLHIV Screened for ADR",
                color:"#589BD4",
                value: 4,
                
            })
            adr19Data.push({
                title:"Total CLHIV Experience ADR",
                color:"#ed7d31",
                value: 4,
                
            })
            adr19Data.push({
                title:"Total on optimal Regimen with documented ADR report",
                value: 2,
                color:"#0A702f",
                
            })
            adr19Data.push({
                title:"Total on optimal Regimen with ADR report sent to NAFDAC",
                value: 1,
                color:"#9b8118",
                
            })
            adr19Data.push({
                title:"Total on optimal Regimen with report requiring drub substitution",
                value: 0,
                color:"#A54A0C",
                
            })
             
             var legendData = [{color:"#589BD4", title:"Total CLHIV Screened for ADR"},
                                {color:"#ed7d31", title:"Total CLHIV Experience ADR"},
                                {color:"#0A702f", title:"Total on optimal Regimen with documented ADR report"},
                                {color:"#9b8118", title:"Total on optimal Regimen with ADR report sent to NAFDAC"},
                                {color:"#A54A0C", title:"Total on optimal Regimen with report requiring drub substitution"},
                                
            ]
                //var legendData = [];
                var graphArray = [
                   {field: "value", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total CLHIV", isOptimum:"eligible6mts", chartType:"vlotz"},
                   
                 ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", adr19Data, graphArray, "title", "adr19", "", "none", legendData, false, true, 0, 0);
            
            
      }
      
      function getAdr20Trend()
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
            
            var adr20Data = new Array();
                
            adr20Data.push({
                title:"Total CLHIV Screened for ADR",
                color:"#589BD4",
                value: 10,
                
            })
            adr20Data.push({
                title:"Total CLHIV Experience ADR",
                color:"#ed7d31",
                value: 8,
                
            })
            adr20Data.push({
                title:"Total on optimal Regimen with documented ADR report",
                value: 3,
                color:"#0A702f",
                
            })
            adr20Data.push({
                title:"Total on optimal Regimen with ADR report sent to NAFDAC",
                value: 1,
                color:"#9b8118",
                
            })
            adr20Data.push({
                title:"Total on optimal Regimen with report requiring drub substitution",
                value: 0,
                color:"#A54A0C",
                
            })
             
             var legendData = [{color:"#589BD4", title:"Total CLHIV Screened for ADR"},
                                {color:"#ed7d31", title:"Total CLHIV Experience ADR"},
                                {color:"#0A702f", title:"Total on optimal Regimen with documented ADR report"},
                                {color:"#9b8118", title:"Total on optimal Regimen with ADR report sent to NAFDAC"},
                                {color:"#A54A0C", title:"Total on optimal Regimen with report requiring drub substitution"},
                                
            ]
                //var legendData = [];
                var graphArray = [
                   {field: "value", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Total CLHIV", isOptimum:"eligible6mts", chartType:"vlotz"},
                   
                 ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", adr20Data, graphArray, "title", "adr20", "", "none", legendData, false, true, 0, 0);
                
                
            /*    
           myAjax({startDate:startDate, endDate:endDate}, "users/getAdrAnalysis.action").then(function(response){
                var data = JSON.parse(response);
               
               
                
               
            }).catch(function(xhr, status, error){
                console.log(xhr);
            });
            */
            
            
      }
      
      
       
      
      /*
      jq(document).ready(function(){
             getAdr19Trend();
             getAdr20Trend();
      })
      */
      
  </script>    
       