package com.zoneol.censortool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.com.zoneol.censortool.R;
import com.google.protobuf.nano.InvalidProtocolBufferNanoException;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zoneol.lovebirds.protocol.AppProperty;
import com.zoneol.lovebirds.protocol.HttpProtoClient;
import com.zoneol.lovebirds.protocol.HttpProtoClient.OnCompleteListener;
import com.zoneol.lovebirds.protocol.JoyProtocol;
import com.zoneol.lovebirds.protocol.JoyProtocol.BbsTopic;
import com.zoneol.lovebirds.protocol.JoyProtocol.HttpQueryBbsTopicsResponse;
import com.zoneol.lovebirds.protocol.JoyProtocol.HttpQueryBbsTopicsResponse.ResultCode;
import com.zoneol.lovebirds.widget.pulltorefresh.PullRefreshUpdate;
import com.zoneol.lovebirds.widget.pulltorefresh.PullToRefreshBase;
import com.zoneol.lovebirds.widget.pulltorefresh.PullToRefreshListView;
import com.zoneol.lovebirds.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class SecretListFragment extends Fragment implements OnRefreshListener<ListView>
        , OnItemLongClickListener
        ,CencerInterface
    {

    public static int DELETE_SECRET = 0;
    public static int PASS_SECRET = 1;
    public static int MOVE_TO_CATEGORY = 2;

	private static final int REFRESH = 0;
	private static final int ONLOAD = 1;
	private static final int REFRESH_FAIL = 2;
	private  static final int ONLOAD_FAIL = 3;
	private int pageIndex = 0;
	private static final int pageSize = 10;
	
	
	static public final String SECRET_MAIN_SUB_URL = "/apps/bbs/getTopic.do?" ;
    static public final String SECRET_UPDATE_URL = "/apps/bbs/updateTopic.do";
	static public final String SECRET_DEL_URL = "/apps/bbs/delTopic.do?";
    static public final String SECRET_GET_CATEGORY = "/apps/bbs/getTopicCategory.do";
	
	private long mId = 1;
	private long mSessionKey = 50734809500169L;
	
	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private List<TopBbsInfo> mSecretList;
	private RelativeLayout mSecretRefreshRel;
	private ArrayAdapter<TopBbsInfo> mListAdapter;
    private ArrayList<JoyProtocol.BbsTopicCategory> mCategoryList;
    private String[] mCategoryItems;
	
	//private int mSelectId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mSecretList = new ArrayList<TopBbsInfo>();
		getActivity().setTitle("秘密");
		super.onCreate(savedInstanceState);
        ImageHelper.getInstance().init(getActivity());
        MemoryCacher.getInstance().init(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.secret_page, container,false);
		mPullRefreshListView = (PullToRefreshListView)v.findViewById(R.id.secret_page_listview);
		mPullRefreshListView.setPullLoadEnabled(false);
		mPullRefreshListView.setScrollLoadEnabled(true);
		mSecretRefreshRel = (RelativeLayout) v
				.findViewById(R.id.secret_page_refresh_rel);
		mListView = mPullRefreshListView.getRefreshableView();
		mListView = mPullRefreshListView.getRefreshableView();
		//mListView.setAdapter(mListViewAdapter);
		loadSecret(REFRESH, 0);
		mListAdapter = new ArrayAdapter<>(getActivity()
				, android.R.layout.simple_list_item_1
				, mSecretList);
		mListView.setAdapter(mListAdapter);
		mListView.setOnItemLongClickListener(this);
		
		mListView.setSelector(R.drawable.bg_all_selector1);
		mPullRefreshListView.setOnRefreshListener(this);
		mPullRefreshListView.doPullRefreshing(true, 500);

        getTopicCategory();

		PullRefreshUpdate.setLastUpdateTime(mPullRefreshListView);
		return v;
	}

        @Override
        public void onResume() {
            super.onResume();
            mListAdapter.notifyDataSetChanged();
        }

        @Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex = 0;
		loadSecret(REFRESH, 0);
		mPullRefreshListView.setHasMoreData(true);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageIndex++;
		loadSecret(ONLOAD, 0);
	}

	
	public void loadSecret(final int what, int select) {
		HttpProtoClient httpClinet = new HttpProtoClient();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", "" + mId);
		map.put("sessionKey", "" + mSessionKey);
		map.put("pageSize", pageSize + "");
		map.put("pageIndex", pageIndex + "");
		map.put("filter", select + "");
        map.put("admin",1 + "");
        map.put("category",-1 + "");
		httpClinet.asyncLoadProtoBuf(SECRET_MAIN_SUB_URL, map,
				new OnCompleteListener() {

					@Override
					public void OnComplete(int result, byte[] data) {
						if (result == HttpProtoClient.SUCCESS) {
                            ImageView view = new ImageView(getActivity());
							HttpQueryBbsTopicsResponse httpBbsTopicsResult;
							try {
								httpBbsTopicsResult = HttpQueryBbsTopicsResponse.parseFrom(data);
//										.parseFrom(data);
							} catch (InvalidProtocolBufferNanoException e) {
								e.printStackTrace();
								return;
							}

							int resultCode = httpBbsTopicsResult.resultCode;
							if (resultCode == ResultCode.SUCCESS) {
								List<BbsTopic> topicsList = new ArrayList<BbsTopic>();// = httpBbsTopicsResult.topics
										//.getTopicsList();
								BbsTopic[] topics = httpBbsTopicsResult.topics;
								for (BbsTopic bbsTopic : topics) {
                                    topicsList.add(bbsTopic);
                                    if (bbsTopic.hasImageUrl() && bbsTopic.getImageUrl() != "") {
                                        //ImageHelper.getInstance().getImageLoader().displayImage(AppProperty.SERVER_URL + bbsTopic.getImageUrl(), view);                                       topicsList.add(bbsTopic);
                                        if(MemoryCacher.getInstance().getBitmapFromMemCache(bbsTopic.getImageUrl()) == null){
                                            MemoryCacher.getInstance().getBitmapFromWeb(AppProperty.SERVER_URL + bbsTopic.getImageUrl());
                                        }
                                    }
                                }
								onReCallResult(what, getTopBbslist(topicsList));

								return;
							} else {
								if (what == REFRESH) {
									onReCallResult(REFRESH_FAIL, null);
								} else {
									onReCallResult(ONLOAD_FAIL, null);
								}
								//Misc.logd("load failed " + resultCode);
								return;
							}
						} else {
							if (what == REFRESH) {
								onReCallResult(REFRESH_FAIL, null);
							} else {
								onReCallResult(ONLOAD_FAIL, null);
							}
							//Misc.logd("load failed ");
						}
					}
				});

	
}
	public List<TopBbsInfo> getTopBbslist(List<BbsTopic> topicsList) {
		List<TopBbsInfo> list = new ArrayList<TopBbsInfo>();
		for (BbsTopic bbs : topicsList) {
			TopBbsInfo topbbs = new TopBbsInfo();
			topbbs.setBbs(bbs);
			topbbs.setHits(bbs.getHits());
			list.add(topbbs);
		}
		return list;
	}

	
	private void onReCallResult(int what, List<TopBbsInfo> list) {
//		if (mMyView == null) {
//			return;
//		}

		switch (what) {
		case REFRESH:
			mPullRefreshListView.onPullDownRefreshComplete();
			mSecretList.clear();
			mSecretList.addAll(list);
			mListAdapter.notifyDataSetChanged();

//			UserManager.getInstance().deleteSecret();
//			UserManager.getInstance().insertSecretList(list);

//			mListViewAdapter.setIsLocal(false);
//			mListViewAdapter.notifyDataSetChanged();
			PullRefreshUpdate.setLastUpdateTime(mPullRefreshListView);
			if (list.size() < 10) {
				mPullRefreshListView.setHasMoreData(false);
			} else {
				mPullRefreshListView.setHasMoreData(true);
			}
			break;
		case ONLOAD:
//			if (mListViewAdapter.getIslocal()) {
//				mSecretList.clear();
//			}
			
			Iterator<TopBbsInfo> sListIterator = list.iterator();
			while (sListIterator.hasNext()) {
				boolean flag = false ;
				TopBbsInfo topbbs = sListIterator.next() ;
				for (TopBbsInfo topBbsInfoSecret : mSecretList) {
					if(topbbs.getBbs().topicId == topBbsInfoSecret.getBbs().topicId) {
						Log.d("sensor", topbbs.getBbs().getPublishUid() + " " + topBbsInfoSecret.getBbs().getPublishUid());
						flag =true ;
						break ;
					}
				}
				
				if(flag) {
					sListIterator.remove();
				}
			}
			
			mPullRefreshListView.onPullUpRefreshComplete();
			mSecretList.addAll(list);

//			mListViewAdapter.setIsLocal(false);
			mListAdapter.notifyDataSetChanged();
			PullRefreshUpdate.setLastUpdateTime(mPullRefreshListView);
			if (list.size() < 10) {
				mPullRefreshListView.setHasMoreData(false);
			} else {
				mPullRefreshListView.setHasMoreData(true);
			}
			break;
		case ONLOAD_FAIL:
			mPullRefreshListView.onPullUpRefreshComplete();
			break;
		case REFRESH_FAIL:
			if (mSecretList == null || mSecretList.size() < 1) {
				mPullRefreshListView.setVisibility(View.GONE);
				mSecretRefreshRel.setVisibility(View.VISIBLE);
			}
			mPullRefreshListView.onPullDownRefreshComplete();
			PullRefreshUpdate.setLastUpdateTime(mPullRefreshListView);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		//mSelectId = position;
		CencerFragment fragment = CencerFragment.newInstance(position,mSecretList.get(position).getBbs().getImageUrl());
		fragment.setCencerInterface(this);
        fragment.setTargetFragment(this,0);
		fragment.show(getActivity().getSupportFragmentManager(), "cencer");
		return false;
	}

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if(resultCode == Activity.RESULT_OK){
                int pos = data.getIntExtra("position", -1);
                if(pos != -1) {
                    if (requestCode == PASS_SECRET) {
                        //int pos = data.getIntExtra("position", -1);
                        //if (pos != -1) {
                        passSecret(pos);
                        MoveToCategoryFragment fragment = MoveToCategoryFragment.newInstance(pos, mCategoryItems, mSecretList.get(pos).getBbs().getCategory());
                        fragment.setTargetFragment(this, 1);
                        fragment.show(getFragmentManager(), "MoveToCategory");
                        //}
                    } else if (requestCode == DELETE_SECRET) {
                        deleteSecret(pos);
                    } else if (requestCode == MOVE_TO_CATEGORY) {
                        //int pos = data.getIntExtra("position", -1);
                        int category = data.getIntExtra("category", 0);
                        moveToCategory(pos, category);
                    }
                }
            }


        }

        //	@Override
//	public void onClick(DialogInterface dialog, int which) {
//		if(mSelectId != -1 && mSecretList.get(mSelectId) != null){
//			deleteSecret();
//		}
//	}
	

	public void deleteSecret(final int pos) {
		//final int pos = id;
		HttpProtoClient httpClinet = new HttpProtoClient();
		Map<String, String> map = new HashMap<String, String>();
		map.put("uid", ""+ mId);
		map.put("sessionKey", ""+mSessionKey);
		map.put("topicId", ""+mSecretList.get(pos).getBbs().topicId);
        map.put("action", "del");
        //map.put("actValue","" + 1);
		httpClinet.asyncLoadProtoBuf(SECRET_UPDATE_URL, map,new OnCompleteListener() {
			
			@Override
			public void OnComplete(int result, byte[] data) {
                if(result == HttpProtoClient.SUCCESS) {
                    Log.d("delete", "delete " + mSecretList.get(pos).getBbs().content + " id :" + mSecretList.get(pos).getBbs().topicId);
                    mSecretList.remove(pos);
                    mListAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(),"delete complete",Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("delete", "delete " + mSecretList.get(pos).getBbs().content + " id :" + mSecretList.get(pos).getBbs().topicId + "fail");
                    Toast.makeText(getActivity(),"delete fail",Toast.LENGTH_SHORT).show();
                }
			}
		});
	}

        @Override
        public void moveToCategory(final int pos,int categoryNo) {
            HttpProtoClient httpClinet = new HttpProtoClient();
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", ""+ mId);
            map.put("sessionKey", ""+mSessionKey);
            map.put("topicId", ""+mSecretList.get(pos).getBbs().topicId);
            map.put("action", "category");
            map.put("actValue","" + categoryNo);

            httpClinet.asyncLoadProtoBuf(SECRET_UPDATE_URL, map,new OnCompleteListener() {

                @Override
                public void OnComplete(int result, byte[] data) {
                    if(result == HttpProtoClient.SUCCESS) {
                        Log.d("moveToCategory", "moveToCategory " + mSecretList.get(pos).getBbs().content + " id :" + mSecretList.get(pos).getBbs().topicId);
                        mSecretList.remove(pos);
                        mListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"moveToCategory complete",Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("moveToCategory", "moveToCategory " + mSecretList.get(pos).getBbs().content + " id :" + mSecretList.get(pos).getBbs().topicId + "fail");
                        Toast.makeText(getActivity(),"moveToCategory fail",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
//
//        @Override
//        public void deleteSecret(int topicId) {
//
//        }

        @Override
        public void passSecret(final int pos ) {
            HttpProtoClient httpClinet = new HttpProtoClient();
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", ""+ mId);
            map.put("sessionKey", ""+mSessionKey);
            map.put("topicId", ""+mSecretList.get(pos).getBbs().topicId);
            map.put("action", "status");
            map.put("actValue","0");
            //map.put("actValue","" + 1);
            httpClinet.asyncLoadProtoBuf(SECRET_UPDATE_URL, map,new OnCompleteListener() {

                @Override
                public void OnComplete(int result, byte[] data) {
                    if(result == HttpProtoClient.SUCCESS) {
                        Log.d("pass", "pass " + mSecretList.get(pos).getBbs().content + " id :" + mSecretList.get(pos).getBbs().topicId);
                        //mSecretList.remove(pos);
                        //mListAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(),"pass complete",Toast.LENGTH_SHORT).show();
                    }else{
                        Log.d("pass", "pass " + mSecretList.get(pos).getBbs().content + " id :" + mSecretList.get(pos).getBbs().topicId + "fail");
                        Toast.makeText(getActivity(),"pass fail",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        public void getTopicCategory() {
            HttpProtoClient httpClinet = new HttpProtoClient();
            Map<String, String> map = new HashMap<String, String>();
            map.put("uid", ""+ mId);
            map.put("sessionKey", ""+mSessionKey);
            httpClinet.asyncLoadProtoBuf(SECRET_GET_CATEGORY,map,new OnCompleteListener() {
                @Override
                public void OnComplete(int result, byte[] data) {
                    JoyProtocol.HttpQueryBbsTopicCategoryResponse response;
                    mCategoryList = new ArrayList<JoyProtocol.BbsTopicCategory>();
                    if(result == HttpProtoClient.SUCCESS){
                        Log.d("getCategory", "getTopicCategory success");
                        try {
                            response = JoyProtocol.HttpQueryBbsTopicCategoryResponse.parseFrom(data);
                            //categories = new ArrayList<JoyProtocol.BbsTopicCategory>();
                            for(JoyProtocol.BbsTopicCategory category :response.categories){
                                mCategoryList.add(category);
                            }
                            mCategoryItems = new String[mCategoryList.size()];
                            for(int i = 0;i < mCategoryList.size();i++){
                                mCategoryItems[i] = mCategoryList.get(i).getTitle();
                            }
                            Log.d("response",response.toString());

                        } catch (InvalidProtocolBufferNanoException e) {
                            e.printStackTrace();
                        }
                    }else{
                        Log.d("getCategory", "getTopicCategory fail");
                    }
                }
            });
        }
    }
