package com.fly.bxh.ioc_api;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * Created by mahao on 17-3-9.
 */

public class ActivityProvider implements Provider {

    @Override
    public View findView(Object source, int id) {
        Log.i("ActivityProvider","source="+source+"  id="+id);
        return ((Activity) source).findViewById(id);
    }
}
