//控制层 
app.controller('typeTemplateController2' ,function($scope,$controller,typeTemplateService){
	
	$controller('baseController2',{$scope:$scope});//继承

	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;	
				// eval()   JSON.parse();   
				$scope.entity.brandIds = JSON.parse($scope.entity.brandIds);
				
				$scope.entity.specIds = JSON.parse($scope.entity.specIds);
				
				$scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);
			}
		);				
	}

	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		typeTemplateService.dele( $scope.selectIds ).success(
			function(response){
				if(response.flag){
					$scope.reloadList2();//刷新列表
					$scope.selectIds = [];
				}						
			}		
		);				
	}

	// $scope.brandList={data:[]}
	// // 查询关联的品牌信息:
	// $scope.findBrandList = function(){
	// 	brandService.selectOptionList().success(function(response){
	// 		$scope.brandList = {data:response};
	// 	});
	// }
    //
	// $scope.specList={data:[]}
	// // 查询关联的品牌信息:
	// $scope.findSpecList = function(){
	// 	specificationService.selectOptionList().success(function(response){
	// 		$scope.specList = {data:response};
	// 	});
	// }

    $scope.searchEntity={};//定义搜索对象
    //搜索
    $scope.search2=function(page,rows){
        typeTemplateService.searchTemlListByStatus(page,rows,$scope.searchEntity).success(
            function(response){
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;//更新总记录数
            }
        );
    }

    // 显示状态
    $scope.status = ["未审核","审核通过","审核未通过","关闭"];

    //批量审核规格
    $scope.updateStatus = function(status){
        typeTemplateService.updateStatus($scope.selectIds,status).success(function(response){
            if(response.flag){
                $scope.reloadList2();//刷新列表
                $scope.selectIds = [];
            }else{
                alert(response.message);
            }
        });
    }

});	
