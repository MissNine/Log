package com.tang.log.common;

import android.os.Environment;

/**
 * Description:常量类
 * Author:qingxia
 * Created:2018/5/8 10:49
 * Version:
 */
public class Constants {
    //日志存储路径
    public static final String YAKDATA_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Log";
    //aes加解密密钥
    public static String AesKey = "1236547899874563";
    //加密文件名
    public static final String encryptName = "EntryLog";
    //解密文件名
    public static final String decryptName = "DecreyLog";
    //源文件名
    public static final String originName = "cachedataLog";
    //上传地址
    public static final String upLoadUrl = "";
}
