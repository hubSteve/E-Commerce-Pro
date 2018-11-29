// 定义服务层:
app.service("ordermService",function($http) {
    this.search = function (page, rows, searchEntity) {
        return $http.post("../orderm/OrdermQuery.do?pageNum=" + page + "&pageSize=" + rows, searchEntity);
    }
    this.search=function (page, rows, searchEntity) {
        return  $http.post("../orderm/Orderf.do?pageNum=" + page + "&pageSize=" + rows, searchEntity);
    }
});