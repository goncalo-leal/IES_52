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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "sensor")
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "type")
    private String type;

    @OneToOne(mappedBy = "sensor")
    @PrimaryKeyJoinColumn
    private SensorShopping sensorShopping;

    @OneToOne(mappedBy = "sensor")
    @PrimaryKeyJoinColumn
    private SensorPark sensorPark;

    @OneToOne(mappedBy = "sensor")
    @PrimaryKeyJoinColumn
    private SensorStore sensorStore;

    @OneToMany(mappedBy = "sensor")
    @JsonIgnore
    private Set<SensorData> sensorData;

    public Sensor() {
    }

   
    public Sensor(String type){
        this.type=type;
    }

    public String getType(){
        return this.type;
    }

    public int getId() {
        return id;
    }


    public void setType(String type){
        this.type=type;
    }
}   

