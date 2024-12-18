package com.StyleUp.backend.models;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")

public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @Column(nullable = false)
    private String fname;

    @Column(nullable = false)
    private String lname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
    //declares that one room can have many decorations, updates everything when one is saved
    /*The @JoinColumn annotation combined with a @OneToOne mapping indicates that a given column in the owner entity
    refers to a primary key in the reference entity*/
    @JoinColumn (name = "fku", referencedColumnName = "user_id")
    private List<Room> rooms=new ArrayList<>();

    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable (name="collaborations",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "room_id")
                )
    private Set<Room> collabRooms=new HashSet<>();

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn (name = "wish_id", referencedColumnName = "user_id")
    private List<Decoration> wishlist=new ArrayList<>();

    // Default constructor for JPA
    public User() {}

    //Actual constructor
    public User(String fname, String lname, String email, String password, List<Room> rooms, List<Decoration> wishlist) {
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
        this.rooms = rooms;
        this.wishlist = wishlist;
    }

    // Getters and setters
    public Long getId() {
        return user_id;
    }

    public void setId(Long id) {
        this.user_id = id;
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

    public Set<Room> getCollabRooms() {
        return collabRooms;
    }

    public void setCollabRooms(Set<Room> collabRooms) {
        this.collabRooms = collabRooms;
    }

    public List<Decoration> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Decoration> wishlist) {
        this.wishlist = wishlist;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + user_id +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", rooms=" + rooms.toString() +
                '}';
    }

    @PrePersist
    public void encryptPassword() {
        this.password = new BCryptPasswordEncoder().encode(this.password);
    }
}
