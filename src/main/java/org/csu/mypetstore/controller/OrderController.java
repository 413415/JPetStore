package org.csu.mypetstore.controller;

import org.csu.mypetstore.domain.Account;
import org.csu.mypetstore.domain.Cart;
import org.csu.mypetstore.domain.CartDb;
import org.csu.mypetstore.domain.Order;
import org.csu.mypetstore.service.OrderService;
import org.csu.mypetstore.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/order")
@SessionAttributes(value = {"account" ,"cartList","cart","order","cartListSize"})
public class OrderController {
    @Autowired
    public OrderService orderService;
    @Autowired
    public StockService stockService;

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @GetMapping("/newOrder")
    public String newOrder(Model model)
    {

        Order order=(Order) model.getAttribute("order");
        order = orderService.insertOrder(order);

        model.addAttribute("order",order);
        model.addAttribute("cart",null);

        //库存减少
        for(int i =0; i<order.getLineItems().size();i++){
            int a = stockService.getStockNumByItemId(order.getLineItems().get(i).getItemId()) - order.getLineItems().get(i).getQuantity();
            stockService.updateStockNum(order.getLineItems().get(i).getItemId(),a);
        }


        return "order/viewOrder";
    }

    @PostMapping("/viewConfirmOrder")
    public  String viewConfirmOrder(Order order1 , Model model)
    {
        Order order=(Order) model.getAttribute("order");

        //从前端ShippingForm.jsp中获取相关改变的ship值，赋值给order
        order.setShipToFirstName(order1.getShipToFirstName());
        order.setShipToLastName(order1.getShipToLastName());
        order.setShipAddress1(order1.getShipAddress1());
        order.setShipAddress2(order1.getShipAddress2());
        order.setShipCity(order1.getShipCity());
        order.setShipState(order1.getShipState());
        order.setShipZip(order1.getShipZip());
        order.setShipCountry(order1.getShipCountry());

        //把order对象中的新数据更新到session("order")
        model.addAttribute("order",order);

        return "order/confirmOrder";
    }

    @GetMapping("/viewNewOrder")
    public String viewNewOrder(Model model)
    {
        String falsemsg = "";

        Order order=new Order();
        Account account= (Account) model.getAttribute("account");
        Cart cart = (Cart) model.getAttribute("cart");

        order.initOrder(account);
        //order对象中的数据存储到session("order")中
        model.addAttribute("order",order);

        List<String> cardList = new ArrayList<String>();
        cardList.add("Visa");
        cardList.add("MasterCard");
        cardList.add("American Express");
        List<String> CARD_TYPE_LIST = Collections.unmodifiableList(cardList);

        model.addAttribute("cardtype",CARD_TYPE_LIST);

        boolean ifbuy = true;

        List<CartDb> cartList = (List<CartDb>)model.getAttribute("cartList");
        //判断库存
        for(int i =0; i<cartList.size();i++){
            System.out.println(cartList.get(i).getQuantity());
            System.out.println("我们检测了！");
            int b = stockService.getStockNumByItemId(cartList.get(i).getItemId()) - cartList.get(i).getQuantity();
            if(b<0)
            {
                ifbuy = false;
            }
        }
        if(ifbuy) {
            return "order/newOrderForm";
        }
        else
        {
        for(int i =0; i<cartList.size();i++){
            int b = stockService.getStockNumByItemId(cartList.get(i).getItemId()) - cartList.get(i).getQuantity();
            if(b<0)
            {
                falsemsg += cartList.get(i).getItemId()+"is not enough /n";
            }
        }
        model.addAttribute("falsemsg",falsemsg);
        return "cart/cart";
    }
    }

    @PostMapping("/viewShippingForm")
    public String viewShippingForm( Order order1,Model model)
    {
        Order order=(Order) model.getAttribute("order");

        order.setCardType(order1.getCardType());
        order.setCreditCard(order1.getCreditCard());
        order.setExpiryDate(order1.getExpiryDate());

        order.setBillToFirstName(order1.getBillToFirstName());
        order.setBillToLastName(order1.getBillToLastName());
        order.setBillAddress1(order1.getBillAddress1());
        order.setBillAddress2(order1.getBillAddress2());
        order.setBillCity(order1.getBillCity());
        order.setBillState(order1.getBillState());
        order.setBillZip(order.getBillZip());
        order.setBillCountry(order1.getBillCountry());

        order.setShipToFirstName(order1.getBillToFirstName());
        order.setShipToLastName(order1.getBillToLastName());
        order.setShipAddress1(order1.getBillAddress1());
        order.setShipAddress2(order1.getBillAddress2());
        order.setShipCity(order1.getBillCity());
        order.setShipState(order1.getBillState());
        order.setShipZip(order1.getBillZip());
        order.setShipCountry(order1.getBillCountry());



        model.addAttribute("order",order);

//        cheked = request.getParameter("shippingAddressRequired");
//        if(cheked == null)
//            request.getRequestDispatcher(CONFIRM_ORDER_JSP).forward(request,response);
//        else
        return "order/shippingForm";
    }
}
