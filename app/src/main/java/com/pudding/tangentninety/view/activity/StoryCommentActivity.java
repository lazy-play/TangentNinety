package com.pudding.tangentninety.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.SwipeFinishActivity;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.presenter.StoryCommentPresent;
import com.pudding.tangentninety.presenter.contract.StoryCommentContract;
import com.pudding.tangentninety.view.adapter.CommentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Error on 2017/7/24 0024.
 */

public class StoryCommentActivity extends SwipeFinishActivity<StoryCommentPresent> implements StoryCommentContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    private static String ID_KEY="id";
    private static String STORYID_KEY="storyID";

    int id;

    @BindView(R.id.container)
    RecyclerView container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    CommentAdapter adapter;

    View errorView;

    List<StoryCommentsBean.CommentsBean> shortBeans;
    List<StoryCommentsBean.CommentsBean> longBeans;
    List<StoryCommentsBean.CommentsBean> totalBean = new ArrayList<>();
    boolean isLongCommentInit;
    boolean isShortCommentInit;
    int lastId;

    public static void startStoryActivity(Activity activity, int storyID) {
        Intent intent = new Intent(activity, StoryCommentActivity.class);
        intent.putExtra(STORYID_KEY, storyID);
        startActivity(activity, intent);
    }

    @Override
    public void setLongCommentList(StoryCommentsBean longCommentList) {
        isLongCommentInit = true;
        longBeans = longCommentList.getComments().size() == 0 ? null : longCommentList.getComments();
        addCommentToTotal();
    }

    @Override
    public void setShortCommentList(StoryCommentsBean shortCommentList) {
        isShortCommentInit = true;
        shortBeans = shortCommentList.getComments().size() == 0 ? null : shortCommentList.getComments();
        addCommentToTotal();
    }

    @Override
    public void showExtra(int num) {
        toolbar.setTitle(num + "条评论");
    }

    private void addCommentToTotal() {
        adapter.setEnableLoadMore(true);
        if (!isLongCommentInit || !isShortCommentInit)
            return;
        if (shortBeans == null && longBeans == null) {
//            if (totalBean.size() != 0)
                loadMoreDate();
            adapter.loadMoreEnd();
            return;
        }

        if (longBeans == null && shortBeans != null) {
            totalBean.addAll(shortBeans);
            loadMoreDate();
            return;
        }
        if (shortBeans == null && longBeans != null) {
            totalBean.addAll(longBeans);
            mPresenter.getLongCommentInfo(id, longBeans.get(longBeans.size() - 1).getId());
            return;
        }
        do {
            StoryCommentsBean.CommentsBean shortBean = shortBeans.get(0);
            StoryCommentsBean.CommentsBean longBean = longBeans.get(0);
            if (shortBean.getTime() >= longBean.getTime()) {
                totalBean.add(shortBean);
                shortBeans.remove(0);
            } else {
                totalBean.add(longBean);
                longBeans.remove(0);
            }
        } while (shortBeans.size() != 0 && longBeans.size() != 0);
        if (shortBeans.size() == 0) {
            loadMoreDate();
            return;
        } else {
            mPresenter.getLongCommentInfo(id, totalBean.get(totalBean.size() - 1).getId());
            return;
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        if(adapter.getData().size()!=0){
            adapter.loadMoreFail();
            return;
        }
        if(errorView==null){
            errorView= LayoutInflater.from(this).inflate(R.layout.layout_error_view,container,false);
            errorView.findViewById(R.id.reflash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    load();
                }
            });
        }
            adapter.setEmptyView(errorView);
    }

    private void loadMoreDate() {
        adapter.setEmptyView(R.layout.layout_empty_view);
        if(totalBean.size()==0)
            return;
        adapter.addData(totalBean);
        adapter.loadMoreComplete();
        lastId = totalBean.get(totalBean.size() - 1).getId();
        totalBean.clear();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_recyclerview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(id!=0)
            outState.putInt(ID_KEY,id);
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState!=null){
            id=savedInstanceState.getInt(ID_KEY);
        }else{
            id = getIntent().getIntExtra(STORYID_KEY, -1);
        }
        setToolBar(toolbar, "0条评论");
        ViewCompat.setOnApplyWindowInsetsListener(container, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if (insets.getSystemWindowInsetBottom() != 0)
                    v.setPadding(v.getPaddingLeft(), 0, v.getPaddingRight(), insets.getSystemWindowInsetBottom());
                return insets;
            }
        });
        totalBean = new ArrayList<>();
        adapter = new CommentAdapter(this);
        container.setLayoutManager(new LinearLayoutManager(this));
        container.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(container);
        adapter.setOnLoadMoreListener(this, container);
        adapter.disableLoadMoreIfNotFullPage();
        load();
    }

    private void load(){
        adapter.setEmptyView(R.layout.layout_loading_view);
                mPresenter.getShortCommentInfo(id);
                mPresenter.getLongCommentInfo(id);
                mPresenter.getStoryExtra(id);

    }
    @Override
    public void onLoadMoreRequested() {
        mPresenter.getShortCommentInfo(id, lastId);
    }
}
