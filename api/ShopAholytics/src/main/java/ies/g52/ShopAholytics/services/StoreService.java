package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.Store;
import ies.g52.ShopAholytics.repository.StoreRepository;

@Service
public class StoreService {
    @Autowired
    private StoreRepository repository;

    public Store saveStore(Store Store) {
        return repository.save(Store);
    }

    public List<Store> saveStore(List<Store> Store) {
        return repository.saveAll(Store);
    }

    public List<Store> getStore() {
        return repository.findAll();
    }

    public Store getStoreById(int id) {
        return repository.findById((int)id).orElse(null);
    }


    public String deleteStore(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    public Store updateStore(Store user) {
        //acho que este e o user esta incompleto mas preciso de testar para saber
        Store existingStore = repository.findById((int)user.getId()).orElse(null);
        existingStore.setName(user.getName());
        existingStore.setLocation(user.getLocation());
        existingStore.setCapacity(user.getCapacity());
        existingStore.setOpening(user.getOpening());
        existingStore.setClosing(user.getClosing());
        return repository.save(existingStore);
    }

}
