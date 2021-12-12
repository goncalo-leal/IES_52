package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.repository.ShoppingRepository;
import ies.g52.ShopAholytics.views.ShoppingIDMappingView;
import ies.g52.ShopAholytics.views.ShoppingStoresParksView;


@Service
public class MQService {
    
    
    @Autowired
    private ShoppingRepository shoppingRepository;


    public List<ShoppingIDMappingView> shoppingIdNameMapper() {
        return shoppingRepository.findMappingsBy();
    }

    
    public List<ShoppingStoresParksView> shoppingStoresParksGet(int id) {
        return shoppingRepository.findParksStoresById(id);
    }
    
}
