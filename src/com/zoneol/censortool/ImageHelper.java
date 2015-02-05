package com.zoneol.censortool;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by liangzili on 2015/2/4.
 */
public class ImageHelper {
    private static ImageHelper mImageHelper;
    private ImageLoader mImageLoader;
    private ImageLoaderConfiguration mConfiguration;
    public static ImageHelper getInstance(){
        if(mImageHelper == null){
            mImageHelper = new ImageHelper();
        }
        return mImageHelper;
    }

    public void init(Context context){
        mConfiguration = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(5)
                .memoryCacheSize(10 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                        // .imageDecoder(decoder)
                .imageDownloader(
                        new BaseImageDownloader(context, 5 * 1000, 30 * 1000))
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(mConfiguration);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }
}
