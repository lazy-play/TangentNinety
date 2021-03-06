package com.pudding.tangentninety.module.bean;

import java.util.List;

/**
 * Created by Error on 2017/7/3 0003.
 */

public class SectionStoryBean{
    /**
     * images : ["http://pic3.zhimg.com/91125c9aebcab1c84f58ce4f8779551e.jpg"]
     * date : 20160601
     * display_date : 6 月 1 日
     * id : 8387524
     * title : 深夜惊奇 · 要穿内衣
     * multipic : true
     */
    private int id;
    private String title;
    private List<String> images;
    private String date;
    private String display_date;
    private boolean multipic;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDisplay_date() {
        return display_date;
    }

    public void setDisplay_date(String display_date) {
        this.display_date = display_date;
    }


    public boolean isMultipic() {
        return multipic;
    }

    public void setMultipic(boolean multipic) {
        this.multipic = multipic;
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
}
