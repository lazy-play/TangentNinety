package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;

/**
 * Created by Error on 2017/7/26 0026.
 */

public interface DownloadContract {
    interface View extends BaseView {
        void showDetail(ZhihuDetailBean zhihuDetailBean);
        void showDailyNewList(DailyNewListBean bean);
        void showDailyBeforeList(DailyListBean bean);
    }
    interface Presenter extends BasePresenter<View> {
        void loadDailyNewList();
        void loadDailyBeforeList(String before);
        void getStory(int id);
        void addDownloadStory(int id);
        boolean isStoryDownload(int id);
        void addImageUrl(String url);
        void loadImageUrl(String url);
    }
}
