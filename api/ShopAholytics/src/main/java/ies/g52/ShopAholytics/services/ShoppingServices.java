package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.repository.ShoppingRepository;

@Service
public class ShoppingServices {
    @Autowired
    private ShoppingRepository repository;

    public Shopping saveShopping(Shopping Shopping) {
        return repository.save(Shopping);
    }

    public List<Shopping> saveShopping(List<Shopping> Shopping) {
        return repository.saveAll(Shopping);
    }

    public List<Shopping> getShopping() {
        return repository.findAll();
    }

    public Shopping getShoppingByName(String name) {
        return repository.findByName(name);
    }

    public Shopping getShoppingById(int id) {
        return repository.findById((int)id).orElse(null);
    }


    public String deleteShopping(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    public Shopping updateShopping(Shopping user) {
        //acho que este e o user esta incompleto mas preciso de testar para saber
        Shopping existingShopping = repository.findById((int)user.getId()).orElse(null);
        return repository.save(existingShopping);
    }

}
