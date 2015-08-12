package com.image.tech;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.image.tech.pojo.ArtistPOJO;
import com.image.tech.pojo.GenrePOJO;
import com.image.tech.utilities.ImageLoader;
import com.image.tech.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

/**
 * This class displays details of an Artist
 * It shows you list of related artists
 */
public class ArtistDetails extends Activity {

    private ArtistPOJO artist;
    private HashMap<String, GenrePOJO> artistMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_artist_details);

        TextView artistName = (TextView) findViewById(R.id.artistName);
        TextView genre = (TextView) findViewById(R.id.genre);
        TextView description = (TextView) findViewById(R.id.description);
        ImageView artistIcon = (ImageView) findViewById(R.id.artistIcon);

        LinearLayout nameLayout = (LinearLayout) findViewById(R.id.nameLayout);
        LinearLayout genreLayout = (LinearLayout) findViewById(R.id.genreLayout);
        LinearLayout descriptionLayout = (LinearLayout) findViewById(R.id.descriptionLayout);
        final LinearLayout relatedArtistLayout = (LinearLayout) findViewById(R.id.relatedArtistLayout);
        final LinearLayout relatedArtistContentLayout = (LinearLayout) findViewById(R.id.relatedArtistContentLayout);

        if(null != getIntent() && null != getIntent().getExtras()) {
            savedInstanceState = getIntent().getExtras();
            artist = (ArtistPOJO) savedInstanceState.getSerializable("artist");
        }

        if(null != artist) {
            setTitle(artist.getName());

            if(null != artist.getName()) {
                artistName.setText(artist.getName());
                nameLayout.setVisibility(View.VISIBLE);
            }
            if(null != artist.getGenres()) {
                genre.setText(artist.getGenres());
                genreLayout.setVisibility(View.VISIBLE);
            }

            if(null != artist.getDescription()) {
                description.setText(Html.fromHtml(artist.getDescription()));
                descriptionLayout.setVisibility(View.VISIBLE);
            }

            final ImageLoader imageLoader = new ImageLoader(this);
            imageLoader.displayImage(artist.getPicture(), artistIcon);

            relatedArtistContentLayout.removeAllViews();


               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       try {
                           Object decodedObject = Utils.decodeFromString(getSharedPreferences("artistPrefs", Context.MODE_PRIVATE).getString("artistMap", null));
                           artistMap = (HashMap<String, GenrePOJO>) decodedObject;
                           int counter = 0;
                           String[] genreArray = artist.getGenres().split(",");
                           for (Map.Entry<String, GenrePOJO> entry : artistMap.entrySet()) {
                               String key = entry.getKey();
                               GenrePOJO value = entry.getValue();

                               for (String genreValue : genreArray) {
                                   if (genreValue.equalsIgnoreCase(key)) {
                                       for (Map.Entry<Integer, ArtistPOJO> artistEntry : value.getArtists().entrySet()) {
                                           int id = artistEntry.getKey();
                                           ArtistPOJO relatedArtist = artistEntry.getValue();

                                           if (id != artist.getId()) {
                                               relatedArtistLayout.setVisibility(View.VISIBLE);

                                               View view = getLayoutInflater().inflate(R.layout.layout_artist_row, null);

                                               TextView relatedName = (TextView) view.findViewById(R.id.artistName);
                                               TextView relatedGenre = (TextView) view.findViewById(R.id.genre);
                                               ImageView relatedIcon = (ImageView) view.findViewById(R.id.artistIcon);
                                               ImageView rightArrow = (ImageView) view.findViewById(R.id.rightArrow);

                                               rightArrow.setVisibility(View.GONE);
                                               relatedName.setText(relatedArtist.getName());
                                               relatedGenre.setText(relatedArtist.getGenres());

                                               imageLoader.displayImage(relatedArtist.getPicture(), relatedIcon);

                                               view.setTag(relatedArtist);

                                               relatedArtistContentLayout.addView(view);
                                           }
                                           if (counter++ >= 5) break;
                                       }
                                   }
                                   if (counter >= 5) break;
                               }
                               if (counter >= 5) break;
                           }
                       } catch (Exception e) {
                           showToast(R.string.message_no_related_artist);
                       }
                   }
               });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home: {
                this.finish();
                break;
            }
        }
        return true;
    }

    public void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
