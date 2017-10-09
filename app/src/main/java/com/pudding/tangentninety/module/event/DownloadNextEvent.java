package com.pudding.tangentninety.module.event;

/**
 * Created by Error on 2017/7/26 0026.
 */

public class DownloadNextEvent {
    int present;
    boolean isFinish;
    public DownloadNextEvent(int now,int total){
        present=now*100/total;
    }
public DownloadNextEvent(boolean isSuccess){
    isFinish=true;
    present=isSuccess?100:0;
}
    public int getPresent() {
        return present;
    }

    public boolean isFinish() {
        return isFinish;
    }
}
