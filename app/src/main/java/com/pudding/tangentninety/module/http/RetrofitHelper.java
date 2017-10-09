package com.pudding.tangentninety.module.http;

import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.SectionDetails;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;
import com.pudding.tangentninety.module.http.api.ZhihuApis;

import javax.inject.Inject;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class RetrofitHelper implements HttpHelper {

    private ZhihuApis mZhihuApiService;

    @Inject
    public RetrofitHelper(ZhihuApis zhihuApiService) {
        this.mZhihuApiService = zhihuApiService;
    }


    @Override
    public Flowable<DailyNewListBean> fetchDailyListInfo() {
        return mZhihuApiService.getDailyList();
    }

    @Override
    public Flowable<DailyListBean> fetchDailyBeforeListInfo(String date) {
        return mZhihuApiService.getDailyBeforeList(date);
    }


    @Override
    public Flowable<ZhihuDetailBean> fetchDetailInfo(int id) {
        return mZhihuApiService.getDetailInfo(id);
    }

    @Override
    public Flowable<DetailExtraBean> fetchDetailExtra(int id) {
        return mZhihuApiService.getDetailExtraInfo(id);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchShortCommentInfo(int id) {
        return mZhihuApiService.getShortCommentInfo(id);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchShortCommentInfo(int id, int userid) {
        return mZhihuApiService.getShortCommentInfo(id,userid);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchLongCommentInfo(int id) {
        return mZhihuApiService.getLongCommentInfo(id);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchLongCommentInfo(int id, int userid) {
        return mZhihuApiService.getLongCommentInfo(id,userid);
    }

    @Override
    public Flowable<SectionListBean> fetchSectionList() {
        return mZhihuApiService.getSectionList();
    }

    @Override
    public Flowable<SectionDetails> fetchSectionDetails(int id) {
        return mZhihuApiService.getSectionDetails(id);
    }

    @Override
    public Flowable<SectionDetails> fetchBeforeSectionsDetails(int id, long timestamp) {
        return mZhihuApiService.getBeforeSectionsDetails(id,timestamp);
    }

    @Override
    public Flowable<ResponseBody> getNetFromState() {
        return mZhihuApiService.getNetFromState();
    }
}