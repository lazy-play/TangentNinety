package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;

/**
 * Created by Error on 2017/8/7 0007.
 */

public interface MainContract {
    interface View extends BaseView {
        void setNetState(boolean isWifi);
    }
    interface Presenter extends BasePresenter<View> {
        void getNetState();
        boolean isAutoDownload();
    }
}
