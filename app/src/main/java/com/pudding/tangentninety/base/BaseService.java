package com.pudding.tangentninety.base;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.di.component.DaggerServiceComponent;
import com.pudding.tangentninety.di.component.ServiceComponent;
import com.pudding.tangentninety.di.module.ServiceModule;

import javax.inject.Inject;

/**
 * Created by Error on 2017/7/26 0026.
 */

public abstract class BaseService<T extends BasePresenter> extends Service implements BaseView{
    @Inject
    protected T mPresenter;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        initInject();
        mPresenter.attachView(this);
        super.onCreate();
        initEventAndData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }
    protected ServiceComponent getServiceComponent(){
        return DaggerServiceComponent.builder()
                .appComponent(App.getAppComponent())
                .serviceModule(getServiceModule())
                .build();
    }
    protected ServiceModule getServiceModule(){
        return new ServiceModule(this);
    }
    protected abstract void initEventAndData();
    protected abstract void initInject();

    @Override
    public void showErrorMsg(String msg) {
    }


    @Override
    public void stateError() {

    }

    @Override
    public void stateEmpty() {

    }

    @Override
    public void stateLoading() {

    }

    @Override
    public void stateMain() {

    }
}
