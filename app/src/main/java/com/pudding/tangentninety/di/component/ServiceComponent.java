package com.pudding.tangentninety.di.component;

import android.app.Activity;
import android.app.Service;

import com.pudding.tangentninety.di.module.ActivityModule;
import com.pudding.tangentninety.di.module.ServiceModule;
import com.pudding.tangentninety.di.scope.ActivityScope;
import com.pudding.tangentninety.di.scope.ServiceScope;
import com.pudding.tangentninety.view.activity.PhotoActivity;
import com.pudding.tangentninety.view.activity.StoryCommentActivity;
import com.pudding.tangentninety.view.activity.StoryDetailActivity;
import com.pudding.tangentninety.view.service.DownloadService;

import dagger.Component;

/**
 * Created by Error on 2017/6/22 0022.
 */

@ServiceScope
@Component(dependencies = AppComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

    Service getService();

    void inject(DownloadService downloadService);

}
