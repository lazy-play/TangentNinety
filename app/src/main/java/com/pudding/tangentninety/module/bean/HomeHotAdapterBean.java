package com.pudding.tangentninety.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Error on 2017/7/3 0003.
 */

public class HomeHotAdapterBean implements Parcelable{
    public static final int TYPE_BEAN=1;
    public static final int TYPE_TITLE=2;
    private StoryInfoBean storyInfoBean;
    private String title;
    private int type;

    public HomeHotAdapterBean(StoryInfoBean storyInfoBean) {
        this.storyInfoBean = storyInfoBean;
        type = TYPE_BEAN;
    }

    public HomeHotAdapterBean(String title) {
        this.title = title;
        type = TYPE_TITLE;
    }

    protected HomeHotAdapterBean(Parcel in) {
        storyInfoBean = in.readParcelable(StoryInfoBean.class.getClassLoader());
        title = in.readString();
        type = in.readInt();
    }

    public static final Creator<HomeHotAdapterBean> CREATOR = new Creator<HomeHotAdapterBean>() {
        @Override
        public HomeHotAdapterBean createFromParcel(Parcel in) {
            return new HomeHotAdapterBean(in);
        }

        @Override
        public HomeHotAdapterBean[] newArray(int size) {
            return new HomeHotAdapterBean[size];
        }
    };

    public StoryInfoBean getStoryInfoBean() {
        return storyInfoBean;
    }

    public String getTitle() {
        return title;
    }


    public int getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(storyInfoBean,0);
        dest.writeString(title);
        dest.writeInt(type);
    }
}
