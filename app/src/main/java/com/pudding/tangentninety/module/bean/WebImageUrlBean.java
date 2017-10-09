package com.pudding.tangentninety.module.bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Error on 2017/7/12 0012.
 */

public class WebImageUrlBean extends RealmObject {
    @PrimaryKey
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
