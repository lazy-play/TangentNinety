package com.pudding.tangentninety.presenter;

import android.net.Uri;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.DetailContract;
import com.pudding.tangentninety.utils.ImageLoader;
import com.pudding.tangentninety.utils.LogUtil;
import com.pudding.tangentninety.utils.RxUtil;
import com.pudding.tangentninety.utils.SystemUtil;

import org.reactivestreams.Publisher;

import java.io.File;
import java.net.URLEncoder;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class DetailPresent extends RxPresenter<DetailContract.View> implements DetailContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public DetailPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
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
    public void getStoryExtra(int id) {
        mDataManager.fetchDetailExtra(id).compose(RxUtil.<DetailExtraBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<DetailExtraBean>(mView) {
                    @Override
                    public void onNext(DetailExtraBean detailExtraBean) {
                        if (mView != null)
                            mView.showExtra(detailExtraBean);
                    }
                });
    }

    @Override
    public boolean isNightMode() {
        return mDataManager.getNightModeState();
    }

    @Override
    public boolean isNoPic() {
        return mDataManager.getNoImageState();
    }

    @Override
    public boolean isBigFont() {
        return mDataManager.getBigFontState();
    }

    @Override
    public boolean isImageUrlExist(String url) {
        return mDataManager.isImageUrlExist(url);
    }

    @Override
    public void addImageUrl(String url) {
        mDataManager.insertImageUrl(url);
    }

    @Override
    public void addStoryToHistory(ZhihuDetailBean bean) {
        mDataManager.insertHistoryBean(bean);
    }

    @Override
    public boolean isCollectionExist(int id) {
        return mDataManager.isCollectionExist(id);
    }

    @Override
    public void collectionStory(CollectionStoryBean bean) {
        mDataManager.insertCollection(bean);
    }

    @Override
    public void uncollectionStory(int id) {
        mDataManager.reomveCollection(id);
    }
    @Override
    public void loadDownloadImage(String url) {
        Flowable.just(url)
                .filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                return isImageUrlExist(s);
            }
        })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String s) throws Exception {
                        String newurl = URLEncoder.encode(s);
                        if (s.endsWith("jpg") || s.endsWith("png") || s.endsWith("gif")){
                            File f= ImageLoader.getGlideCache(s);
                            String base = "file://"+f.getAbsolutePath();
                            return Flowable.just("javascript:img_replace(\"" + newurl + "\",\"" + base + "\");");
                        }else return Flowable.just("javascript:img_replace(\"" + newurl + "\",\"" + newurl + "\");");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber<String>(mView) {
                    @Override
                    public void onNext(String s) {
                        if(mView!=null)
                        mView.loadJsUrl(s);
                    }
                });
    }
    @Override
    public void loadImage(String url) {
        Flowable.just(url).filter(new Predicate<String>() {
            @Override
            public boolean test(@NonNull String s) throws Exception {
                addImageUrl(s);
                return true;
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(new Function<String, Publisher<String>>() {
                    @Override
                    public Publisher<String> apply(@NonNull String s) throws Exception {
                        String newurl = URLEncoder.encode(s);
                        if (s.endsWith("jpg") || s.endsWith("png") || s.endsWith("gif")){
                        File f= ImageLoader.getGlideCache(s);
                        String base = "file://"+f.getAbsolutePath();
                        return Flowable.just("javascript:img_replace(\"" + newurl + "\",\"" + base + "\");");
                        }else return Flowable.just("javascript:img_replace(\"" + newurl + "\",\"" + newurl + "\");");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonSubscriber<String>(mView) {
                    @Override
                    public void onNext(String s) {
                        if(mView!=null)
                        mView.loadJsUrl(s);
                    }
                });
    }
}
