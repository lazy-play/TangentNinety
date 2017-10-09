package com.pudding.tangentninety.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.Request;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.BaseFragment;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.HomeHotAdapterBean;
import com.pudding.tangentninety.module.bean.StoryInfoBean;
import com.pudding.tangentninety.module.event.NightModeEvent;
import com.pudding.tangentninety.module.event.NoPicEvent;
import com.pudding.tangentninety.module.event.RecyclerBottomPudding;
import com.pudding.tangentninety.presenter.HomePresent;
import com.pudding.tangentninety.presenter.contract.HomeContract;
import com.pudding.tangentninety.utils.ImageLoader;
import com.pudding.tangentninety.utils.SystemUtil;
import com.pudding.tangentninety.view.activity.MainActivity;
import com.pudding.tangentninety.view.activity.StoryDetailActivity;
import com.pudding.tangentninety.view.adapter.HomeAdapter;
import com.pudding.tangentninety.weight.InsetFrameLayout;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import butterknife.BindView;


/**
 * Created by Error on 2017/6/26 0026.
 */

public class HomeFragment extends BaseFragment<HomePresent> implements HomeContract.View
        , BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.RequestLoadMoreListener
        , BaseQuickAdapter.OnItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TYPE_VALUE = "VALUE";
    private static final String TYPE_POSITION = "POSITION";
    private static final String TYPE_OFFSET = "OFFSET";
    private static final String TYPE_DATE = "DATE";
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    RecyclerView container;
    @BindView(R.id.reflash)
    SwipeRefreshLayout reflash;

    boolean isNoPic;
    Banner banner;
    TextView bannerTitle;

    ArrayList<HomeHotAdapterBean> adapterBeen;
    List<DailyNewListBean.TopStoriesBean> top_stories;
    HomeAdapter adapter;
    LinearLayoutManager layoutManager;
    String lastDay;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_reflash_recyclerview;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapterBeen.size() == 0)
            return;
        int position = layoutManager.findFirstVisibleItemPosition();
        outState.putParcelableArrayList(TYPE_VALUE, adapterBeen);
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        outState.putInt(TYPE_OFFSET, firstVisiableChildView.getTop());
        outState.putInt(TYPE_POSITION, position);
        outState.putString(TYPE_DATE, lastDay);
    }


    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBar(toolbar, getString(R.string.action_home));
        }
        adapterBeen = new ArrayList<>();
        top_stories=new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext());
        container.setLayoutManager(layoutManager);
        isNoPic=mPresenter.isNoPic();
        adapter = new HomeAdapter(adapterBeen, this, isNoPic);
        adapter.bindToRecyclerView(container);
        adapter.setOnItemChildClickListener(this);
        adapter.setOnItemClickListener(this);
        adapter.setOnLoadMoreListener(this, container);
        adapter.disableLoadMoreIfNotFullPage();
        reflash.setOnRefreshListener(this);
        banner= (Banner) LayoutInflater.from(mContext).inflate(R.layout.item_banner_view, container,false);
        bannerTitle= (TextView) banner.findViewById(R.id.story_title);
        banner.setImageLoader(new com.youth.banner.loader.ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImageLoader.load(HomeFragment.this,((DailyNewListBean.TopStoriesBean)path).getImage(),imageView,isNoPic);
            }
        });
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                StoryDetailActivity.startStoryActivity(mActivity,top_stories.get(position).getId());
            }
        });
        banner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                position--;
                if(position>=top_stories.size())
                    position-=top_stories.size();
                else if(position<0)
                    position+=top_stories.size();
                bannerTitle.setText(top_stories.get(position).getTitle());
            }
        });
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(TYPE_VALUE) != null) {
            lastDay = savedInstanceState.getString(TYPE_DATE);
            ArrayList<HomeHotAdapterBean> lists = savedInstanceState.getParcelableArrayList(TYPE_VALUE);
            adapter.addData(lists);
            adapter.setEmptyView(R.layout.layout_empty_view);
            layoutManager.scrollToPositionWithOffset(savedInstanceState.getInt(TYPE_POSITION), savedInstanceState.getInt(TYPE_OFFSET));
        } else {
            onRefresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
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
                    onRefresh();
                }
            });
        }
        adapter.setEmptyView(errorView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NightModeEvent event) {
        Resources.Theme theme = getContext().getTheme();
        TypedValue mainColor = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, mainColor, true);
        appbar.setBackgroundResource(mainColor.resourceId);
        adapter.changeTheme(getContext());
        adapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RecyclerBottomPudding pudding) {
        if (container != null) {
            container.setPadding(container.getPaddingLeft(), container.getPaddingTop(), container.getPaddingRight(), InsetFrameLayout.recyclerBottomPudding);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NoPicEvent event) {
        isNoPic=mPresenter.isNoPic();
        adapter.setNoPicMode(isNoPic);
    }

    @Override
    public void showDailyNewList(DailyNewListBean bean) {
        reflash.setRefreshing(false);
        adapter.setEmptyView(R.layout.layout_empty_view);
        if (bean == null || bean.getStories().size() == 0)
            return;
        if (adapter.getData().size() != 0)
            if (adapter.getData().get(1).getStoryInfoBean().equals(bean.getStories().get(0))) {
                Toast.makeText(mActivity, R.string.already_new, Toast.LENGTH_SHORT).show();
                return;
            }
        lastDay = bean.getDate();
        adapterBeen.clear();
        top_stories.clear();
        top_stories.addAll(bean.getTop_stories());
        banner.setImages(top_stories);
        List<HomeHotAdapterBean> lists = new ArrayList<>();
        lists.add(new HomeHotAdapterBean(getResources().getString(R.string.daily_hot)));
        for (StoryInfoBean story : bean.getStories()) {
            lists.add(new HomeHotAdapterBean(story));
        }
        adapter.addData(lists);
        adapter.setEnableLoadMore(true);
        if(adapter.getHeaderLayoutCount()==0)
        adapter.addHeaderView(banner);
        banner.start();
        banner.startAutoPlay();
    }

    @Override
    public void showDailyBeforeList(DailyListBean bean) {
        lastDay = bean.getDate();
        List<HomeHotAdapterBean> lists = new ArrayList<>();
        lists.add(new HomeHotAdapterBean(SystemUtil.getDateFormat(lastDay)));
        for (StoryInfoBean story : bean.getStories()) {
            lists.add(new HomeHotAdapterBean(story));
        }
        adapter.addData(lists);
        adapter.loadMoreComplete();
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void onItemClick(BaseQuickAdapter quickAdapter, View view, int position) {
        if (adapter.getItemViewType(position+quickAdapter.getHeaderLayoutCount()) == HomeHotAdapterBean.TYPE_BEAN) {
            adapter.getData().get(position).getStoryInfoBean().setHasRead(true);
            adapter.setTitleColorClicked(view);
            StoryDetailActivity.startStoryActivity(mActivity, adapter.getData().get(position).getStoryInfoBean().getId());
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.loadDailyBeforeList(lastDay);

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter baseAdapter, View view, int position) {
        Request r = ImageLoader.loadRound(this, adapter.getData().get(position).getStoryInfoBean().getImages().get(0), (ImageView) view, false);
        if (r.isComplete()) {
            StoryDetailActivity.startStoryActivity(mActivity, adapter.getData().get(position).getStoryInfoBean().getId());
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEmptyView(R.layout.layout_loading_view);
        mPresenter.loadDailyNewList();
        Intent i=new Intent(getActivity(),StoryDetailActivity.class);
        Intent[] p=new Intent[]{i};
        getContext().startActivity(p[0]);
    }

}
