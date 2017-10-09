package com.pudding.tangentninety.module.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.app.Constants;

import javax.inject.Inject;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class ImplPreferencesHelper implements PreferencesHelper {

    private static final boolean DEFAULT_NIGHT_MODE = false;
    private static final boolean DEFAULT_NO_IMAGE = false;
    private static final boolean DEFAULT_AUTO_SAVE = true;
    private static final boolean DEFAULT_BIG_FONT = false;

    private static final String SHAREDPREFERENCES_NAME = "my_sp";

    private final SharedPreferences mSPrefs;

    @Inject
    public ImplPreferencesHelper() {
        App app=App.getInstance();

        mSPrefs = app.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean getNightModeState() {
        return mSPrefs.getBoolean(Constants.SP_NIGHT_MODE, DEFAULT_NIGHT_MODE);
    }

    @Override
    public void setNightModeState(boolean state) {
        mSPrefs.edit().putBoolean(Constants.SP_NIGHT_MODE, state).apply();
    }

    @Override
    public boolean getNoImageState() {
        return mSPrefs.getBoolean(Constants.SP_NO_IMAGE, DEFAULT_NO_IMAGE);
    }

    @Override
    public void setNoImageState(boolean state) {
        mSPrefs.edit().putBoolean(Constants.SP_NO_IMAGE, state).apply();
    }

    @Override
    public boolean getAutoCacheState() {
        return mSPrefs.getBoolean(Constants.SP_AUTO_CACHE, DEFAULT_AUTO_SAVE);
    }

    @Override
    public void setAutoCacheState(boolean state) {
        mSPrefs.edit().putBoolean(Constants.SP_AUTO_CACHE, state).apply();
    }

    @Override
    public boolean getBigFontState() {
        return mSPrefs.getBoolean(Constants.SP_BIG_FONT, DEFAULT_BIG_FONT);
    }

    @Override
    public void setBigFontState(boolean state) {
        mSPrefs.edit().putBoolean(Constants.SP_BIG_FONT, state).apply();
    }

}