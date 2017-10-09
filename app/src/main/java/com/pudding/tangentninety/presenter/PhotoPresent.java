package com.pudding.tangentninety.presenter;


import android.net.Uri;

import com.pudding.tangentninety.base.RxPresenter;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.contract.PhotoContract;
import com.pudding.tangentninety.utils.SystemUtil;

import javax.inject.Inject;

/**
 * Created by Error on 2017/7/3 0003.
 */

public class PhotoPresent extends RxPresenter<PhotoContract.View> implements PhotoContract.Presenter {
    @Inject
    public PhotoPresent() {
    }

    @Override
    public void downloadImage(String url) {
        SystemUtil.saveImageToFile(url, new CommonSubscriber<Uri>(mView) {
            @Override
            public void onNext(Uri uri) {
                if(mView!=null)
                mView.showResult("已保存图片至\n" + uri.getPath());
            }

            @Override
            public void onError(Throwable e) {
                if(mView!=null)
                mView.showResult("保存失败");
            }
        });
    }
}
