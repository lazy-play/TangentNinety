package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.presenter.contract.HistoryContract;
import com.pudding.tangentninety.utils.RxUtil;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class HistoryPresent extends RxPresenter<HistoryContract.View> implements HistoryContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public HistoryPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getHistoryList() {
mView.showHistoryList(mDataManager.getHistoryBean());
    }
}
