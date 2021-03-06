/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.playandroid.riccardo.play.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.playandroid.riccardo.play.data.PlayContract.BoardgamesEntry;

/**
 * Manages a local database for boardgames data.
 */
public class PlayDbHelper extends SQLiteOpenHelper {

    /*
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     */
    public static final String DATABASE_NAME = "play.db";

    /*
     * If you change the database schema, you must increment the database version or the onUpgrade
     * method will not be called.
     *
     * The reason DATABASE_VERSION starts at 3 is because Sunshine has been used in conjunction
     * with the Android course for a while now. Believe it or not, older versions of Sunshine
     * still exist out in the wild. If we started this DATABASE_VERSION off at 1, upgrading older
     * versions of Sunshine could cause everything to break. Although that is certainly a rare
     * use-case, we wanted to watch out for it and warn you what could happen if you mistakenly
     * version your databases.
     */
    private static final int DATABASE_VERSION = 7;

    public PlayDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /*
         * This String will contain a simple SQL statement that will create a table that will
         * cache our boardgames data.
         */
        final String SQL_CREATE_BOARDGAMES_TABLE =

                "CREATE TABLE " + PlayContract.BoardgamesEntry.TABLE_NAME + " (" +

                        BoardgamesEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        BoardgamesEntry.COLUMN_PK       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_TITLE       + " VARCHAR NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_DESCRIPTION       + " VARCHAR NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_IMG       + " VARCHAR NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_THUMBNAIL       + " VARCHAR NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_MINAGE       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_PLAYINGTIME       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_MINPLAYERS       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_MAXPLAYERS       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_YEARPUBLISHED       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_MAXPLAYTIME       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_MINPLAYTIME       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_AVERAGE       + " REAL NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_USERSRATED       + " INTEGER NOT NULL, "                 +

                        BoardgamesEntry.COLUMN_FAVOURITE       + " INTEGER NOT NULL, "                 +



                " UNIQUE (" + BoardgamesEntry.COLUMN_PK + ") ON CONFLICT IGNORE);";

        sqLiteDatabase.execSQL(SQL_CREATE_BOARDGAMES_TABLE);
    }

    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BoardgamesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}