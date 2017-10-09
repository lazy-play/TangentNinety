package com.pudding.tangentninety.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatDelegate;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.pudding.tangentninety.component.InitializeService;
import com.pudding.tangentninety.di.component.AppComponent;
import com.pudding.tangentninety.di.component.DaggerAppComponent;
import com.pudding.tangentninety.di.module.AppModule;
import com.pudding.tangentninety.di.module.HttpModule;

import java.util.HashSet;
import java.util.Set;

import io.realm.Realm;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class App extends Application{
    private static App instance;
    public static AppComponent appComponent;
    private Set<Activity> allActivities;

    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    public static synchronized App getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //初始化屏幕宽高
        getScreenSize();

        //初始化数据库
        Realm.init(getApplicationContext());

        //在子线程中完成其他初始化
        InitializeService.start(this);
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    public void getScreenSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;

        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
        Resources resources=getResources();
        int resIdShow = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        boolean hasNavigationBar = false;
        if(resIdShow > 0){
            hasNavigationBar = resources.getBoolean(resIdShow);//是否显示底部navigationBar
        }
        if(hasNavigationBar) {
            int resIdNavigationBar = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            int navigationbarHeight = 0;
            if (resIdNavigationBar > 0) {
                navigationbarHeight = resources.getDimensionPixelSize(resIdNavigationBar);//navigationBar高度
                SCREEN_HEIGHT=SCREEN_HEIGHT+navigationbarHeight;
            }
        }
    }

    public static AppComponent getAppComponent(){
        if (appComponent == null) {
            appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(instance))
                    .httpModule(new HttpModule())
                    .build();
        }
        return appComponent;
    }

    private static Handler toastHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(getInstance().getApplicationContext(), (String)msg.obj, Toast.LENGTH_SHORT).show();
        }
    };
    public static void toastMessage(String message){
        Message m=new Message();
        m.obj=message;
        toastHandler.dispatchMessage(m);
    }
}
