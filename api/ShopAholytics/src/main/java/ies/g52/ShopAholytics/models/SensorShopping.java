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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Sensor_Shopping")
public class SensorShopping {
    
    @Id
    @Column(name = "id_sensor")
    @JsonIgnore
    private int id;

    @Column(name="park_associated")
    private boolean park_associated;
    @ManyToOne
    @JoinColumn(name="id_shopping")
    @JsonIgnore
    private Shopping shopping;

    @OneToOne
    @MapsId
    @JoinColumn(name="id_sensor")
    private Sensor sensor;


    public SensorShopping(Shopping shopping, Sensor sensor,boolean x) {
        this.sensor=sensor;
        this.shopping = shopping;
        this.park_associated=x;
    }

    public SensorShopping(){}

    public Sensor getSensor() {
        return this.sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }


    public int getId() {
        return this.id;
    }


    public boolean isPark_associated() {
        return park_associated;
    }

    public void setPark_associated(boolean park_associated) {
        this.park_associated = park_associated;
    }

    public Shopping getShopping() {
        return this.shopping;
    }

}
