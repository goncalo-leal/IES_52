package ies.g52.ShopAholytics.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.Shopping;
import ies.g52.ShopAholytics.views.ShoppingIDMappingView;
import ies.g52.ShopAholytics.views.ShoppingStoresParksView;

public interface ShoppingRepository  extends JpaRepository<Shopping,Integer> {
    
    //ver o que precisa depois;
    Shopping findByName(String name);

    List<ShoppingIDMappingView> findMappingsBy();

    ShoppingStoresParksView findParksStoresById(int id);
    
}
