package com.santeh.rjhonsl.fishtaordering.Util;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;

public class VolleyAPI extends Application {

    private RequestQueue mRequestQueue;
    private static VolleyAPI mInstance;
    Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Stetho.initializeWithDefaults(this);
    }

    public static synchronized VolleyAPI getInstance() {
        return mInstance;
    }

    public RequestQueue getReqQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(ctx);
        }
        return mRequestQueue;
    }

    public <T> void addToReqQueue(Request<T> req, String tag) {
        getReqQueue().add(req);
    }

    public <T> void addToReqQueue(Request<T> req, Context context) {
        ctx = context;
        getReqQueue().add(req);
    }

    public void cancelPendingReq(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    public void cancelPendingReq(Context tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public void stopRequest(){
        if (mRequestQueue != null) {
            mRequestQueue.stop();
        }
    }

}
