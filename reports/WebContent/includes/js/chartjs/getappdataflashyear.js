// A $( document ).ready() block.
$( document ).ready(function() {
    console.log( "ready!" );
    $.ajax({
    	type: 'Get',
        //url: 'http://cvyhj3a27:8181/reports/flashsum',
    	
    	
        url: 'http://localhost:8181/reports/flashsum',
        
        dataType: "json",
        success: function(data) {
        	console.log(data);
        	var ms_arr = [];
        	var mt_arr = [];
        	var qs_arr = [];
        	var qt_arr = [];
        	var ys_arr = [];
        	var yt_arr = [];
        	
        	var ms_val = 0;
        	var mt_val = 0;
        	var qs_val = 0;
        	var qt_val = 0;
        	var ys_val = 0;
        	var yt_val = 0;
         
        	
        	
        	
        	for (var i in data) {
        		
        		console.log("msSum:" + data[i].msSum);
        		console.log("mtSum:" + data[i].mtSum);
        		console.log("qsSum:" + data[i].qsSum);
        		console.log("qtSum:" + data[i].qtSum);
        		console.log("ysSum:" + data[i].ysSum);
        		console.log("ytSum:" + data[i].ytSum);
        		
        		
        		ms_arr.push(data[i].msSum)
        	 	mt_arr.push(data[i].mtSum)
        		qs_arr.push(data[i].qsSum)
        	 	qt_arr.push(data[i].qtSum)
        	 	ys_arr.push(data[i].ysSum)
        	 	yt_arr.push(data[i].ytSum)
        	 	
        	 	
        		/*	
        		os_val = data[i].Order_Submitted;
        		dr_val = data[i].Docs_Received;
        		mi_val = data[i].Doc_Missing_Info;
        		ca_val = data[i].Need_Credit_Approval;
        		console.log("^^^^^ os:" + os_val);
        		console.log("^^^^^ dr:" + dr_val);
        		console.log("^^^^^ mi:" + mi_val);
        		//console.log("********** DR:" + data[i].Docs_Received);
        		//console.log("***********OS:" + data[i].Order_Submitted);
        	*/	
        	}
        	
        	var ms = ms_arr[0];
        	var mt = mt_arr[0];
        	var qs = qs_arr[0];
        	var qt = qt_arr[0];
        	var ys = ys_arr[0];
        	var yt = yt_arr[0];
        	
        	 var color_arr = ["#08896d", "#ba1ece","#3c0889","#ad3432", "#3effcd","#fcba9f", "#bb9ccd", "#2effa3","#a00637","#ccc3f9", "#aac3f9",
         		"#b9e0aa", "#92efef", "#92a4ef", "#ef92a3", "#efcd92", "#7a6159", "#022311", "#d4d6c2", "#f7ad85", "#d5d4db"  ];
        	 var color_arr2 = ["#3effcd", "#e8aeef","#aa74e8","#e87d74", "#aac3f9",
          		"#b9e0aa", "#92efef", "#92a4ef", "#ef92a3", "#efcd92", "#7a6159", "#022311", "#d4d6c2", "#f7ad85", "#d5d4db"  ];
        	 
 /******************************************************************************************************************************************************************/
     	
        	 
        	 
        	 
/******************************************************************************************************************************************************************/
     var config = {
      	  type: 'horizontalBar',
    	  data: {
    	    labels: ['YTD Sales', 'YTD Target', ],
    	    	    
    	    datasets: [	    	    	
	            {
	            	backgroundColor:color_arr,
	            	label: 'Dollars',
		      	      xAxisID: 'A',
		      	      data: [ ys_arr[0], yt_arr[0]]	
	            } 
	          ]    
    	  },
    	  
    	
    	  options: {
    		  title: {
                  display: true,
                  fontStyle: 'bold',
                  fontSize: 20,
                  text: 'Flash Yearly'
              },
              
              
              tooltips: {
                  mode: 'label',
                   
                  callbacks: {
                      label: function(tooltipItem, data) {
                          return '$' + tooltipItem.xLabel.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ","); }, },
               },
     
    	    scales: {
    	      xAxes: [
    	    	  {
    	    	
    	    		  id: 'A',
    	    			 title: "Dollars",
                		titleFontSize: 20,
                            display: true,
                            ticks: {
                            	
                                beginAtZero: true,
                                //steps: 1000000,
                               //stepValue: 1000000,
                               stepSize: 10000000,
                                max: 400000000,
                               callback: function(value, index, values) {              
                                     return '$' + value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                               	}
                               
                               
                               
                            }
    	    	  
    	      } ]
    	  
    	    }
    	  
    	  }
        	
     }      	
/******************************************************************************************************************************************************************/
        	
        	  var ctx = document.getElementById("mycanvasFlashYear").getContext("2d");
              new Chart(ctx, config);
        	
        	//console.log("************** OS: " + os_arr[0] );
        	//console.log("************** DR: " + dr_arr[0] );
        }, // end ajax success
        error:	function(data) {
        	console.log(data);
        },  // end ajax error
        
    }); // end ajax
  
});