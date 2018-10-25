package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment.deleteitem;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.controllerapp.jdfree.jjandroidedustudy.controllerapp.activity.MainActivity;

public class DeleteDialogFragment extends DialogFragment {

    private int position;

    private onDeleteListener mDeleteListener;

    public interface onDeleteListener {
        void deleteListener(int position);
    }

    public DeleteDialogFragment setDeleteListener(onDeleteListener mDeleteListener, int position) {
        this.position = position;
        this.mDeleteListener = mDeleteListener;

        return this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("제어 앱 삭제")
                .setMessage("제어 중인 앱을 삭제하시겠습니까?")
                .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDeleteListener.deleteListener(position);
                    }
                })
                .setNegativeButton("취소", null);

        return builder.create();
    }
}
