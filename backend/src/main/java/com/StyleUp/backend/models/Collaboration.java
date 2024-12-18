package com.StyleUp.backend.models;

import jakarta.persistence.*;

@Entity
@Table (name ="collaborations")
public class Collaboration {
    @Id
    @Column(name ="collaboration_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long collaborationId;

    @Column(name="user_id")
    private Long userId;

    @Column(name="room_id")
    private Long roomId;

    public Collaboration() {}

    public Collaboration(Long userId, Long roomId) {
        this.userId = userId;
        this.roomId = roomId;
    }

    public Long getCollaborationId() {
        return collaborationId;
    }

    public void setCollaborationId(Long collaborationId) {
        this.collaborationId = collaborationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public String toString() {
        return "Collaboration{" +
                "collaborationId=" + collaborationId +
                ", userId=" + userId +
                ", roomId=" + roomId +
                '}';
    }
}
