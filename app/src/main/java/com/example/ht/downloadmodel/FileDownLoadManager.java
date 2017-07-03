package com.example.ht.downloadmodel;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.LongSparseArray;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import static android.content.ContentValues.TAG;

/**
 * Google  官方下载
 */

public class FileDownLoadManager {

    private static String app_name;
    private static File file;
    public static LongSparseArray<String> mApkPaths;
    public static long id;

    public static long startDownload(Context context, String uri, String title, String description) {
        clearApk(context, title);
        mApkPaths = new LongSparseArray<>();
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);



        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);
        req.setShowRunningNotification(true);
        // 此方法表示在下载过程中通知栏会一直显示该下载，在下载完成后仍然会显示，
        // 直到用户点击该通知或者消除该通知。还有其他参数可供选择
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        //点击正在下载的Notification进入下载详情界面，如果设为true则可以看到下载任务的进度，如果设为false，则看不到我们下载的任务
        req.setVisibleInDownloadsUi(true);
        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
//        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "update.apk");
        //第二种
        //file:///storage/emulated/0/Download/update.apk
//        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "update.apk");
//       TODO   req.setDestinationInExternalPublicDir(getPath(), title);
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), title);
        req.setDestinationUri(Uri.fromFile(file));
        //第三种 自定义文件路径

//        req.setDestinationUri(Uri.parse("/sdcard/"));
//         当路径中该文件已经存在时，会自动以迭代的方式命名。
        // 此方法表示在下载过程中通知栏会一直显示该下载，在下载完成后仍然会显示，
        // 直到用户点击该通知或者消除该通知。还有其他参数可供选择
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // 通过setAllowedNetworkTypes方法可以设置允许在何种网络下下载，
        // 也可以使用setAllowedOverRoaming方法，它更加灵活
        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);

        // 设置一些基本显示信息
        req.setTitle(title);
        req.setDescription(description);
        req.setMimeType("application/vnd.android.package-archive");
        //把DownloadId保存到本地

        //加入下载队列
        id = dm.enqueue(req);
        mApkPaths.put(id, file.getAbsolutePath());
        return id;
    }

    /**
     * @return 下载路径
     */
    static String getPath() {
//        String path = "netfits_app";
//        return path;
        return file.getAbsolutePath();
    }

    /**
     * 删除之前的apk
     *
     * @param apkName apk名字
     * @return
     */
    public static File clearApk(Context context, String apkName) {
        File apkFile = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
            String md5 = getMd5(apkFile);
        Log.d(TAG, "clearApk: 获取到的 MD5 信息：：： " + md5);


        if (apkFile.exists()) {
            apkFile.delete();
        }
        return apkFile;
    }

    public static String getMd5(File file){
        String value = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

}
