//业务处理
/*定义控制器*/
app.controller('brandController',function ($scope,$controller,brandService) {

    /*继承伪类 {$scope:$scope} 获取当前数据进行共享 */
    $controller('baseController', {$scope:$scope});


    /*初始化加载全部数据*/
    $scope.findAll=function () {
        brandService.findAll().success(function (response) {
            /*接收返回结果*/
            $scope.list =response;
        });

    };

    /*条件查询*/
    $scope.search = function () {
       brandService.search($scope.paginationConf.currentPage,$scope.paginationConf.itemsPerPage, $scope.searchEntity).success(
            function (response) {
                /*获取结果集*/
                $scope.list = response.rows;
                /*获取总页数*/
                $scope.paginationConf.totalItems=response.total;
            });
    };

    /*添加品牌信息*/
    $scope.add =function () {
        var url = brandService.add($scope.entity);//默认更新
        if ($scope.entity.id!=null){
            url = brandService.update($scope.entity);//修改品牌名称
        }
            /*发送请求*/
            url.success(function (response) {
            /*响应判断*/
            if (response.flag){
                alert(response.message);
                //成功刷新列表
                $scope.reloadList();
            }else{
                alert(response.message);
            }
        });
    };

    /*修改商品信息*/
    $scope.findOne=function (id) {
        brandService.findOne(id).success(function (response) {
            $scope.entity=response;

        })
    };

    //删除品牌信息
    $scope.del =function () {
        brandService.del($scope.selectIds).success(function (response) {
            if (response.flag){
                alert(response.message);
                $scope.reloadList();

                // 初始化数组
            }else {
                alert(response.message);
            }
        })
    }

});