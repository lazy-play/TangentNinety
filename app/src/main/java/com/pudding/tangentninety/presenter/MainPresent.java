package com.pudding.tangentninety.presenter;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.DataManager;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.MainContract;
import com.pudding.tangentninety.utils.RxUtil;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.ResponseBody;

/**
 * Created by Error on 2017/6/30 0030.
 */

public class MainPresent extends RxPresenter<MainContract.View> implements MainContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MainPresent(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getNetState() {
        mDataManager.getNetFromState()
                .compose(RxUtil.<ResponseBody>rxSchedulerHelper())
                .subscribe(new CommonSubscriber<ResponseBody>(mView){

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String str=responseBody.string();
                            if(str.contains("3G")||str.contains("4G"))
                                mView.setNetState(false);
                            else mView.setNetState(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                            mView.setNetState(false);
                        }
                    }
                });
    }

    @Override
    public boolean isAutoDownload() {
        return mDataManager.getAutoCacheState();
    }
}
