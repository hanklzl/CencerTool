package com.zoneol.lovebirds.protocol;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import android.os.Environment;

/**
 * 
 */
public class AppProperty {
	static public boolean test = false;
	
	static public final String PRODUCT_NS = "com.zoneol.lovebirds";
	static public final String PRODUCT_NAME = "lovebirds";
	static public final String CONFIG_FILE = "config.ini";
	static public final String SERVER_URL = "http://203.195.190.19:8080";
//	static public final String SERVER_URL = "http://192.168.1.110:8080";
//	static public final String SERVER_URL = "http://us.zoneol.com:8080";
	static public final String LOGIN_SUB_URL = "/apps/user/login.do?";
	static public final String UPLOAD_SUB_URL = "/apps/client/publishRes.do?";
	static public final String CHECKUPDATE_SUB_URL = "/apps/client/queryUpdate.do?";
	static public final String SUGGEST_SUB_URL = "/apps/client/feedback.do?";
	static public final String SECRET_MAIN_SUB_URL = "/apps/bbs/getTopic.do?" ;
	static public final String SECRET_PUBLIC_SUB_URL = "/apps/bbs/publishTopic.do?" ;
	static public final String SECRET_HIT_SUB_URL = "/apps/bbs/hitTopic.do?" ;
	static public final String USERINFO_ICON_PUBLISICON_SUB_URL = "/apps/user/updateUserImg.do" ;
	static public final String ATTENTION_ADD_FRIEND = "/apps/user/addFriend.do" ;
	static public final String ATTENTION_SELECET_FRIEND = "/apps/user/queryServerFriends.do" ;
	static public final String ATTENTION_DELETE_FRIEND = "/apps/user/delFriend.do" ;
	static public final String AUDIO_SAVE_PATH = "/msg/audio/";
	static public final String IMAGE_SAVE_PATH ="/msg/image/";
	static public final String IMAGE_CROP_SAVE_PATH = "/msg/image/crop/" ;
	static public final String ERROR_SAVE_PATH = "/crash/" ;
	
//	static public final String FEMALE_DATA_URL_PREFIX = "http://203.195.190.19:8080/download/female/";
//	static public final String MALE_DATA_URL_PREFIX = "http://203.195.190.19:8080/download/male/";
	
	private static AtomicInteger mAudioIdx = new AtomicInteger();
	private static AtomicInteger mImageIdx = new AtomicInteger();
	
	static public String getExtraDataDir() {
		return Environment.getExternalStorageDirectory().getPath() +"/"+ PRODUCT_NAME;
	}
	
	static public String getAudioSaveDir() {
		return getExtraDataDir() + AUDIO_SAVE_PATH;
	}
	
	static public int getAudioSaveIdx() {
		return Math.abs(UUID.randomUUID().hashCode()) ;
//		return mAudioIdx.getAndIncrement();
	}
	
	static public String getImageSaveDir() {
		return getExtraDataDir() + IMAGE_SAVE_PATH;
	}
	
	static public String getImageCropSaveDir() {
		return getExtraDataDir() + IMAGE_CROP_SAVE_PATH;
	}
	
	static public int getImageSaveIdx() {
		return Math.abs(UUID.randomUUID().hashCode()) ;
//		return mImageIdx.getAndIncrement();
	}
	
	static public String getErrorSaveId() {
		return getExtraDataDir() + ERROR_SAVE_PATH ;
	}
}
