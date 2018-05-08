package com.tang.log;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tang.log.common.Constants;
import com.tang.log.utils.AESFileUtil;
import com.tang.log.utils.CacheUtil;
import com.tang.log.utils.PermissionUtil;
import com.tang.log.utils.WLog;

import java.io.File;

import static com.tang.log.common.Constants.YAKDATA_PATH;
import static com.tang.log.utils.CacheUtil.getFileNameById;

public class MainActivity extends Activity {
    private Button mBtnShowLog, mBtnStart, mBtnEntry, mBtnDetry, mBtnUpLoad;
    private TextView mTxtLog;
    // 所需的全部权限
    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private PermissionUtil mPermissionUtil;
    //是否需要检测权限
    public static boolean isRequireCheck = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPermissionUtil = new PermissionUtil(this, this);
        if (isRequireCheck) {
            if (mPermissionUtil.lacksPermissions(PERMISSIONS)) {
                mPermissionUtil.requestPermissions(PERMISSIONS); // 请求权限
                isRequireCheck = true;
            } else {
                isRequireCheck = false;
            }
        }
        initView();
    }

    public void initView() {
        mBtnStart = findViewById(R.id.btn_start);
        mBtnShowLog = findViewById(R.id.btn_show_log);
        mBtnEntry = findViewById(R.id.btn_entry);
        mBtnDetry = findViewById(R.id.btn_detry);
        mBtnUpLoad = findViewById(R.id.btn_upload);
        mTxtLog = findViewById(R.id.txt_log);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//        testV();
//        testD();
//        testI();
//        testW();
//        testE();
                try {
                    int a = 3;
                    int b = 0;
                    int c = a / b;
                    System.out.println(c);
                } catch (Exception e) {
                    WLog.w("MainActivity", "除法运算异常", e, true);
                    WLog.e("MainActivity", "这是一条错误信息错误", e);
                }
            }
        });
        mBtnShowLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtLog.setText(CacheUtil.getRecentSubList());
            }
        });
        mBtnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AESFileUtil.encryptFile(Constants.AesKey, YAKDATA_PATH + File.separator + getFileNameById(Constants.originName),YAKDATA_PATH + File.separator + getFileNameById(Constants.encryptName));
            }
        });
        mBtnDetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AESFileUtil.decryptFile(Constants.AesKey, YAKDATA_PATH + File.separator + getFileNameById(Constants.encryptName),YAKDATA_PATH + File.separator + getFileNameById(Constants.decryptName));
            }
        });
        mBtnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    private void testV() {
        long endTime = System.currentTimeMillis();
        WLog.v("testV", "测试testV  " + "Verbose 通常用于开发中调试输出，开发结束后不建议留存于代码");
    }

    private void testD() {
        long endTime = System.currentTimeMillis();
        WLog.d("MainActivity", "测试testD  " + "Debug 通常用于在调试中输出，此信息给程序员看");
    }

    private void testI() {
        long endTime = System.currentTimeMillis();
        WLog.i("UserDao", "测试testI  " + "Info 通常用于在输出程序状态信息，此信息给用户看，应该更加友好。例如某账号登录成功");
    }

    private void testW() {

        try {
            int a = 3;
            int b = 0;
            int c = a / b;
            System.out.println(c);
        } catch (Exception e) {
            WLog.w("", "除法运算异常");
            e.printStackTrace();
        }
    }

    private void testE() {

        try {
            int a = 3;
            int b = 0;
            int c = a / b;
            System.out.println(c);
        } catch (Exception e) {
            Log.e("aa", "aa", e);
        }

    }

    private static String generateTag(StackTraceElement stack) {
        String tag = "%s.%s(L:%d)";
        String className = stack.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        tag = String.format(tag, stack.getClassName(), className, stack.getLineNumber());
        tag = "测试测试测试" + ":" + tag;
        return tag;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == mPermissionUtil.PERMISSION_REQUEST_CODE && mPermissionUtil.hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = false;
        } else {
            isRequireCheck = true;
            mPermissionUtil.showMissingPermissionDialog();
        }
    }
}
