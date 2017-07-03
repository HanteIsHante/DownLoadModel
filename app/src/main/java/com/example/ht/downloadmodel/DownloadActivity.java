package com.example.ht.downloadmodel;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private Button down_load;
    private static final int REQUEST_CODE = 0;
    //    private ApkInstallReceiver mApkLoadSuccess;
    // 权限
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private long id;
    private Button check_net;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();
//        mApkLoadSuccess = new ApkInstallReceiver();
//        registerReceiver(mApkLoadSuccess, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void initView() {
        down_load = (Button) findViewById(R.id.down_load);

        down_load.setOnClickListener(this);
        check_net = (Button) findViewById(R.id.check_net);
        check_net.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.down_load:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PermissionCheck permissionCheck = new PermissionCheck(getApplicationContext());
                    if (permissionCheck.leaksPermissions(PERMISSIONS)) {
                        requestPermissions(PERMISSIONS, REQUEST_CODE);
                    } else {
                        downLoad();
                    }
                } else {
                    downLoad();
                }
                break;
            case R.id.check_net:
                NetworkCheck.isNetworkAvailable(getApplicationContext());
                break;
        }
    }

    // 获取权限返回说明
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "获得权限", Toast.LENGTH_SHORT).show();
                downLoad();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // ask permission
                boolean b = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    b = shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (!b) { // 当用户勾选 不在询问时会执行此
                        Toast.makeText(this, "需要此权限", Toast.LENGTH_SHORT).show();
                    } else {
                        downLoad();
                    }
                }

            }
        }
    }

    private void downLoad() {
        NotificationManager notificationManagerCompat = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder(getApplicationContext());
        mNotificationBuilder.setAutoCancel(false);
        mNotificationBuilder.setOngoing(true);
        mNotificationBuilder.setWhen(0);
        mNotificationBuilder.setPriority(Notification.PRIORITY_MAX);
        mNotificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("affaaf")
                .setContentText("afsfdadfa");
        Notification build = mNotificationBuilder.build();
        notificationManagerCompat.notify(1, build);
        String url = "https://og3tpc3bg.qnssl.com/Android/Netfits_Android_2.3.1.2814.apk";
        id = FileDownLoadManager.startDownload(getApplicationContext(), url, "Netfits_Android_2.3.1.2814.apk", "");
        Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver(mApkLoadSuccess);
    }

    public static void setPermission(String filePath) {
        String command = "chmod " + "777" + " " + filePath;
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
