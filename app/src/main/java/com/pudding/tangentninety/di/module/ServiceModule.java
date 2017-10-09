package com.pudding.tangentninety.di.module;

import android.app.Activity;
import android.app.Service;

import com.pudding.tangentninety.di.scope.ActivityScope;
import com.pudding.tangentninety.di.scope.ServiceScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Error on 2017/6/22 0022.
 */

@Module
public class ServiceModule {
    private Service mService;

    public ServiceModule(Service service) {
        this.mService = service;
    }

    @Provides
    @ServiceScope
    public Service provideService() {
        return mService;
    }
}