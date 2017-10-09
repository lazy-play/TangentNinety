package com.pudding.tangentninety.module.prefs;

/**
 * Created by Error on 2017/6/22 0022.
 */
public interface PreferencesHelper {

    boolean getNightModeState();

    void setNightModeState(boolean state);

    boolean getNoImageState();

    void setNoImageState(boolean state);

    boolean getAutoCacheState();

    void setAutoCacheState(boolean state);

    boolean getBigFontState();

    void setBigFontState(boolean state);

}