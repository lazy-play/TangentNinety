package com.pudding.tangentninety.base;

/**
 * Created by Error on 2017/6/22 0022.
 */

public interface BasePresenter<T extends BaseView>{

    void attachView(T view);

    void detachView();
}
