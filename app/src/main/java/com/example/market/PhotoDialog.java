package com.example.market;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.example.market.fragment.OrderFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoDialog extends Dialog {

    private PhotoDialogListener photoDialogListener;

    public PhotoDialog(@NonNull Context context, PhotoDialogListener photoDialogListener) {
        super(context);
        this.photoDialogListener = photoDialogListener;
    }

    public interface PhotoDialogListener {
        void clickBtnCamera();
        void clickBtnAlbum();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photo_dialog);

        LinearLayout btnCamera = (LinearLayout)findViewById(R.id.btn_camera),
                btnAlbum = (LinearLayout)findViewById(R.id.btn_album);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                photoDialogListener.clickBtnCamera();
                dismiss();
            }
        });

        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoDialogListener.clickBtnAlbum();
                dismiss();
            }
        });
    }
}
