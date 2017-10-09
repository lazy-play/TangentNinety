package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.SectionListBean;

/**
 * Created by Error on 2017/7/31 0031.
 */

public interface SectionContract {
    interface View extends BaseView {
        void showSectionList(SectionListBean sectionListBean);
    }
    interface Presenter extends BasePresenter<View>{
        void getSectionList();
    }
}
