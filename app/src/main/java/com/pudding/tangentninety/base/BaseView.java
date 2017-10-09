package com.pudding.tangentninety.base;

/**
 * Created by Error on 2017/6/22 0022.
 */

public interface BaseView {

    void showErrorMsg(String msg);

    //=======  State  =======
    void stateError();

    void stateEmpty();

    void stateLoading();

    void stateMain();
}
