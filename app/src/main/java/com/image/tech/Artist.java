package com.image.tech;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.image.tech.adapter.ArtistAdapter;
import com.image.tech.exception.AppException;
import com.image.tech.exception.ExceptionHandler;
import com.image.tech.network.NetworkHandler;
import com.image.tech.parser.JSONParser;
import com.image.tech.pojo.ArtistPOJO;
import com.image.tech.signatures.UIHandler;
import com.image.tech.utilities.Utils;

import java.io.IOException;

/**
 * Main Activity class that list all artists
 * It helps you to search an artist by name
 * The list can be refreshed either by pressing the menu or swipe the list down
 */
public class Artist extends ListActivity implements OnRefreshListener, UIHandler, OnItemClickListener, TextWatcher {

    private SwipeRefreshLayout artistSwipeLayout;
    private ArtistAdapter adapter;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_artist);

        artistSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.artistSwipeLayout);
        searchText = (EditText) findViewById(R.id.searchText);

        artistSwipeLayout.setOnRefreshListener(this);
        getListView().setOnItemClickListener(this);
        searchText.addTextChangedListener(this);
    }

    @Override
    public void onDestroy() {
        setListAdapter(null);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(null == adapter) fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_artist, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.refresh: {
                fetchData();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        fetchData();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        try {
            ArtistPOJO pojo = (ArtistPOJO) adapterView.getItemAtPosition(position);

            Intent intent = new Intent(this, ArtistDetails.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("artist", pojo);
            intent.putExtras(bundle);
            startActivity(intent);
        } catch(Exception e) {
            showToast(R.string.message_no_artist_found);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable text) {
        if(null != adapter) {
            adapter.search(text.toString());
        }
    }

    @Override
    public void onUIRender(Object data) {
        artistSwipeLayout.setRefreshing(false);

        SharedPreferences preferences = getSharedPreferences("artistPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        try {
            editor.putString("artistMap", Utils.encodeToString(data));
            editor.commit();
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            adapter = new ArtistAdapter(this, data);
            setListAdapter(adapter);
        } catch(Exception e) {
            showToast(R.string.message_data_render_error);
        }
    }

    @Override
    public void showAlert(AlertType alertType, int message) {
        artistSwipeLayout.setRefreshing(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        switch(alertType) {
            case Info:{
                builder.setTitle(getString(R.string.title_dialog_type_info));
                break;
            }
            case Error: {
                builder.setTitle(getString(R.string.title_dialog_type_error));
                break;
            }
        }

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public void showToast(int message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void fetchData() {
        DataFetcher dataFetcher = new DataFetcher(this);
        dataFetcher.execute("fetch");
    }

    /**
     * This class handles network connection and data parsing in the background
     */
    class DataFetcher extends AsyncTask<String, String, ExceptionHandler<Object>> {

        private UIHandler uiHandler;

        DataFetcher(UIHandler uiHandler) {
            this.uiHandler = uiHandler;
        }

        @Override
        protected ExceptionHandler<Object> doInBackground(String... params) {
            NetworkHandler networkHandler = new NetworkHandler();
            String jsonString = null;
            try {
                jsonString = networkHandler.readFromServer();
            } catch(AppException e) {
                e.printStackTrace();
                return new ExceptionHandler<Object>(e);
            }

            JSONParser parser = new JSONParser();

            try {
                Object data = parser.parseArtist(jsonString);
                return new ExceptionHandler<Object>(data);
            } catch (AppException e) {
                e.printStackTrace();
                return new ExceptionHandler<Object>(e);
            }

        }

        @Override
        public void onPreExecute() {
            final ConnectivityManager connectivityManager =  (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isConnected()) {
                //notify user you are not online
                uiHandler.showAlert(AlertType.Info, R.string.message_noNetwork);
            }
        }

        @Override
        public void onPostExecute(final ExceptionHandler<Object> result){
            if(null != result.getError()) {
                int errorType = result.getErrorType();
                Log.i(getClass().toString(), "Generated error type > " + errorType);
                uiHandler.showAlert(AlertType.Error, errorType);
            } else {
                uiHandler.onUIRender(result.getResult());
            }
        }
    }
}
