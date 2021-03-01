package com.example.market.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.market.LoginActivity;
import com.example.market.MainActivity;
import com.example.market.OrderList.OrderItem;
import com.example.market.PhotoDialog;
import com.example.market.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class OrderFragment extends Fragment {


    private static final int PICK_FROM_ALBUM = 2;
    private static final int PICK_FROM_CAMERA = 1;
    private static final String TAG = OrderFragment.class.getSimpleName();
    ;
    public final String DATA_STORE = "DATA_STORE";
    private File tempFile = null;
    Bitmap bitmap;
    String currentPhotoPath;
    Uri photoUri;

    public OrderFragment() {
    }

    public static OrderFragment newInstance() {
        OrderFragment orderFragment = new OrderFragment();
        //데이터 전달
        Bundle bundle = new Bundle();
        orderFragment.setArguments(bundle);
        return orderFragment;


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        ConstraintLayout btnUpload = view.findViewById(R.id.btn_upload);

        EditText editOrder = view.findViewById(R.id.edit_order);

        //test
        ImageView firstImg = view.findViewById(R.id.img1);

        firstImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permCheckCamera = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                int permCheckCameraWrite = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                int permCheckCameraRead = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (permCheckCameraWrite == PackageManager.PERMISSION_DENIED || permCheckCamera == PackageManager.PERMISSION_DENIED || permCheckCameraRead == PackageManager.PERMISSION_DENIED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
                    } else {
                        System.out.println("이미지 버튼 누르기>>>>>>");
                        showContent();

                    }
                } else {
                    System.out.println("이미지 버튼 누르기(2)>>>>>>");
                    showContent();
                }
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = editOrder.getText().toString();

                if ((content.trim().length() == 0 || content == null) && StringSubstring(tempFile).length() == 0) {
                    System.out.println("156-업로드 >>>" + StringSubstring(tempFile));
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("알림")
                            .setMessage("주문할 재료를 입력바랍니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    volleyPost();
                }
            }
        });

        return view;
    }

    private void showContent() {


        System.out.println("함수로 넘어가기>>>>>>");

        final PhotoDialog dialog = new PhotoDialog(getActivity(), new PhotoDialog.PhotoDialogListener() {

            @Override
            public void clickBtnCamera() {
                takePhoto();
            }

            @Override
            public void clickBtnAlbum() {

                albumPhoto();
            }
        });
        dialog.show();
    }

    public void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            tempFile = createImageFile();
            Log.d(TAG, "tempFile : " + tempFile);
        } catch (IOException e) {
            Toast.makeText(getContext(), "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        if (tempFile != null) {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                photoUri = FileProvider.getUriForFile(getContext(),
                        "com.example.market.provider", tempFile);

            } else {
                photoUri = Uri.fromFile(tempFile);
            }
            //intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            Log.d(TAG, "photoUri : " + photoUri);
            Log.d(TAG, "resolveActivity : " + intent.resolveActivity(getActivity().getPackageManager()));

            Log.d(TAG, "intent : " + intent);
        }

        startActivityForResult(intent, PICK_FROM_CAMERA);
    }

    public void albumPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);

        Log.d(TAG, "album_intent : " + intent);
    }

    private File createImageFile() throws IOException {


        // 이미지 파일 이름 ( blackJin_{시간}_ )
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "photo_order_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름 ( blackJin )
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        // 파일 생성
        File image = File.createTempFile(imageFileName, ".png", storageDir);
        Log.d(TAG, "createImageFile : " + image.getAbsolutePath());

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }


////////////////////여기부터


    public void volleyPost() {


        String postUrl = "http://211.229.250.40/api_create_order"; // 서버 주소
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        JSONObject postData = new JSONObject();


        String token = "token";


        try {
            //텍스트 주문 업로드
            EditText editOrder = getActivity().findViewById(R.id.edit_order);
            String contents = editOrder.getText().toString();
            contents = contents.replace("'", "''");
            System.out.println("내용>>>>>>>>>>>>>>>>>>" + contents);

            postData.put("order_content", contents);
            postData.put("order_img", StringSubstring(tempFile));

            System.out.println("이미지 로드 >>>>>>>>>>>"+ bitmap);


        } catch (Exception e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                String success = "200";
                System.out.println("응답해 >>>>>>>>>>(주문하기)" + response);

                try {
                    if (response.getString("result").equals(success)) {
                        System.out.println("응답해 >>>>>>>>>>(주문하기)" + success);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("알림")
                                .setMessage("주문을 진행하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                        builder.setTitle("알림")
                                                .setMessage("주문이 정상접수되었습니다.\n주문해주셔서 감사합니다:)")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }).create().show();
                                    }
                                })
                                .setNegativeButton("취소", null)
                                .create()
                                .show();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                final HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", getPreferenceString(token));
                return headers;
            }


        };

        requestQueue.add(jsonObjectRequest); // 서버에 요청

    }

    public String getPreferenceString(String key) {
        SharedPreferences pref = getContext().getSharedPreferences(DATA_STORE, MODE_PRIVATE);
        return pref.getString(key, "");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults[0] == 0 && grantResults[1] == 0 && grantResults[2] == 0) {
                Toast.makeText(getActivity(), " 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), " 권한이 거절되었습니다. 사진 첨부를 이용하시려면 권한을 승인해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView img1 = getActivity().findViewById(R.id.img1);


        System.out.println("data >>>>>>>>>>>" + data);
        System.out.println("requestCode >>>>>>>>>>>" + requestCode);

        ImageView openImg = (ImageView) getActivity().findViewById(R.id.open_img1);

        if (resultCode == RESULT_OK && requestCode == PICK_FROM_CAMERA) {
            img1.setVisibility(View.GONE);
            bitmap = (Bitmap) data.getParcelableExtra("data");
            openImg.setImageBitmap(bitmap);
            System.out.println("bitmap >>>>>>>>>>>" + bitmap);

        } else if (resultCode == RESULT_OK && requestCode == PICK_FROM_ALBUM) {
            Uri photoUri = data.getData();
            Log.d(TAG, "PICK_FROM_ALBUM photoUri : >>>>>>>>>>>" + photoUri);

            Cursor cursor = null;

            try {
                 /*  Uri 스키마를
                content:/// 에서 file:/// 로  변경한다.*/

                String[] proj = {MediaStore.Images.Media.DATA};

                assert photoUri != null;
                cursor = getContext().getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

                Log.d(TAG, "tempFile_Album: >>>>>>>>>>>>>>" +tempFile);

                Log.d(TAG, "tempFile Uri : >>>>>>>>>>>>>>" + Uri.fromFile(tempFile));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            img1.setVisibility(View.GONE);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
                openImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String StringSubstring(File file) {
        String result = file.getName();
        return result;
    }
}

