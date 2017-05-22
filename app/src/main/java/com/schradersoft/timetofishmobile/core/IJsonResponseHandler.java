package com.schradersoft.timetofishmobile.core;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IJsonResponseHandler<T> {
    void HandleResponse(T response);
    void HandleException(VolleyError error);
}
