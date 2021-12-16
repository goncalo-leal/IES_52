package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.repository.StoreManagerRepository;


@Service
public class StoreManagerService {
    @Autowired
    private StoreManagerRepository repository;

    public StoreManager saveStoreManager(StoreManager StoreManager) {
        return repository.save(StoreManager);
    }

    public List<StoreManager> saveStoreManager(List<StoreManager> StoreManager) {
        return repository.saveAll(StoreManager);
    }

    public List<StoreManager> getStoreManagers() {
        return repository.findAll();
    }

    public StoreManager getStoreManagerById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public String deleteStoreManager(int id) {
        repository.deleteById(id);
        return "StoreManager removed !! " + id;
    }

    public StoreManager updateStoreManager(StoreManager StoreManager) {
        StoreManager existingStoreManager = repository.findById((int)StoreManager.getId()).orElse(null);
        existingStoreManager.setStore(StoreManager.getStore());
        existingStoreManager.setUser(StoreManager.getUser());
        return repository.save(existingStoreManager);
    }

   
}
