package com.pudding.tangentninety.module.db;


import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;

import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Error on 2017/6/22 0022.
 */

public interface DBHelper {

    void insertImageUrl(String url);
    boolean isImageUrlExist(String url);
    void insertHistoryBean(ZhihuDetailBean bean);
    List<HistoryBean> getHistoryBean();
    boolean isHistoryExist(int id);
    void insertCollection(CollectionStoryBean bean);
    void reomveCollection(int id);
    boolean isCollectionExist(int id);
    List<CollectionStoryBean> getCollectionList();
    void removeCollectionList(List<Integer> ids);
    void insertDownloadStory(int id);
    boolean isStoryDownload(int id);
    void clearCache();
}

