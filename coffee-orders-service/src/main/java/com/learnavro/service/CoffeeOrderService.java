package com.learnavro.service;

import com.learnavro.domain.generated.*;
import com.learnavro.dto.CoffeeOrderDTO;
import com.learnavro.dto.CoffeorderUpdateDTO;
import com.learnavro.dto.OrderLineItemDTO;
import com.learnavro.producer.CoffeeOrderProducer;
import com.learnavro.producer.CoffeeOrderUpdateProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CoffeeOrderService {
    @Autowired
    private CoffeeOrderProducer coffeeOrderProducer;

    @Autowired
    private CoffeeOrderUpdateProducer coffeeOrderUpdateProducer;


    public CoffeeOrderDTO newOrder(CoffeeOrderDTO coffeeOrderDTO) {
        var coffeOrderAVRO=mapToCoffeOrder(coffeeOrderDTO);
        coffeeOrderDTO.setId(coffeOrderAVRO.getId().toString());
        coffeeOrderProducer.sendMessage(coffeOrderAVRO);
        return coffeeOrderDTO;

    }

    private CoffeeOrder mapToCoffeOrder(CoffeeOrderDTO coffeeOrderDTO) {

        var store=builStore(coffeeOrderDTO);
        var orderLineItems=buildOrderLineItems(coffeeOrderDTO.getOrderLineItems());
        return CoffeeOrder
                .newBuilder()
                .setId(UUID.randomUUID())
                .setName(coffeeOrderDTO.getName())
                .setStore(store)
                .setOrderLineItems(orderLineItems)
//                .setOrderedTime(Instant.now())
                .setOrderedTime(coffeeOrderDTO.getOrderedTime().toInstant(ZoneOffset.UTC))
                .setPickUp(coffeeOrderDTO.getPickUp())
                .setStatus(coffeeOrderDTO.getStatus())
                .build();

    }

    private List<OrderLineItem> buildOrderLineItems(List<OrderLineItemDTO> orderLineItems) {
        return orderLineItems.stream()
                .map(orderLineItemDTO -> new OrderLineItem(orderLineItemDTO.getName(),
                        orderLineItemDTO.getSize(),
                        orderLineItemDTO.getQuantity(),
                        orderLineItemDTO.getCost())).collect(Collectors.toList());
    }

    private Store builStore(CoffeeOrderDTO coffeeOrderDTO) {
        var store=coffeeOrderDTO.getStore();
        return Store
                .newBuilder()
                .setId(store.getStoredId())
                .setAddress(new Address(store.getAddress().getAddressLine1(),
                        store.getAddress().getCity(),
                        store.getAddress().getState(),
                        store.getAddress().getCountry(),
                        store.getAddress().getZip())
                )
                .build();
    }

    public CoffeorderUpdateDTO updateExistingCoffeeOrder(String orderId,CoffeorderUpdateDTO coffeorderUpdateDTO) {
        var coffeeUpdateEvent=mapToCoffeOrderUpdate(orderId,coffeorderUpdateDTO);
        coffeeOrderUpdateProducer.sendMessage(coffeeUpdateEvent);
        return coffeorderUpdateDTO;
    }

    private CoffeeUpdateEvent mapToCoffeOrderUpdate(String orderId,CoffeorderUpdateDTO coffeorderUpdateDTO) {

        return CoffeeUpdateEvent.newBuilder()
                .setId(UUID.fromString(orderId))
                .setStatus(coffeorderUpdateDTO.getOrderStatus()).
                build();
    }
}
