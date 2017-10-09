package com.pudding.tangentninety.weight;

import android.content.Context;

import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.BadgeActionProvider;

/**
 * Created by Error on 2017/7/12 0012.
 */

public class CommentMenuBadge extends BadgeActionProvider{
    /**
     * Creates a new instance.
     *
     * @param context Context for accessing resources.
     */
    public CommentMenuBadge(Context context) {
        super(context);
        title=getContext().getString(R.string.comments_num);
    }

    @Override
    protected int getLayout() {
        return R.layout.badge_provider_comment;
    }
}
