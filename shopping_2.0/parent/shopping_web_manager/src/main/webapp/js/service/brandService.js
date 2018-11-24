//定义service
/*定义控制器*/
app.service('brandService',function ($http) {

    //查询所有
    this.findAll=function () {
      return  $http.get('/brand/findAll.do');
    };

    /*条件查询*/
    this.search = function (pageNum,pageSize ,searchEntity) {
        return $http.post('/brand/search.do?pageNum='+pageNum+"&pageSize="+pageSize,searchEntity);
    };
    /*添加品牌信息*/
    this.add =function (entity) {
        return   $http.post( '/brand/add.do',entity);
    };

    /*更新品牌信息*/
    this.update =function (entity) {

        return  $http.post('/brand/update.do',entity);

    };

    /*根据id查询商品信息 进行回显*/
    this.findOne=function (id) {
        return     $http.get('/brand/findOne.do?id='+id);
    };


    //删除品牌信息
    this.del =function (Ids) {
        return  $http.get('/brand/delete.do?ids='+Ids);
    };


});