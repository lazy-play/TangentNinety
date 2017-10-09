package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.SectionDetails;

/**
 * Created by Error on 2017/8/2 0002.
 */

public interface SectionDetailContract {
    interface View extends BaseView {
        void showResult(SectionDetails result);
    }
    interface Presenter extends BasePresenter<View> {
        void getSectionDetail(int id);
        void getSectionDetailBefore(int id,long timeBefore);
        boolean isNoPic();
    }
}
