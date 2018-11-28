//服务层
app.service('loginService',function($http){
	//读取列表数据绑定到表单中
	this.showName=function(){
		return $http.get('../login/name.do');
	}

    this.search = function(page,rows){
        return $http.post("../user/pageInfoOrdeList.do?pageNum="+page+"&pageSize="+rows);
    }
	
});