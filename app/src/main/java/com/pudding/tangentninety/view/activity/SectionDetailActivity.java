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
import com.pudding.tangentninety.module.bean.SectionDetails;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.presenter.SectionDetailPresent;
import com.pudding.tangentninety.presenter.contract.SectionDetailContract;
import com.pudding.tangentninety.view.adapter.CommentAdapter;
import com.pudding.tangentninety.view.adapter.SectionDetailAdapter;

import java.net.IDN;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Error on 2017/7/24 0024.
 */

public class SectionDetailActivity extends SwipeFinishActivity<SectionDetailPresent> implements SectionDetailContract.View, BaseQuickAdapter.RequestLoadMoreListener {
    private static String TITLE_KEY="Title";
    private static String SECTIONID_KEY="SectionID";
    int SectionID;
    long timestamp;
    @BindView(R.id.container)
    RecyclerView container;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    SectionDetailAdapter adapter;

    public static void startSectionDetailActivity(Activity activity, int SectionID, String title) {
        Intent intent = new Intent(activity, SectionDetailActivity.class);
        intent.putExtra(SECTIONID_KEY, SectionID);
        intent.putExtra(TITLE_KEY, title);
        startActivity(activity, intent);
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TITLE_KEY,toolbar.getTitle().toString());
        outState.putInt(SECTIONID_KEY,SectionID);
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        String title = null;
        if (savedInstanceState != null) {
            title = savedInstanceState.getString(TITLE_KEY, "");
            SectionID = savedInstanceState.getInt(SECTIONID_KEY);
        } else {
            title = getIntent().getStringExtra(TITLE_KEY);
            if (title == null)
                title = "";
            SectionID = getIntent().getIntExtra(SECTIONID_KEY, -1);
        }
        setToolBar(toolbar, title);
        ViewCompat.setOnApplyWindowInsetsListener(container, new OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if (insets.getSystemWindowInsetBottom() != 0)
                    v.setPadding(v.getPaddingLeft(), 0, v.getPaddingRight(), insets.getSystemWindowInsetBottom());
                return insets;
            }
        });
        adapter = new SectionDetailAdapter(this, mPresenter.isNoPic());

        container.setLayoutManager(new LinearLayoutManager(this));
        container.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(container);
        adapter.setOnLoadMoreListener(this, container);
        adapter.disableLoadMoreIfNotFullPage();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseAdapter, View view, int position) {
                StoryDetailActivity.startStoryActivity(SectionDetailActivity.this, adapter.getData().get(position).getId());
            }
        });
onLoad();
    }
private void onLoad(){
    adapter.setEmptyView(R.layout.layout_loading_view);
    mPresenter.getSectionDetail(SectionID);
}
    @Override
    public void onLoadMoreRequested() {
        mPresenter.getSectionDetailBefore(SectionID, timestamp);
    }
    @Override
    public void showResult(SectionDetails result) {
        adapter.setEmptyView(R.layout.layout_empty_view);
        adapter.setEnableLoadMore(true);
        if (result.getStories().size()==0) {
            adapter.loadMoreEnd();
            return;
        }
        timestamp = result.getTimestamp();
        adapter.addData(result.getStories());
        adapter.loadMoreComplete();
    }

    View errorView;
    @Override
    public void showErrorMsg(String msg) {
        if(adapter.getData().size()!=0){
            adapter.loadMoreFail();
            return;
        }
        if(errorView==null){
            errorView= LayoutInflater.from(mContext).inflate(R.layout.layout_error_view,container,false);
            errorView.findViewById(R.id.reflash).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLoad();
                }
            });
        }
        adapter.setEmptyView(errorView);
    }
}
