package com.tang.log.utils;

import android.os.Environment;
import android.util.Log;

import com.tang.log.common.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;

import static com.tang.log.common.Constants.YAKDATA_PATH;

/**
 * Description:本地缓存工具类
 * Author:qingxia
 * Created:2018/3/6 10:31
 * Version:
 */
public class CacheUtil {

    /**
     * 获取文件名
     *
     * @return
     */
    public static String getFileNameById(String fileName) {
        File file = new File(YAKDATA_PATH);
        if (!file.exists())
            file.mkdirs();
        return fileName;
    }

    /**
     * 存储缓存文件：
     */
    public static void saveRecentSubList(String log) {
        String fileName = YAKDATA_PATH + File.separator + getFileNameById(Constants.originName);
        File file = new File(fileName);
        try {
//            if (!file.exists())
//                file.createNewFile();
//            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
//            oos.writeObject(getRecentSubList() + "\r\n" + log);
//            oos.flush();
//            oos.close();
            //使用RandomAccessFile是在原有的文件基础之上追加内容，
            //而使用outputstream则是要先清空内容再写入
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            //光标移到原始文件最后，再执行写入
            raf.seek(file.length());
            raf.write((log + "\n\n").getBytes());
            raf.close();
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
        String fileName = YAKDATA_PATH + File.separator + getFileNameById(Constants.originName);
        try {
            File file = new File(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"GB2312"));
            String readline = "";
            StringBuffer sb = new StringBuffer();
            while ((readline = br.readLine()) != null) {
                System.out.println("readline:" + readline);
                sb.append(readline);
            }
            br.close();
            System.out.println("读取成功：" + sb.toString());
            log = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return log;
    }
}
