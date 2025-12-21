package com.codewithvenkat.demo.Impl;

import org.springframework.stereotype.Service;

import com.codewithvenkat.demo.Interface.IPaymentService;

@Service("phonepayService")
public class PhonepayService implements IPaymentService {

    @Override
    public String transferFunds(double amount) {
         return "Transferring funds through Phonepay: " + amount;
    }
    
}
