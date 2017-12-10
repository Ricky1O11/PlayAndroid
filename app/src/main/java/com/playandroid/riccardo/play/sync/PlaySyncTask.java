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
package com.playandroid.riccardo.play.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.playandroid.riccardo.play.MainActivity;
import com.playandroid.riccardo.play.R;
import com.playandroid.riccardo.play.data.PlayContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class PlaySyncTask {

    private static final String URI = "http://playapi.pythonanywhere.com/server/";
    private static final String API_BOARDGAMES ="boardgames/";

    synchronized public static void syncBoardgames(Context context) {

        final String API_PK = context.getResources().getString(R.string.pk);
        final String API_TITLE = context.getResources().getString(R.string.title);
        final String API_DESCRIPTION = context.getResources().getString(R.string.description);
        final String API_IMG = context.getResources().getString(R.string.img);
        final String API_THUMBNAIL = context.getResources().getString(R.string.thumbnail);
        final String API_AVERAGE = context.getResources().getString(R.string.average);
        final String API_MINAGE = context.getResources().getString(R.string.minage);
        final String API_PLAYINGTIME = context.getResources().getString(R.string.playingtime);
        final String API_MINPLAYERS = context.getResources().getString(R.string.minplayers);
        final String API_MAXPLAYERS = context.getResources().getString(R.string.maxplayers);
        final String API_YEARPUBLISHED = context.getResources().getString(R.string.yearpublished);
        final String API_MAXPLAYTIME = context.getResources().getString(R.string.maxplaytime);
        final String API_MINPLAYTIME = context.getResources().getString(R.string.minplaytime);
        final String API_USERSRATED = context.getResources().getString(R.string.usersrated);
        final String FAVOURITE = context.getResources().getString(R.string.favourite);
       // final String API_FAVOURITE = context.getResources().getString(R.string.favourite);

        String orderingField = MainActivity.getOrderingField(context);

        if(!orderingField.equals("favourite")) {
            Uri builtUri = Uri.parse(URI).buildUpon()
                    .appendPath(API_BOARDGAMES)
                    .build();

            URL boardgamesRequestUrl = null;
            try {
                boardgamesRequestUrl = new URL(builtUri.toString());
            } catch (MalformedURLException e) {

                e.printStackTrace();
            }

            try {
                String jsonResponse = PlaySyncUtils.getResponseFromHttpUrl(boardgamesRequestUrl);
                JSONArray boardgameArray = new JSONArray(jsonResponse);

                ContentValues[] parsedBoardgamesData = new ContentValues[boardgameArray.length()];

                for (int i = 0; i < boardgameArray.length(); i++) {
                    JSONObject singleBoardgameJsonData = boardgameArray.getJSONObject(i);
                    ContentValues boardgameData = new ContentValues();
                    boardgameData.put(API_PK, singleBoardgameJsonData.getString(API_PK));
                    boardgameData.put(API_TITLE, singleBoardgameJsonData.getString(API_TITLE));
                    boardgameData.put(API_DESCRIPTION, singleBoardgameJsonData.getString(API_DESCRIPTION));
                    boardgameData.put(API_IMG, singleBoardgameJsonData.getString(API_IMG));
                    boardgameData.put(API_THUMBNAIL, singleBoardgameJsonData.getString(API_THUMBNAIL));
                    boardgameData.put(API_AVERAGE, singleBoardgameJsonData.getString(API_AVERAGE));
                    boardgameData.put(API_THUMBNAIL, singleBoardgameJsonData.getString(API_THUMBNAIL));
                    boardgameData.put(API_MINAGE, singleBoardgameJsonData.getString(API_MINAGE));
                    boardgameData.put(API_PLAYINGTIME, singleBoardgameJsonData.getString(API_PLAYINGTIME));
                    boardgameData.put(API_MINPLAYERS, singleBoardgameJsonData.getString(API_MINPLAYERS));
                    boardgameData.put(API_MAXPLAYERS, singleBoardgameJsonData.getString(API_MAXPLAYERS));
                    boardgameData.put(API_YEARPUBLISHED, singleBoardgameJsonData.getString(API_YEARPUBLISHED));
                    boardgameData.put(API_MAXPLAYTIME, singleBoardgameJsonData.getString(API_MAXPLAYTIME));
                    boardgameData.put(API_MINPLAYTIME, singleBoardgameJsonData.getString(API_MINPLAYTIME));
                    boardgameData.put(API_USERSRATED, singleBoardgameJsonData.getString(API_USERSRATED));
                    boardgameData.put(FAVOURITE, 0);
                    parsedBoardgamesData[i] = boardgameData;
                }
                if (parsedBoardgamesData != null && parsedBoardgamesData.length != 0) {
                    ContentResolver boardgameContentResolver = context.getContentResolver();
                    boardgameContentResolver.bulkInsert(
                            PlayContract.BoardgamesEntry.BOARDGAMES_URI,
                            parsedBoardgamesData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}