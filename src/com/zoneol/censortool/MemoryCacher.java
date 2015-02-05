package com.zoneol.censortool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;

import com.zoneol.lovebirds.protocol.AppProperty;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by liangzili on 2015/2/5.
 */
public class MemoryCacher {
    private int maxMemorySize;
    private int cacheSize;
    private Context mContext;
    private static MemoryCacher sMemoryCacher;
    private LruCache<String,Bitmap> mMemoryCache;
    private MemoryCacher(){}

    public static MemoryCacher getInstance(){
        if(sMemoryCacher == null){
            sMemoryCacher = new MemoryCacher();
        }
        return sMemoryCacher;
    }

    public void init(Context context){
        maxMemorySize = (int)(Runtime.getRuntime().maxMemory() / 1024);
        cacheSize = maxMemorySize / 4;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap){
        if(getBitmapFromMemCache(key) == null){
            mMemoryCache.put(key,bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key){
        return mMemoryCache.get(key);
    }

    public void getBitmapFromWeb(String url){
        new GetBitmapFromWebTask(url).execute();
    }



    class GetBitmapFromWebTask extends AsyncTask<Void,Void,Bitmap>{
        String urlStr;

        public GetBitmapFromWebTask(String url){
            urlStr = url;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            //String urlStr = params[0];
            Bitmap bitmap = null;
            HttpURLConnection conn = null;
            InputStream is = null;
            try {
                URL url = new URL(urlStr);
                conn = (HttpURLConnection)url.openConnection();
                is = conn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }catch(IOException e2){
                e2.printStackTrace();
            }finally{
                if(conn != null){
                    conn.disconnect();
                }

                if(is != null){
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return bitmap;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(bitmap != null){
                Log.d("bitmap", "load bitmap from web finish " + urlStr);
                addBitmapToMemoryCache(urlStr, bitmap);
            }
        }
    }
}
