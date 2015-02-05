package com.zoneol.lovebirds.widget.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import com.zoneol.lovebirds.widget.pulltorefresh.PullToRefreshListView;
import com.zoneol.lovebirds.widget.pulltorefresh.PullToRefreshScrollView;

public class PullRefreshUpdate {
	@SuppressLint("SimpleDateFormat")
	static SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    public static void setLastUpdateTime(PullToRefreshListView pullRefresh) {
        String text = formatDateTime(System.currentTimeMillis());
        pullRefresh.setLastUpdatedLabel(text);
    }
    
    public static void setLastUpdateTimeScroll(PullToRefreshScrollView pullRefresh) {
    	 String text = formatDateTime(System.currentTimeMillis());
         pullRefresh.setLastUpdatedLabel(text);
    }
    
    private static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        
        return mDateFormat.format(new Date(time));
    }
}
