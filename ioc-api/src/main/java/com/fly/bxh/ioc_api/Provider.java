package com.fly.bxh.ioc_api;

import android.view.View;

/**
 * Created by mahao on 17-3-9.
 */
public interface Provider {

    View findView(Object source, int id);

}
