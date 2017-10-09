package com.pudding.tangentninety.di.component;

import android.app.Activity;

import com.pudding.tangentninety.di.module.ActivityModule;
import com.pudding.tangentninety.di.scope.ActivityScope;
import com.pudding.tangentninety.view.activity.MainActivity;
import com.pudding.tangentninety.view.activity.PhotoActivity;
import com.pudding.tangentninety.view.activity.SectionDetailActivity;
import com.pudding.tangentninety.view.activity.SplashActivity;
import com.pudding.tangentninety.view.activity.StoryCommentActivity;
import com.pudding.tangentninety.view.activity.StoryDetailActivity;

import dagger.Component;

/**
 * Created by Error on 2017/6/22 0022.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    Activity getActivity();

    void inject(MainActivity mainActivity);

    void inject(StoryDetailActivity storyDetailActivity);

    void inject(PhotoActivity photoActivity);

    void inject(StoryCommentActivity storyCommentActivity);

    void inject(SectionDetailActivity sectionDetailActivity);
//    void inject(MainActivity mainActivity);
//
//    void inject(ZhihuDetailActivity zhihuDetailActivity);

}
