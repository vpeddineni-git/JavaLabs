package com.codewithvenkat.demo;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codewithvenkat.demo.Interface.IOrderService;

@Controller
public class HomeController {

    // Injecting application name from properties file
    @Value("${spring.application.name}")
    private String  applicationName;
    
    // Dependency injection via constructor (IOC)
    private final IOrderService orderService;
    
    public HomeController( IOrderService orderService) {
        this.orderService = orderService;
    }
    @RequestMapping("/")
    public String home() {
        return "home.html";
    }
    @RequestMapping("/Order")
    @ResponseBody
    public String orderItem() {
        return orderService.placeOrder(2500.00);
    }
}
