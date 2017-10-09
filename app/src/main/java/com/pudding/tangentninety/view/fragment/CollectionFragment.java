package com.pudding.tangentninety.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.base.BaseFragment;
import com.pudding.tangentninety.base.SimpleFragment;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.event.CollectionUpdate;
import com.pudding.tangentninety.module.event.NightModeEvent;
import com.pudding.tangentninety.module.event.RecyclerBottomPudding;
import com.pudding.tangentninety.presenter.CollectionPresent;
import com.pudding.tangentninety.presenter.contract.CollectionContract;
import com.pudding.tangentninety.view.activity.MainActivity;
import com.pudding.tangentninety.view.activity.StoryDetailActivity;
import com.pudding.tangentninety.view.adapter.CollectionAdapter;
import com.pudding.tangentninety.weight.InsetFrameLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Error on 2017/6/27 0027.
 */

public class CollectionFragment extends BaseFragment<CollectionPresent> implements CollectionContract.View,View.OnClickListener {
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    RecyclerView container;

    @BindView(R.id.edit)
    TextView edit;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.delete)
    TextView delete;

    private boolean isCollectionUpdate;
    private CollectionAdapter adapter;



    @Override
    protected int getLayoutId() {
        return R.layout.fragment_collection;
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
    public boolean onBackPressedSupport() {
        if (adapter.isEditMode()) {
            showEditMode(false);
            return true;
        }
        return false;
    }




    private void init() {
        isCollectionUpdate = true;
        adapter = new CollectionAdapter(this);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (adapter.isEditMode()) {
                    adapter.checkItem(position, view);
                } else {
                    StoryDetailActivity.startStoryActivity(mActivity, adapter.getData().get(position).getId());
                }
            }
        });
        adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter quickAdapter, View view, int position) {
                if (!adapter.isEditMode()) {
                    showEditMode(true);
                    adapter.checkItem(position, view);
                }
                return false;
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter quickAdapter, View view, int position) {
                adapter.checkItem(position);
            }
        });
        edit.setOnClickListener(this);
        delete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        container.setLayoutManager(new LinearLayoutManager(mContext));
        adapter.bindToRecyclerView(container);
        adapter.setEmptyView(R.layout.layout_empty_view);
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBar(toolbar, getString(R.string.action_collection));
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        update();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
        showEditMode(false);
    }

    private void update() {
        if (isCollectionUpdate) {
            mPresenter.getCollectionList();
            isCollectionUpdate = false;
        }
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
    public void onMessageEvent(CollectionUpdate update) {
        isCollectionUpdate = true;
    }

    @Override
    public void showCollectionList(List<CollectionStoryBean> beans) {
        adapter.setNewData(beans);
    }

    private void showEditMode(boolean show) {
        if(edit==null)
            return;
        edit.setVisibility(!show?View.VISIBLE:View.GONE);
        delete.setVisibility(show?View.VISIBLE:View.GONE);
        cancel.setVisibility(show?View.VISIBLE:View.GONE);
        if (show)
            adapter.openEditMode();
        else adapter.closeEditMode();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit:
                showEditMode(true);
                break;
            case R.id.cancel:
                showEditMode(false);
                break;
            case R.id.delete:
                if (adapter.getWaitToDelete().size() != 0) {
                    AlertDialog dialog = new AlertDialog.Builder(mContext)
                            .setMessage("是否要删除这" + adapter.getWaitToDelete().size() + "个收藏记录")
                            .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.deleteCollections(adapter.getWaitToDelete());
                                    mPresenter.getCollectionList();
                                    showEditMode(false);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(mActivity, R.string.choose_delete, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
