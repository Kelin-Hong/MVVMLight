package com.kelin.mvvmlight.zhihu;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by kelin on 16-4-12.
 */
public class ZhiHuApp extends Application {
    public static String sPackageName;

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        initPackageName();
    }

    private void initPackageName() {
        PackageInfo info;
        try {
            info = getApplicationContext().getPackageManager().getPackageInfo(this.getPackageName(), 0);
            sPackageName = info.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
