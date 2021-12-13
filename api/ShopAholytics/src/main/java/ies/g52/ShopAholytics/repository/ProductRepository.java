

package ies.g52.ShopAholytics.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import ies.g52.ShopAholytics.models.Product;

public interface ProductRepository  extends JpaRepository<Product,Integer> {
    
    Product findByName(String name);
    
}
