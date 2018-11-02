package com.controllerapp.jdfree.jjandroidedustudy.controllerapp.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class DeleteDialogFragment extends DialogFragment {

    private onDeleteListener mDeleteListener;

    public interface onDeleteListener {
        void deleteListener();
    }

    public DeleteDialogFragment setDeleteListener(onDeleteListener mDeleteListener) {
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
                        mDeleteListener.deleteListener();
                    }
                })
                .setNegativeButton("취소", null);

        return builder.create();
    }
}
