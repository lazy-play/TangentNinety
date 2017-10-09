package com.pudding.tangentninety.presenter.contract;

import android.net.Uri;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;


/**
 * Created by Error on 2017/7/15 0015.
 */

public interface PhotoContract {
    interface View extends BaseView {
        void showResult(String result);
    }
    interface Presenter extends BasePresenter<View> {
        void downloadImage(String url);
    }
}
