package com.playandroid.riccardo.play;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.playandroid.riccardo.play.data.PlayContract;
import com.squareup.picasso.Picasso;

/**
 * Created by Riccardo on 14/01/2017.
 */
public class BoardgamesAdapter extends RecyclerView.Adapter<BoardgamesAdapter.BoardgamesAdapterViewHolder> {

    private final BoardgamesAdapterOnClickHandler mClickHandler;
    private Context mContext;

    public interface BoardgamesAdapterOnClickHandler {
        void onClick(long id);
    }

    private Cursor mCursor;

    public BoardgamesAdapter(@NonNull Context context, BoardgamesAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    public class BoardgamesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mBoardgameImageView;
        public final TextView mTitleTextView;
        public final ImageView mHeartImageView;

        public BoardgamesAdapterViewHolder(View view) {
            super(view);
            mBoardgameImageView = (ImageView) view.findViewById(R.id.iv_bg_thumbnail);
            mTitleTextView = (TextView) view.findViewById(R.id.tv_bg_title);
            mHeartImageView = (ImageView) view.findViewById(R.id.iv_bg_favourite);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long id = mCursor.getLong(MainActivity.INDEX_BOARDGAME_PK);
            mClickHandler.onClick(id);
        }
    }

    @Override
    public BoardgamesAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutIdForListItem = R.layout.boardgames_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new BoardgamesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BoardgamesAdapterViewHolder boardgamesAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        String thumb_path = mCursor.getString(MainActivity.INDEX_BOARDGAME_THUMBNAIL);
        Picasso.with(mContext).load("http:"+thumb_path).resize(100,0)
                .into(boardgamesAdapterViewHolder.mBoardgameImageView);

        final String title = mCursor.getString(MainActivity.INDEX_BOARDGAME_TITLE);
        final String pk = mCursor.getString(MainActivity.INDEX_BOARDGAME_PK);
        boardgamesAdapterViewHolder.mTitleTextView.setText(title);

        final ContentValues valueToUpdate = new ContentValues();
        final String message;
        if(Integer.parseInt(mCursor.getString(MainActivity.INDEX_BOARDGAME_FAVOURITE)) == 1){
            boardgamesAdapterViewHolder.mHeartImageView.setImageResource(R.drawable.ic_favorite);
            valueToUpdate.put(mContext.getResources().getString(R.string.favourite), 0);
            message  = mContext.getResources().getString(R.string.removed_from_favourite);
        }
        else {
            boardgamesAdapterViewHolder.mHeartImageView.setImageResource(R.drawable.ic_favorite_border);
            valueToUpdate.put(mContext.getResources().getString(R.string.favourite), 1);
            message = mContext.getResources().getString(R.string.added_to_favourite);
        }

        boardgamesAdapterViewHolder.mHeartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriForBoardgame = PlayContract.BoardgamesEntry.buildBoardgameUriWithId(Long.parseLong(pk));
                ContentResolver boardgameContentResolver = mContext.getContentResolver();
                boardgameContentResolver.update(
                        uriForBoardgame,
                        valueToUpdate,
                        null,
                        null);
                Toast.makeText(v.getContext(), title + " " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
