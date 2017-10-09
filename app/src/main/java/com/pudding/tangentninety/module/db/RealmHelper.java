package com.pudding.tangentninety.module.db;


import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.DownloadStory;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.bean.WebImageUrlBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;


import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class RealmHelper implements DBHelper {

    private static final String DB_NAME = "myRealm.realm";
private static final int MAX_HISTORY_NUM=500;
    private Realm mRealm;

    @Inject
    public RealmHelper() {
        mRealm = Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(DB_NAME)
                .build());
    }


    @Override
    public void insertImageUrl(String url) {
        WebImageUrlBean bean = new WebImageUrlBean();
        bean.setUrl(url);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    @Override
    public boolean isImageUrlExist(String url) {
        return mRealm.where(WebImageUrlBean.class).equalTo("url", url).findFirst()!=null;
    }

    @Override
    public void insertHistoryBean(ZhihuDetailBean bean) {
        HistoryBean history=new HistoryBean();
        history.setId(bean.getId());
        history.setTitle(bean.getTitle());
        history.setTime(System.currentTimeMillis());
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(history);
        mRealm.commitTransaction();
        RealmResults<HistoryBean> results = mRealm.where(HistoryBean.class).findAll();
        if(results.size()>MAX_HISTORY_NUM){
        results=results.sort("time", Sort.DESCENDING);
            mRealm.beginTransaction();
            results.deleteLastFromRealm();
            mRealm.commitTransaction();
        }
    }

    @Override
    public List<HistoryBean> getHistoryBean() {
        RealmResults<HistoryBean> results = mRealm.where(HistoryBean.class).findAll();
        results=results.sort("time", Sort.DESCENDING);
        return mRealm.copyFromRealm(results);
    }

    @Override
    public boolean isHistoryExist(int id) {
        return mRealm.where(HistoryBean.class).equalTo("id", id).findFirst()!=null;
    }

    @Override
    public void insertCollection(CollectionStoryBean bean) {
        mRealm.beginTransaction();
        if(bean!=null){
            bean.setTime(System.currentTimeMillis());
        mRealm.copyToRealmOrUpdate(bean);
        }
        mRealm.commitTransaction();
    }

    @Override
    public void reomveCollection(int id) {
        CollectionStoryBean bean =mRealm.where(CollectionStoryBean.class).equalTo("id", id).findFirst();
        mRealm.beginTransaction();
        if(bean!=null)
        bean.deleteFromRealm();
        mRealm.commitTransaction();
    }

    @Override
    public boolean isCollectionExist(int id) {
        return mRealm.where(CollectionStoryBean.class).equalTo("id", id).findFirst()!=null;
    }

    @Override
    public List<CollectionStoryBean> getCollectionList() {
        return  mRealm.copyFromRealm(mRealm.where(CollectionStoryBean.class).findAll().sort("time", Sort.DESCENDING));
    }

    @Override
    public void removeCollectionList(List<Integer> ids) {
        mRealm.beginTransaction();
        for(int id:ids){
            CollectionStoryBean bean =mRealm.where(CollectionStoryBean.class).equalTo("id", id).findFirst();
            if(bean!=null)
                bean.deleteFromRealm();
        }
        mRealm.commitTransaction();
    }

    @Override
    public void insertDownloadStory(int id) {
        DownloadStory bean=new DownloadStory();
        bean.setId(id);
        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(bean);
        mRealm.commitTransaction();
    }

    @Override
    public boolean isStoryDownload(int id) {
        return mRealm.where(DownloadStory.class).equalTo("id",id).findFirst()!=null;
    }

    @Override
    public void clearCache() {
        mRealm.beginTransaction();
        mRealm.where(DownloadStory.class).findAll().deleteAllFromRealm();
        mRealm.where(WebImageUrlBean.class).findAll().deleteAllFromRealm();
        mRealm.commitTransaction();
    }

}
