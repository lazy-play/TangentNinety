/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pudding.tangentninety.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.view.ActionProvider;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pudding.tangentninety.R;

/**
 * Created on 2016/7/13.
 *
 * @author Yan Zhenjie.
 */
public abstract class BadgeActionProvider extends ActionProvider implements View.OnClickListener,View.OnLongClickListener{

    private ImageView mIvIcon;
    private TextView mTvBadge;
    private View view;
    private Toast toast;
    private View.OnClickListener onClickListener;
    protected String title;

    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public BadgeActionProvider(Context context) {
        super(context);
    }

    @Override
    public View onCreateActionView() {
        int size = getContext().getResources().getDimensionPixelSize(android.support.design.R.dimen.abc_action_bar_stacked_max_height);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, size);
        view = LayoutInflater.from(getContext()).inflate(getLayout(), null, false);
        view.setLayoutParams(layoutParams);
        mIvIcon = (ImageView) view.findViewById(R.id.iv_icon);
        mTvBadge = (TextView) view.findViewById(R.id.tv_badge);
        mIvIcon.setOnClickListener(this);
        mIvIcon.setOnLongClickListener(this);
        if(title!=null){
        toast=Toast.makeText(getContext(), title, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.RIGHT|Gravity.TOP,0,size);
        }
        return view;
    }

    protected abstract int getLayout();
    @Override
    public void onClick(View v) {
        if (onClickListener != null)
            onClickListener.onClick(v);
    }
    @Override
    public boolean onLongClick(View v) {
        if(toast!=null)
        toast.show();
        return true;
    }
    /**
     * 设置点击监听。
     *
     * @param onClickListener listener。
     */
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /**
     * 设置图标。
     *
     * @param icon drawable 或者mipmap中的id。
     */
    public void setIcon(@DrawableRes int icon) {
        mIvIcon.setImageResource(icon);
    }

    public void setIcon(Drawable icon) {
        mIvIcon.setImageDrawable(icon);
    }

    /**
     * 设置显示的数字。
     *
     * @param i 数字。
     */
    public void setBadge(int i) {
        if (i > 999)
            mTvBadge.setText("999+");
        else
            mTvBadge.setText(Integer.toString(i));
    }

    /**
     * 设置文字。
     *
     * @param i string.xml中的id。
     */
    public void setTextInt(@StringRes int i) {
        mTvBadge.setText(i);
    }

    /**
     * 设置显示的文字。
     */
//    public void setText(CharSequence i) {
//        mTvBadge.setText(i);
//    }
    public View getView() {
        return view;
    }
}