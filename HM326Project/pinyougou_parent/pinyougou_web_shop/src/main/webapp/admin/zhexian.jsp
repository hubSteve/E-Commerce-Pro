<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>echarts动态数据交互</title>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery-1.8.3.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath() %>/js/echarts.js"></script>
    <script type="text/javascript">

        $(function () {
            var url = '${pageContext.request.contextPath}/sellerEcharts/zhexian.do';
            var id = 'main';
            setChartBar(url);
        });

        //设置ajax访问后台填充图
        function setChartBar(url) {

            var Chart = echarts.init(document.getElementById("main"));
            Chart.showLoading(
                {text: 'Loding...'}
            );

         /*   var datas = [11, 11, 20, 13, 12, 13, 10];
            var data2 = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']*/
         var datas=[];
         var data2=[];
            $.ajax({
                url: url,
                dataType: "json",
                type: 'post',
                success: function (json) {

                     datas = json.datas;
                     data2 = json.data2;

                    var option = {
                        title: {text: 'echarts折线图'},
                        tooltip: {
                            trigger: 'axis'
                        },
                        legend: {data: ['销售额']},
                        toolbox: {
                            show: true,
                            feature: {
                                mark: {show: true},
                                dataView: {show: true, readOnly: false},
                                magicType: {show: true, type: ['line', 'bar']},
                                restore: {show: true},
                                saveAsImage: {show: true}
                            }
                        },
                        calculable: true,
                        xAxis: [{type: 'category', boundaryGap: false, data: data2}],
                        yAxis: [{
                            type: 'value', axisLabel: {
                                formatter: '{value}'
                            }
                        }],
                        series: [
                            {
                                'name': '销售额',
                                'type': 'line',
                                'data': datas
                                // markPoint: {data: [{type: 'max', name: '最大值'}, {type: 'min', name: '最小值'}]}
                                // markLine: {data: [{type: 'average', name: '平均值'}]}
                            }
                        ]
                    };
                    Chart.hideLoading();
                    Chart.setOption(option);
                }
            });
        }

    </script>
</head>
<body>
<div id="main" style="width: 1000px;height:500px;"></div>
</body>
</html>