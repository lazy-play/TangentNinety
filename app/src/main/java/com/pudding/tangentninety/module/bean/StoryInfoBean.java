package com.pudding.tangentninety.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Error on 2017/6/29 0029.
 */

public class StoryInfoBean implements Parcelable {
    /**
     * images : ["https://pic3.zhimg.com/v2-c2597b3c4f43297457a032976ec41b7a.jpg"]
     * type : 0
     * id : 9491311
     * ga_prefix : 062614
     * title : 只想安静地放空表情，一不小心却向领导摆了张臭脸
     */

    private int type;
    private String ga_prefix;
    private int id;
    private String title;
    private List<String> images;
    private boolean hasRead;

    protected StoryInfoBean(Parcel in) {
        type = in.readInt();
        ga_prefix = in.readString();
        id = in.readInt();
        title = in.readString();
        images = in.createStringArrayList();
    }

    public static final Creator<StoryInfoBean> CREATOR = new Creator<StoryInfoBean>() {
        @Override
        public StoryInfoBean createFromParcel(Parcel in) {
            return new StoryInfoBean(in);
        }

        @Override
        public StoryInfoBean[] newArray(int size) {
            return new StoryInfoBean[size];
        }
    };

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        dest.writeString(ga_prefix);
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeStringList(images);
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false ;
            if (obj instanceof StoryInfoBean){
                StoryInfoBean s = (StoryInfoBean) obj;
                if(s.getId()==this.getId()){
                    return true ;
                }
            }
        return false ;
    }
}