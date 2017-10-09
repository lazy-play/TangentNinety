package com.pudding.tangentninety.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.Target;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutionException;


/**
 * Created by codeest on 2016/8/2.
 */
public class ImageLoader {
    private static final StreamModelLoader<String> cacheOnlyStreamLoader = new StreamModelLoader<String>() {
        @Override
        public DataFetcher<InputStream> getResourceFetcher(final String model, int i, int i1) {
            return new DataFetcher<InputStream>() {
                @Override
                public InputStream loadData(Priority priority) throws Exception {
                    throw new IOException();
                }

                @Override
                public void cleanup() {

                }

                @Override
                public String getId() {
                    return model;
                }

                @Override
                public void cancel() {

                }
            };
        }
    };
public  static File getGlideCache(String url){
    try {
        return Glide.with(App.getInstance())
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    public static void load(Activity activity, String url, ImageView iv,boolean isNopic) {
        if (activity.isDestroyed())
            return;
            WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(activity);
        DrawableTypeRequest request = null;
        if (isNopic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .centerCrop()
                    .into(wr.get());

    }
    public static void load(Context context, String url, ImageView iv,boolean isNopic) {
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(context);
        DrawableTypeRequest request = null;
        if (isNopic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(wr.get());

    }
    public static void load(Fragment fragment, String url, ImageView iv,boolean isNopic) {
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(fragment);
        DrawableTypeRequest request = null;
        if (isNopic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        request.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(wr.get());

    }

    public static Request loadRound(Fragment fragment, String url, ImageView iv, boolean isNoPic) {
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(fragment);
        DrawableTypeRequest request = null;
        if (isNoPic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        return request.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_image)
                .transform(new CenterCrop(fragment.getContext()), new GlideRoundTransform(fragment.getContext(), 3))
                .into(wr.get()).getRequest();
    }
    public static Request loadRound(Activity activity, String url, ImageView iv, boolean isNoPic) {
        if(activity.isDestroyed())
            return null;
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(activity);
        DrawableTypeRequest request = null;
        if (isNoPic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        return request.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_image)
                .transform(new CenterCrop(activity), new GlideRoundTransform(activity, 3))
                .into(wr.get()).getRequest();
    }
    public static Request loadRound(Context context, String url, ImageView iv, boolean isNoPic) {
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(context);
        DrawableTypeRequest request = null;
        if (isNoPic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        return request.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_image)
                .transform(new CenterCrop(context), new GlideRoundTransform(context, 3))
                .into(wr.get()).getRequest();
    }
    public static void loadCircle(Fragment fragment, String url, ImageView iv,boolean isNoPic) {
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(fragment);
        DrawableTypeRequest request = null;
        if (isNoPic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        request.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_image)
                .transform(new CenterCrop(fragment.getContext()), new GlideCircleTransform(fragment.getContext()))
                .into(wr.get());
    }
    public static void loadCircle(Activity activity, String url, ImageView iv,boolean isNoPic) {
        if(activity.isDestroyed())
            return;
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(activity);
        DrawableTypeRequest request = null;
        if (isNoPic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        request.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_image)
                .transform(new CenterCrop(activity), new GlideCircleTransform(activity))
                .into(wr.get());
    }
    public static void loadCircle(Context context, String url, ImageView iv,boolean isNoPic) {
        WeakReference<ImageView> wr = new WeakReference<ImageView>(iv);
        RequestManager rm = Glide.with(context);
        DrawableTypeRequest request = null;
        if (isNoPic) {
            request = rm.using(cacheOnlyStreamLoader)
                    .load(url);
        } else {
            request = rm.load(url);
        }
        request.asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.loading_image)
                .transform(new CenterCrop(context), new GlideCircleTransform(context))
                .into(wr.get());
    }
}

class GlideRoundTransform extends BitmapTransformation {

    private static float radius = 0f;

    public GlideRoundTransform(Context context) {
        this(context, 4);
    }

    public GlideRoundTransform(Context context, int dp) {
        super(context);
        this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private static Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, radius, radius, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}

class GlideCircleTransform extends BitmapTransformation {
    public GlideCircleTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private static Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        // TODO this could be acquired from the pool too
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
