package com.pudding.tangentninety.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.di.component.ActivityComponent;
import com.pudding.tangentninety.di.component.DaggerActivityComponent;
import com.pudding.tangentninety.di.module.ActivityModule;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SwipeBackLayout;
import me.yokeyword.fragmentation_swipeback.SwipeBackActivity;

/**
 * Created by Error on 2017/7/6 0006.
 */

public abstract class SwipeFinishActivity<T extends BasePresenter> extends SwipeBackActivity implements BaseView {

    @Inject
    protected T mPresenter;
    private Unbinder mUnBinder;
    protected Activity mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDayNightTheme();
        setTransition();
        setContentView(getLayout());
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        onViewCreated();
        App.getInstance().addActivity(this);
        initEventAndData(savedInstanceState);
    }
    protected void setTransition(){
    };
    protected void setDayNightTheme(){
        if(!App.getAppComponent().preferencesHelper().getNightModeState()){
            setTheme(R.style.AppTheme);
        }else{
            setTheme(R.style.AppTheme_Night);
        }
    }
    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressedSupport();
            }
        });
    }

    public static void startActivity(Activity activity, Intent intent){
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in,0);
    }
    @Override
    protected void onDestroy() {
        if (mPresenter != null)
            mPresenter.detachView();
        super.onDestroy();
        App.getInstance().removeActivity(this);
        mUnBinder.unbind();
    }
    protected ActivityComponent getActivityComponent(){
        return  DaggerActivityComponent.builder()
                .appComponent(App.getAppComponent())
                .activityModule(getActivityModule())
                .build();
    }
    protected void onViewCreated() {
        initInject();
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            finish();
            overridePendingTransition(0,R.anim.slide_right_out);
        }
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
    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }
    protected abstract int getLayout();
    protected abstract void initEventAndData(@Nullable Bundle savedInstanceState);
    protected abstract void initInject();
}
