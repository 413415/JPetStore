package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/orderManagementController")
@SessionAttributes(value = {"account" ,"cartList","cart","order","cartListSize"})

public class OrderManagementController {

    @Autowired
    private OrderService orderService;

    //进入订单模块
    @GetMapping("/enterOrder")
    public String enterOrder(Model model)
    {
        model.addAttribute("orderList" , orderService.getAllOrder());
        return "orderManagerment/index";
    }


    //进入修改单一订单
    @GetMapping("/enterOneOrder")
    public String enterOneOrder(int orderId,Model model)
    {
        model.addAttribute("order", orderService.getOrder(orderId));
        return "orderManagerment/changeOrder";
    }


    //修改单一订单信息
    @PostMapping("/updateOrder")
    public String updateOrder(Order order , Model model)
    {
        orderService.updateOrder(order);
        model.addAttribute("orderList" , orderService.getAllOrder());
        model.addAttribute("order",orderService.getOrder(order.getOrderId()));
        return "orderManagerment/index";
    }

    //删除单一订单信息
    @GetMapping("/deleteOrder")
    public String deleteOrder(int orderId,Model model)
    {
        orderService.deleteOrder(orderId);
        model.addAttribute("orderList" , orderService.getAllOrder());
        return "orderManagerment/index";
    }

    //单一订单发货功能
    @GetMapping("/postOrder")
    public String postOrder(int orderId, Model model)
    {
        orderService.changeOrderStatus(orderId,"Y");
        model.addAttribute("orderList" , orderService.getAllOrder());
        model.addAttribute("order",orderService.getOrder(orderId));
        return "orderManagerment/index";
    }


    //单一订单确认到达
    @GetMapping("/surePostOrder")
    public String surePostOrder(int orderId, Model model)
    {
        orderService.changeOrderStatus(orderId,"S");
        model.addAttribute("orderList" ,  orderService.getOrdersByUsername(((Account)model.getAttribute("account")).getUsername()));
        model.addAttribute("order",orderService.getOrder(orderId));
        return "cart/myOrder";
    }

    //返回搜索结果
    @PostMapping("/getSearchOrder")
    public String getSearchOrder(String keywords,Model model)
    {
        model.addAttribute("orderList" , orderService.searchOrder(keywords));
        return "orderManagerment/searchOrder";
    }

}
