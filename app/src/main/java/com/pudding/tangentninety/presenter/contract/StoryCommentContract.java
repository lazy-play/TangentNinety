package com.pudding.tangentninety.presenter.contract;

import com.pudding.tangentninety.base.BasePresenter;
import com.pudding.tangentninety.base.BaseView;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;

/**
 * Created by Error on 2017/7/24 0024.
 */

public interface StoryCommentContract {
    interface View extends BaseView {
        void setLongCommentList(StoryCommentsBean longCommentList);

        void setShortCommentList(StoryCommentsBean shortCommentList);
        void showExtra(int num);
    }

    interface Presenter extends BasePresenter<View> {
        void getLongCommentInfo(int id);

        void getLongCommentInfo(int id, int userid);

        void getShortCommentInfo(int id);

        void getShortCommentInfo(int id, int userid);
        void getStoryExtra(int id);
    }
}
