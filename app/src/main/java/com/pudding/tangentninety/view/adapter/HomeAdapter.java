package com.pudding.tangentninety.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.util.MultiTypeDelegate;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.module.bean.HomeHotAdapterBean;
import com.pudding.tangentninety.utils.ImageLoader;

import java.util.List;
import java.util.Scanner;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by Error on 2017/7/3 0003.
 */

public class HomeAdapter extends BaseQuickAdapter<HomeHotAdapterBean, BaseViewHolder> {
    private Fragment fragment;
    private int colorTextHotId;
    private int colorCardBackgroundId;
    private int colorTextTitleId;
    private int colorTextTitleClickId;
    private boolean noPic;
    public HomeAdapter(@Nullable List<HomeHotAdapterBean> data, Fragment fragment, boolean noPic) {
        super(data);
        this.fragment = fragment;
        this.noPic=noPic;
        changeTheme(fragment.getContext());
        openLoadAnimation();
        setMultiTypeDelegate(new MultiTypeDelegate<HomeHotAdapterBean>() {
            @Override
            protected int getItemType(HomeHotAdapterBean bean) {
                return bean.getType();
            }
        });
        //Step.2
        getMultiTypeDelegate()
                .registerItemType(HomeHotAdapterBean.TYPE_TITLE, R.layout.item_main_title_view)
                .registerItemType(HomeHotAdapterBean.TYPE_BEAN, R.layout.item_main_view);
    }
public void setNoPicMode(boolean noPic){
    this.noPic=noPic;
}
    public void changeTheme(Context context) {
        Resources.Theme theme = context.getTheme();

        TypedValue colorTextHot = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextHot, colorTextHot, true);
        colorTextHotId = context.getResources().getColor(colorTextHot.resourceId);

        TypedValue colorCardBackground = new TypedValue();
        theme.resolveAttribute(R.attr.colorCardBackground, colorCardBackground, true);
        colorCardBackgroundId = context.getResources().getColor(colorCardBackground.resourceId);

        TypedValue colorTextTitle = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitle, colorTextTitle, true);
        colorTextTitleId = context.getResources().getColor(colorTextTitle.resourceId);

        TypedValue colorTextTitleClick = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitleClick, colorTextTitleClick, true);
        colorTextTitleClickId = context.getResources().getColor(colorTextTitleClick.resourceId);
    }

    public void setTitleColorClicked(View view) {
        TextView text = (TextView) view.findViewById(R.id.title);
        text.setTextColor(colorTextTitleClickId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeHotAdapterBean item) {
        switch (helper.getItemViewType()) {
            case HomeHotAdapterBean.TYPE_TITLE:
                helper.setTextColor(R.id.title, colorTextHotId);
                helper.setText(R.id.title, item.getTitle());
                break;
            case HomeHotAdapterBean.TYPE_BEAN:
                helper.setBackgroundColor(R.id.cardview, colorCardBackgroundId);
                if (item.getStoryInfoBean().isHasRead()) {
                    helper.setTextColor(R.id.title, colorTextTitleClickId);
                } else {
                    helper.setTextColor(R.id.title, colorTextTitleId);
                }
                helper.setText(R.id.title, item.getStoryInfoBean().getTitle());
                ImageView image = helper.getView(R.id.image);
                if(noPic){
                    helper.addOnClickListener(R.id.image);
                }
                ImageLoader.loadRound(fragment, item.getStoryInfoBean().getImages().get(0), image,noPic);
                break;
        }
    }
}
