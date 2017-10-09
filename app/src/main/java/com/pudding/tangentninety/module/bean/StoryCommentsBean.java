package com.pudding.tangentninety.module.bean;

import java.util.List;

/**
 * Created by Error on 2017/7/24 0024.
 */

public class StoryCommentsBean {

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * author : 晨曦中的拥抱
         * content : 好棒，可以去写个回答啊
         * avatar : http://pic2.zhimg.com/da7f21892456af2079b8488511937b21_im.jpg
         * time : 1500879144
         * reply_to : {"content":"看到倒写典故，我满脑子想着藕娃哪吒想变成有血有肉的人，从太乙真人手下逃出大闹东海，老龙王给烦的不行，想亲自动手灭了哪吒又怕得罪太乙，就强行甩锅给陈塘关总兵李靖。于是用法力把哪吒变成一个肉球，塞到李靖夫人的肚子里，这一消化就消化了三年又六个月\u2026\u2026","status":0,"id":29713932,"author":"爱做梦的被害妄想者"}
         * id : 29715774
         * likes : 0
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private ReplyToBean reply_to;
        private int id;
        private int likes;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public static class ReplyToBean {
            /**
             * content : 看到倒写典故，我满脑子想着藕娃哪吒想变成有血有肉的人，从太乙真人手下逃出大闹东海，老龙王给烦的不行，想亲自动手灭了哪吒又怕得罪太乙，就强行甩锅给陈塘关总兵李靖。于是用法力把哪吒变成一个肉球，塞到李靖夫人的肚子里，这一消化就消化了三年又六个月……
             * status : 0
             * id : 29713932
             * author : 爱做梦的被害妄想者
             */

            private String content;
            private int status;
            private int id;
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
