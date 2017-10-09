package com.pudding.tangentninety.view.adapter;

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
import com.pudding.tangentninety.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Error on 2017/7/17 0017.
 */

public class CollectionAdapter extends BaseQuickAdapter<CollectionStoryBean, BaseViewHolder> {
    private Fragment fragment;
    private int colorTextTitleId;
    private int colorCardBackgroundId;
    private boolean isEdit;

    private List<Integer> waitToDelete;
    public CollectionAdapter(Fragment fragment) {
        super(R.layout.item_main_view,new ArrayList<CollectionStoryBean>());
        this.fragment=fragment;
        changeTheme(fragment.getContext());
    }
    public void changeTheme(Context context){
        Resources.Theme theme=context.getTheme();
        TypedValue colorTextTitle = new TypedValue();
        theme.resolveAttribute(R.attr.colorTextTitle, colorTextTitle, true);
        colorTextTitleId=context.getResources().getColor(colorTextTitle.resourceId);

        TypedValue colorCardBackground = new TypedValue();
        theme.resolveAttribute(R.attr.colorCardBackground, colorCardBackground, true);
        colorCardBackgroundId = context.getResources().getColor(colorCardBackground.resourceId);
    }
    @Override
    protected void convert(BaseViewHolder helper, CollectionStoryBean item) {
        if(isEdit){
            helper.setVisible(R.id.checkBox,true)
                    .setChecked(R.id.checkBox,item.isChecked())
            .addOnClickListener(R.id.checkBox);
        }else{
            helper.setVisible(R.id.checkBox,false);
        }
        helper.setBackgroundColor(R.id.cardview, colorCardBackgroundId);
        helper.setTextColor(R.id.title,colorTextTitleId)
        .setText(R.id.title, item.getTitle());
        ImageView image=helper.getView(R.id.image);
        ImageLoader.load(fragment,item.getImage(),image,false);
    }
    public boolean isEditMode(){
        return isEdit;
    }
    public void openEditMode(){
        isEdit=true;
        notifyDataSetChanged();
        waitToDelete=new ArrayList<>();
    }
    public void checkItem(int position){
        checkItem(position,null);
    }
    public void checkItem(int position,View view){
        boolean check=!getData().get(position).isChecked();
        if(check){
            waitToDelete.add(getData().get(position).getId());
        }else{
            waitToDelete.remove(Integer.valueOf(getData().get(position).getId()));
        }
        getData().get(position).setChecked(check);
        if(view!=null){
            ((CheckBox)view.findViewById(R.id.checkBox)).setChecked(check);
        }
    }
    public List<Integer> getWaitToDelete(){
        return waitToDelete;
    }
    public void closeEditMode(){
        isEdit=false;
        for(CollectionStoryBean bean:getData()){
            bean.setChecked(false);
        }
        waitToDelete=null;
        notifyDataSetChanged();
    }
}
