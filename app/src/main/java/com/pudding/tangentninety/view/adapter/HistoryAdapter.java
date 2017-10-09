package com.pudding.tangentninety.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.TypedValue;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.module.bean.HistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Error on 2017/7/17 0017.
 */

public class HistoryAdapter extends BaseQuickAdapter<HistoryBean, BaseViewHolder> {
    private int colorTextTitleId;
    public HistoryAdapter(Context context) {
        super(R.layout.item_history_view,new ArrayList<HistoryBean>());
        changeTheme(context);
    }
    public void changeTheme(Context context){
        Resources.Theme theme=context.getTheme();
        TypedValue colorTextTitleClick = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitleClick, colorTextTitleClick, true);
        colorTextTitleId=context.getResources().getColor(colorTextTitleClick.resourceId);
    }
    @Override
    protected void convert(BaseViewHolder helper, HistoryBean item) {
        helper.setTextColor(R.id.title,colorTextTitleId);
        helper.setText(R.id.title, item.getTitle());
    }
}
