package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.presenter.contract.CollectionContract;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class CollectionPresent extends RxPresenter<CollectionContract.View> implements CollectionContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public CollectionPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getCollectionList() {
        mView.showCollectionList(mDataManager.getCollectionList());
    }

    @Override
    public void deleteCollections(List<Integer> ids) {
        mDataManager.removeCollectionList(ids);
    }
}
