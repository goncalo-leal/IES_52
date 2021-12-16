package ies.g52.ShopAholytics.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.models.StoreManager;
import ies.g52.ShopAholytics.repository.StoreManagerRepository;


@Service
public class StoreManagerService {
    @Autowired
    private StoreManagerRepository repository;

    @Autowired
    private ShoppingServices shoppingServices;

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
        return repository.save(existingStoreManager);
    }

    public List<StoreManager> storeManagersOfShopping(int id) {

        Shopping s = shoppingServices.getShoppingById(id);
            
        Set<Store> stores=s.getStores();
        List<StoreManager> managers = this.getStoreManagers();
        List<StoreManager> ret= new ArrayList<>();

        for (StoreManager a: managers){
            if (stores.contains(a.getStore()) ){
                ret.add(a);
            }
        }
        
        return ret;
    }

   
}
