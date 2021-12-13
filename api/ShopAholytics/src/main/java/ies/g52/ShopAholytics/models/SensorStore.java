package ies.g52.ShopAholytics.models;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.InheritanceType;


@Entity
@Table(name = "Sensor_Store")
public class SensorStore {
    
    @Id
    @Column(name = "id_sensor")
    private int id;

    @ManyToOne
    @JoinColumn(name="id_store")
    private Store store;

    @OneToOne
    @MapsId
    @JoinColumn(name="id_sensor")
    private Sensor sensor;


    public SensorStore(Store store, Sensor sensor) {
        this.sensor=sensor;
        this.store = store;
    }

    public SensorStore(){}

    public Sensor getSensor() {
        return this.sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }


    public int getId() {
        return this.id;
    }


    public Store getStore() {
        return this.store;
    }

}
