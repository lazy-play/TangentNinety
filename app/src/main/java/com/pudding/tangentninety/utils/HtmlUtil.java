package com.pudding.tangentninety.utils;

import java.util.List;

/**
 * Created by codeest on 16/8/14.
 * <p>
 * 在html中引入外部css,js文件   常规拼接顺序css->html->js
 * https://github.com/HotBitmapGG/RxZhiHu/blob/https--github.com/HotBitmapGG/RxZhiHuDaily/app/src/main/java/com/hotbitmapgg/rxzhihu/utils/HtmlUtil.java#L13
 */

public class HtmlUtil {
    private static final String HTML_HEAD = "<!doctype html>\n<html>\n";
    private static final String HTML_END="</html>";
    private static final String HEAD_START = "<head>";
    private static final String HEAD_END = "</head>";
    private static final String BODY_START = "<body className=\"\" onload=\"onLoaded(%s)\">";
    private static final String BODY_END = "</body>";
    private static final String META = "<meta charset=\"utf-8\">\n" +
            "\t<meta name=\"viewport\" content=\"width=device-width,user-scalable=no\">\n";
    private static final String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/news_qa.min.css\"/>";
    // js script tag,需要格式化
    private static final String NEEDED_FORMAT_JS_TAG = "<script src=\"%s\"></script>";
    private static final String[] BASE_JS_LIST = {"file:///android_asset/zepto.min.js", "file:///android_asset/img_replace.js", "file:///android_asset/video.js","file:///android_asset/buttom.js"};
    private static final String JS_NIGHT = "file:///android_asset/night.js";
    private static final String JS_LARGE_FONT = "file:///android_asset/large-font.js";

    public static final String MIME_TYPE = "text/html; charset=utf-8";

    public static final String ENCODING = "utf-8";

    private HtmlUtil() {

    }
    private static String createBody(String body,boolean isNoPic,boolean isNight){
//        String replace=isNight?
//                isNoPic?"img class=\"content-image\" src=\"file:///android_asset/default_pic_content_image_download_dark.png\" zhimg-src"
//                        :"img class=\"content-image\" src=\"file:///android_asset/default_pic_content_image_loading_dark.png\" zhimg-src"
//                : isNoPic?"img class=\"content-image\" src=\"file:///android_asset/default_pic_content_image_download_light.png\" zhimg-src"
//                        :"img class=\"content-image\" src=\"file:///android_asset/default_pic_content_image_loading_light.png\" zhimg-src";
//        return body.replace("img class=\"content-image\" src",replace);
        String replace=isNight?
                isNoPic?"img src=\"file:///android_asset/default_pic_content_image_download_dark.png\" zhimg-src"
                        :"img src=\"file:///android_asset/default_pic_content_image_loading_dark.png\" zhimg-src"
                : isNoPic?"img src=\"file:///android_asset/default_pic_content_image_download_light.png\" zhimg-src"
                :"img src=\"file:///android_asset/default_pic_content_image_loading_light.png\" zhimg-src";
        return body.replaceAll("img\\D*src",replace);
    }
    /**
     * 根据js链接生成Script标签
     *
     * @param url String
     * @return String
     */
    private static String createJsTag(String url) {
        return String.format(NEEDED_FORMAT_JS_TAG, url);
    }

    public static String createJsTag(boolean isNight, boolean isLargeFont) {

        final StringBuilder sb = new StringBuilder();
        for (String url : BASE_JS_LIST) {
            sb.append(createJsTag(url));
        }
        if (isNight)
            sb.append(createJsTag(JS_NIGHT));
        if (isLargeFont)
            sb.append(createJsTag(JS_LARGE_FONT));
        return sb.toString();
    }
    public static String createOnLoadMode(boolean isBackground){
        return String.format(BODY_START, isBackground);
    }
    /**
     * 根据样式标签,html字符串,js标签
     * 生成完整的HTML文档
     */
    public static String createHtmlData(String html){
        return createHtmlData(html,true,false,false,false);
    }
    public static String createHtmlData(String html,boolean isNoPic, boolean isNight, boolean isLargeFont) {
        return createHtmlData(html,false,isNoPic,isNight,isLargeFont);
    }
    private static String createHtmlData(String html,boolean isBackground,boolean isNoPic, boolean isNight, boolean isLargeFont) {
        StringBuilder sb = new StringBuilder();
        sb.append(HTML_HEAD)
                .append(HEAD_START)
                .append(META)
                .append(NEEDED_FORMAT_CSS_TAG)
                .append(HEAD_END)
                .append(createOnLoadMode(isBackground))
                .append(createJsTag(isNight, isLargeFont))
                .append(createBody(html,isNoPic,isNight))
                .append(BODY_END)
                .append(HTML_END);
        return sb.toString();
    }
}
