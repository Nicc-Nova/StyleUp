package com.StyleUp.backend.models;

import com.StyleUp.backend.repositories.DecorationRepository;
import jakarta.persistence.*;
import java.lang.*;
import java.util.*;

@Entity
@Table (name = "rooms")
public class Room {
    @Id
    @Column(name="room_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long room_id;

    @Column(name = "roomName", nullable = false)
    private String roomName;

    @Column(name = "fku")
    private Long fku;

    @OneToMany (cascade = CascadeType.ALL, orphanRemoval = true)//declares that one room can have many decorations, updates everything when one is saved
    /*The @JoinColumn annotation combined with a @OneToOne mapping indicates that a given column in the owner entity
    refers to a primary key in the reference entity*/
    @JoinColumn (name = "fkr", referencedColumnName = "room_id")
    private List<Decoration> decorations=new ArrayList<>();

    public Room() {}

    public Room(String roomName, Long fkUserId, List<Decoration> decorations) {
        this.roomName = roomName;
        this.fku = fkUserId;
        this.decorations = decorations;
    }

    public Long getRoom_id() {
        return this.room_id;
    }

    public void setRoom_id(Long rm_id) {
        this.room_id = rm_id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Decoration> getDecorations() {
        return this.decorations;
    }

    public void setDecorations(List<Decoration> decorations) {
        this.decorations = decorations;
    }

    @Override
    public String toString() {
        return "Room{" +
                "room_id=" + this.room_id +
                ", roomName='" + this.roomName + '\'' +
                ", fku=" + this.fku +
                ", decorations=" + this.decorations.toString() +
                '}';
    }

    public Long getFku() {
        return fku;
    }

    public void setFku(Long fku) {
        this.fku = fku;
    }
}
