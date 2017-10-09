package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;

/**
 * Created by Error on 2017/6/28 0028.
 */

public interface DetailContract {
    interface View extends BaseView {
        void showDetail(ZhihuDetailBean zhihuDetailBean);
        void showExtra(DetailExtraBean detailExtraBean);
        void loadJsUrl(String url);
    }
    interface Presenter extends BasePresenter<View> {

        void getStory(int id);
        void getStoryExtra(int id);
        boolean isNightMode();
        boolean isNoPic();
        boolean isBigFont();
        boolean isImageUrlExist(String url);
        void addImageUrl(String url);
        void addStoryToHistory(ZhihuDetailBean bean);
        boolean isCollectionExist(int id);
        void collectionStory(CollectionStoryBean bean);
        void uncollectionStory(int id);
        void loadDownloadImage(String url);
        void loadImage(String url);
    }
}
