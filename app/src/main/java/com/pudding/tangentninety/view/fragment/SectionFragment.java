package com.pudding.tangentninety.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.BaseFragment;
import com.pudding.tangentninety.base.SimpleFragment;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.module.event.NightModeEvent;
import com.pudding.tangentninety.module.event.RecyclerBottomPudding;
import com.pudding.tangentninety.presenter.SectionPresent;
import com.pudding.tangentninety.presenter.contract.SectionContract;
import com.pudding.tangentninety.view.activity.MainActivity;
import com.pudding.tangentninety.view.activity.SectionDetailActivity;
import com.pudding.tangentninety.view.adapter.SectionAdapter;
import com.pudding.tangentninety.weight.InsetFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Error on 2017/6/27 0027.
 */

public class SectionFragment extends BaseFragment<SectionPresent> implements SectionContract.View {
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    RecyclerView container;

    SectionAdapter adapter;
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
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBar(toolbar, getString(R.string.action_section));
        }
        adapter=new SectionAdapter(this);
//        container.setLayoutManager(new GridLayoutManager(mContext,2));
//        container.setLayoutManager(new LinearLayoutManager(mContext));
        container.setLayoutManager(new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL));
        adapter.bindToRecyclerView(container);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseAdapter, View view, int position) {
                SectionDetailActivity.startSectionDetailActivity(mActivity,adapter.getData().get(position).getId(),adapter.getData().get(position).getName());
            }
        });
onLoad();
    }
private void onLoad(){
    adapter.setEmptyView(R.layout.layout_loading_view);
    mPresenter.getSectionList();
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
    @Override
    public void showSectionList(SectionListBean sectionListBean) {
        adapter.setEmptyView(R.layout.layout_empty_view);
        adapter.addData(sectionListBean.getData());
    }
}
