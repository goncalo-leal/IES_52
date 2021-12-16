package ies.g52.ShopAholytics.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies.g52.ShopAholytics.models.Product;
import ies.g52.ShopAholytics.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product saveProduct(Product Product) {
        return repository.save(Product);
    }

    public List<Product> saveProduct(List<Product> Product) {
        return repository.saveAll(Product);
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProductById(int id) {
        return repository.findById((int)id).orElse(null);
    }

    public Product getProductByName(String email) {
        return repository.findByName(email);
    }

    public String deleteProduct(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    public Product updateProduct(Product Product) {
        Product existingProduct = repository.findById((int)Product.getId()).orElse(null);
        existingProduct.setName(Product.getName());
        existingProduct.setDescription(Product.getDescription());
        existingProduct.setReference(Product.getReference());
        existingProduct.setStock(Product.getStock());
        existingProduct.setPrice(Product.getPrice());
        return repository.save(existingProduct);
    }

   
}
