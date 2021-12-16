package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.ShoppingManager;
import ies.g52.ShopAholytics.repository.ShoppingManagerRepository;


@Service
public class ShoppingManagerService {
    @Autowired
    private ShoppingManagerRepository repository;

    public ShoppingManager saveShoppingManager(ShoppingManager ShoppingManager) {
        return repository.save(ShoppingManager);
    }

    public List<ShoppingManager> saveShoppingManager(List<ShoppingManager> ShoppingManager) {
        return repository.saveAll(ShoppingManager);
    }

    public List<ShoppingManager> getShoppingManagers() {
        return repository.findAll();
    }

    public ShoppingManager getShoppingManagerById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public String deleteShoppingManager(int id) {
        repository.deleteById(id);
        return "ShoppingManager removed !! " + id;
    }

    public ShoppingManager updateShoppingManager(ShoppingManager ShoppingManager) {
        ShoppingManager existingShoppingManager = repository.findById((int)ShoppingManager.getId()).orElse(null);
        existingShoppingManager.setShopping(ShoppingManager.getShopping());
        existingShoppingManager.setUser(ShoppingManager.getUser());
        return repository.save(existingShoppingManager);
    }

   
}
