package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.event.NoPicEvent;
import com.pudding.tangentninety.presenter.contract.SettingContract;
import com.pudding.tangentninety.utils.GlideCacheUtil;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class SettingPresent extends RxPresenter<SettingContract.View> implements SettingContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SettingPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public boolean isNoPic() {
        return mDataManager.getNoImageState();
    }

    @Override
    public boolean isBigFont() {
        return mDataManager.getBigFontState();
    }

    @Override
    public boolean isAutoCache() {
        return mDataManager.getAutoCacheState();
    }

    @Override
    public String getCacheSize() {
        return GlideCacheUtil.getInstance().getCacheSize(App.getInstance());
    }

    @Override
    public void setNoPicState(boolean state) {
        mDataManager.setNoImageState(state);
        EventBus.getDefault().post(new NoPicEvent());
    }

    @Override
    public void setBigFontState(boolean state) {
        mDataManager.setBigFontState(state);
    }

    @Override
    public void setAutoCacheState(boolean state) {
        mDataManager.setAutoCacheState(state);
    }

    @Override
    public void clearCache() {
        GlideCacheUtil.getInstance().clearImageAllCache(App.getInstance());
        mDataManager.clearCache();
    }
}
