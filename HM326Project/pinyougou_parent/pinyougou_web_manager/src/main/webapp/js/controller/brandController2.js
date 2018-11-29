// 定义控制器:
app.controller("brandController2",function($scope,$controller,$http,brandService){
	// AngularJS中的继承:伪继承
	$controller('baseController2',{$scope:$scope});


	
	// 删除品牌:
	$scope.dele = function(){
		brandService.dele($scope.selectIds).success(function(response){
			// 判断保存是否成功:
			if(response.flag==true){
				// 保存成功
				// alert(response.message);
				$scope.reloadList2();
				$scope.selectIds = [];
			}else{
				// 保存失败
				alert(response.message);
			}
		});
	}
	

	


    // 显示状态
    $scope.status = ["未审核","审核通过","审核未通过","关闭"];


    $scope.searchEntity={};

    // 定义一个方法，查询所有的status==0的品牌，分页查询
    $scope.search2 = function(page,rows){
        // 向后台发送请求获取数据:
        brandService.searchforStatus(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
            $scope.list = response.rows;
        });
    }

    // 审核的方法:
    $scope.updateStatus = function(status){
        brandService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList2();//刷新列表
                $scope.selectIds = [];
                alert(response.message);
            }else{
                alert(response.message);
            }
        });
    }
});
