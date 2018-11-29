app.service("contentService",function($http){
    this.findByCategoryId = function(categoryId){
        return $http.get("content/findByCategoryId.do?categoryId="+categoryId);
    }




    //查询商品分类信息
    this.findItemCatList = function () {

        return $http.get("content/findItemCatList.do");
    }

});