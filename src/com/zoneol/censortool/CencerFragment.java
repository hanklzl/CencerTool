package com.zoneol.censortool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.com.zoneol.censortool.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zoneol.lovebirds.protocol.AppProperty;

public class CencerFragment extends DialogFragment{
	//private DialogInterface.OnClickListener mOnClickListener;
    private CencerInterface mCencerInterface;
    private int mSecretPos;
    private String mImagePath;

	
	public static CencerFragment newInstance(int position,String path)
    {
        CencerFragment fragment = new CencerFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("image_url",path);
        fragment.setArguments(args);
		return fragment;
	}
	
//	public void setOnClickListener(DialogInterface.OnClickListener listener){
//		mOnClickListener = listener;
//	}

    public void setCencerInterface(CencerInterface i){
        mCencerInterface = i;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSecretPos = getArguments().getInt("position");
        mImagePath = getArguments().getString("image_url");
        //mCencerInterface.getTopicCategory();
    }

    @Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        View imageView;
        imageView = LayoutInflater.from(getActivity()).inflate(R.layout.secret_image,null);
        ImageView image = (ImageView) imageView.findViewById(R.id.secret_image);
        if(mImagePath != null){
//            if(!ImageLoader.getInstance().isInited())
//            {
//                Log.d("imageloader","init imageloader");
//                ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity())
//                        .memoryCacheExtraOptions(480, 800)
//                        .threadPoolSize(5)
//                        .memoryCacheSize(10 * 1024 * 1024)
//                        .diskCacheSize(50 * 1024 * 1024)
//                        .tasksProcessingOrder(QueueProcessingType.LIFO)
//                        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                        .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//                                // .imageDecoder(decoder)
//                        .imageDownloader(
//                                new BaseImageDownloader(getActivity(), 5 * 1000, 30 * 1000))
//                        .build();
//                ImageLoader.getInstance().init(config);
//            }
            ImageHelper.getInstance().getImageLoader().getLoadingUriForView(image);
            ImageHelper.getInstance().getImageLoader().displayImage(AppProperty.SERVER_URL + mImagePath, image);

            Log.d("image", "load image: " + AppProperty.SERVER_URL + mImagePath);
        }
        return new AlertDialog.Builder(getActivity())
			.setTitle("该说说是否审核通过")
            .setView(imageView)
			.setPositiveButton("通过", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //mCencerInterface.passSecret(mSecretPos);
                    Intent intent = new Intent();
                    intent.putExtra("position",mSecretPos);
                    getTargetFragment().onActivityResult(SecretListFragment.PASS_SECRET, Activity.RESULT_OK,intent);
                }
            })
			.setNegativeButton("删除", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mCencerInterface.deleteSecret(mSecretPos);
                }
            })
			.create();
			
	}
	
	
	
}
