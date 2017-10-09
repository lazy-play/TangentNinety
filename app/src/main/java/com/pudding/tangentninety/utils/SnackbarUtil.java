package com.pudding.tangentninety.utils;

import android.support.design.widget.Snackbar;
import android.view.View;


/**
 * Created by Error on 2017/6/22 0022.
 */

public class SnackbarUtil {

    public static void show(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }

    public static void showShort(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }
}
