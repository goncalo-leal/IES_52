

package ies.g52.ShopAholytics.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Product;
import ies.g52.ShopAholytics.services.ProductService;
import ies.g52.ShopAholytics.services.StoreService;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/")
public class ProductController {
    @Autowired
    private ProductService serviceProduct;

    

   @Autowired
   private StoreService serviceStore;

    @PostMapping("/addProduct")
    public Product newProduct( @RequestBody Product s,@PathVariable(value = "pid") int pid) {
        return serviceProduct.saveProduct(new Product(s.getName(),s.getReference(),s.getStock(),s.getPrice(),s.getDescription(), serviceStore.getStoreById(pid)));
    }

    
    @GetMapping("/Products")
    public List<Product> findAllProducts() {
        List<Product> a = serviceProduct.getProducts();
        return a;
    }
    

    @GetMapping("/Product")
    public Product findProductById(@RequestParam(value = "id")  int id) {
        Product a = serviceProduct.getProductById(id);
        return a;
        
    }



    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestBody Product Product) {
        return serviceProduct.updateProduct(Product);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id) {
        return serviceProduct.deleteProduct(id);
    }
}