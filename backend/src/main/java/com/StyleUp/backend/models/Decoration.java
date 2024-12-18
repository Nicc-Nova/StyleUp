package com.StyleUp.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "decorations")
public class Decoration {
    @Id
    @Column(name="decoration_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long decoration_id;

    @Column (name = "fkr")
    private Long fkr;

    @Column (name = "wish_id")
    private Long wishId;

    @Column
    private String search_link;

    @Column (name="description")
    private String description;

    @Column (name="title")
    private String title;

    @Column (name="image")
    private String image;

    public Decoration() {}

    public Decoration(String searchLink, Long fkRoomId, Long wishId,String description, String title, String image) {
        this.search_link = searchLink;
        this.fkr = fkRoomId;
        this.wishId = wishId;
        this.description = description;
        this.title = title;
        this.image = image;
    }

    public Long getDec_id() {
        return decoration_id;
    }

    public void setDec_id(Long dec_id) {
        this.decoration_id = dec_id;
    }

    public Long getFkr() {
        return fkr;
    }

    public void setFkr(Long fkr) {
        this.fkr = fkr;
    }

    public Long getWishId() {
        return wishId;
    }

    public void setWishId(Long wishId) {
        this.wishId = wishId;
    }

    public String getSearchLink() {
        return this.search_link;
    }

    public void setSearchLink(String searchLink) {
        this.search_link = searchLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Decoration{" +
                "DecorationId=" + decoration_id +
                ", fkr=" + fkr +
                ", searchLink='" + search_link + '\'' +
                ", description=" + description + '\'' +
                ", title=" + title + '\'' +
                ", image=" + image + '\'' +
                '}';
    }
}
