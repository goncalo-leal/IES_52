package ies.g52.ShopAholytics.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.services.MQService;
import ies.g52.ShopAholytics.views.ShoppingIDMappingView;
import ies.g52.ShopAholytics.views.ShoppingStoresParksView;

import java.util.List;

@RestController
@RequestMapping("/mq/")
public class MQController {
    @Autowired
    private MQService mqService;

    @GetMapping("/Shoppings")
    public List<ShoppingIDMappingView> shoppingIdName() {
        return mqService.shoppingIdNameMapper();
    }

    @GetMapping("/Shopping")
    public ShoppingStoresParksView storesAndParksOfShopping(@RequestParam(value = "id")  int id) {
        return mqService.shoppingStoresParksGet(id);   
    }
}
