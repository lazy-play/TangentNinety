package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.StoryInfoBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.DownloadContract;
import com.pudding.tangentninety.utils.ImageLoader;
import com.pudding.tangentninety.utils.RxUtil;

import org.reactivestreams.Publisher;

import java.io.File;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Error on 2017/6/28 0028.
 */

public class DownloadPresent extends RxPresenter<DownloadContract.View> implements DownloadContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public DownloadPresent(DataManager mDataManager) {
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
    public void getStory(int id) {
        mDataManager.fetchDetailInfo(id).compose(RxUtil.<ZhihuDetailBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ZhihuDetailBean>(mView) {
                    @Override
                    public void onNext(ZhihuDetailBean zhihuDetailBean) {
                        if (mView != null)
                            mView.showDetail(zhihuDetailBean);
                    }
                });
    }

    @Override
    public void addDownloadStory(int id) {
        mDataManager.insertDownloadStory(id);
    }

    @Override
    public boolean isStoryDownload(int id) {
        return mDataManager.isStoryDownload(id);
    }


    @Override
    public void addImageUrl(String url) {
        Flowable.just(url)
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String s) throws Exception {
                        ImageLoader.getGlideCache(s);
                        return Flowable.just(s);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber<String>(mView) {
                    @Override
                    public void onNext(String s) {
                        mDataManager.insertImageUrl(s);
                    }
                });

    }

    @Override
    public void loadImageUrl(String url) {
        Flowable.just(url).observeOn(Schedulers.io())
                .subscribe(new CommonSubscriber<String>(mView){
                    @Override
                    public void onNext(String s) {
                        ImageLoader.getGlideCache(s);
                    }
                });
    }
}
