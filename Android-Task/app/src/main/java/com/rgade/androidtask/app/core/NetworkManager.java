package com.rgade.androidtask.app.core;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class NetworkManager {
    private static NetworkManager mInstance;
    private static RequestQueue mRequestQueue;
    private static Context mContext;

    private NetworkManager(Context ctx) {
        if (ctx == null)
            return;
        mContext = ctx;
        mRequestQueue = Volley.newRequestQueue(ctx);
    }

    public static synchronized NetworkManager getInstance(Context ctx) {
        if (mInstance == null)
            mInstance = new NetworkManager(ctx);
        return mInstance;
    }

    public <T> boolean addToRequestQueue(Request<T> request) {
        if (mRequestQueue == null)
            return false;
        mRequestQueue.add(request);
        return true;
    }

}
