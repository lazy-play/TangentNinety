package com.pudding.tangentninety.base;

import android.support.v7.app.AppCompatDelegate;
import android.view.ViewGroup;

import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.di.component.ActivityComponent;
import com.pudding.tangentninety.di.component.DaggerActivityComponent;
import com.pudding.tangentninety.di.module.ActivityModule;
import com.pudding.tangentninety.utils.SnackbarUtil;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/22 0022.
 */

public abstract class BaseActivity<T extends BasePresenter> extends SimpleActivity implements BaseView {

    @Inject
    protected T mPresenter;

    protected ActivityComponent getActivityComponent(){
        return  DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }

    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    @Override
    protected void onViewCreated() {
        super.onViewCreated();
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void showErrorMsg(String msg) {
//        SnackbarUtil.show(((ViewGroup) findViewById(android.R.id.content)).getChildAt(0), msg);
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

    protected abstract void initInject();
}
