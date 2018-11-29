// 定义控制器:
app.controller("userManagerController",function($scope,$controller,$http,userManagerService){
	// AngularJS中的继承:伪继承
	$controller('baseController',{$scope:$scope});


    $scope.status = ["","男","女"]; // 1 男  2 女


	// 查询所有的品牌列表的方法:
	$scope.findAll = function(){
		// 向后台发送请求:
        userManagerService.findAll().success(function(response){
			$scope.list = response;
		});
	}

	// 分页查询
	$scope.findPage = function(page,rows){
		// 向后台发送请求获取数据:
        userManagerService.findPage(page,rows).success(function(response){
			$scope.paginationConf.totalItems = response.total;
			$scope.list = response.list;
		});
	}
	
	// 保存品牌的方法:
	$scope.save = function(){
		// 区分是保存还是修改
		var object;
		if($scope.entity.id != null){
			// 更新
			object = userManagerService.update($scope.entity);
		}else{
			// 保存
			object = userManagerService.add($scope.entity);
		}
		object.success(function(response){
			// {flag:true,message:xxx}
			// 判断保存是否成功:
			if(response.flag){
				// 保存成功
				alert(response.message);
				$scope.reloadList();
			}else{
				// 保存失败
				alert(response.message);
			}
		});
	}
	
	// 查询一个:
	$scope.findById = function(id){
        userManagerService.findOne(id).success(function(response){
			// {id:xx,name:yy,firstChar:zz}
			$scope.entity = response;
		});
	}
	
	// 删除品牌:
	$scope.dele = function(){
		if (confirm("亲!确定要删除吗")) {
            userManagerService.dele($scope.selectIds).success(function (response) {
                alert($scope.selectIds)
            	// 判断保存是否成功:
                if (response.flag == true) {
                    // 保存成功
                    // alert(response.message);
                    $scope.reloadList();
                    $scope.selectIds = [];
                    alert(response.message);

                } else {
                    // 保存失败
                    alert(response.message);
                }
            });
        }
	}
	
	$scope.searchEntity={};
	
	// 假设定义一个查询的实体：searchEntity
	$scope.search = function(page,rows){
		// 向后台发送请求获取数据:
        userManagerService.search(page,rows,$scope.searchEntity).success(function(response){
            $scope.paginationConf.totalItems = response.total;
			$scope.list = response.list;
        });
	}
	
});
