package com.pudding.tangentninety.component;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;
import com.pudding.tangentninety.utils.CrashHandler;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public InitializeService() {
        super("InitializeService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }
    private void initApplication() {
        //初始化日志
        Logger.init(getPackageName()).hideThreadInfo();
        CrashHandler.getInstance(getApplicationContext());
    }
}
