package ies.g52.ShopAholytics.models;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;

@Entity
@Table(name = "Sensor_Data")
public class SensorData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "data")
    private String data;

    @Column(name="timestamp")
    private LocalDateTime date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_sensor", nullable = false)
    private Sensor sensor;

    public SensorData(String data, Sensor sensor, LocalDateTime date ){
        this.data=data;
        this.sensor=sensor;
        this.date =date;
    }
    public SensorData(){}


    public String getData() {
        return this.data;
    }

    public LocalDateTime getDate() {
        return this.date;
    }

    public Sensor getSensor() {
        return this.sensor;
    }

    public int getId() {
        return this.id;
    }


    public void setData(String data) {
        this.data = data;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }
    

}

