
<br/>
<div class="row">
    <label class="col-sm-4"><strong>Age Disaggregation</strong></label>
    <div class="col-sm-4">
        <select class="form-control">
            <option value="">Select age disaggregation</option>
            <option value="1">Select age disaggregation</option>
            <option value="1_4">1-4</option>
            <option value="5_9">5-9</option>
            <option value="10_14">10-14</option>
            <option value="15_19">15_19</option>
            <option value="15_19">20-24</option>
            <option value="15_19">25-29</option>
            <option value="15_19">30-24</option>
            <option value="15_19">35-39</option>
            <option value="15_19">40-44</option>
            <option value="15_19">45-49</option>
            <option value="15_19">50+</option>
        </select>
    </div>
    <div class="col-sm-4"><a href="#" class="btn btn-success">Filter</a></div>
</div>
<br/><br/>

<h2>Retention Summary</h2>
<div class="row">
    <div class="col-sm-6">
        <div class="divBox1"><p>Aged out</p></div>
        <div class="divBox2"><p>Current on ART: 12%</p>
                        <p>New on ART:1%</p>
        </div>
        
    </div>

    <div class="col-sm-6">
        <div class="divBox1"><p>Died</p></div>
        <div class="divBox2"><p>Current on ART: 1.5%</p>
                        <p>New on ART:1%</p>
        </div>
        
    </div>
</div>
<br/><br/>
<div class="row">
    <div class="col-sm-6">
        <div class="divBox1"><p>LTFU</p></div>
        <div class="divBox2"><p>Current on ART: 43%</p>
                        <p>New on ART:4%</p>
        </div>
        
    </div>

    <div class="col-sm-6">
        <div class="divBox1"><p>Transfer out</p></div>
        <div class="divBox2"><p>Current on ART: 3%</p>
                        <p>New on ART:0%</p>
        </div>
        
    </div>
</div>

</br><br/>
<div class="row">
    
        <div class="col-sm-12">
            <h4>Retention Trend</h4>
            <div id="txRetention" style="width: 100%; height: 650px;"></div>
        </div>
        <div class="col-sm-12">
            <h3>TX New Vs TX ML Trend</h3>
            <div id="txNewTrend" class="chart" style="width: 100%; height: 350px;"></div>
        </div>
        
    </div>
   

    
  <script type="text/javascript">
      var jq = jQuery;
       var txCurrData = new Array();
        var txNewData = new Array();
      
      
      function getRetentionTrend()
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
            
            var retentionData = new Array();
                
            retentionData.push({
                title:"9 months ago",
                monthTx: 19,
                monthMl: 6,
                died: 1

            })
             retentionData.push({
                title:"6 months ago",
                monthTx: 15,
                monthMl: 8,
                died: 0
                
            })
            
            retentionData.push({
                title:"3 months ago",
                monthTx: 8,
                monthMl: 5,
                died: 0
                
            })
             retentionData.push({
                title:"Current Date",
                monthTx: 8,
                monthMl: 5,
                died: 0
                
            })
            
             var legendData = [{color:"#589BD4", title:"Total Patients on treatment "},
                                {color:"#ed7d31", title:"TX ML"},
                               {color:"#c42c1c", title:"Dead"}    
                ]
                //var legendData = [];
                var graphArray = [
                   {field: "monthTx", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Tx Curr", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "monthMl", type:"column", valueAxis:1, color:"#ed7d31", description:"title", title:"Tx ML", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "died", type:"column", valueAxis:1, color:"#c42c1c", description:"title", title:"Tx ML", isOptimum:"eligible6mts", chartType:"vlotz"},
                 ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", retentionData, graphArray, "title", "txRetention", "", "none", legendData, true, true, 0, 0);
            
            
      }
      
      function getTxNewTrend()
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
            
            var txNewData = new Array();
               
            txNewData.push({
                title:"9 months ago",
                txNew: 7,
                txMl:2
                

            })
            txNewData.push({
                title:"6 months ago",
                txNew: 6,
                txMl:5
                

            })

            txNewData.push({
                title:"3 months ago",
                txNew: 10,
                txMl:4
                

            })
            txNewData.push({
                title:"Current date",
                txNew: 10,
                txMl:2
                

            })

            
              var legendData = [{color:"#589BD4", title:"Tx New"},
                                {color:"#ed7d31", title:"TX ML"}]
                var graphArray = [
                   {field: "txNew", type:"column", valueAxis:1, color:"#589BD4", description:"title", title:"Tx New", isOptimum:"eligible6mts", chartType:"vlotz"},
                   {field: "txMl", type:"column", valueAxis:1, color:"#ed7d31", description:"title", title:"Tx ML", isOptimum:"eligible6mts", chartType:"vlotz"},
                 ]
              
               //create chart
                artDashboard_buildBarCharts("Total Patients", txNewData, graphArray, "title", "txNewTrend", "", "none", legendData, true, true, 0, 0);
            
            
      }
      
      
       
      
      /*
      jq(document).ready(function(){
             getRetentionTrend();
             getTxNewTrend();
      })
      */
      
  </script>    
       