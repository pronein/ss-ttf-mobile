package com.schradersoft.timetofishmobile.core;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IJsonResponseHandler {
    void HandleResponse(JSONObject response);
    void HandleException(VolleyError error);
}
