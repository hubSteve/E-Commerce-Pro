//抽取公共
/*定义控制器*/
app.controller('baseController',function ($scope) {

    // 分页的配置信息
    $scope.paginationConf = {
        currentPage: 1, // 当前页码
        totalItems: 0,  // 总条数
        itemsPerPage: 5, // 每页显示的条数
        perPageOptions: [5, 10, 20, 30, 40, 50],    // 可设置每页显示多少条
        onChange: function(){
            $scope.reloadList(); // 重新加载
        }
    };
    /*分页重新加载*/
    $scope.reloadList = function () {
        $scope.search();
    };
    /*接收要传递的数据*/
    $scope.searchEntity={} ;  //初始化 json对象

    /*删除商品 之获取商品id*/
    $scope.selectIds=[];//将获取的数据添加到数组中
    $scope.updateSelected=function ($event,id) {
        if ($event.target.checked){
            //选中添加 要删除的品牌信息
            $scope.selectIds.push(id);
        }else {
            //取消删除 选中的信息
            var index = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(index,1);
        }

    }
});