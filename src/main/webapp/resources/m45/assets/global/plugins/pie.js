var welcomeInfo = {
	init:function(){
		this.setPieChart();
	},
	pieChart : null,
	pieOption : null,
	setPieChart: function(){
		var myChart = echarts.init(document.getElementById('pie')); 
		var option = {
			color:['#cdcdcd','#2ec7c9','#1691cf','#97b552','#e5cf0c','#d97a81','#1691cf'],
			tooltip : {
		        trigger: 'item',
		        formatter:function(param){
					if(param[1] == '出勤率'){
						return param[1]+':'+parseFloat(param[2]).toFixed(1)+'('+param[3]+'%)';	
					}else{
						return param[1]+':'+param[2]+'('+param[3]+'%)';	
					}
		        }
		    },
		    noDataLoadingOption: {
		    	effect: 'whirling'
		    },
		    calculable : false,
		    legend: {
		        orient : 'vertical',
		        x : 'left',
		        y :15,
		        data:['离线','行驶','静止','维修','故障'],//'充电1',
		        formatter: function(name){
		    		var datas = option.series,
		    			total = datas[2].data[0].value+datas[2].data[1].value+datas[2].data[2].value,//+datas[2].data[3].value,
		    			total_1 = datas[1].data[0].value+datas[1].data[1].value+datas[2].data[0].value;
		    		switch(name){
		    			case '离线':
		    				return '离线'+datas[2].data[0].value+'('+(parseFloat(datas[2].data[0].value/total)*100).toFixed(2)+'%)';
		    				break;
		    			//case '充电1':
		    				//return '充电'+datas[2].data[1].value+'('+(parseFloat(datas[2].data[1].value/total)*100).toFixed(2)+'%)';
		    				//break;
		    			case '行驶':
		    				return '行驶'+datas[2].data[1].value+'('+(parseFloat(datas[2].data[1].value/total)*100).toFixed(2)+'%)';
		    				break;
		    			case '静止':
		    				return '静止'+datas[2].data[2].value+'('+(parseFloat(datas[2].data[2].value/total)*100).toFixed(2)+'%)';
		    				break;
		    			case '维修':
		    				return '维修'+datas[1].data[0].value+'('+(parseFloat(datas[1].data[0].value/total)*100).toFixed(2)+'%)';
		    				break;
		    			case '故障':
		    				return '故障'+datas[1].data[1].value+'('+(parseFloat(datas[1].data[1].value/total)*100).toFixed(2)+'%)';
		    				break;
		    		}
		        }
		    },
		    series : [
		      	{
		          name:'车辆信息',
		          type:'pie',
		          center:['70%','50%'],
		          radius : [0, 30],
		          selectedOffset: 0,
		          itemStyle : {
		            emphasis : {
		              show:true,
		              label :{
		                show:true,
		                formatter : function (params){
		                  return params.name+'\n'+parseFloat(params.value).toFixed(1)+'%'
		                },
		                textStyle:{
		                  fontWeight:'bold',
		                  color:'#f3f2ee',
		                  fontSize:'8',
		                  align:'center',
		                  baseline:'bottom'
		                }
		              }
		            },
		            normal : {
		              color:'#228b22',
		              labelLine : {
		                show : false
		              },
		              label :{
		                position: 'inner',
		                formatter : function (params){
		                  return params.name+'\n'+parseFloat(params.value).toFixed(1)+'%'
		                },
		                textStyle:{
		                  fontWeight:'bold',
		                  color:'#f3f2ee',
		                  fontSize:'8',
		                  align:'center',
		                  baseline:'bottom'
		                }
		              }                                 
		            }
		          },
		          data:[
		            //{value:0,name:'出勤率'}
		          ]
		        },
	           	{
		            name:'车辆信息',
		            type:'pie',
		            center:['70%','50%'],
		            radius : [35, 50],
		            itemStyle:{
		              	normal:{
			                labelLine:{
			                  length: 0
			                },
			                label :{
			                  show:false
			                }
		              	}
		            }, 
		            data:[
/*		                {value:0, name:'维修'},#cdcdcd	                
 						{value:0, name:'故障'},#2ec7c9		                
 						{value:0,name:'离线 '} */
		            ]
		        },
		        {
		            name:'车辆信息',
		            type:'pie',
		            center:['70%','50%'],
		            radius : [60, 75],
		            itemStyle:{
		              normal:{
		                labelLine:{
		                  length: 0
		                },
		                label :{
		                  show:false
		                }
		              }
		            }, 
		            data:[
		                //{value:0, name:'离线'},//#cdcdcd
		                //{value:0, name:'充电'},//#2ec7c9
		                //{value:0, name:'行驶'},//#97b552
		                //{value:0, name:'静止'}//#1691cf
		            ]
		        }
		    ]
		};
		this.pieOption = option;
		this.pieChart = myChart;
		myChart.setOption(option);
	}
};
