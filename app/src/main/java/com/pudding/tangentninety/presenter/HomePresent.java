package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.StoryInfoBean;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.HomeContract;
import com.pudding.tangentninety.utils.RxUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Error on 2017/7/3 0003.
 */

public class HomePresent extends RxPresenter<HomeContract.View> implements HomeContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public HomePresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void loadDailyNewList() {
        mDataManager.fetchDailyListInfo().compose(RxUtil.<DailyNewListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DailyNewListBean>(mView) {
                    @Override
                    public void onNext(DailyNewListBean dailyNewListBean) {
                        List<StoryInfoBean> beans = dailyNewListBean.getStories();
                        for (StoryInfoBean bean : beans) {
                            bean.setHasRead(mDataManager.isHistoryExist(bean.getId()));
                        }
                        if (mView != null)
                            mView.showDailyNewList(dailyNewListBean);
                    }
                });
    }

    @Override
    public void loadDailyBeforeList(String before) {
        mDataManager.fetchDailyBeforeListInfo(before).compose(RxUtil.<DailyListBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DailyListBean>(mView) {
                    @Override
                    public void onNext(DailyListBean dailyListBean) {
                        List<StoryInfoBean> beans = dailyListBean.getStories();
                        for (StoryInfoBean bean : beans) {
                            bean.setHasRead(mDataManager.isHistoryExist(bean.getId()));
                        }
                        if (mView != null)
                            mView.showDailyBeforeList(dailyListBean);
                    }
                });
    }

    @Override
    public boolean isNoPic() {
        return mDataManager.getNoImageState();
    }
}
