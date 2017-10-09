package com.pudding.tangentninety.di.module;

import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.db.DBHelper;
import com.pudding.tangentninety.module.db.RealmHelper;
import com.pudding.tangentninety.module.http.HttpHelper;
import com.pudding.tangentninety.module.http.RetrofitHelper;
import com.pudding.tangentninety.module.prefs.ImplPreferencesHelper;
import com.pudding.tangentninety.module.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Error on 2017/6/22 0022.
 */

@Module
public class AppModule {
    private final App application;

    public AppModule(App application) {
        this.application = application;
    }

    @Provides
    @Singleton
    App provideApplicationContext() {
        return application;
    }

    @Provides
    @Singleton
    HttpHelper provideHttpHelper(RetrofitHelper retrofitHelper) {
        return retrofitHelper;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(RealmHelper realmHelper) {
        return realmHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(ImplPreferencesHelper implPreferencesHelper) {
        return implPreferencesHelper;
    }

    @Provides
    @Singleton
    DataManager provideDataManager(HttpHelper httpHelper, DBHelper DBHelper, PreferencesHelper preferencesHelper) {
        return new DataManager(httpHelper, DBHelper, preferencesHelper);
    }
}
