package com.pudding.tangentninety.di.component;

import android.app.Activity;

import com.pudding.tangentninety.di.module.FragmentModule;
import com.pudding.tangentninety.di.scope.FragmentScope;
import com.pudding.tangentninety.view.fragment.CollectionFragment;
import com.pudding.tangentninety.view.fragment.HistoryFragment;
import com.pudding.tangentninety.view.fragment.HomeFragment;
import com.pudding.tangentninety.view.fragment.SectionFragment;
import com.pudding.tangentninety.view.fragment.SettingFragment;

import dagger.Component;

/**
 * Created by Error on 2017/6/22 0022.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    Activity getActivity();

    void inject(HomeFragment homeFragment);
    void inject(HistoryFragment historyFragment);
    void inject(CollectionFragment collectionFragment);
    void inject(SettingFragment settingFragment);
    void inject(SectionFragment sectionFragment);
}

