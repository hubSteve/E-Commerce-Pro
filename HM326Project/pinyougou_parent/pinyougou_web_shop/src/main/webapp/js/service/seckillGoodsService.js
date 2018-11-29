app.service('seckillGoodsService',function($http) {


    //添加秒杀商品
    this.add=function(entity){
        return  $http.post('../seckillGoods/add.do',entity );
    }

})