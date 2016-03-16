package com.rgade.androidtask.app.core;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rgade.androidtask.app.models.Message;
import com.rgade.androidtask.app.models.MessageFull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataManager {
    private static DataManager mInstance;
    private NetworkManager mNetworkManager;
    private static final String URL_MESSAGES = "http://127.0.0.1:8088/api/message/";
    private List<Message> messages;
    private static Context mContext;

    private DataManager(Context ctx) {
        mContext = ctx;
        mNetworkManager = NetworkManager.getInstance(ctx);
        messages = new ArrayList<Message>();
    }

    public interface Callback<T> {
        void onCall(T response);
    }

    public static synchronized DataManager getInstance(Context ctx) {
        if (mInstance == null)
            mInstance = new DataManager(ctx);
        return mInstance;
    }

    public void fetchMessages(final Callback<List<Message>> callback) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL_MESSAGES, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                boolean success = false;
                try {
                    Type collectionType = new TypeToken<ArrayList<Message>>() {
                    }.getType();

                    Gson gson = new Gson();
                    Log.d(getClass().getSimpleName()+" - fetchMessages",response.toString());
                    ArrayList<Message> temp = gson.fromJson(response.toString(), collectionType);
                    messages.clear();
                    messages.addAll(temp);
                    success = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (success)
                    callback.onCall(messages);
                else
                    callback.onCall(null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(getClass().getSimpleName(), error.getMessage());
                error.printStackTrace();
                callback.onCall(null);
            }
        });
        mNetworkManager.addToRequestQueue(request);
    }

    public void fetchMessage(final Callback<MessageFull> callback, String id) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_MESSAGES + id, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MessageFull temp = null;
                try {
                    Gson gson = new Gson();
                    Log.d(getClass().getSimpleName()+" - fetchMessage",response.toString());
                    temp = gson.fromJson(response.toString(), MessageFull.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                callback.onCall(temp);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onCall(null);
            }
        });

        mNetworkManager.addToRequestQueue(request);
    }

    public void deleteMessage(final Callback<Boolean> callback, String id) {
        StringRequest request = new StringRequest(Request.Method.DELETE, URL_MESSAGES + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(getClass().getSimpleName()+" - deleteMessage","success");
                callback.onCall(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onCall(false);
                Log.d(getClass().getSimpleName()+" - deleteMessage","failure");
            }
        });
        mNetworkManager.addToRequestQueue(request);
    }

    public List<Message> getMessages() {
        return messages;
    }
}
