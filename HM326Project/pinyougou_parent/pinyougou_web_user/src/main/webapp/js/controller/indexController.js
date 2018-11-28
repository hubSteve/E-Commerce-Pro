//首页控制器
app.controller('indexController',function($scope,loginService,userService){
	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}

    //查询用户订单列表
    $scope.findCartList=function(){
        alert("1");
        userService.findCartList().success(
            function(response){
                alert(2);
                $scope.cartList=response;
                alert( JSON.stringify($scope.cartList));

            }
        );
    }
    // 分页的配置的信息
    $scope.paginationConf = {
        currentPage: 1, // 当前页数
        totalItems: 0, // 总记录数
        itemsPerPage: 5, // 每页显示多少条记录
        perPageOptions: [5,10, 20, 30, 40, 50],// 显示多少条下拉列表
        onChange: function(){ // 当页码、每页显示多少条下拉列表发生变化的时候，自动触发了
            $scope.reloadList();// 重新加载列表
        }
    };

    $scope.reloadList = function(){
        // $scope.findByPage($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
        $scope.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage);
    }
});