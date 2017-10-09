package com.pudding.tangentninety.di.component;

import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.di.module.AppModule;
import com.pudding.tangentninety.di.module.HttpModule;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.db.RealmHelper;
import com.pudding.tangentninety.module.http.RetrofitHelper;
import com.pudding.tangentninety.module.prefs.ImplPreferencesHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Error on 2017/6/22 0022.
 */

@Singleton
@Component(modules = {AppModule.class, HttpModule.class})
public interface AppComponent {

    App getContext();  // 提供App的Context

    DataManager getDataManager(); //数据中心

    RetrofitHelper retrofitHelper();  //提供http的帮助类

    RealmHelper realmHelper();    //提供数据库帮助类

    ImplPreferencesHelper preferencesHelper(); //提供sp帮助类
}
