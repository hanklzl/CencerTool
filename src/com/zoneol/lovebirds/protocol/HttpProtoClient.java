package com.zoneol.lovebirds.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.os.Message;

public class HttpProtoClient {
    public static final int SUCCESS = 0;
    public static final int ERROR_NETWORK = 1;
    public static final int ERROR_PROTO = 2;
    public static final int ERROR_OTHER = 3;
    
    private final int HTTP_TIME_OUT = 30 * 1000;
    
    private HttpClient mHttpClient;
    private OnCompleteListener mListener = null;
    private Handler mHandler;
	private String mKey;
	private Map<String, String> mPostData;

    public HttpProtoClient() {
        HttpParams parms = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(parms, HTTP_TIME_OUT);
        HttpConnectionParams.setSoTimeout(parms, HTTP_TIME_OUT);
        mHttpClient = new DefaultHttpClient(parms);
    }

    public interface OnCompleteListener {
        public void OnComplete(int result, byte[] data);
    }

    public void asyncLoadProtoBuf(String key, Map<String, String> postData, 
    		OnCompleteListener onCompleteListener) {
    	mListener = onCompleteListener;
        if (mListener != null) {
        	mHandler = new Handler() {
                public void handleMessage(Message msg) {
                	mListener.OnComplete(msg.what, (byte[])msg.obj);
                }
            };
        }
        
        mKey = key;
        mPostData = postData;
        new WorkThread().start();
    }
    

    private class WorkThread extends Thread {
        @Override
        public void run() {
        	int result;
            byte[] data = loadProtoBuf(mKey, mPostData);
            if (data == null) {
            	result = ERROR_NETWORK;
            } else {
            	result = SUCCESS;
            }
            if (mListener != null){
                Message message = mHandler.obtainMessage(result, data);
                mHandler.sendMessage(message);
            }
        }
    }
    
    public byte[] loadProtoBuf(String key, Map<String, String> postData) {
    	HttpEntity httpEntity;
        MultipartEntity entity = new MultipartEntity();
        HttpPost httpPost = new HttpPost(AppProperty.SERVER_URL + key);
        Charset charset = Charset.forName("UTF-8");
        HttpResponse resp;
        byte[] data;
        
        try {

            for (Map.Entry<String, String> entry : postData.entrySet()) {
                if (entry.getValue() != null) {
                    entity.addPart(entry.getKey(), new StringBody(entry.getValue(), charset));
                }
            }
            entity.addPart("version", new StringBody("V1.0"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        
        httpPost.setEntity(entity);
        try {
			resp = mHttpClient.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

        if (resp.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
        	return null;
        }
        
        httpEntity = resp.getEntity();
        data = streamToByteArray(httpEntity);
        
        try {
            httpEntity.consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return data;
    }
    
    private byte[] streamToByteArray(HttpEntity httpEntity) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        byte[] buffer = new byte[256];
        int len;

        try {
        	InputStream inputStream = httpEntity.getContent(); 
			while ((len = inputStream.read(buffer)) != -1) {
			  byteBuffer.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

        return byteBuffer.toByteArray();
    }
}
