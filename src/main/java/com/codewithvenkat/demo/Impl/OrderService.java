package com.codewithvenkat.demo.Impl;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.codewithvenkat.demo.Interface.IOrderService;
import com.codewithvenkat.demo.Interface.IPaymentService;

@Service
public class OrderService implements IOrderService {

    private  IPaymentService paymentService;

    public OrderService(@Qualifier("phonepayService") IPaymentService paymentService) {
        this.paymentService = paymentService;   
    }
    @Override
    public String placeOrder(double amount) {
        // Implementation of order placement logic
        return paymentService.transferFunds(amount);
        // String output = "Order placed with amount: " + amount;
        // System.out.println(output);
        // return output;
    }
    
}
