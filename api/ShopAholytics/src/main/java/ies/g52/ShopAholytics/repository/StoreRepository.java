package ies.g52.ShopAholytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.Store;

public interface StoreRepository  extends JpaRepository<Store,Integer> {
    
    //ver o que precisa depois;

    /*
    @Query("SELECT * FROM Store WHERE id_shopping = :id")
    List<Store> findByShoppingId(@Param("id") Integer shopping_id);
    */
}
