package cn.edu.xidian.xidian3d.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.edu.xidian.xidian3d.R;
import cn.edu.xidian.xidian3d.opengl.Model;
import cn.edu.xidian.xidian3d.opengl.StlModel;
import cn.edu.xidian.xidian3d.opengl.XDGlSurfaceView;
import cn.edu.xidian.xidian3d.util.FileUtils;
import cn.edu.xidian.xidian3d.util.LogUtils;

public class MainActivity extends FragmentActivity {
    ViewGroup mContainer;
    TextView mTitle;
    TextView mOpenFile;
    private XDGlSurfaceView mSurfaceView;
    private Model mModel;
    private String filePath;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avtivity_main);
        checkPermission();
        Intent intent = getIntent();
        if (intent != null) {
            Uri uri = intent.getData();
            if (uri != null) {
                filePath = FileUtils.getFilePath(uri);
            }
        }
        initView();
        setData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.d("====> 111");
        if (intent != null) {
            LogUtils.d("====> 222");
            String schema = intent.getScheme();
            Uri uri = intent.getData();
            if (uri != null) {
                LogUtils.d("====>" + schema + "  " + uri.getPath());
                filePath = FileUtils.getFilePath(uri);
                setData();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSurfaceView != null) {
            mSurfaceView.onResume();
        }
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int REQUEST_CODE_CONTACT = 101;
            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
            //验证是否许可权限
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
                    return;
                }
            }
        }
    }

    private void initView() {
        mContainer = findViewById(R.id.main_content);
        mTitle = findViewById(R.id.title);
        mOpenFile = findViewById(R.id.open_file);
        mOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setData() {
        loadSampleModel();
        mSurfaceView = new XDGlSurfaceView(this, mModel);
        mContainer.removeAllViews();
        mContainer.addView(mSurfaceView);
        mTitle.setText(fileName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSurfaceView != null) {
            mSurfaceView.onPause();
        }
    }

    private void loadSampleModel() {
        try {
            InputStream stream;
            if (TextUtils.isEmpty(filePath) || !".stl".equals(filePath.substring(filePath.lastIndexOf(".")))) {
                stream = getApplicationContext().getAssets().open("spine.stl");
                fileName = "spine";
            } else {
                File file = new File(filePath);
                stream = new FileInputStream(file);
                fileName = file.getName();
            }
            mModel = new StlModel(stream);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "请打开app存储空间权限以正常运行", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            LogUtils.d("====> result");
            Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            filePath = FileUtils.getFilePath(uri);
            LogUtils.d("====> " + filePath);
            setData();
        }
    }
}
