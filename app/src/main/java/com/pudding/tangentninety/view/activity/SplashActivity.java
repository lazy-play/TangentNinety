package com.pudding.tangentninety.view.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.mcxtzhang.pathanimlib.PathAnimView;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.base.SimpleActivity;
import com.pudding.tangentninety.presenter.contract.DetailContract;
import com.pudding.tangentninety.utils.SvgPathParser;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class SplashActivity extends SimpleActivity {
    @BindView(R.id.pathAnimView1)
    PathAnimView pathAnimView1;
    @BindView(R.id.pathAnimView2)
    PathAnimView pathAnimView2;
    @BindView(R.id.pathAnimView3)
    PathAnimView pathAnimView3;
    @BindView(R.id.logo_view)
    ImageView logo_view;

    boolean onstop, animOver, hasStart;
    private static final int START_TIME = 500;
    private static final int PATH_TIME = 1500;
    private static final int BACKGROUND_TIME = 1500;
    private static final int TOTAL_TIME = START_TIME + PATH_TIME + BACKGROUND_TIME;

    @Override
    protected void onStop() {
        super.onStop();
        onstop = true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (onstop) {
            onstop = false;
            startActivity();
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    protected void setTransition() {
        Slide slide = new Slide();
        slide.setDuration(1000);
        slide.setSlideEdge(Gravity.LEFT);
        getWindow().setExitTransition(slide);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        logo_view.measure(0, 0);
        float width = logo_view.getMeasuredWidth();
        float size = width / 25000f;
        SvgPathParser svgPathParser = new SvgPathParser(size);
        try {
            Path path1 = svgPathParser.parsePath("M825,18740 L9355,3225 L13673,3127 L7068,16081 L15393,15815 M15393,15815 Z");
            Path path2 = svgPathParser.parsePath("M12346,11843 L17134,18084 L825,18740 L2475,21873 L23584,20989 M23584,20989 Z");
            Path path3 = svgPathParser.parsePath("M13673,3127 L24175,16908 L23584,20989 L13757,9157 L10171,15982 M10171,15982 Z");
            pathAnimView1.setColorBg(Color.TRANSPARENT);
            pathAnimView1.setColorFg(Color.WHITE);
            pathAnimView1.setSourcePath(path1);
            pathAnimView2.setColorBg(Color.TRANSPARENT);
            pathAnimView2.setColorFg(Color.WHITE);
            pathAnimView2.setSourcePath(path2);
            pathAnimView3.setColorBg(Color.TRANSPARENT);
            pathAnimView3.setColorFg(Color.WHITE);
            pathAnimView3.setSourcePath(path3);
            logo_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    pathAnimView1.setAnimTime(PATH_TIME).setAnimInfinite(false).startAnim();
                    pathAnimView2.setAnimTime(PATH_TIME).setAnimInfinite(false).startAnim();
                    pathAnimView3.setAnimTime(PATH_TIME).setAnimInfinite(false).startAnim();
                }
            }, START_TIME);
            logo_view.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f).setDuration(BACKGROUND_TIME);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            float value = (Float) animation.getAnimatedValue();
                            logo_view.setAlpha(1 - value);
                            pathAnimView1.setAlpha(value);
                            pathAnimView2.setAlpha(value);
                            pathAnimView3.setAlpha(value);
                        }
                    });
                    animator.addListener(new AnimatorListenerAdapter() {

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animOver = true;
                            startActivity();
                            super.onAnimationEnd(animation);
                        }
                    });
                    animator.start();
                    boolean isnightMode = App.getAppComponent().preferencesHelper().getNightModeState();
                    ValueAnimator colorAnim = ObjectAnimator.ofInt((View) logo_view.getParent(), "backgroundColor", Color.BLACK, getResources().getColor(isnightMode ? R.color.colorMain_Night : R.color.colorMain))
                            .setDuration(BACKGROUND_TIME);
                    colorAnim.setEvaluator(new ArgbEvaluator());
                    colorAnim.start();
                }
            }, START_TIME + PATH_TIME - 500);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void startActivity() {
        if (onstop)
            return;
        if (!animOver)
            return;
        if (hasStart)
            return;
        hasStart = true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
        finish();
        overridePendingTransition(R.anim.slide_right_in,R.anim.slide_left_out);
//        startActivity(new Intent(SplashActivity.this, MainActivity.class), ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
//        finishAfterTransition();
//        logo_view.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ((View) logo_view.getParent()).setVisibility(View.GONE);
//            }
//        }, 1000);
    }

    @Override
    public void onBackPressedSupport() {
        exitByDoubleClick();
    }

    private boolean isExit;

    private void exitByDoubleClick() {
        Timer tExit = null;
        if (!isExit) {
            isExit = true;
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;//取消退出
                }
            }, 2000);// 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            App.getInstance().exitApp();
        }
    }
}