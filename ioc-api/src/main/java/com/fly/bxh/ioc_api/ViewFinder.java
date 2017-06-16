package com.fly.bxh.ioc_api;

import android.app.Activity;
import android.util.Log;
import android.view.View;

/**
 * Created by mahao on 17-3-9.
 */

public class ViewFinder {

    private static final ActivityProvider PROVIDER_ACTIVITY = new ActivityProvider();
    private static final ViewProvider PROVIDER_VIEW = new ViewProvider();

    /**
     * 从activity 注入
     * @param activity
     */
    public static void inject(Activity activity) {
        inject(activity, activity, PROVIDER_ACTIVITY);
    }
    /**
     * 从View 中查找
     * @param host
     * @param view
     */
    public static void inject(Object host, View view) {
        // for fragment
        inject(host, view, PROVIDER_VIEW);
    }
    // 注入的最终调用
    private static void inject(Object host, Object source, Provider provider) {
        Class<?> clazz = host.getClass();
        String proxyClassFullName = clazz.getName()+"$$Finder";// 根据传入的对象获取辅助类的全路径名
        Class<?> proxyClazz = null;
        try {
            proxyClazz = Class.forName(proxyClassFullName);// 获取辅助类的class
            Log.i("inject","proxyClazz="+proxyClazz);
            Finder viewInjector = (Finder) proxyClazz.newInstance(); // 生成辅助类的实例
            Log.i("inject","viewInjector="+viewInjector);
            Log.i("inject","provider="+provider);
            Log.i("inject","source="+source);
            Log.i("inject","host="+host);
            viewInjector.inject(host, source, provider); // 调用辅助类的inject()方法
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
