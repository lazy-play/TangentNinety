package com.pudding.tangentninety.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.module.bean.HistoryBean;
import com.pudding.tangentninety.module.bean.StoryCommentsBean;
import com.pudding.tangentninety.utils.ImageLoader;
import com.pudding.tangentninety.weight.ExpandableTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Error on 2017/7/17 0017.
 */

public class CommentAdapter extends BaseQuickAdapter<StoryCommentsBean.CommentsBean, BaseViewHolder> {
    Activity activity;
    Date date;
    SimpleDateFormat sdf;
    int year; ;
    public CommentAdapter(Activity activity) {
        super(R.layout.item_comment_view,new ArrayList<StoryCommentsBean.CommentsBean>());
        date=new Date();
        year=date.getYear();
        sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
        this.activity=activity;
    }
    @Override
    protected void convert(BaseViewHolder helper, StoryCommentsBean.CommentsBean item) {
        date.setTime(item.getTime()*1000l);
        helper.setText(R.id.user_name,item.getAuthor())
                .setText(R.id.good_num,String.valueOf(item.getLikes()))
        .setText(R.id.time,date.getYear()==year?sdf.format(date):new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        ExpandableTextView text=helper.getView(R.id.container);
        text.setText(item.getContent(),helper.getAdapterPosition());
        ImageView icon=helper.getView(R.id.user_icon);
        ImageLoader.loadCircle(activity,item.getAvatar(),icon,false);
        if(item.getReply_to()!=null&&item.getReply_to().getStatus()==0){
            ExpandableTextView textReply=helper.getView(R.id.reply_container);
            textReply.setVisibility(View.VISIBLE);
            SpannableString str=new SpannableString("@"+item.getReply_to().getAuthor()+": "+item.getReply_to().getContent());
            str.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),0,item.getReply_to().getAuthor().length()+2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            textReply.setText(str,helper.getAdapterPosition());
        }else{
            helper.setVisible(R.id.reply_container,false);
        }
    }
}
