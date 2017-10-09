function img_replace(source, replaceSource) {

    $('img[zhimg-src*="'+source+'"]').each(function () {
        $(this).attr('src', replaceSource);
    });
}
var DEFAULT_IMAGE_URI = "file:///android_asset/default_pic_content_image_loading";
var DEFAULT_LOADING_IMAGE_URI = "file:///android_asset/default_pic_content_image_download";

function onLoaded(isBackground) {
	var allImage = document.querySelectorAll("img");
	allImage = Array.prototype.slice.call(allImage, 0);

	allImage.forEach(function(image) {
	if(isBackground){
	if (image.src.indexOf(DEFAULT_IMAGE_URI)>=0) {
    			ZhihuDaily.loadImage(image.getAttribute("zhimg-src"));
    		}
	}else{
	image.addEventListener('click',function(){
            onImageClick(this)
        },false);

		if (image.src.indexOf(DEFAULT_IMAGE_URI)>=0) {
			ZhihuDaily.loadImage(image.getAttribute("zhimg-src"));
		}else if(image.src.indexOf(DEFAULT_LOADING_IMAGE_URI)>=0){
		ZhihuDaily.loadDownloadImage(image.getAttribute("zhimg-src"));
		}
		}
	});
}

function onImageClick(pImage) {
//	console.log(pImage);
	if (pImage.src.indexOf(DEFAULT_LOADING_IMAGE_URI)>=0) {
		ZhihuDaily.loadImage(pImage.getAttribute("zhimg-src"));
	} else {
	console.log(pImage.getAttribute("zhimg-src"));
		ZhihuDaily.openImage(pImage.getAttribute("zhimg-src"));
	}
};

function onImageLoadingComplete(pOldUrl, pNewUrl) {
	var allImage = document.querySelectorAll("img");
	allImage = Array.prototype.slice.call(allImage, 0);
	allImage.forEach(function(image) {
		if (image.getAttribute("zhimg-src") == pOldUrl || image.getAttribute("zhimg-src") == decodeURIComponent(pOldUrl)) {
			image.src = pNewUrl;
		}
	});
}