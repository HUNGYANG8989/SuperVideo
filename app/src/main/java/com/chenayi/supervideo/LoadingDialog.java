package com.chenayi.supervideo;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.othershe.nicedialog.BaseNiceDialog;
import com.othershe.nicedialog.ViewHolder;

/**
 * Created by Chenwy on 2018/3/26.
 */

public class LoadingDialog extends BaseNiceDialog {
    private ImageView ivLoad;
    private ObjectAnimator rotation;

    public static LoadingDialog newInstance() {
        Bundle args = new Bundle();
        LoadingDialog fragment = new LoadingDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int intLayoutId() {
        return R.layout.dialog_loading;
    }

    @Override
    public void convertView(ViewHolder viewHolder, BaseNiceDialog baseNiceDialog) {
        ivLoad = viewHolder.getView(R.id.dialog_loading_iv);
        setOutCancel(false);
        startRotateAnim();
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                }
                return false;
            }
        });
    }

    private void startRotateAnim() {
        //最好是0f到359f，0f和360f的位置是重复的
        rotation = ObjectAnimator.ofFloat(ivLoad, "rotation", 0f, 359f);
        rotation.setRepeatCount(ObjectAnimator.INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setDuration(1000);
        rotation.start();
    }
}
