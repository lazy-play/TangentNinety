package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;

/**
 * Created by Error on 2017/7/3 0003.
 */

public interface HomeContract {
    interface View extends BaseView {
        void showDailyNewList(DailyNewListBean bean);
        void showDailyBeforeList(DailyListBean bean);
    }
    interface Presenter extends BasePresenter<View> {
        void loadDailyNewList();
        void loadDailyBeforeList(String before);
        boolean isNoPic();
    }
}
