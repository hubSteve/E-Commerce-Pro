//购物车服务层
app.service('cartService',function($http){

	this.findCartList=function (cartList) {
		return $http.post("/cart/findCartList.do",cartList);
    }

	//获取购物车列表
	this.getCartList=function () {
		var cartList=localStorage.getItem("cartList");
		if (cartList==null){
			return [];
		} else{
			return JSON.parse(cartList);
		}
    }

    //设置购物车列表
    this.setCartList=function (cartList) {
		return localStorage.setItem("cartList",JSON.stringify(cartList));
    }
    
    this.addGoodsToCartList=function (cartList, itemId, num) {
		return $http.post("/cart/addGoodsToCartList.do?itemId="+itemId+"&num="+num,cartList);
    }


    this.sum=function (cartList) {
		var totalValue={totalNum:0,totalMoney:0};
		for (var i=0;i<cartList.length;i++){
			var cart=cartList[i];
			for (var j=0;j<cart.orderItemList.length;j++){
				var orderItem=cart.orderItemList[j];
				totalValue.totalNum+=orderItem.num;
				totalValue.totalMoney+=orderItem.totalFee;
			}
		}

		return totalValue;
    }

    this.removeCartList=function () {
		localStorage.removeItem("cartList");
    }

    this.findAddressList=function () {
		return $http.get("/address/findListByLoginUser.do");
    }

    this.submitOrder=function (order) {
		return $http.post("/order/add.do",order);
    }

});