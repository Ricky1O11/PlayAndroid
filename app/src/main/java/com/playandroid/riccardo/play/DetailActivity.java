package com.playandroid.riccardo.play;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.playandroid.riccardo.play.data.PlayContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Riccardo on 28/02/2017.
 */
public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    public static final String[] DETAIL_BOARDGAMES_PROJECTION = {
            PlayContract.BoardgamesEntry.COLUMN_PK,
            PlayContract.BoardgamesEntry.COLUMN_IMG,
            PlayContract.BoardgamesEntry.COLUMN_TITLE,
            PlayContract.BoardgamesEntry.COLUMN_DESCRIPTION,
            PlayContract.BoardgamesEntry.COLUMN_FAVOURITE,
            PlayContract.BoardgamesEntry.COLUMN_AVERAGE,
    };

    public static final int INDEX_BOARDGAME_PK = 0;
    public static final int INDEX_BOARDGAME_IMG= 1;
    public static final int INDEX_BOARDGAME_TITLE= 2;
    public static final int INDEX_BOARDGAME_DESCRIPTION= 3;
    public static final int INDEX_BOARDGAME_FAVOURITE= 4;
    public static final int INDEX_BOARDGAME_AVERAGE= 5;

    private static final int ID_DETAIL_LOADER = 1;
    private static final String SAVED_ID = "saved_id";

    /* The URI that is used to access the chosen boardgame's details */
    private long boardgameId;
    private Uri mUri;
    private ScrollView mDetailsLayout;
    private TextView mBoardgameTitle, mBoardgameOverview, mBoardgameRating;
    private ImageView mBoardgameImg, mFavourite;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mDetailsLayout = (ScrollView) findViewById(R.id.sv_details);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mBoardgameTitle = (TextView) findViewById(R.id.tv_boardgame_title);
        mBoardgameOverview = (TextView) findViewById(R.id.tv_boardgame_overview);
        mBoardgameImg = (ImageView) findViewById(R.id.iv_boardgame_img);
        mBoardgameRating = (TextView) findViewById(R.id.tv_boardgame_rating);
        mFavourite = (ImageView) findViewById(R.id.iv_favourite);
        boardgameId = getIntent().getLongExtra("id", 0);
        showLoading();

        if (boardgameId > 0) {
            mUri = PlayContract.BoardgamesEntry.buildBoardgameUriWithId(boardgameId);
        }
        if (mUri != null)
            getSupportLoaderManager().initLoader(ID_DETAIL_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {

            case ID_DETAIL_LOADER:

                return new CursorLoader(this,
                        mUri,
                        DETAIL_BOARDGAMES_PROJECTION,
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor data) {
        boolean cursorHasValidData = false;

        if (data != null && data.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        mBoardgameTitle.setText(data.getString(INDEX_BOARDGAME_TITLE));

        mBoardgameOverview.setText(data.getString(INDEX_BOARDGAME_DESCRIPTION));
        Picasso.with(this).load("http:"+data.getString(INDEX_BOARDGAME_IMG))
                .resize(0,200)
                .into(mBoardgameImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        showDetails();
                    }

                    @Override
                    public void onError() {
                        showDetails();
                    }
                });

        mBoardgameRating.setText((Math.round(data.getLong(INDEX_BOARDGAME_AVERAGE)*100)/100)+"/10");

        final ContentValues valueToUpdate = new ContentValues();
        final String message;
        if(Integer.parseInt(data.getString(INDEX_BOARDGAME_FAVOURITE)) == 0){
            mFavourite.setImageResource(R.drawable.ic_favorite_border);
            valueToUpdate.put(getResources().getString(R.string.favourite), 1);
            message = getResources().getString(R.string.added_to_favourite);
        }
        else{
            mFavourite.setImageResource(R.drawable.ic_favorite);
            valueToUpdate.put(getResources().getString(R.string.favourite), 0);
            message  = getResources().getString(R.string.removed_from_favourite);
        }
        mFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriForBoardgame = PlayContract.BoardgamesEntry.buildBoardgameUriWithId(Long.parseLong(data.getString(INDEX_BOARDGAME_PK)));
                ContentResolver movieContentResolver = getContentResolver();
                movieContentResolver.update(
                        uriForBoardgame,
                        valueToUpdate,
                        null,
                        null);

                Toast.makeText(v.getContext(),
                        data.getString(INDEX_BOARDGAME_TITLE) +
                                " " +
                                message
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVED_ID, boardgameId);
    }

    private void showDetails() {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        mDetailsLayout.setVisibility(View.VISIBLE);
    }

    private void showLoading() {
        mDetailsLayout.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}