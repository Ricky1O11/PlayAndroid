/**
 * Created by Riccardo on 27/02/2017.
 */
package com.playandroid.riccardo.play.data;

import android.net.Uri;
import android.provider.BaseColumns;


public class PlayContract {

    public static final String CONTENT_AUTHORITY = "com.playandroid.riccardo.play";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BOARDGAMES = "boardgames";

    public static final class BoardgamesEntry implements BaseColumns {

        public static final Uri BOARDGAMES_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_BOARDGAMES)
                .build();
        //set the names of the db attributes
        public static final String TABLE_NAME = "Boardgames";
        public static final String COLUMN_PK = "pk";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_IMG = "img";
        public static final String COLUMN_THUMBNAIL = "thumbnail";
        public static final String COLUMN_MINAGE = "minage";
        public static final String COLUMN_PLAYINGTIME = "playingtime";
        public static final String COLUMN_MINPLAYERS = "minplayers";
        public static final String COLUMN_MAXPLAYERS = "maxplayers";
        public static final String COLUMN_YEARPUBLISHED = "yearpublished";
        public static final String COLUMN_MAXPLAYTIME = "maxplaytime";
        public static final String COLUMN_MINPLAYTIME = "minplaytime";
        public static final String COLUMN_AVERAGE = "average";
        public static final String COLUMN_USERSRATED = "usersrated";
        public static final String COLUMN_FAVOURITE = "favourite";

        public static Uri buildBoardgameUriWithId( long boardgameId){
            return BOARDGAMES_URI.buildUpon()
                    .appendPath(Long.toString(boardgameId))
                    .build();
        }

        /*
        public static String getSqlSelect(String status, Context context) {
            if (status.equals(context.getResources().getString(R.string.favourite))){
                return MoviesEntry.COLUMN_FAVOURITE + " = '1'";
            }
            else{
                return MoviesEntry.COLUMN_STATUS + " = '" + status + "'";
            }
        }
        */
    }

    public static final class ProfilesEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Profiles";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_BIRTH_DATE = "birth_date";
        public static final String COLUMN_IMG = "img";
    }

    public static final class MatchesEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Matches";
        public static final String COLUMN_BOARDGAME = "boardgame";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_STATUS = "status";
    }

    public static final class FriendsEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Friends";
        public static final String COLUMN_USER_1 = "user1";
        public static final String COLUMN_USER_2 = "user2";
    }

    public static final class FavouritesEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Favourites";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_BOARDGAME = "boardgame";
    }

    public static final class PlaysEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Plays";
        public static final String COLUMN_USER = "user";
        public static final String COLUMN_MATCH = "match";
        public static final String COLUMN_POINTS = "points";
    }

    public static final class TemplatesEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Templates";
        public static final String COLUMN_BOARDGAME = "boardgame";
        public static final String COLUMN_VOTE = "vote";
    }

    public static final class DictionaryEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "Dictionary";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_DESCRIPTION = "description";
    }

    public static final class ScoringFieldsEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "ScoringFields";
        public static final String COLUMN_TEMPLATE = "template";
        public static final String COLUMN_WORD = "word";
        public static final String COLUMN_BONUS = "bonus";
    }

    public static final class DetailedPointsEntry implements BaseColumns {
        //set the names of the db attributes
        public static final String TABLE_NAME = "DetailedPoints";
        public static final String COLUMN_SCORING_FIELD = "scoringField";
        public static final String COLUMN_PLAY = "play";
        public static final String COLUMN_DETAILED_POINTS = "detailed_points";
        public static final String COLUMN_NOTES = "notes";
    }
}
