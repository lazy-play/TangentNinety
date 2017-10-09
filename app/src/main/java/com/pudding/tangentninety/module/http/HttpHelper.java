package com.pudding.tangentninety.module.http;

import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.SectionDetails;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * Created by Error on 2017/6/22 0022.
 */

public interface HttpHelper {

    Flowable<DailyNewListBean> fetchDailyListInfo();
    Flowable<DailyListBean> fetchDailyBeforeListInfo(String date);
    Flowable<ZhihuDetailBean> fetchDetailInfo(int id);
    Flowable<DetailExtraBean> fetchDetailExtra(int id);

    Flowable<StoryCommentsBean> fetchShortCommentInfo(int id);
    Flowable<StoryCommentsBean> fetchShortCommentInfo(int id,int userid);
    Flowable<StoryCommentsBean> fetchLongCommentInfo(int id);
    Flowable<StoryCommentsBean> fetchLongCommentInfo(int id,int userid);

    Flowable<SectionListBean> fetchSectionList();
    Flowable<SectionDetails> fetchSectionDetails(int id);
    Flowable<SectionDetails> fetchBeforeSectionsDetails(int id,long timestamp);

    Flowable<ResponseBody> getNetFromState();
}