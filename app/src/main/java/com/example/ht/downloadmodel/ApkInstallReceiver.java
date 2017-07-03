package com.example.ht.downloadmodel;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.content.ContentValues.TAG;
import static com.example.ht.downloadmodel.FileDownLoadManager.mApkPaths;

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
            if (downloadApkId == FileDownLoadManager.id) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(FileDownLoadManager.id);
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
                            String s_path = mApkPaths.get(FileDownLoadManager.id);
                            File file = new File(s_path);
                            Intent intentN = new Intent(Intent.ACTION_VIEW);
                            // 由于没有在Activity环境下启动Activity,设置下面的标签
                            intentN.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                            Uri apkUri = FileProvider.getUriForFile(context, "com.example.ht.downloadmodel.fileprovider", file);
                            //添加这一句表示对目标应用临时授权该Uri所代表的文件
                            intentN.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            intentN.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            context.startActivity(intentN);
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
                            Log.d(TAG, "onReceive: 安装包路径" + path);
                            Uri data = Uri.parse("file://" + path);
                            promptInstall(context, data);
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

    private void promptInstall(Context context, Uri date) {
        Intent promptInstall = new Intent(Intent.ACTION_VIEW)
                .setDataAndType(date, "application/vnd.android.package-archive");
        // FLAG_ACTIVITY_NEW_TASK 可以保证安装成功时可以正常打开 app
        promptInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(promptInstall);
    }

}
