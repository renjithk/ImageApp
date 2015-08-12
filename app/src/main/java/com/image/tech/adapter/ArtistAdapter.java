package com.image.tech.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.image.tech.R;
import com.image.tech.pojo.ArtistPOJO;
import com.image.tech.pojo.GenrePOJO;
import com.image.tech.utilities.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter class handles data rendering for ListView
 */
public class ArtistAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private final HashMap<String, GenrePOJO> artistMap;
    private final ImageLoader imageLoader;
    private List<ArtistPOJO> artistList;
    private List<ArtistPOJO> tempList;

    public ArtistAdapter(Context context, Object data) {
        this.mInflater = LayoutInflater.from(context);
        this.artistMap = (HashMap<String, GenrePOJO>) data;
        this.imageLoader = new ImageLoader(context.getApplicationContext());

        artistList = new ArrayList<>();
        for (Map.Entry<String, GenrePOJO> entry : artistMap.entrySet()) {
            String key = entry.getKey();
            GenrePOJO value = entry.getValue();
            Log.i(getClass().toString(), "genres [" + key + "]" + "with artist count ? " + value.getArtists().size());

            artistList.addAll(value.getArtists().values());
        }

        tempList = new ArrayList<ArtistPOJO>();
        tempList.addAll(artistList);
    }

    @Override
    public int getCount() {
        try {
            return artistList.size();
        } catch(Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        try {
            return artistList.get(position);
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        try {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.layout_artist_row, null);
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.artistName);
                holder.genre = (TextView) convertView.findViewById(R.id.genre);
                holder.icon = (ImageView) convertView.findViewById(R.id.artistIcon);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(artistList.get(position).getName());
            holder.genre.setText(artistList.get(position).getGenres());

            imageLoader.displayImage(artistList.get(position).getPicture(), holder.icon);
        } catch(Exception e) {
        }
        return convertView;
    }

    public void search(String searchText) {
        searchText = searchText.toLowerCase();
        artistList.clear();
        if(0 == searchText.length()) {
            artistList.addAll(tempList);
        } else {
            for(ArtistPOJO artist : tempList) {
                if(artist.getName().toLowerCase().contains(searchText)) artistList.add(artist);
            }
        }

        notifyDataSetChanged();
    }

    public HashMap<String, GenrePOJO> getArtistMap() {
        return artistMap;
    }

    static class ViewHolder {
        TextView name;
        TextView genre;
        ImageView icon;
    }
}
