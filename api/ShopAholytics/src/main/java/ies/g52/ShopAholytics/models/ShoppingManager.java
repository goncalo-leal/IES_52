package ies.g52.ShopAholytics.models;

import java.util.Date;

import javax.persistence.CascadeType;
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
@Table(name = "shopping_manager")
public class ShoppingManager {

    @Id
    @Column(name = "id_user")
    private int id;

    @ManyToOne
    @JoinColumn(name="id_shopping")
    private Shopping shopping;

    public ShoppingManager(){}

    @OneToOne(fetch = FetchType.EAGER,
    cascade = {
            CascadeType.MERGE,
            CascadeType.REFRESH
        })
    @MapsId
    @JoinColumn(name="id_user")
    private User user;

    public ShoppingManager(User user, Shopping shopping) {
        this.user=user;
        this.shopping = shopping;
    }



    public Shopping getShopping() {
        return this.shopping;
    }

    public void setShopping(Shopping shopping) {
        this.shopping = shopping;
    }


    public int getId() {
        return this.id;
    }

    public void setUser(User user){
        this.user=user;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

  


    @Override
    public String toString() {
        return "ShoppingManager [id=" + id + ", shopping= " + shopping + ", user=" + user + "]";
    }

}
