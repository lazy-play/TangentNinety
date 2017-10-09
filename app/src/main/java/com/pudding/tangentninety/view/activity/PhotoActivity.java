package com.pudding.tangentninety.view.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.transition.Fade;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.chrisbanes.photoview.BuildConfig;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.base.BaseActivity;
import com.pudding.tangentninety.presenter.PhotoPresent;
import com.pudding.tangentninety.presenter.contract.PhotoContract;
import com.pudding.tangentninety.utils.ImageLoader;
import com.pudding.tangentninety.utils.LogUtil;


import butterknife.BindView;

/**
 * Created by Error on 2017/7/13 0013.
 */

public class PhotoActivity extends BaseActivity<PhotoPresent> implements PhotoContract.View {
    private static String IMAGEURL_KEY = "imageUrl";
    @BindView(R.id.photo_view)
    PhotoView photo;
    int backguound;
    String imageUrl;


    public static void startPhotoActivity(Activity activity, String url) {
        if (url.endsWith("jpg") || url.endsWith("png") || url.endsWith("gif")){
        Intent intent = new Intent(activity, PhotoActivity.class);
        intent.putExtra(IMAGEURL_KEY, url);
        activity.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
        }
    }

    @Override
    protected int getLayout() {
        Fade fade = new Fade();
        fade.setDuration(500);
        fade.setStartDelay(100);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);
        return R.layout.activity_large_image;
    }

    @Override
    protected void setDayNightTheme() {
    }


    public void mateImageWidth() {
        RectF imageRect = photo.getDisplayRect();
        float imageW = imageRect.width();
        float photoViewW = photo.getWidth();
        if (imageW != photoViewW) {
            float scale = photoViewW / imageW;
            photo.setScale(scale, imageRect.centerX(), 0, false);
            photo.setMaximumScale(scale*3);
            photo.setMediumScale(scale*2);
            photo.setMinimumScale(scale);
        }
    }

    public void downloadImage() {
        mPresenter.downloadImage(imageUrl);
    }

    public void canSaveImage() {
        photo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.showContextMenu();
                return false;
            }
        });
    }
    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        imageUrl = getIntent().getStringExtra(IMAGEURL_KEY);
        backguound=Color.BLACK;
//        imageUrl="http://d.hiphotos.baidu.com/zhidao/pic/item/a71ea8d3fd1f41348c54a92b251f95cad1c85e07.jpg";//测试白背景
//        imageUrl = "http://wx4.sinaimg.cn/mw1024/6b41c925ly1fhhbi5oki8j20hs1n3n7n.jpg";//测试长图
//        imageUrl="http://wx2.sinaimg.cn/mw690/0069eDEjly1fhcevbbxeng30fs08whe0.gif";//测试动图
        photo.setOnCreateContextMenuListener(this);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });
        photo.setOnMatrixChangeListener(new OnMatrixChangedListener() {
            @Override
            public void onMatrixChanged(RectF rect) {

                if(photo.getScale()<photo.getMinimumScale()){
                    photo.setScale(photo.getMinimumScale(),photo.getWidth()/2f,0,false);
                }
            }
        });
        Glide.with(this).load(imageUrl).diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new GlideDrawableImageViewTarget(photo) {
                    @Override
                    public void onResourceReady(final GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, null);
                        canSaveImage();
                        mateImageWidth();
                    }
                });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_large_image, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            BuildConfig.VERSION_CODE);
                } else {
                    downloadImage();
                }
                break;
            case R.id.change_background:
                backguound=backguound==Color.BLACK?Color.WHITE:Color.BLACK;
                photo.setBackgroundColor(backguound);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case BuildConfig.VERSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadImage();
                } else {
                    Toast.makeText(this, R.string.image_save_failed, Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    public void showResult(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }
}
