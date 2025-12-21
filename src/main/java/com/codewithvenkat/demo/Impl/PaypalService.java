package com.codewithvenkat.demo.Impl;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.codewithvenkat.demo.Interface.IPaymentService;

@Service("paypalService")
@Primary
public class PaypalService implements IPaymentService {

    @Override
    public String transferFunds(double amount) {
        return "Transferring funds through paypal: " + amount;
    }
    
}
