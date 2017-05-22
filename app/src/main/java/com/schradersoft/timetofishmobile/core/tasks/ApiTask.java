package com.schradersoft.timetofishmobile.core.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.JsonObject;
import com.schradersoft.timetofishmobile.core.Api;

import java.io.UnsupportedEncodingException;

/**
 * Created by Adam Schrader on 5/3/2017.
 */

abstract class ApiTask<T> extends AsyncTask<Void, Void, T> {

    protected RequestFuture<T> _future;
    protected Api _api;

    ApiTask(Context context) {
        _future = RequestFuture.newFuture();
        _api = Api.getInstance(context);
    }

    protected void onErrorResponse(VolleyError error) {
        //TODO: Handle error here or override in sub-task class
        try {
            String msg = error.getMessage();
            if (msg == null && error.networkResponse != null) {
                String statusText = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                msg = "[" + error.networkResponse.statusCode + "] " + statusText;
            }
            System.out.println(msg);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            // setTaskComplete();
        }
    }
}
