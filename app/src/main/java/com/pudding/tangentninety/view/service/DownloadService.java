package com.pudding.tangentninety.view.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.pudding.tangentninety.base.BaseService;
import com.pudding.tangentninety.module.bean.DailyListBean;
import com.pudding.tangentninety.module.bean.DailyNewListBean;
import com.pudding.tangentninety.module.bean.StoryInfoBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;
import com.pudding.tangentninety.module.event.DownloadNextEvent;
import com.pudding.tangentninety.module.http.CommonSubscriber;
import com.pudding.tangentninety.presenter.DownloadPresent;
import com.pudding.tangentninety.presenter.contract.DownloadContract;
import com.pudding.tangentninety.utils.HtmlUtil;
import com.pudding.tangentninety.utils.ImageLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

/**
 * Created by Error on 2017/7/26 0026.
 */

public class DownloadService extends BaseService<DownloadPresent> implements DownloadContract.View {
    private static int MAX_DAY = 2;//缓存天数
public static boolean DOWNLOAD_SUCCESS;
    List<DailyNewListBean.TopStoriesBean> topStorys;
    List<StoryInfoBean> storys;
    WebView  webView;
    int lastDay;
    int progress;
    int progressNow;
    public static void start(Activity activity) {
        activity.startService(new Intent(activity, DownloadService.class));
    }

    @Override
    public void showDetail(ZhihuDetailBean zhihuDetailBean) {
        loadImage(zhihuDetailBean.getImage());
        String htmlData = HtmlUtil.createHtmlData(zhihuDetailBean.getBody());
        webView.loadDataWithBaseURL(null, htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING, null);
//        webView.loadUrl(zhihuDetailBean.getShare_url());
    }

    @Override
    public void showDailyNewList(DailyNewListBean bean) {
        for (DailyNewListBean.TopStoriesBean b:bean.getTop_stories()){
            if(!mPresenter.isStoryDownload(b.getId()))
                topStorys.add(b);
        }
        if(topStorys.size()==0)
        topStorys = null;
        for(StoryInfoBean b:bean.getStories()){
            if(!mPresenter.isStoryDownload(b.getId()))
                storys.add(b);
        }
        lastDay++;
        mPresenter.loadDailyBeforeList(bean.getDate());
    }
    private void loadImage(String url) {
        mPresenter.loadImageUrl(url);
    }

    @Override
    public void showDailyBeforeList(DailyListBean bean) {
        if (lastDay < MAX_DAY) {
            for(StoryInfoBean b:bean.getStories()){
                if(!mPresenter.isStoryDownload(b.getId()))
                    storys.add(b);
            }
            lastDay++;
            mPresenter.loadDailyBeforeList(bean.getDate());
        } else {

            progress=(topStorys==null?0:topStorys.size())+storys.size();
            if(storys.size()==0){
                storys=null;
            }
            loadStory();
        }
    }
private void loadNextStory(){
    if(topStorys != null){
        mPresenter.addDownloadStory(topStorys.get(0).getId());
        topStorys.remove(0);
        if (topStorys.size() == 0)
            topStorys = null;
    }else if (storys != null){
        mPresenter.addDownloadStory(storys.get(0).getId());
        storys.remove(0);
        if (storys.size() == 0)
            storys = null;
    }
    loadStory();
}
    private void loadStory() {
        if (topStorys != null) {
            loadImage(topStorys.get(0).getImage());
            mPresenter.getStory(topStorys.get(0).getId());
        } else if (storys != null) {
            loadImage(storys.get(0).getImages().get(0));
            mPresenter.getStory(storys.get(0).getId());
        } else {
            EventBus.getDefault().post(new DownloadNextEvent(true));
            DOWNLOAD_SUCCESS=true;
            System.out.println("离线下载成功");
//            Toast.makeText(this, "离线下载成功", Toast.LENGTH_SHORT).show();
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initEventAndData() {
        webView=new WebView(this);
        topStorys=new ArrayList<>();
        storys=new ArrayList<>();
        WebSettings settings=webView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        ImagePlugin imagePlugin = new ImagePlugin();
        webView.addJavascriptInterface(imagePlugin, "ZhihuDaily");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(final WebView view, String url) {
                progressNow++;
                EventBus.getDefault().post(new DownloadNextEvent(progressNow,progress));
                super.onPageFinished(view, url);
            }
        });
        EventBus.getDefault().post(new DownloadNextEvent(0,1));
        EventBus.getDefault().register(this);
        mPresenter.loadDailyNewList();
    }

    @Override
    protected void initInject() {
        getServiceComponent().inject(this);
    }

    @Override
    public void showErrorMsg(String msg) {
        EventBus.getDefault().post(new DownloadNextEvent(false));
        Toast.makeText(this, "离线下载失败", Toast.LENGTH_SHORT).show();
        stopSelf();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DownloadNextEvent event) {
        if(!event.isFinish())
        loadNextStory();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadWebImage event) {
            mPresenter.addImageUrl(event.url);
    }
    @SuppressLint("JavascriptInterface")
    class ImagePlugin {
        @JavascriptInterface
        public void loadImage(String url) {
EventBus.getDefault().post(new LoadWebImage(url));
        }
    }
    static class LoadWebImage{
        String url;

        public LoadWebImage(String url) {
            this.url = url;
        }
    }
}
