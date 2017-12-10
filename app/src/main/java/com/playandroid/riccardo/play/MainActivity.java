package com.playandroid.riccardo.play;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.playandroid.riccardo.play.data.PlayContract;
import com.playandroid.riccardo.play.sync.PlaySyncUtils;


public class MainActivity
        extends AppCompatActivity
        implements  BoardgamesAdapter.BoardgamesAdapterOnClickHandler,
                    LoaderCallbacks<Cursor>,
                    SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String[] MAIN_BOARDGAMES_PROJECTION = {
            PlayContract.BoardgamesEntry.COLUMN_PK,
            PlayContract.BoardgamesEntry.COLUMN_THUMBNAIL,
            PlayContract.BoardgamesEntry.COLUMN_TITLE,
            PlayContract.BoardgamesEntry.COLUMN_FAVOURITE,
    };

    public static final int INDEX_BOARDGAME_PK = 0;
    public static final int INDEX_BOARDGAME_THUMBNAIL= 1;
    public static final int INDEX_BOARDGAME_TITLE= 2;
    public static final int INDEX_BOARDGAME_FAVOURITE= 3;

    private LinearLayoutManager listLayout;
    private RecyclerView mRecyclerView;
    private BoardgamesAdapter mBoardgamesAdapter;
    private int mPosition = RecyclerView.NO_POSITION;

    private LinearLayout mEmptyMessage;
    private TextView tvEmptyMessage1;
    private TextView tvEmptyMessage2;

    private ProgressBar mLoadingIndicator;

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;
    private static final int BOARDGAME_LOADER_ID = 0;

    private String orderingField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_boardgames);

        mEmptyMessage = (LinearLayout) findViewById(R.id.ll_empty_message);
        tvEmptyMessage1 = (TextView) findViewById(R.id.tv_empty_message1);
        tvEmptyMessage2 = (TextView) findViewById(R.id.tv_empty_message2);

        listLayout = new LinearLayoutManager(this);

        orderingField = getOrderingField(this);

        mRecyclerView.setLayoutManager(listLayout);

        mRecyclerView.setHasFixedSize(true);

        mBoardgamesAdapter = new BoardgamesAdapter(this, this);

        mRecyclerView.setAdapter(mBoardgamesAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        showLoading();

        getSupportLoaderManager().initLoader(BOARDGAME_LOADER_ID, null, this);

        PlaySyncUtils.startImmediateSync(this);

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * This method is overridden by our MainActivity class in order to handle RecyclerView item
     * clicks.
     */
    @Override
    public void onClick(long id) {
        Intent movieDetailIntent = new Intent(MainActivity.this, DetailActivity.class);
        movieDetailIntent.putExtra("id", id);
        startActivity(movieDetailIntent);
    }

    private void showMoviesList() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if(mBoardgamesAdapter.getItemCount() > 0){
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyMessage.setVisibility(View.INVISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.INVISIBLE);
            mEmptyMessage.setVisibility(View.VISIBLE);
            if(orderingField.equals(getResources().getString(R.string.pref_field_favourite))){
                tvEmptyMessage1.setVisibility(View.INVISIBLE);
                tvEmptyMessage2.setVisibility(View.VISIBLE);
            }
            else{
                tvEmptyMessage1.setVisibility(View.VISIBLE);
                tvEmptyMessage2.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void showLoading() {
        mEmptyMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {

        switch (loaderId) {

            case BOARDGAME_LOADER_ID:
                Uri movieQueryUri = PlayContract.BoardgamesEntry.BOARDGAMES_URI;
                //String selection = PlayContract.BoardgamesEntry.getSqlSelect(orderingField, this);

                return new CursorLoader(this,
                        movieQueryUri,
                        MAIN_BOARDGAMES_PROJECTION,
                        null,
                        null,
                        null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mBoardgamesAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        showMoviesList();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mBoardgamesAdapter.swapCursor(null);
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
            getSupportLoaderManager().restartLoader(BOARDGAME_LOADER_ID, null, this);
            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

		/* Unregister MainActivity as an OnPreferenceChangedListener to avoid any memory leaks. */
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        PREFERENCES_HAVE_BEEN_UPDATED = true;
        orderingField = getOrderingField(this);
    }

    public static String getOrderingField(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String key = context.getString(R.string.pref_field_key);
        String defaultOrderingField = context.getString(R.string.pref_field_alphabetical);
        String preferredUnits = prefs.getString(key, defaultOrderingField);
        return preferredUnits;
    }
}