

package ies.g52.ShopAholytics.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ies.g52.ShopAholytics.models.Product;
import ies.g52.ShopAholytics.services.ProductService;
import ies.g52.ShopAholytics.services.StoreService;

import java.util.List;

@RestController
@RequestMapping("/api/products/")
public class ProductController {
    @Autowired
    private ProductService productService;

    

   @Autowired
   private StoreService storeService;

    @PostMapping("/addProduct/{pid}")
    public Product newProduct( @RequestBody Product s,@PathVariable(value = "pid") int pid) {
        if (s.getPrice() > 0 && s.getStock() > 0 ){
            return productService.saveProduct(new Product(s.getName(),s.getReference(),s.getStock(),s.getPrice(),s.getDescription(), storeService.getStoreById(pid)));
        }
        return null;
    }

    
    @GetMapping("/Products")
    public List<Product> findAllProducts() {
        List<Product> a = productService.getProducts();
        return a;
    }
    

    @GetMapping("/Product")
    public Product findProductById(@RequestParam(value = "id")  int id) {
        Product a = productService.getProductById(id);
        return a;
        
    }



    @PutMapping("/updateProduct")
    public Product updateProduct(@RequestBody Product Product) {
        return productService.updateProduct(Product);
    }

    @DeleteMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable int id) {
        return productService.deleteProduct(id);
    }
}