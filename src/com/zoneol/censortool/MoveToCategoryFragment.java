package com.zoneol.censortool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by liangzili on 2015/2/4.
 */
public class MoveToCategoryFragment extends DialogFragment {

    String[] items;
    int pos;
    int category;

    public static MoveToCategoryFragment newInstance(int pos,String items[],int category)
    {
        MoveToCategoryFragment fragment = new MoveToCategoryFragment();
        Bundle args = new Bundle();
        args.putInt("position",pos);
        args.putStringArray("item", items);
        args.putInt("category",category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pos = getArguments().getInt("position");
        items = getArguments().getStringArray("item");
        category = getArguments().getInt("category");

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog dialog =  new AlertDialog.Builder(getActivity())
                .setTitle("选择分组")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(pos, which);
                        //Toast.makeText(getActivity(), items[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setCancelable(false)
                .setInverseBackgroundForced(true)
                .setNegativeButton("选择默认：" + items[category], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(pos, category);
                        //Toast.makeText(getActivity(), items[category], Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void sendResult(int pos,int category){
        if(getTargetFragment() == null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("position",pos);
        intent.putExtra("category",category);
        getTargetFragment().onActivityResult(SecretListFragment.MOVE_TO_CATEGORY, Activity.RESULT_OK,intent);
    }
}
