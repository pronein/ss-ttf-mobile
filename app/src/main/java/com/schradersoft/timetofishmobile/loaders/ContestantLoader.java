package com.schradersoft.timetofishmobile.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.AsyncTaskLoader;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.schradersoft.timetofishmobile.core.Api;
import com.schradersoft.timetofishmobile.domain.models.Contestant;
import com.schradersoft.timetofishmobile.domain.models.Photo;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by Adam Schrader on 5/8/2017.
 */

public class ContestantLoader extends AsyncTaskLoader<List<Contestant>> {

    public ContestantLoader(Context context) {
        super(context);
    }

    @Override
    public List<Contestant> loadInBackground() {
        HttpURLConnection connection = null;
        List<Contestant> contestants = new ArrayList<>();

        try {
            URL url = new URL("http://192.168.1.108:3000/api/contest/59148168e8719547ea5fee3e/scores");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + Api.getInstance(getContext()).getToken());
            InputStream in = new BufferedInputStream(connection.getInputStream());
            JsonParser jsonParser = new JsonParser();
            JsonElement contestantsElement = jsonParser.parse(new InputStreamReader(in, "UTF-8"));
            Gson gson = new Gson();
            for (JsonElement contestantElement: contestantsElement.getAsJsonObject().get("contestants").getAsJsonArray()) {
                Contestant contestant = (Contestant) gson.fromJson(contestantElement, Contestant.class);

                System.out.println("Response: " + contestantElement.toString());
                contestants.add(contestant);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (connection != null)
                connection.disconnect();
        }

        return contestants;
    }
}
