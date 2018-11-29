 //控制层 
app.controller('itemCatController2' ,function($scope,$controller   ,itemCatService){
	
	$controller('baseController2',{$scope:$scope});//继承

	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}

	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList2();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search2=function(page,rows){
		itemCatService.searchItemCatListByStatus(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    // 显示状态
    $scope.status = ["未审核","审核通过","审核未通过","关闭"];

    // 审核的方法:
    $scope.updateStatus = function(status){
        itemCatService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList2();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }

});	
