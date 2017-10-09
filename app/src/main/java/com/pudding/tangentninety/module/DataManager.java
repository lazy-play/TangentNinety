package com.pudding.tangentninety.module;

import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.bean.SectionDetails;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;
import com.pudding.tangentninety.module.db.DBHelper;
import com.pudding.tangentninety.module.http.HttpHelper;
import com.pudding.tangentninety.module.prefs.PreferencesHelper;

import java.util.List;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class DataManager implements HttpHelper, DBHelper, PreferencesHelper {

    HttpHelper mHttpHelper;
    DBHelper mDbHelper;
    PreferencesHelper mPreferencesHelper;

    public DataManager(HttpHelper httpHelper, DBHelper dbHelper, PreferencesHelper preferencesHelper) {
        mHttpHelper = httpHelper;
        mDbHelper = dbHelper;
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public boolean getNightModeState() {
        return mPreferencesHelper.getNightModeState();
    }

    @Override
    public void setNightModeState(boolean state) {
        mPreferencesHelper.setNightModeState(state);
    }

    @Override
    public boolean getNoImageState() {
        return mPreferencesHelper.getNoImageState();
    }

    @Override
    public void setNoImageState(boolean state) {
        mPreferencesHelper.setNoImageState(state);
    }

    @Override
    public boolean getAutoCacheState() {
        return mPreferencesHelper.getAutoCacheState();
    }

    @Override
    public void setAutoCacheState(boolean state) {
        mPreferencesHelper.setAutoCacheState(state);
    }

    @Override
    public boolean getBigFontState() {
        return mPreferencesHelper.getBigFontState();
    }

    @Override
    public void setBigFontState(boolean state) {
mPreferencesHelper.setBigFontState(state);
    }

    @Override
    public void insertImageUrl(String url) {
        mDbHelper.insertImageUrl(url);
    }

    @Override
    public boolean isImageUrlExist(String url) {
        return mDbHelper.isImageUrlExist(url);
    }

    @Override
    public void insertHistoryBean(ZhihuDetailBean bean) {
        mDbHelper.insertHistoryBean(bean);
    }

    @Override
    public List<HistoryBean> getHistoryBean() {
        return mDbHelper.getHistoryBean();
    }

    @Override
    public boolean isHistoryExist(int id) {
        return mDbHelper.isHistoryExist(id);
    }

    @Override
    public void insertCollection(CollectionStoryBean bean) {
        mDbHelper.insertCollection(bean);
    }

    @Override
    public void reomveCollection(int id) {
mDbHelper.reomveCollection(id);
    }

    @Override
    public boolean isCollectionExist(int id) {
        return mDbHelper.isCollectionExist(id);
    }

    @Override
    public List<CollectionStoryBean> getCollectionList() {
        return mDbHelper.getCollectionList();
    }

    @Override
    public void removeCollectionList(List<Integer> ids) {
        mDbHelper.removeCollectionList(ids);
    }

    @Override
    public void insertDownloadStory(int id) {
        mDbHelper.insertDownloadStory(id);
    }

    @Override
    public boolean isStoryDownload(int id) {
        return mDbHelper.isStoryDownload(id);
    }

    @Override
    public void clearCache() {
mDbHelper.clearCache();
    }

    @Override
    public Flowable<DailyNewListBean> fetchDailyListInfo() {
        return mHttpHelper.fetchDailyListInfo();
    }

    @Override
    public Flowable<DailyListBean> fetchDailyBeforeListInfo(String date) {
        return mHttpHelper.fetchDailyBeforeListInfo(date);
    }

    @Override
    public Flowable<ZhihuDetailBean> fetchDetailInfo(int id) {
        return mHttpHelper.fetchDetailInfo(id);
    }

    @Override
    public Flowable<DetailExtraBean> fetchDetailExtra(int id) {
        return mHttpHelper.fetchDetailExtra(id);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchShortCommentInfo(int id) {
        return mHttpHelper.fetchShortCommentInfo(id);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchShortCommentInfo(int id, int userid) {
        return mHttpHelper.fetchShortCommentInfo(id,userid);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchLongCommentInfo(int id) {
        return mHttpHelper.fetchLongCommentInfo(id);
    }

    @Override
    public Flowable<StoryCommentsBean> fetchLongCommentInfo(int id, int userid) {
        return mHttpHelper.fetchLongCommentInfo(id,userid);
    }

    @Override
    public Flowable<SectionListBean> fetchSectionList() {
        return mHttpHelper.fetchSectionList();
    }

    @Override
    public Flowable<SectionDetails> fetchSectionDetails(int id) {
        return mHttpHelper.fetchSectionDetails(id);
    }

    @Override
    public Flowable<SectionDetails> fetchBeforeSectionsDetails(int id, long timestamp) {
        return mHttpHelper.fetchBeforeSectionsDetails(id,timestamp);
    }

    @Override
    public Flowable<ResponseBody> getNetFromState() {
        return mHttpHelper.getNetFromState();
    }
}

