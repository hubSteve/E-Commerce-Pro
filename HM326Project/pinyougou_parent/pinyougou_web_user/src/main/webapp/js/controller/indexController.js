//首页控制器
app.controller('indexController',function($scope,loginService,userAddressService){

	$scope.showName=function(){
			loginService.showName().success(
					function(response){
						$scope.loginName=response.loginName;
					}
			);
	}


    $scope.addInfo=function(){
        userAddressService.update($scope.entity).success(
            function(response){
                alert(response.message);

            }
        )
    }
});