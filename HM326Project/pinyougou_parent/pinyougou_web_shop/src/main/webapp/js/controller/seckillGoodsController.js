app.controller('seckillGoodsController' ,function($scope,$controller,goodsService,seckillGoodsService) {

    //查询实体
    $scope.seckilApply=function(){
        var id = $location.search()['id'];
        if(null == id){
            return;
        }
        goodsService.findOne(id).success(
            function(response){
                $scope.entity= response;

                // 调用处理富文本编辑器：
                editor.html($scope.entity.goodsDesc.introduction);

                // 处理图片列表，因为图片信息保存的是JSON的字符串，让前台识别为JSON格式对象
                $scope.entity.goodsDesc.itemImages = JSON.parse( $scope.entity.goodsDesc.itemImages );

                // 处理扩展属性:
                $scope.entity.goodsDesc.customAttributeItems = JSON.parse( $scope.entity.goodsDesc.customAttributeItems );

                // 处理规格
                $scope.entity.goodsDesc.specificationItems = JSON.parse($scope.entity.goodsDesc.specificationItems);

                // 遍历SKU的集合:
                for(var i=0;i<$scope.entity.itemList.length;i++){
                    $scope.entity.itemList[i].spec = JSON.parse( $scope.entity.itemList[i].spec );
                }
            }
        );
    }


    //秒杀商品添加
    $scope.add=function(){
        seckillGoodsService.add( $scope.entity).success(
            function(response){
                if(response.flag){
                    alert(response.message);
                    location.href="seckillgoods.html";
                }else{
                    alert(response.message);
                }
            }
        );
    }
})