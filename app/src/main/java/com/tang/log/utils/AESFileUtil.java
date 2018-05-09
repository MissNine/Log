package com.tang.log.utils;

import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Description:
 * Author:qingxia
 * Created:2018/5/8 10:01
 * Version:
 */
public class AESFileUtil {
    public static final String VIPARA = "5556667888999222";
    private static final String TAG = AESFileUtil.class.getSimpleName();

    /**
     * 初始化 AES Cipher
     *
     * @param sKey
     * @param cipherMode
     * @return
     */
    private static Cipher initAESCipher(String sKey, int cipherMode) {
        //创建Key gen
        KeyGenerator keyGenerator = null;
        Cipher cipher = null;
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
            SecretKeySpec key = new SecretKeySpec(sKey.getBytes(), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, key, zeroIv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidAlgorithmParameterException e) { // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cipher;
    }

    /**
     * 对文件进行AES加密
     *
     * @param key
     * @param sourceFilePath 　原文件地址
     * @param destFilePath   加密后的文件地址
     * @return
     */
    public static File encryptFile(String key, String sourceFilePath, String destFilePath) {
        System.out.printf(sourceFilePath);
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            System.out.printf(sourceFilePath + "---" + sourceFile.getAbsolutePath());
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key, Cipher.ENCRYPT_MODE);
                //以加密流写入文件
                CipherInputStream cipherInputStream = new CipherInputStream(in, cipher);
                byte[] cache = new byte[1024];
                int nRead = 0;
                while ((nRead = cipherInputStream.read(cache)) != -1) {
                    out.write(cache, 0, nRead);
                    out.flush();
                }
                cipherInputStream.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates. }
            }
            return destFile;
        }
    }

    /**
     * AES方式解密文件
     *
     * @param key
     * @param sourceFilePath 源文件地址
     * @param destFilePath   解密后的文件地址
     * @return
     */
    public static File decryptFile(String key, String sourceFilePath, String destFilePath) {
        FileInputStream in = null;
        FileOutputStream out = null;
        File destFile = null;
        File sourceFile = null;
        try {
            sourceFile = new File(sourceFilePath);
            destFile = new File(destFilePath);
            if (sourceFile.exists() && sourceFile.isFile()) {
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();
                }
                destFile.createNewFile();
                in = new FileInputStream(sourceFile);
                out = new FileOutputStream(destFile);
                Cipher cipher = initAESCipher(key, Cipher.DECRYPT_MODE);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(out, cipher);
                byte[] buffer = new byte[1024];
                int r;
                while ((r = in.read(buffer)) >= 0) {
                    cipherOutputStream.write(buffer, 0, r);
                }
                cipherOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
            }
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
                e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return destFile;
    }

    //    private static final String CipherMode = "AES/ECB/PKCS5Padding";使用ECB加密，不需要设置IV，但是不安全
    private static final String CipherMode = "AES/CFB/NoPadding";//使用CFB加密，需要设置IV

    /**
     * 对字符串加密
     *
     * @param key  密钥
     * @param data 源字符串
     * @return 加密后的字符串
     */
    public static String encrypt(String key, String data) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
            byte[] encrypted = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 对字符串解密
     *
     * @param key  密钥
     * @param data 已被加密的字符串
     * @return 解密得到的字符串
     */
    public static String decrypt(String key, String data) throws Exception {
        try {
            byte[] encrypted1 = Base64.decode(data.getBytes(), Base64.DEFAULT);
            Cipher cipher = Cipher.getInstance(CipherMode);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keyspec, new IvParameterSpec(
                    new byte[cipher.getBlockSize()]));
            byte[] original = cipher.doFinal(encrypted1);
            return new String(original, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
