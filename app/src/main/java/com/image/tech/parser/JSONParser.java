package com.image.tech.parser;

import android.util.Log;

import com.image.tech.exception.AppException;
import com.image.tech.pojo.ArtistPOJO;
import com.image.tech.pojo.GenrePOJO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is responsible for parsing JSON data
 * It parses through the data and builds corresponding model
 */
public class JSONParser {

    /**
     * Parses JSON string and make a list of artists
     * @param response valid JSON string
     * @return list of artists
     * @throws AppException if any error occurs during parsing
     */
     public HashMap<String, GenrePOJO> parseArtist(String response) throws AppException {
        if(null == response) throw new AppException(AppException.EMPTY_JSON);

        JSONObject json = null;
        try {
            json = new JSONObject(response);
        } catch (JSONException e) {
            e.printStackTrace();
            throw new AppException(AppException.JSON_OBJECT_CREATE_ERROR);
        }

        HashMap<String, GenrePOJO> artistMap = new HashMap<String, GenrePOJO>(20, 0.75f);

        String tmpGenre = null;
        try {
            JSONArray resultArray = json.getJSONArray("artists");
            for(int i = 0; i < resultArray.length(); i++) {
                JSONObject row = resultArray.getJSONObject(i);

                tmpGenre = row.optString("genres", null);

                if(null == tmpGenre) continue;

                String[] genreSplit = tmpGenre.split(",");

                if(0 == genreSplit.length) continue;

                for(String genre : genreSplit) {
                    if(!artistMap.containsKey(genre)) {
                        HashMap<Integer, ArtistPOJO> artists = new HashMap<Integer, ArtistPOJO>(40, 0.75f);
                        GenrePOJO genrePOJO = new GenrePOJO();
                        genrePOJO.setArtists(artists);
                        genrePOJO.setName(genre);
                        artistMap.put(genre, genrePOJO);
                    }

                    HashMap<Integer, ArtistPOJO> artists = artistMap.get(genre).getArtists();

                    ArtistPOJO artistPOJO = new ArtistPOJO();
                    artistPOJO.setId(row.getInt("id"));
                    artistPOJO.setGenres(row.optString("genres", null));
                    artistPOJO.setPicture(row.optString("picture", null));
                    artistPOJO.setName(row.optString("name", null));
                    artistPOJO.setDescription(row.optString("description", null));

                    if(!artists.containsKey(artistPOJO.getId()))
                        artists.put(artistPOJO.getId(), artistPOJO);
                }
            }

            Log.i(getClass().toString(), "genres available  " + artistMap.size());

            for (Map.Entry<String, GenrePOJO> entry : artistMap.entrySet()) {
                String key = entry.getKey();
                GenrePOJO value = entry.getValue();
                Log.i(getClass().toString(), "genres [" + key + "]" + "with artist count ? " + value.getArtists().size());
            }

        } catch(JSONException e) {
            e.printStackTrace();
            throw new AppException(AppException.JSON_PARSE_ERROR);
        }

        return artistMap;
    }
}
