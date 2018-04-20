package com.tang.log.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Description:本地缓存工具类
 * Author:qingxia
 * Created:2018/3/6 10:31
 * Version:
 */
public class CacheUtil {
    //临时图片路径
    private static final String YAKDATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Log/";

    /**
     * 获取文件名
     *
     * @return
     */
    public static String getFileNameById() {
        File file = new File(YAKDATA_PATH);
        if (!file.exists())
            file.mkdirs();
        String fileName = "cachedata_log.dat";
        return fileName;
    }

    /**
     * 存储缓存文件：
     */
    public static void saveRecentSubList(String log) {
        String fileName = YAKDATA_PATH + File.separator + getFileNameById();
        File file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(getRecentSubList() + "\r\n" + log);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取缓存文件：
     */
    public static String getRecentSubList() {
        String log = "";
        String fileName = YAKDATA_PATH + File.separator + getFileNameById();
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            log = (String) ois.readObject();
            Log.e("-------TAG", log);
            ois.close();
        } catch (Exception e) {
            return log;
        }
        return log;
    }
}
