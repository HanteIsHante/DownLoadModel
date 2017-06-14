package com.example.ht.downloadmodel;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static com.example.ht.downloadmodel.FileDownLoadManager.mApkPaths;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private Button down_load;
    private static final int REQUEST_CODE = 0;
    private ApkInstallReceiver mApkLoadSuccess;
    // 权限
    public static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        initView();
        mApkLoadSuccess = new ApkInstallReceiver();
        registerReceiver(mApkLoadSuccess, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    private void initView() {
        down_load = (Button) findViewById(R.id.down_load);

        down_load.setOnClickListener(this);
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
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
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
        String url = "https://og3tpc3bg.qnssl.com/Android/Netfits_Android_2.3.1.2814.apk";
        id = FileDownLoadManager.startDownload(getApplicationContext(), url, "Netfits_Android_2.3.1.2814.apk", "");
        Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();
    }

    /**
     * DownloadManager下载完成后会发出一个广播
     * android.intent.action.DOWNLOAD_COMPLETE 新建一个广播接收者即可：
     */
    public class ApkInstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                Log.d("", "onReceive: 下载完成。。。。。。");
                Toast.makeText(context, "下载完成。。。。。。", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "下载完成***********", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "下载完成$$$$$$$$$$$", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "下载完成%%%%%%%%%%%", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "下载完成&&&&&&&&&&&", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "下载完成@@@@@@@@@@@", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "下载完成###########", Toast.LENGTH_SHORT).show();
                long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (downloadApkId == id) {
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(id);
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    Cursor c = dm.query(query);
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        // 下载失败也会返回这个广播，所以要判断下是否真的下载成功
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {
                            // 获取下载好的 apk 路径
                            String path = null;
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                             /*   String uriString =
                                        c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));*/
//                                String path1 = Uri.parse(uriString).getPath();
//                                Uri data = Uri.parse("/storage/emulated/0/" + path1);
//                                promptInstall(data);
//                                path = new File(Uri.parse(uriString).getPath()).getAbsolutePath();
                                //  所在路径
                               /* String file_path = "/storage/emulated/0/" + getPath() + "/Netfits_Android_2.3.1.2814.apk";
                                setPermission(file_path);
//                                File apkFile =new File(file_path);
                                File fileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
*/
                                String s_path = mApkPaths.get(id);
                                File file = new File(s_path);
                                Intent intentN = new Intent(Intent.ACTION_VIEW);
                                // 由于没有在Activity环境下启动Activity,设置下面的标签
                                intentN.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                                Uri apkUri = FileProvider.getUriForFile(context, "com.example.ht.downloadmodel.fileprovider", file);
                                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                                intentN.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intentN.setDataAndType(apkUri, "application/vnd.android.package-archive");
                                getApplicationContext().startActivity(intentN);
                                /*
                                File file = new File(file_path);
                                if (file.exists()) {
                                    Log.d("", "onReceive: 存在文件路径");
                                }
//                                File apkFile = new File(uriString);
                                Intent intent_To = new Intent(Intent.ACTION_VIEW);
                                Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                                        "com.example.ht.downloadmodel.fileprovider", file);
                                intent_To.setDataAndType(contentUri,
                                        "application/vnd.android.package-archive");
                                intent_To.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                intent_To.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                getApplicationContext().startActivity(intent_To);*/
                            } else {
                                path = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                                Uri data = Uri.parse("file://" + path);
                                promptInstall(data);
                            }
                            // 提示用户安装
//                            Toast.makeText(context, "路径 位置：： " + uriString, Toast.LENGTH_SHORT).show();
//                            String path = "/storage/emulated/0/" +  getPath();
//                            promptInstall(data);
                        }
                    }
                }

            }
        }
    }

    private void promptInstall(Uri date) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(date, "application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功时可以正常打开 app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(promptInstall);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mApkLoadSuccess);
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
