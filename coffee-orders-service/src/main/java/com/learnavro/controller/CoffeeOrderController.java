package com.learnavro.controller;

import com.learnavro.dto.CoffeeOrderDTO;
import com.learnavro.dto.CoffeorderUpdateDTO;
import com.learnavro.service.CoffeeOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.POST;

@RestController
@RequestMapping("v1/coffee_orders")
@Slf4j
public class CoffeeOrderController {

    @Autowired
    private CoffeeOrderService coffeeOrderService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CoffeeOrderDTO newOrder(@RequestBody CoffeeOrderDTO coffeeOrderDTO){
        log.info("Received the request for an order: {}",coffeeOrderDTO);
        return coffeeOrderService.newOrder(coffeeOrderDTO);
    }


    @PutMapping("/{order-id}")
    @ResponseStatus(HttpStatus.OK)
    public CoffeorderUpdateDTO updateExistingOrder(@PathVariable("order_id") String orderId,
                                                    @RequestBody CoffeorderUpdateDTO coffeorderUpdateDTO){

        return coffeeOrderService.updateExistingCoffeeOrder(orderId,coffeorderUpdateDTO);

    }
}
