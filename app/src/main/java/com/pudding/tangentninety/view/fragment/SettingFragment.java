package com.pudding.tangentninety.view.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.BaseFragment;
import com.pudding.tangentninety.base.SimpleFragment;
import com.pudding.tangentninety.module.event.AutoDownloadModeChange;
import com.pudding.tangentninety.module.event.NightModeEvent;
import com.pudding.tangentninety.module.event.NoPicEvent;
import com.pudding.tangentninety.presenter.SettingPresent;
import com.pudding.tangentninety.presenter.contract.SettingContract;
import com.pudding.tangentninety.utils.GlideCacheUtil;
import com.pudding.tangentninety.view.activity.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;

/**
 * Created by Error on 2017/6/27 0027.
 */

public class SettingFragment extends BaseFragment<SettingPresent> implements SettingContract.View, View.OnClickListener {
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.title_normal)
    TextView title_normal;
    @BindView(R.id.title_other)
    TextView title_other;

    @BindView(R.id.clear_cache_title)
    TextView clear_cache_title;
    @BindView(R.id.download_title)
    TextView download_title;
    @BindView(R.id.nopic_title)
    TextView nopic_title;
    @BindView(R.id.big_font_title)
    TextView big_font_title;

    @BindView(R.id.download_subtitle)
    TextView download_subtitle;
    @BindView(R.id.nopic_subtitle)
    TextView nopic_subtitle;
    @BindView(R.id.clear_cache_subtitle)
    TextView clear_cache_subtitle;

    @BindView(R.id.download_check)
    CheckBox download_check;
    @BindView(R.id.nopic_check)
    CheckBox nopic_check;
    @BindView(R.id.big_font_check)
    CheckBox big_font_check;

    @BindView(R.id.download)
    View download;
    @BindView(R.id.nopic)
    View nopic;
    @BindView(R.id.big_font)
    View big_font;
    @BindView(R.id.clear_cache)
    View clear_cache;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        clear_cache_subtitle.setText(mPresenter.getCacheSize());
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setToolBar(toolbar, getString(R.string.action_setting));
        }
        nopic.setOnClickListener(this);
        big_font.setOnClickListener(this);
        download.setOnClickListener(this);
        clear_cache.setOnClickListener(this);
        nopic_check.setOnClickListener(this);
        big_font_check.setOnClickListener(this);
        download_check.setOnClickListener(this);

        nopic_check.setChecked(mPresenter.isNoPic());
        big_font_check.setChecked(mPresenter.isBigFont());
        download_check.setChecked(mPresenter.isAutoCache());
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

        TypedValue menuTitleColor = new TypedValue();
        theme.resolveAttribute(R.attr.colorMenuTitle, menuTitleColor, true);
        title_normal.setTextColor(getResources().getColor(menuTitleColor.resourceId));
        title_other.setTextColor(getResources().getColor(menuTitleColor.resourceId));

        TypedValue titleColor = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitle, titleColor, true);
        clear_cache_title.setTextColor(getResources().getColor(titleColor.resourceId));
        download_title.setTextColor(getResources().getColor(titleColor.resourceId));
        nopic_title.setTextColor(getResources().getColor(titleColor.resourceId));
        big_font_title.setTextColor(getResources().getColor(titleColor.resourceId));

        TypedValue subTitleColor = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitleClick, subTitleColor, true);
        download_subtitle.setTextColor(getResources().getColor(subTitleColor.resourceId));
        nopic_subtitle.setTextColor(getResources().getColor(subTitleColor.resourceId));
        clear_cache_subtitle.setTextColor(getResources().getColor(subTitleColor.resourceId));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.download:
                boolean downloadState=!download_check.isChecked();
                download_check.setChecked(downloadState);
                mPresenter.setAutoCacheState(downloadState);
                EventBus.getDefault().post(new AutoDownloadModeChange(downloadState));
                break;
            case R.id.nopic:
                boolean nopicState = !nopic_check.isChecked();
                nopic_check.setChecked(nopicState);
                mPresenter.setNoPicState(nopicState);
                break;
            case R.id.big_font:
                boolean bigState = !big_font_check.isChecked();
                big_font_check.setChecked(bigState);
                mPresenter.setBigFontState(bigState);
                break;
            case R.id.download_check:
                mPresenter.setAutoCacheState(download_check.isChecked());
                EventBus.getDefault().post(new AutoDownloadModeChange(download_check.isChecked()));
                break;
            case R.id.nopic_check:
                mPresenter.setNoPicState(nopic_check.isChecked());
                break;
            case R.id.big_font_check:
                mPresenter.setBigFontState(big_font_check.isChecked());
                break;
            case R.id.clear_cache:
                mPresenter.clearCache();
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        clear_cache_subtitle.setText(mPresenter.getCacheSize());
                        Toast.makeText(mActivity, "清除成功", Toast.LENGTH_SHORT).show();
                    }
                },500);

                break;

        }
    }
}
