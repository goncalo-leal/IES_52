package ies.g52.ShopAholytics.models;

import java.util.Date;

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
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;

@Entity
@Table(name = "User")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "password")
    //@JsonIgnore
    //@ToString.Exclude
    private String password;


    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "gender")
    private String gender;

    //algumas duvidas se as dates é assim
    @Column(name = "birtday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    // Vai ser estrangeiro aqui
    @ManyToOne(optional = false)
    @JoinColumn(name = "state", nullable = false)
    private UserState user_state;
    

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private StoreManager storeManager;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private ShoppingManager shoppingManager;


    
    public User(){

    }

    public User(String password, String email, String name, String gender, Date birthday, UserState user_state) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.user_state = user_state;
    }

    public int getId() {
        return id;
    }

  
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public UserState getState() {
        return user_state;
    }

    public void setState(UserState user_state) {
        this.user_state = user_state;
    }

    @Override
    public String toString() {
        return "User [birthday=" + birthday + ", email=" + email + ", gender=" + gender + ", id=" + id + ", name="
                + name + ", password=" + password + ", user_state=" + user_state + "]";
    }

    

    
    
}
