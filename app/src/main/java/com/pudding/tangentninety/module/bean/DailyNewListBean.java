package com.pudding.tangentninety.module.bean;

import java.util.List;

/**
 * Created by Error on 2017/6/26 0026.
 */

public class DailyNewListBean extends DailyListBean{
    private List<TopStoriesBean> top_stories;
    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }
    public static class TopStoriesBean {
        /**
         * image : https://pic1.zhimg.com/v2-582f85f6b7deaaf9aaadb53fbf1fb3a8.jpg
         * type : 0
         * id : 9493628
         * ga_prefix : 062611
         * title : 贴吧「关停并转」，高层人事动荡，陆奇和百度寻找下一个千亿美元计划
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
