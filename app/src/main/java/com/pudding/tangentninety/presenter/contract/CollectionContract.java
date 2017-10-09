package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.HistoryBean;

import java.util.List;

/**
 * Created by Error on 2017/6/28 0028.
 */

public interface CollectionContract {
    interface View extends BaseView {
        void showCollectionList(List<CollectionStoryBean> beans);
    }
    interface Presenter extends BasePresenter<View> {
        void getCollectionList();
        void deleteCollections(List<Integer> ids);
    }
}
