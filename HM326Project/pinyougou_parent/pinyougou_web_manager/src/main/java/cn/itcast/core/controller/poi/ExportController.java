package cn.itcast.core.controller.poi;


import cn.itcast.core.pojo.good.Goods;
import cn.itcast.core.pojo.order.Order;
import cn.itcast.core.service.goods.GoodsService;

import cn.itcast.core.service.order.OrderService;
import cn.itcast.core.utils.time.DateFormatUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("export")
public class ExportController {

    @Reference
    private GoodsService goodsService;

    @Reference
    private OrderService orderService;

    @RequestMapping("exportGoodsByPoi")
    public void exportGoodsByPoi(HttpServletResponse response){
        List<Goods> goodsList = goodsService.findList();
        System.out.println(goodsList);
        try {
            HSSFWorkbook book=new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("商品信息");
            HSSFRow row = sheet.createRow(0);

            HSSFCell cell0 = row.createCell(0,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell1 = row.createCell(1,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell2 = row.createCell(2,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell3 = row.createCell(3,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell4 = row.createCell(4,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell5 = row.createCell(5,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell6 = row.createCell(6,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell7 = row.createCell(7,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell8 = row.createCell(8,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell9 = row.createCell(9,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell10 = row.createCell(10,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell11 = row.createCell(11,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell12 = row.createCell(12,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell13 = row.createCell(13,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell14 = row.createCell(14,HSSFCell.CELL_TYPE_STRING);
            HSSFCell cell15 = row.createCell(15,HSSFCell.CELL_TYPE_STRING);

            cell0.setCellValue("商品编号");
            cell1.setCellValue("商家编号");
            cell2.setCellValue("商品名称");
            cell3.setCellValue("默认SKU");
            cell4.setCellValue("状态");
            cell5.setCellValue("是否上架");
            cell6.setCellValue("品牌");
            cell7.setCellValue("副标题");
            cell8.setCellValue("一级类目");
            cell9.setCellValue("二级类目");
            cell10.setCellValue("三级类目");
            cell11.setCellValue("小图");
            cell12.setCellValue("商城价");
            cell13.setCellValue("分类模板ID");
            cell14.setCellValue("是否启用规格");
            cell15.setCellValue("是否删除");

            for (int i = 0; i < goodsList.size(); i++) {
                Goods goods = goodsList.get(i);
                HSSFRow rowi = sheet.createRow(i+1);
                rowi.createCell(0).setCellValue(goods.getId()+"");
                rowi.createCell(1).setCellValue(goods.getSellerId()+"");
                rowi.createCell(2).setCellValue(goods.getGoodsName()+"");
                rowi.createCell(3).setCellValue(goods.getDefaultItemId()+"");
                rowi.createCell(4).setCellValue(goods.getAuditStatus()+"");
                rowi.createCell(5).setCellValue(goods.getIsMarketable()+"");
                rowi.createCell(6).setCellValue(goods.getBrandId()+"");
                rowi.createCell(7).setCellValue(goods.getCaption()+"");
                rowi.createCell(8).setCellValue(goods.getCategory1Id()+"");
                rowi.createCell(9).setCellValue(goods.getCategory2Id()+"");
                rowi.createCell(10).setCellValue(goods.getCategory3Id()+"");
                rowi.createCell(11).setCellValue(goods.getSmallPic()+"");
                rowi.createCell(12).setCellValue(goods.getPrice()+"");
                rowi.createCell(13).setCellValue(goods.getTypeTemplateId()+"");
                rowi.createCell(14).setCellValue(goods.getIsEnableSpec()+"");
                rowi.createCell(15).setCellValue(goods.getIsDelete()+"");
            }

            OutputStream output=response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=goods.xls");
            response.setContentType("application/msexcel");
            book.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping("exportOrdersByPoi")
    public void exportOrdersByPoi(HttpServletResponse response){
        List<Order> orderList = orderService.findAll();
        try {
            HSSFWorkbook book=new HSSFWorkbook();
            HSSFSheet sheet = book.createSheet("订单信息");
            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue("订单编号");
            row.createCell(1).setCellValue("实付金额");
            row.createCell(2).setCellValue("支付类型");
            row.createCell(3).setCellValue("邮费");
            row.createCell(4).setCellValue("状态");
            row.createCell(5).setCellValue("订单创建时间");
            row.createCell(6).setCellValue("订单更新时间");
            row.createCell(7).setCellValue("付款时间");
            row.createCell(8).setCellValue("发货时间");
            row.createCell(9).setCellValue("交易完成时间");
            row.createCell(10).setCellValue("交易关闭时间");
            row.createCell(11).setCellValue("物流名称");
            row.createCell(12).setCellValue("物流编号");
            row.createCell(13).setCellValue("用户ID");
            row.createCell(14).setCellValue("买家留言");
            row.createCell(15).setCellValue("买家昵称");
            row.createCell(16).setCellValue("买家是否评价");
            row.createCell(17).setCellValue("收货人地址");
            row.createCell(18).setCellValue("收货人电话");
            row.createCell(19).setCellValue("收货人邮编");
            row.createCell(20).setCellValue("收货人姓名");
            row.createCell(21).setCellValue("过期时间");
            row.createCell(22).setCellValue("发票类型");
            row.createCell(23).setCellValue("订单来源");
            row.createCell(24).setCellValue("商家ID");
            row.createCell(25).setCellValue("支付订单号");



            for (int i = 0; i < orderList.size(); i++) {
                Order order = orderList.get(i);
                HSSFRow rowi = sheet.createRow(i+1);
                rowi.createCell(0).setCellValue(order.getOrderId()+"");
                rowi.createCell(1).setCellValue(order.getPayment()+"");
                rowi.createCell(2).setCellValue(order.getPaymentType()+"");
                rowi.createCell(3).setCellValue(order.getPostFee()+"");
                rowi.createCell(4).setCellValue(order.getStatus()+"");
                if (order.getCreateTime()!=null){
                    rowi.createCell(5).setCellValue(DateFormatUtils.formatDate(order.getCreateTime()));
                }else {
                    rowi.createCell(5).setCellValue("");
                }

                if (order.getUpdateTime()!=null){
                    rowi.createCell(6).setCellValue(DateFormatUtils.formatDate(order.getUpdateTime()));
                }else {
                    rowi.createCell(6).setCellValue("");
                }

                if (order.getPaymentTime()!=null){
                    rowi.createCell(7).setCellValue(DateFormatUtils.formatDate(order.getPaymentTime()));
                }else {
                    rowi.createCell(7).setCellValue("");
                }

                if (order.getConsignTime()!=null){
                    rowi.createCell(8).setCellValue(DateFormatUtils.formatDate(order.getConsignTime()));
                }else {
                    rowi.createCell(8).setCellValue("");
                }

                if (order.getEndTime()!=null){
                    rowi.createCell(9).setCellValue(DateFormatUtils.formatDate(order.getEndTime()));
                }else {
                    rowi.createCell(9).setCellValue("");
                }

                if (order.getCloseTime()!=null){
                    rowi.createCell(10).setCellValue(DateFormatUtils.formatDate(order.getCloseTime()));
                }else {
                    rowi.createCell(10).setCellValue("");
                }
                rowi.createCell(11).setCellValue(order.getShippingName()+"");
                rowi.createCell(12).setCellValue(order.getShippingCode()+"");
                rowi.createCell(13).setCellValue(order.getUserId()+"");
                rowi.createCell(14).setCellValue(order.getBuyerMessage()+"");
                rowi.createCell(15).setCellValue(order.getBuyerNick()+"");
                rowi.createCell(16).setCellValue(order.getBuyerRate()+"");
                rowi.createCell(17).setCellValue(order.getReceiverAreaName()+"");
                rowi.createCell(18).setCellValue(order.getReceiverMobile()+"");
                rowi.createCell(19).setCellValue(order.getReceiverZipCode()+"");
                rowi.createCell(20).setCellValue(order.getReceiver()+"");
                rowi.createCell(21).setCellValue(order.getExpire()+"");
                rowi.createCell(22).setCellValue(order.getInvoiceType()+"");
                rowi.createCell(23).setCellValue(order.getSourceType()+"");
                rowi.createCell(24).setCellValue(order.getSellerId()+"");
                rowi.createCell(25).setCellValue(order.getOutTradeNo()+"");
            }

            OutputStream output=response.getOutputStream();
            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=Orders.xls");
            response.setContentType("application/msexcel");
            book.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
