package com.pudding.tangentninety.module.event;

/**
 * Created by Error on 2017/8/7 0007.
 */

public class AutoDownloadModeChange {
    boolean isOpen;
    public AutoDownloadModeChange(boolean isOpen){
        this.isOpen=isOpen;
    }

    public boolean isOpen() {
        return isOpen;
    }
}
