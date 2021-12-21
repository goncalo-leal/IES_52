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
@Table(name = "store_manager")
public class StoreManager {
    
    @Id
    @Column(name = "id_user")
    private int id;

    @ManyToOne
    @JoinColumn(name="id_store")
    private Store store;

    @OneToOne
    @MapsId
    @JoinColumn(name="id_user")
    private User user;


    public StoreManager(User user, Store store) {
        this.user=user;
        this.store = store;
    }

    public StoreManager(){}

    public Store getStore() {
        return this.store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public void setUser(User u){
        this.user=u;
    }

    public int getId() {
        return this.id;
    }


    public User getUser() {
        return this.user;
    }

}
