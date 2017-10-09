package com.pudding.tangentninety.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.bean.SectionListBean;
import com.pudding.tangentninety.utils.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Error on 2017/7/17 0017.
 */

public class SectionAdapter extends BaseQuickAdapter<SectionListBean.DataBean, BaseViewHolder> {
    private Fragment fragment;
    private int colorCardBackgroundId;
    private int colorTextTitleId;
    public SectionAdapter(Fragment fragment) {
        super(R.layout.item_section_view,new ArrayList<SectionListBean.DataBean>());
        this.fragment=fragment;
        changeTheme(fragment.getContext());
    }
    public void changeTheme(Context context){
        Resources.Theme theme = context.getTheme();

        TypedValue colorCardBackground = new TypedValue();
        theme.resolveAttribute(R.attr.colorCardBackground, colorCardBackground, true);
        colorCardBackgroundId = context.getResources().getColor(colorCardBackground.resourceId);

        TypedValue colorTextTitle = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitle, colorTextTitle, true);
        colorTextTitleId = context.getResources().getColor(colorTextTitle.resourceId);
    }
    @Override
    protected void convert(BaseViewHolder helper,SectionListBean.DataBean item) {
        CardView cardView=helper.getView(R.id.card);
        cardView.setCardBackgroundColor(colorCardBackgroundId);
        helper.setTextColor(R.id.title,colorTextTitleId);
        helper.setText(R.id.title, item.getName());
        ImageView image=helper.getView(R.id.image);
        ImageLoader.load(fragment,item.getThumbnail(),image,false);
    }
}
