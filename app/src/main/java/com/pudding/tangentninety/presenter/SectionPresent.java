package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.SectionContract;
import com.pudding.tangentninety.utils.RxUtil;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class SectionPresent extends RxPresenter<SectionContract.View> implements SectionContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public SectionPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getSectionList() {
        mDataManager.fetchSectionList().compose(RxUtil.<SectionListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<SectionListBean>(mView) {
                    @Override
                    public void onNext(SectionListBean sectionListBean) {
                        if (mView != null)
                            mView.showSectionList(sectionListBean);
                    }
                });
    }
}
