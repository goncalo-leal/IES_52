package ies.g52.ShopAholytics.models;


import java.time.LocalTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Park")
public class Park {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "name")
    private String name;



    @Column(name = "location")
    private String location;


    @Column(name = "capacity")
    private int capacity;

    @Column(name = "opening")
    private LocalTime opening;

    @Column(name = "closing")
    private LocalTime closing;
    

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_shopping", nullable = false)
    private Shopping shopping;


    @OneToMany(mappedBy = "park")
    @JsonIgnore
    private Set<SensorPark> sensorPark;

    public Park() {
    }

    
    public Park(String name, String location, int capacity, LocalTime opening, LocalTime closing,
            Shopping shopping) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.opening = opening;
        this.closing = closing;
        this.shopping = shopping;
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


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
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


    public Shopping getId_shopping() {
        return shopping;
    }


    public void setId_shopping(Shopping shopping) {
        this.shopping = shopping;
    }

    
    
}
