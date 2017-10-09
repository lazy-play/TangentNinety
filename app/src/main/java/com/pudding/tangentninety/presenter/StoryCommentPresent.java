package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.StoryCommentContract;
import com.pudding.tangentninety.utils.RxUtil;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Error on 2017/7/24 0024.
 */

public class StoryCommentPresent extends RxPresenter<StoryCommentContract.View> implements StoryCommentContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public StoryCommentPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }
    @Override
    public void getLongCommentInfo(int id) {
        mDataManager.fetchLongCommentInfo(id).compose(RxUtil.<StoryCommentsBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<StoryCommentsBean>(mView){

                    @Override
                    public void onNext(StoryCommentsBean storyCommentsBean) {
                        mView.setLongCommentList(storyCommentsBean);
                    }
                });
    }

    @Override
    public void getLongCommentInfo(int id, int userid) {
        mDataManager.fetchLongCommentInfo(id,userid).compose(RxUtil.<StoryCommentsBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<StoryCommentsBean>(mView){

                    @Override
                    public void onNext(StoryCommentsBean storyCommentsBean) {
                        mView.setLongCommentList(storyCommentsBean);
                    }
                });
    }

    @Override
    public void getShortCommentInfo(int id) {
        mDataManager.fetchShortCommentInfo(id).compose(RxUtil.<StoryCommentsBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<StoryCommentsBean>(mView){

                    @Override
                    public void onNext(StoryCommentsBean storyCommentsBean) {
                        mView.setShortCommentList(storyCommentsBean);
                    }
                });
    }

    @Override
    public void getShortCommentInfo(int id, int userid) {
        mDataManager.fetchShortCommentInfo(id,userid).compose(RxUtil.<StoryCommentsBean>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<StoryCommentsBean>(mView){
                    @Override
                    public void onNext(StoryCommentsBean storyCommentsBean) {
                        mView.setShortCommentList(storyCommentsBean);
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
                            mView.showExtra(detailExtraBean.getComments());
                    }
                });
    }
}
