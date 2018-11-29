 //控制层 
app.controller('specificationController2' ,function($scope,$controller   ,specificationService){

	$controller('baseController2',{$scope:$scope});//继承


	

	
	//查询实体 
	$scope.findOne=function(id){				
		specificationService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	

	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		specificationService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList2();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象

    // 显示状态
    $scope.status = ["未审核","审核通过","审核未通过","关闭"];
	//显示所有status==0的规格
	$scope.search2=function(page,rows){
        specificationService.findSpecByStatus(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;//结果集
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }
    //批量审核规格
    $scope.updateStatus = function(status){
        specificationService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList2();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }
});	
