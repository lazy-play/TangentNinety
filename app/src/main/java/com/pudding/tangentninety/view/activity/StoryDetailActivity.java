package com.pudding.tangentninety.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;
import com.pudding.tangentninety.base.SwipeFinishActivity;
import com.pudding.tangentninety.module.bean.CollectionStoryBean;
import com.pudding.tangentninety.module.bean.DetailExtraBean;
import com.pudding.tangentninety.module.bean.ZhihuDetailBean;
import com.pudding.tangentninety.module.event.CollectionUpdate;
import com.pudding.tangentninety.module.event.HistoryUpdate;
import com.pudding.tangentninety.presenter.DetailPresent;
import com.pudding.tangentninety.presenter.contract.DetailContract;
import com.pudding.tangentninety.utils.HtmlUtil;
import com.pudding.tangentninety.base.BadgeActionProvider;
import com.pudding.tangentninety.utils.ImageLoader;
import com.pudding.tangentninety.weight.HeadCoordinatorLayout;
import com.pudding.tangentninety.weight.NestedScrollWebview;


import org.greenrobot.eventbus.EventBus;

import java.net.URLEncoder;

import butterknife.BindView;

/**
 * Created by Error on 2017/7/6 0006.
 */

public class StoryDetailActivity extends SwipeFinishActivity<DetailPresent> implements DetailContract.View {
    private static String STORYID_KEY="storyID";
    private static String JS_NAME="ZhihuDaily";
    @BindView(R.id.webview)
    public NestedScrollWebview webView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.main_content)
    HeadCoordinatorLayout headCoordinatorLayout;
    @BindView(R.id.backdrop)
    ImageView backdrop;
    @BindView(R.id.story_title)
    TextView title;
    @BindView(R.id.cover)
    View cover;


    private static int StoryID;
    private static int webScroll;
    private static int toolBarOffset;
    private boolean lastStart;
    private ImagePlugin imagePlugin;
    private CollectionStoryBean collectionBean;
    private String share_url = "";
    MenuItem collectionMenu;
    BadgeActionProvider goodMenu;
    BadgeActionProvider commentsMenu;

    public static void startStoryActivity(Activity activity, int storyID) {
        Intent intent = new Intent(activity, StoryDetailActivity.class);
        intent.putExtra(STORYID_KEY, storyID);
        startActivity(activity,intent);
    }

    public void setCollectionChecked(boolean checked, boolean isEdit) {
        collectionMenu.setChecked(checked);
        collectionMenu.setIcon(checked ? R.drawable.ic_collection_fill : R.drawable.ic_collection_empty);
        if (isEdit) {
            if (checked) {
                mPresenter.collectionStory(collectionBean);
            } else {
                mPresenter.uncollectionStory(StoryID);
            }
            EventBus.getDefault().post(new CollectionUpdate());
        }
    }

    @Override
    public void showDetail(ZhihuDetailBean zhihuDetailBean) {
        mPresenter.addStoryToHistory(zhihuDetailBean);
        EventBus.getDefault().post(new HistoryUpdate());
        collectionBean = new CollectionStoryBean();
        collectionBean.setTitle(zhihuDetailBean.getTitle());
        collectionBean.setId(StoryID);
        collectionBean.setImage(zhihuDetailBean.getImages().get(0));
        title.setText(zhihuDetailBean.getTitle());
        share_url = zhihuDetailBean.getShare_url();
        boolean noPic = mPresenter.isNoPic();
        boolean isNight = mPresenter.isNightMode();
        boolean islargeFont = mPresenter.isBigFont();
        ImageLoader.load(this, zhihuDetailBean.getImage(), backdrop, noPic);
        String htmlData = HtmlUtil.createHtmlData(zhihuDetailBean.getBody(), noPic, isNight, islargeFont);
        System.out.println(htmlData);
        webView.loadDataWithBaseURL(null, htmlData, HtmlUtil.MIME_TYPE, HtmlUtil.ENCODING, null);
    }

    @Override
    public void showExtra(DetailExtraBean detailExtraBean) {
        commentsMenu.setBadge(detailExtraBean.getComments());
        goodMenu.setBadge(detailExtraBean.getPopularity());
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initEventAndData(@Nullable Bundle savedInstanceState) {
        setToolBar(toolbar, "");
        int id = getIntent().getIntExtra(STORYID_KEY, -1);
        if (id != StoryID) {
            StoryID = id;
        } else {
            lastStart = true;
        }
        loadStartView();
        WebSettings settings = webView.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        String cacheDirPath =
//                getFilesDir().getAbsolutePath() + "web_cache";
//                this.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
                getApplicationContext().getCacheDir().getAbsolutePath();
//        settings.setDatabasePath(cacheDirPath);
        settings.setAppCachePath(cacheDirPath);
        settings.setAllowFileAccess(true);
//        settings.setAppCacheMaxSize(1024);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        imagePlugin = new ImagePlugin();
        webView.addJavascriptInterface(imagePlugin, JS_NAME);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                startNewWeb(request.getUrl().toString());
                return true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                startNewWeb(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (view != null){
                    loadEndView();
                    cover.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(cover!=null)
                            cover.setVisibility(View.GONE);
                        }
                    },100);
                }
                super.onPageFinished(view, url);
            }
        });
        mPresenter.getStory(StoryID);

    }
private void startNewWeb(String url){
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
}
    private void loadStartView() {
        if (lastStart && toolBarOffset != 0) {
            backdrop.setAlpha(0f);
        }
    }
    private void loadEndView() {
        if (headCoordinatorLayout == null)
            return;
        if (headCoordinatorLayout.getButtomHeight() != 0) {
            imagePlugin.setButtom((int) (headCoordinatorLayout.getButtomHeight() / App.DIMEN_RATE));
        }
        if (lastStart && (webScroll != 0 || toolBarOffset != 0)) {
            webView.scrollTo(0, webScroll, toolBarOffset);
            appBarLayout.addOnOffsetChangedListener(listener);
            ViewCompat.animate(backdrop).alpha(1f).setStartDelay(collapsingToolbarLayout.getScrimAnimationDuration()).setDuration(collapsingToolbarLayout.getScrimAnimationDuration()).start();
        } else {
            appBarLayout.addOnOffsetChangedListener(listener);
        }
    }


    @Override
    protected void onViewCreated() {
        super.onViewCreated();
    }

    @Override
    protected void onDestroy() {
        webScroll = webView.getScrollY();
        appBarLayout.removeOnOffsetChangedListener(listener);
        webView.removeJavascriptInterface(JS_NAME);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        collectionMenu = menu.findItem(R.id.collection);
        goodMenu = (BadgeActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.good));
        commentsMenu = (BadgeActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.comments));
        commentsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoryCommentActivity.startStoryActivity(mContext, StoryID);
            }
        });
        setCollectionChecked(mPresenter.isCollectionExist(StoryID), false);
        mPresenter.getStoryExtra(StoryID);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                String str="《"+title.getText()+"》\n"+share_url;
                intent.putExtra(Intent.EXTRA_TEXT, str);
                intent.putExtra(Intent.EXTRA_SUBJECT,getResources().getText(R.string.app_name) );
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,getResources().getText(R.string.app_name)));
                break;
            case R.id.collection:
                item.setChecked(!item.isChecked());
                setCollectionChecked(item.isChecked(), true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void loadJsUrl(String url) {
        if (webView == null)
            return;
        webView.onResume();
        webView.loadUrl(url);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    private static AppBarLayout.OnOffsetChangedListener listener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            toolBarOffset = verticalOffset;
        }
    };


    @SuppressLint("JavascriptInterface")
    class ImagePlugin {
        @JavascriptInterface
        public void loadImage(String url) {
            mPresenter.loadImage(url);
//            webView.post(new Runnable() {
//                @Override
//                public void run() {
//                    mPresenter.addImageUrl(url);
//                    String newurl = URLEncoder.encode(url);
//                    loadJsUrl("javascript:img_replace(\"" + newurl + "\",\"" + newurl + "\");");
//                }
//            });

        }

        @JavascriptInterface
        public void loadDownloadImage(String url) {
            mPresenter.loadDownloadImage(url);
//            webView.post(new Runnable() {
//                @Override
//                public void run() {
//                    if (mPresenter.isImageUrlExist(url)) {
//                        String newurl = URLEncoder.encode(url);
//                        loadJsUrl("javascript:img_replace(\"" + newurl + "\",\"" + newurl + "\");");
//                    }
//                }
//            });
        }

        @JavascriptInterface
        public void openImage(String url) {
            String lowUrl = url.toLowerCase();
                PhotoActivity.startPhotoActivity(StoryDetailActivity.this, lowUrl);
        }

        public void setButtom(final int buttom) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    loadJsUrl("javascript:set_buttom(" + buttom + ");");
                }
            });

        }

    }
}


