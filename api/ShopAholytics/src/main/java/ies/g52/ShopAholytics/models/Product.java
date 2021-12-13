package ies.g52.ShopAholytics.models;


import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name")
    private String name;
    @Column(name = "reference")
    private String reference;

    @Column(name = "stock")
    private int stock;

    @Column(name = "price")
    private float price;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_store", nullable = false)
    private Store store;

    


    public Product() {
    }


    public Product(String name, String reference, int stock, float price, String description, Store store) {
        this.name = name;
        this.reference = reference;
        this.stock = stock;
        this.price = price;
        this.description = description;
        this.store = store;
    }



    public int getId() {
        return id;
    }

    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Store getId_store() {
        return store;
    }

    public void setId_store(Store store) {
        this.store = store;
    }

    

    
}
