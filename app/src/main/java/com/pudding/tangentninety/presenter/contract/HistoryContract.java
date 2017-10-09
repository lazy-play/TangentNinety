package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;

import java.util.List;

/**
 * Created by Error on 2017/6/28 0028.
 */

public interface HistoryContract {
    interface View extends BaseView {
        void showHistoryList(List<HistoryBean> beans);
    }
    interface Presenter extends BasePresenter<View> {
        void getHistoryList();
    }
}
