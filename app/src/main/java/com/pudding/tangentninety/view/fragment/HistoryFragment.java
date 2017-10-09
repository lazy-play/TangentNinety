package com.pudding.tangentninety.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.BaseFragment;
import com.pudding.tangentninety.base.SimpleFragment;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.event.HistoryUpdate;
import com.pudding.tangentninety.module.event.NightModeEvent;
import com.pudding.tangentninety.module.event.RecyclerBottomPudding;
import com.pudding.tangentninety.presenter.HistoryPresent;
import com.pudding.tangentninety.presenter.contract.HistoryContract;
import com.pudding.tangentninety.view.activity.MainActivity;
import com.pudding.tangentninety.view.activity.StoryDetailActivity;
import com.pudding.tangentninety.view.adapter.HistoryAdapter;
import com.pudding.tangentninety.weight.InsetFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Error on 2017/6/27 0027.
 */

public class HistoryFragment extends BaseFragment<HistoryPresent> implements HistoryContract.View {
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    RecyclerView container;

    private boolean isHistoryUpdate;
    HistoryAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recyclerview;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBar(toolbar, getString(R.string.action_history));
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
            update();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NightModeEvent event) {
        Resources.Theme theme = getContext().getTheme();
        TypedValue mainColor = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, mainColor, true);
        appbar.setBackgroundResource(mainColor.resourceId);
        adapter.changeTheme(mContext);
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecyclerBottomPudding pudding) {
        if (container != null) {
            container.setPadding(container.getPaddingLeft(), container.getPaddingTop(), container.getPaddingRight(), InsetFrameLayout.recyclerBottomPudding);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HistoryUpdate update) {
        isHistoryUpdate = true;
    }

    protected void init() {
        isHistoryUpdate = true;
        adapter = new HistoryAdapter(mContext);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                StoryDetailActivity.startStoryActivity(mActivity,adapter.getData().get(position).getId());
            }
        });
        container.setLayoutManager(new LinearLayoutManager(mContext));
        container.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        adapter.bindToRecyclerView(container);
        adapter.setEmptyView(R.layout.layout_empty_view);
    }

    protected void update() {
        if (isHistoryUpdate){
            mPresenter.getHistoryList();
        isHistoryUpdate=false;}
    }

    @Override
    public void showHistoryList(List<HistoryBean> beans) {
        adapter.setNewData(beans);
    }
}
