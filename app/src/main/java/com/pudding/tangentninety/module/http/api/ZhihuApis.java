package com.pudding.tangentninety.module.http.api;

import com.pudding.tangentninety.app.Constants;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.SectionDetails;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

/**
 * Created by Error on 2017/6/22 0022.
 */

public interface ZhihuApis {
//根地址
    String HOST = "http://news-at.zhihu.com/api/4/";

    //永远联网请求最新
    String NET_CACHE_STRING="Cache-Control:public, max-age="+ Constants.NET_CACHE_TIME;
    //只使用缓存
    String NO_NET_CACHE_STRING="Cache-Control:public, max-stale="+ Constants.NO_NET_CACHE_TIME;
    /**
     * 最新日报
     */
    @Headers(NET_CACHE_STRING)
    @GET("news/latest")
    Flowable<DailyNewListBean> getDailyList();

    /**
     * 往期日报
     */
    @Headers(NO_NET_CACHE_STRING)
    @GET("news/before/{date}")
    Flowable<DailyListBean> getDailyBeforeList(@Path("date") String date);


    /**
     * 日报详情
     */
    @Headers(NO_NET_CACHE_STRING)
    @GET("news/{id}")
    Flowable<ZhihuDetailBean> getDetailInfo(@Path("id") int id);

    /**
     * 日报的额外信息
     */
    @Headers(NET_CACHE_STRING)
    @GET("story-extra/{id}")
    Flowable<DetailExtraBean> getDetailExtraInfo(@Path("id") int id);
    /**
     * 日报的长评论
     */
    @Headers(NET_CACHE_STRING)
    @GET("story/{id}/long-comments")
    Flowable<StoryCommentsBean> getLongCommentInfo(@Path("id") int id);
    /**
     * 日报某条长评论之前的长评论
     */
    @Headers(NET_CACHE_STRING)
    @GET("story/{id}/long-comments/before/{userid}")
    Flowable<StoryCommentsBean> getLongCommentInfo(@Path("id") int id,@Path("userid") int userid);

    /**
     * 日报的短评论
     */
    @Headers(NET_CACHE_STRING)
    @GET("story/{id}/short-comments")
    Flowable<StoryCommentsBean> getShortCommentInfo(@Path("id") int id);
    /**
     * 日报某条短评论之前的短评论
     */
    @Headers(NET_CACHE_STRING)
    @GET("story/{id}/short-comments/before/{userid}")
    Flowable<StoryCommentsBean> getShortCommentInfo(@Path("id") int id,@Path("userid") int userid);
    /**
     * 专栏日报
     */
    @Headers(NET_CACHE_STRING)
    @GET("sections")
    Flowable<SectionListBean> getSectionList();

    /**
     * 专栏日报详情
     */
    @Headers(NET_CACHE_STRING)
    @GET("section/{id}")
    Flowable<SectionDetails> getSectionDetails(@Path("id") int id);
    /**
     * 获取专栏的之前消息
     */
    @Headers(NET_CACHE_STRING)
    @GET("section/{id}/before/{timestamp}")
    Flowable<SectionDetails> getBeforeSectionsDetails(@Path("id") int id, @Path("timestamp") long timestamp);

    @Headers(NET_CACHE_STRING)
    @GET("http://ip.chinaz.com/getip.aspx")
    Flowable<ResponseBody> getNetFromState();

}
