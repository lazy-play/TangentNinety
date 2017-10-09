package com.pudding.tangentninety.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.Target;
import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.app.Constants;

import org.reactivestreams.Publisher;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * Created by Error on 2017/6/22 0022.
 */

public class SystemUtil {
    private static DateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    public static boolean isNetworkAvailable() {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) App.getInstance().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo aNetworkInfo : networkInfo) {
                    // 判断当前网络状态是否为连接状态
                    if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断WIFI是否打开
     *
     * @return
     */
    public static boolean isWifiEnabled() {
        ConnectivityManager mgrConn = (ConnectivityManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判断是否是3G网络
     *
     * @return
     */
    public static boolean is3rd() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断是wifi还是3g网络
     *
     * @return
     */
    public static boolean isWifi() {
        ConnectivityManager cm = (ConnectivityManager) App.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }


    /**
     * 保存文字到剪贴板
     *
     * @param context
     * @param text
     */
    public static void copyToClipBoard(Context context, String text) {
        ClipData clipData = ClipData.newPlainText("url", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, "已复制到剪贴板", Toast.LENGTH_SHORT).show();
    }

    /**
     * 保存图片到本地
     *
     * @param url
     * @param bitmap
     */
    public static Uri saveBitmapToFile(View view, String url, Bitmap bitmap, boolean isShare) {
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        File fileDir = new File(Constants.PATH_IAMGE);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File imageFile = new File(fileDir, fileName);
        final Uri uri = Uri.fromFile(imageFile);
        if (isShare && imageFile.exists()) {
            return uri;
        }
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            boolean isCompress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if (isCompress) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().toastMessage("已保存图片至\n" + uri.getPath());
                    }
                });
            } else {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        App.getInstance().toastMessage("保存失败");
                    }
                });

            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            App.getInstance().toastMessage("保存失败");
        }
        try {
            MediaStore.Images.Media.insertImage(view.getContext().getContentResolver(), imageFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        view.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return uri;
    }

    public static Uri saveBitmapToFile(View view, String url, GifDrawable drawable, boolean isShare) {
        String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        File fileDir = new File(Constants.PATH_IAMGE);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File imageFile = new File(fileDir, fileName);
        final Uri uri = Uri.fromFile(imageFile);
        if (isShare && imageFile.exists()) {
            return uri;
        }
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(drawable.getData());
            view.post(new Runnable() {
                @Override
                public void run() {
                    App.getInstance().toastMessage("已保存图片至\n" + uri.getPath());
                }
            });
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            App.getInstance().toastMessage("保存失败");
        }
        try {
            MediaStore.Images.Media.insertImage(view.getContext().getContentResolver(), imageFile.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        view.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        return uri;
    }


    public static String imageUrlToBase64(String url) {
        StringBuffer string = new StringBuffer();
        byte[] bytes = null;
        try {
            if (url.toLowerCase().endsWith("gif")) {
                string.append("data:image/gif;base64,");
                bytes = Glide.with(App.getInstance())
                        .load(url)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                        .getData();
                string.append(Base64.encodeToString(bytes, Base64.NO_WRAP));
            } else {
                string.append("data:image/png;base64,");
                ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                Glide.with(App.getInstance())
                        .load(url)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get()
                        .compress(Bitmap.CompressFormat.PNG, 100, bStream);
                bStream.flush();
                bStream.close();
                bytes = bStream.toByteArray();
                string.append(Base64.encodeToString(bytes, Base64.NO_WRAP));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string.toString();
    }

    public static void saveImageToFile(String url, ResourceSubscriber<Uri> subscriber) {
        Flowable<Uri> flowable = Flowable.just(url).flatMap(new Function<String, Publisher<Uri>>() {
            @Override
            public Publisher<Uri> apply(@NonNull String url) throws Exception {
                String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
                File fileDir = new File(Constants.PATH_IAMGE);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File imageFile = new File(fileDir, fileName);
                Uri uri = Uri.fromFile(imageFile);
                if (imageFile.exists()) {
                    return Flowable.just(uri);
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(imageFile);
                    if (url.toLowerCase().endsWith("png") || url.toLowerCase().endsWith("jpg"))
                        Glide.with(App.getInstance())
                                .load(url)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get()
                                .compress(Bitmap.CompressFormat.PNG, 100, fos);
                    if (url.toLowerCase().endsWith("gif"))
                        fos.write(Glide.with(App.getInstance())
                                .load(url)
                                .asGif()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .get()
                                .getData());

                } catch (Exception e) {
                    throw e;
                } finally {
                    if (fos != null) {
                        fos.flush();
                        fos.close();
                    }
                }
                MediaStore.Images.Media.insertImage(App.getInstance().getContentResolver(), imageFile.getAbsolutePath(), fileName, null);
                App.getInstance().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                return Flowable.just(uri);
            }
        }).compose(RxUtil.<Uri>rxSchedulerHelper());
        if (subscriber != null)
            flowable.subscribe(subscriber);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 屏幕截图
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight());
        view.destroyDrawingCache();
        return b;
    }

    public static String getDateFormat(String date) {
        Date utilDate = null;
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            utilDate = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        DateFormat format = new SimpleDateFormat("M月d日 EEEE");
        return format.format(utilDate);
    }

    public static void writeToLocaleFile(String sb) {
        // 存储日志
        try {
            File dir = new File(Constants.PARH_LOG);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File fos = new File(getActionLog("bug"));
            BufferedWriter buffer = new BufferedWriter(
                    new FileWriter(fos, true));
            buffer.write(sb);
            buffer.write("\r\n");
            buffer.newLine();
            buffer.flush();
            buffer.close();
        } catch (Exception e) {
        }
    }
    private static String getActionLog(String bno) {
        String time = formatter.format(new Date());
        return Constants.PARH_LOG +bno+"Log"+time+".txt";
    }
}