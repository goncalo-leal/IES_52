package ies.g52.ShopAholytics.models;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    @Column(name = "birthday")
    private String birthday;

    // Vai ser estrangeiro aqui
    @ManyToOne(optional = false)
    @JoinColumn(name = "state", nullable = false)
    private UserState state;
    


    
    public User(){

    }

    public User(String password, String email, String name, String gender, String birthday, UserState state) {
        this.password = password;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.state = state;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public UserState getState() {
        return state;
    }

    public void setState(UserState state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User [birthday=" + birthday + ", email=" + email + ", gender=" + gender + ", id=" + id + ", name="
                + name + ", password=" + password + ", state=" + state + "]";
    }

    

    
    
}
