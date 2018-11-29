app.service('userAddressService',function($http){

    this.update=function(entity){

     return  $http.post("../userAddress/update.do",entity);
    }

});