package com.LaptopWeb.utils;

import com.LaptopWeb.entity.StatusOrder;
import com.LaptopWeb.repository.StatusOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

//@Component
//public class AddStatusOrder implements CommandLineRunner {
//    @Autowired
//    private StatusOrderRepository statusOrderRepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        StatusOrder pending = new StatusOrder("Pending");
//        StatusOrder confirmed = new StatusOrder("Confirmed");
//        StatusOrder shipped = new StatusOrder("Shipped");
//        StatusOrder delivered = new StatusOrder("Delivered");
//        StatusOrder canceled = new StatusOrder("Canceled");
//
//        statusOrderRepository.saveAll(Arrays.asList(pending, confirmed, shipped, delivered, canceled));
//    }
//}
