//控制层
app.controller('seckillGoodsController' ,function($scope,$location,seckillGoodsService,$interval){
	//读取列表数据绑定到表单中
	$scope.findList=function(){
		seckillGoodsService.findList().success(
			function(response){
				$scope.list=response;
			}
		);
	}
	
	//查询商品
	$scope.findOne=function(){
		//接收参数ID
		var id= $location.search()['id'];
		seckillGoodsService.findOne(id).success(
			function(response) {
                $scope.entity = response;

                //倒计时开始
                //获取从结束时间到当前日期的秒数

                allsecond = Math.floor((new Date(response.endTime).getTime() - new Date().getTime()) / 1000);
                gapSeconds = (new Date().getTime()) - (new Date('2018-11-29 16:09:00').getTime());
                $scope.result = gapSeconds > 0 ? "0" : "1";
                alert($scope.result)
                time = $interval(function () {
                    allsecond--;
                    $scope.timeString = convertTimeString(allsecond);
                    if (allsecond <= 0) {
                        $interval.cancel(time);
                    }
                }, 1000)
            }
		);
	}
	
	
	//转换秒为   天小时分钟秒格式  XXX天 10:22:33
    convertTimeString=function(allsecond){
		var days=Math.floor(allsecond/(60*60*24));
		var hours = Math.floor((allsecond-days*60*60*24)/(60*60));
		var minutes = Math.floor((allsecond-days*60*60*24-hours*60*60)/60);
		var seconds = allsecond-days*60*60*24-hours*60*60-minutes*60;

		if (days>0){
			return days+"天"+hours+":"+minutes+":"+seconds;
		} else{
			return hours+":"+minutes+":"+seconds;
		}
	}
	
	//提交订单 
	$scope.submitOrder=function(){
		seckillGoodsService.submitOrder( $scope.entity.id ).success(
			function(response){
				if(response.flag){//如果下单成功
					alert("抢购成功，请在5分钟之内完成支付");
					location.href="pay.html";//跳转到支付页面				
				}else{
					alert(response.message);
				}				
			}
		);
		
	}
	
});