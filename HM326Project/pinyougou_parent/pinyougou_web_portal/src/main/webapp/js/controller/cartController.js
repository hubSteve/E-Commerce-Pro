//购物车控制层
app.controller('cartController',function($scope,$location,cartService){

    $scope.loginname="";

    $scope.addGoodsToCartList=function (itemId,num) {
        // $scope.cartList=cartService.getCartList();
        cartService.addGoodsToCartList($scope.cartList,itemId,num).success(
            function (response) {
                if (response.success) {
                    if (response.loginname==""){
                        cartService.setCartList(response.data);
                    } else{
                        $scope.findCartList();
                    }
                    $scope.cartList=response.data;
                    $scope.loginname=response.loginname;
                }
            })
    };

	$scope.init=function () {
		$scope.cartList=cartService.getCartList();
		var itemId=$location.search()["itemId"];
		var num=$location.search()["num"];
		if (itemId!=null && num!=null) {
            $scope.addGoodsToCartList(itemId,num);
		}else{
            $scope.findCartList();
        }
    }

    $scope.$watch("cartList",function (newValue,oldValue) {
    	$scope.totalValue=cartService.sum(newValue);
    })

    $scope.findCartList=function () {
        $scope.cartList= cartService.getCartList();//取出本地购物车
        cartService.findCartList($scope.cartList).success(function (response) {
            if (response.success){
                //如果用户登陆，清除本地购物车
                if (response.loginname!=""){
                    cartService.removeCartList();
                }
            }
            $scope.cartList=response.data;
            $scope.loginname=response.loginname;
        })
    }


    $scope.findAddressList=function () {
        cartService.findAddressList().success(function (response) {
            $scope.addressList=response;
            for (var i=0;i<$scope.addressList.length;i++){
                if ($scope.addressList[i].isDefault=='1'){
                    $scope.address=$scope.addressList[i];
                }
            } 
        })
    }
    
    $scope.selectAddress=function (address) {
        $scope.address=address;
    }

    $scope.isSeletedAddress=function (address) {
        if (address == $scope.address) {
            return true;
        }else{
            return false;
        }
    }

    $scope.order={paymentType:'1'}

    //选择支付方式
    $scope.selectPayType=function (type) {
        $scope.order.paymentType=type;
    }

    $scope.submitOrder=function () {
	    $scope.order.receiverAreaName=$scope.address.address;
	    $scope.order.receiverMobile=$scope.address.mobile;
	    $scope.order.receiver=$scope.address.contact;

        cartService.submitOrder($scope.order).success(function (response) {
                if (response.flag){
                    if ($scope.order.paymentType=='1'){
                        location.href="pay.html";
                    }else{
                        location.href="ordersuccess.html";
                    }
                } else{
                    alert(response.message);
                }
        })
    }
});