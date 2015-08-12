package com.image.tech.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Model POJO class for Artist
 */
public class ArtistPOJO implements Serializable {
    private static final long serialVersionUID = 2L;
    private int id;
    private String genres;
    private String picture;
    private String name;
    private String description;

    private List<ArtistPOJO> relatedArtists;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ArtistPOJO> getRelatedArtists() {
        return relatedArtists;
    }

    public void setRelatedArtists(List<ArtistPOJO> relatedArtists) {
        this.relatedArtists = relatedArtists;
    }

    @Override
    public boolean equals(Object obj) {
        if(null == obj) return false;
        if(!(obj instanceof ArtistPOJO)) return false;

        if(this.getId() == ((ArtistPOJO) obj).getId() &&
                this.getName().equalsIgnoreCase(((ArtistPOJO) obj).getName())) return true;
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 31 + getId();
        hash = hash * 31 + (null == getName() ? 0 : getName().hashCode());

        return hash;
    }
}
