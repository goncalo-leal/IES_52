package ies.g52.ShopAholytics.models;


import java.time.LocalTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Shopping")
public class Shopping {
    private int sum_shops_capacity;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "opening")
    private LocalTime opening;

    @Column(name = "closing")
    private LocalTime closing;

    @OneToMany(cascade= CascadeType.ALL ,mappedBy = "id_shopping", orphanRemoval = true)
    private Set<Park> parks;

    @OneToMany(cascade = CascadeType.ALL ,mappedBy = "id_shopping", orphanRemoval = true)
    private Set<Store> stores;

    public Shopping() {
    }

    public Shopping( String location, String name, int capacity, LocalTime opening, LocalTime closing) {
        
        this.location = location;
        this.name = name;
        this.capacity = capacity;
        this.opening = opening;
        this.closing = closing;
        this.sum_shops_capacity=0;
    }


    public int getId() {
        return id;
    }


    public int getSum_shops_capacity() {
        return this.sum_shops_capacity;
    }

    public void setSum_shops_capacity(int sum_shops_capacity) {
        this.sum_shops_capacity = sum_shops_capacity;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public LocalTime getOpening() {
        return opening;
    }

    public void setOpening(LocalTime opening) {
        this.opening = opening;
    }

    public LocalTime getClosing() {
        return closing;
    }

    public void setClosing(LocalTime closing) {
        this.closing = closing;
    }

    public Set<Park> getParks() {
        return this.parks;
    }

    public void setParks(Set<Park> parks) {
        this.parks = parks;
    }

    public Set<Store> getStores() {
        return this.stores;
    }

    public void setStores(Set<Store> stores) {
        this.stores = stores;
    }

    
}
