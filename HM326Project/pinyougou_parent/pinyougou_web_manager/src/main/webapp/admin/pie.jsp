<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- 移动设备优先（让项目友好的支持移动设备） -->
<meta name="viewport" content="width=device-width, initial-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>echarts实例——动态加载数据</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/echarts.js"></script>
</head>
<body>
	<div id="main" style="width:1000px;height:500px;"></div>
	<script type="text/javascript">
	
	$(function(){
		var url = '${pageContext.request.contextPath}/managerEcharts/pie.do';
		serChartPie(url);
	});
	
	function serChartPie(url){
		var myChart = echarts.init(document.getElementById("main"));
		myChart.showLoading({text: '正在努力的读取数据中...'  });
		var label = [];
		var value = [];
		
		$.ajax({
			url:url,
			type:'post',
			dataType:'json',
			success:function(json){
				label = json.label;
		        values = json.value;
		        for(var i=0;i<label.length;i++){
		        	value[i] = {'value':values[i],'name':json.label[i]};
		        }
				var option = {
						title:{
							text:'Echarts 饼图动态加载',
							subtext:'',
							x:'center'
						},
						legend:{
							orient:'vertical',
							x:'left',
							data:label
						},
						tooltip:{
							trigger:'item',
							formatter:'{a}:{b}<br/>{c}:{d}%'
						},
						toolbox:{
							feature:{
								mark:{show:true},
								dataView:{show:true,readonly:false},
								magicType : {
					                show: true, 
					                type: 'pie',
					                option: {
					                    funnel:{
					                        x:'25%',
					                        width: '50%',
					                        funnelAlign: 'left',
					                        max: 400
					                    }
					                }
					            },
								restore:{show:true},
								saveAsImage:{show:true}
							}
						},
						series:[{
							name:'访问来源',
							type:'pie',
							radius:'55%',
							center:['50%','60%'],
							roseType:'angle',
							data:value
						}]	
				}
				myChart.hideLoading();
				myChart.setOption(option);
				
			}
		});
	}
	</script>
</body>
</html>