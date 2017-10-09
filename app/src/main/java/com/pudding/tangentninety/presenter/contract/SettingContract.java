package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.HistoryBean;

import java.util.List;

/**
 * Created by Error on 2017/6/28 0028.
 */

public interface SettingContract {
    interface View extends BaseView {
    }
    interface Presenter extends BasePresenter<View> {
        boolean isNoPic();
        boolean isBigFont();
        boolean isAutoCache();
        String getCacheSize();
        void setNoPicState(boolean state);
        void setBigFontState(boolean state);
        void setAutoCacheState(boolean state);
        void clearCache();
    }
}
