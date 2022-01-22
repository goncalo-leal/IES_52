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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "Store")

public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "current_capacity")
    private int current_capacity;

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

    @JsonIgnoreProperties("stores")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_shopping", nullable = false)
    private Shopping id_shopping;

    @Column(name = "img")
    private String img;

    public Store() {
    }

    public Store( String location, String name, int capacity, LocalTime opening, LocalTime closing ,Shopping id_shopping, String img) {
        
        this.location = location;
        this.name = name;
        this.capacity = capacity;
        this.opening = opening;
        this.closing = closing;
        this.id_shopping=id_shopping;
        this.current_capacity=0;
        this.img = img;
    }

    public int getId() {
        return id;
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

    public int getCurrent_capacity() {
        return current_capacity;
    }



    public void setCurrent_capacity(int current_capacity) {
        this.current_capacity = current_capacity;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "Store [capacity=" + capacity + ", closing=" + closing + ", current_capacity=" + current_capacity
                + ", id=" + id +  ", location=" + location + ", name=" + name
                + ", opening=" + opening + "]";
    }
    
    
}
