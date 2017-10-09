package com.pudding.tangentninety.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.SectionStoryBean;
import com.pudding.tangentninety.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Error on 2017/7/17 0017.
 */

public class SectionDetailAdapter extends BaseQuickAdapter<SectionStoryBean, BaseViewHolder> {
    private Activity activity;
    private boolean isNoPic;
    public SectionDetailAdapter(Activity activity,boolean isNoPic) {
        super(R.layout.item_main_view,new ArrayList<SectionStoryBean>());
        this.activity=activity;
        this.isNoPic=isNoPic;
    }
    @Override
    protected void convert(BaseViewHolder helper, SectionStoryBean item) {
        helper.setText(R.id.title, item.getTitle());
        ImageView image=helper.getView(R.id.image);
        ImageLoader.loadRound(activity,item.getImages().get(0),image,isNoPic);
    }
}
