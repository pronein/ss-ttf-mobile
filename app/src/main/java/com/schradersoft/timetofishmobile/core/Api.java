package com.schradersoft.timetofishmobile.core;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Api {
    private static Api _instance;
    private Context _context;
    private RequestQueue _requestQueue;
    private String _jwt;
    private String _rootUri = "http://192.168.10.101:3000/api/";

    private Api(Context context) {
        _context = context;
        _requestQueue = GetRequestQueue();
        _jwt = null;
    }

    public synchronized static Api getInstance(Context context){
        if (_instance == null) {
            _instance = new Api(context);
        }

        return _instance;
    }

    public RequestQueue GetRequestQueue() {
        if (_requestQueue == null) {
            _requestQueue = Volley.newRequestQueue(_context.getApplicationContext());
        }

        return _requestQueue;
    }

    public void TryLogin(String username, String password, IJsonResponseHandler handler) {
        Get("user/me/token", handler, BuildCredentialHeader(username, password));
    }

    public void Get(String uri, IJsonResponseHandler handler) {
        Get(uri, handler, new HashMap<String, String>());
    }

    public void Get(String uri, IJsonResponseHandler handler, Map<String, String> headers) {
        JsonObjectRequest req = BuildRequest(_rootUri + uri, Request.Method.GET, null, handler, headers);
        _requestQueue.add(req);
    }

    private Map<String, String> BuildCredentialHeader(String username, String password) {
        Map<String, String> headers = new HashMap<>();

        String credentials = username + ":" + password;
        String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

        headers.put("Authorization", auth);

        return headers;
    }

    private JsonObjectRequest BuildRequest(String uri, int method, JSONObject payload,
                                           final IJsonResponseHandler handler,
                                           final Map<String, String> headers) {

        JsonObjectRequest request = new JsonObjectRequest(method, uri, payload,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handler.HandleResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            String msg = error.getMessage();
                            if (msg == null && error.networkResponse != null) {
                                String statusText = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                                msg = "[" + error.networkResponse.statusCode + "] " + statusText;
                            }
                            System.out.println(msg);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        handler.HandleException(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> customHeaders = new HashMap<>();
                customHeaders.putAll(headers);

                if (!customHeaders.containsKey("Content-Type"))
                    customHeaders.put("Content-Type", "application/json");

                if (!customHeaders.containsKey("Authorization") && _jwt != null)
                    customHeaders.put("Authorization", "Bearer " + _jwt);

                return customHeaders;
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                String token = response.headers.get("x-ss-auth");
                if (token.length() > 0)
                    _jwt = token;

                try{
                    String data = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

                    JSONObject obj = response.data.length > 0
                            ? new JSONObject(data)
                            : new JSONObject();

                    return Response.success(obj, HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return Response.error(new VolleyError(response));
            }
        };

        return request;
    }
}
