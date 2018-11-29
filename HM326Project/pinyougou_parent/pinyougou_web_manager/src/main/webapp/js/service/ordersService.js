// 定义服务层:
app.service("ordersService",function($http) {
    this.search = function (page, rows, searchEntity) {
        return $http.post("../orders/OrdersQuery.do?pageNum=" + page + "&pageSize=" + rows, searchEntity);
    }
});