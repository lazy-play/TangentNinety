package com.pudding.tangentninety.view.activity;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pudding.tangentninety.BuildConfig;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.app.Constants;
import com.pudding.tangentninety.base.BaseActivity;
import com.pudding.tangentninety.module.event.AutoDownloadModeChange;
import com.pudding.tangentninety.module.event.DownloadNextEvent;
import com.pudding.tangentninety.module.event.NightModeEvent;
import com.pudding.tangentninety.presenter.MainPresent;
import com.pudding.tangentninety.presenter.contract.MainContract;
import com.pudding.tangentninety.utils.SystemUtil;
import com.pudding.tangentninety.view.fragment.CollectionFragment;
import com.pudding.tangentninety.view.fragment.HistoryFragment;
import com.pudding.tangentninety.view.fragment.HomeFragment;
import com.pudding.tangentninety.view.fragment.SectionFragment;
import com.pudding.tangentninety.view.fragment.SettingFragment;
import com.pudding.tangentninety.view.service.DownloadService;
import com.pudding.tangentninety.weight.InsetRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Error on 2017/6/22 0022.
 */
public class MainActivity extends BaseActivity<MainPresent>
        implements MainContract.View, NavigationView.OnNavigationItemSelectedListener {
    private static String SHOWFRAGMENT_KEY = "showFragment";
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.start_bar_cover)
    InsetRelativeLayout start_bar_cover;

    ViewGroup container;
    Bitmap bitmap_cover;
    ImageView cover;
    RelativeLayout nav_head_view;

    MenuItem downloadItem;

    int showFragment, hideFragment;
    HomeFragment homeFragment;
    SectionFragment sectionFragment;
    CollectionFragment collectionFragment;
    HistoryFragment historyFragment;
    SettingFragment settingFragment;

    Timer timer;
    private boolean isExit;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setTransition() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SHOWFRAGMENT_KEY, showFragment);
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        View headView = navigationView.getHeaderView(0);
        container = (ViewGroup) drawer.getParent();
        nav_head_view = (RelativeLayout) headView.findViewById(R.id.nav_head_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (savedInstanceState != null) {
            showFragment = savedInstanceState.getInt(SHOWFRAGMENT_KEY);
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof HomeFragment) {
                    homeFragment = (HomeFragment) fragment;
                } else if (fragment instanceof SectionFragment) {
                    sectionFragment = (SectionFragment) fragment;
                } else if (fragment instanceof CollectionFragment) {
                    collectionFragment = (CollectionFragment) fragment;
                } else if (fragment instanceof HistoryFragment) {
                    historyFragment = (HistoryFragment) fragment;
                } else if (fragment instanceof SettingFragment) {
                    settingFragment = (SettingFragment) fragment;
                }
            }
        } else {
            showFragment = Constants.TYPE_HOME;
            navigationView.setCheckedItem(R.id.nav_home);
            homeFragment = new HomeFragment();
            sectionFragment = new SectionFragment();
            collectionFragment = new CollectionFragment();
            historyFragment = new HistoryFragment();
            settingFragment = new SettingFragment();
            loadMultipleRootFragment(R.id.main_fragment, 0, homeFragment, sectionFragment, collectionFragment, historyFragment, settingFragment);
            if (mPresenter.isAutoDownload())
                waitToDownload();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DownloadNextEvent event) {
        if (downloadItem == null)
            return;
        if (event.isFinish()) {
            if (event.getPresent() == 100){
                downloadItem.setTitle(R.string.download_success);
            Toast.makeText(this, R.string.download_success, Toast.LENGTH_SHORT).show();
            } else{
                downloadItem.setTitle(R.string.download_failed);
                Toast.makeText(this, R.string.download_failed, Toast.LENGTH_SHORT).show();
            }
            downloadItem = null;
        } else {
            downloadItem.setTitle(Integer.toString(event.getPresent()) + "%");
        }
    }

    public void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                (Activity) toolbar.getContext(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public void onBackPressedSupport() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (showFragment != Constants.TYPE_HOME) {
            navigationView.setCheckedItem(R.id.nav_home);
            hideFragment = showFragment;
            showFragment = Constants.TYPE_HOME;
            showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
        } else {
            exitByDoubleClick();
        }
    }

    private void exitByDoubleClick() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            Toast.makeText(MainActivity.this, R.string.double_exit, Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            App.getInstance().exitApp();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        boolean shouleClose = false;
        switch (item.getItemId()) {
            case R.id.nav_home:
                shouleClose = true;
                hideFragment = showFragment;
                showFragment = Constants.TYPE_HOME;
                break;
            case R.id.nav_section:
                shouleClose = true;
                hideFragment = showFragment;
                showFragment = Constants.TYPE_SECTION;
                break;
            case R.id.nav_collection:
                shouleClose = true;
                hideFragment = showFragment;
                showFragment = Constants.TYPE_COLLECTION;
                break;
            case R.id.nav_history:
                shouleClose = true;
                hideFragment = showFragment;
                showFragment = Constants.TYPE_HISTORY;
                break;
            case R.id.nav_setting:
                shouleClose = true;
                hideFragment = showFragment;
                showFragment = Constants.TYPE_SETTING;
                break;
            case R.id.nav_download:
                downloadItem = item;
                DownloadService.start(this);
                break;
            case R.id.nav_theme:
                changeTheme();
                break;
            case R.id.nav_about_me:
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle(R.string.action_about_me)
                        .setMessage("作者：晓尐仆町\n版本：V" + BuildConfig.VERSION_NAME + "\n简介：\n这个人很懒，什么都没有写。。\n\n彩蛋？不存在的。")
                        .setPositiveButton(R.string.sure, null).show();
                break;
        }
        if (shouleClose)
            drawer.closeDrawer(GravityCompat.START);
        if (hideFragment != showFragment)
            showHideFragment(getTargetFragment(showFragment), getTargetFragment(hideFragment));
        return true;
    }

    private SupportFragment getTargetFragment(int item) {
        switch (item) {
            case Constants.TYPE_HOME:
                return homeFragment;
            case Constants.TYPE_SECTION:
                return sectionFragment;
            case Constants.TYPE_COLLECTION:
                return collectionFragment;
            case Constants.TYPE_HISTORY:
                return historyFragment;
            case Constants.TYPE_SETTING:
                return settingFragment;
        }
        return homeFragment;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(AutoDownloadModeChange isOpen) {
        if (isOpen.isOpen()) {
            waitToDownload();
        } else {
            cancelTimer();
        }
    }

    private void cancelTimer() {
        if (timer == null)
            return;
        timer.cancel();
        timer = null;
    }

    private void waitToDownload() {
        if (timer != null)
            return;

        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(isDestroyed())
                    return;
                if (DownloadService.DOWNLOAD_SUCCESS)
                    return;
                if (!SystemUtil.isNetworkAvailable() || !SystemUtil.isWifi())
                    return;
                mPresenter.getNetState();
            }
        };
        timer.schedule(task, 60000);
    }

    public void changeTheme() {
        ViewGroup container = (ViewGroup) findViewById(R.id.drawer_layout).getParent();
        if (cover != null) {
            container.removeView(cover);
            cover = null;
        }
        if (bitmap_cover != null) {
            bitmap_cover.recycle();
            bitmap_cover = null;
        }
        cover = new ImageView(this);
        cover.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        bitmap_cover = SystemUtil.takeScreenShot(this);
        cover.setImageBitmap(bitmap_cover);
        container.addView(cover);
        ViewCompat.animate(cover).alpha(0).setDuration(300);

        cover.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewGroup container = (ViewGroup) findViewById(R.id.drawer_layout).getParent();
                if (cover != null) {
                    container.removeView(cover);
                    cover = null;
                }
                if (bitmap_cover != null) {
                    bitmap_cover.recycle();
                    bitmap_cover = null;
                }
            }
        }, 300);
        boolean isNightModeState = !App.getAppComponent().preferencesHelper().getNightModeState();
        App.getAppComponent().preferencesHelper().setNightModeState(isNightModeState);

        setDayNightTheme();
        Resources.Theme theme = getTheme();

        //切换背景色
        TypedValue colorBackground = new TypedValue();
        theme.resolveAttribute(R.attr.colorBackground, colorBackground, true);
        drawer.setBackgroundResource(colorBackground.resourceId);

        //切换主题颜色
        TypedValue mainColor = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, mainColor, true);
        nav_head_view.setBackgroundResource(mainColor.resourceId);
        if (start_bar_cover.getCover() != null)
            start_bar_cover.getCover().setBackgroundResource(mainColor.resourceId);

        //切换侧边栏字体颜色
        TypedValue colorNavigation = new TypedValue();
        theme.resolveAttribute(R.attr.colorNavigation, colorNavigation, true);
        navigationView.setItemIconTintList(getResources().getColorStateList(colorNavigation.resourceId));
        navigationView.setItemTextColor(getResources().getColorStateList(colorNavigation.resourceId));

        //切换侧边栏背景颜色
        TypedValue colorUnselectBackground = new TypedValue();
        theme.resolveAttribute(R.attr.colorUnselectBackground, colorUnselectBackground, true);
        navigationView.setBackgroundResource(colorUnselectBackground.resourceId);

        //通知其他界面切换颜色
        NightModeEvent nightModeEvent = new NightModeEvent();
        nightModeEvent.setNightMode(isNightModeState);
        EventBus.getDefault().post(nightModeEvent);
    }

    @Override
    public void setNetState(boolean isWifi) {
        if (isWifi)
            DownloadService.start(this);
    }
}
