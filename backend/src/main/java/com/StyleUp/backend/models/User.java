package com.StyleUp.backend.models;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long UserId;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false)
    private String lname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)//declares that one room can have many decorations, updates everything when one is saved
    /*The @JoinColumn annotation combined with a @OneToOne mapping indicates that a given column in the owner entity
    refers to a primary key in the reference entity*/
    @JoinColumn (name = "fk_user_id", referencedColumnName = "UserId")
    private List<Room> rooms;


    // Default constructor for JPA
    public User() {}

    //Actual constructor
    public User(String fname, String lname, String email, String password, List<Room> rooms) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.rooms = rooms;
    }

    // Getters and setters
    public Long getId() {
        return UserId;
    }

    public void setId(Long id) {
        this.UserId = id;
    }

    public String getFname() {return fname;}

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    @PrePersist
    public void encryptPassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }
}
