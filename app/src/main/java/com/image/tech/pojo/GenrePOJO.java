package com.image.tech.pojo;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Model POJO class for Genre
 */
public class GenrePOJO implements Serializable {
    private static final long serialVersionUID = 2L;
    private String name;
    private HashMap<Integer, ArtistPOJO> artists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<Integer, ArtistPOJO> getArtists() {
        return artists;
    }

    public void setArtists(HashMap<Integer, ArtistPOJO> artists) {
        this.artists = artists;
    }
}
