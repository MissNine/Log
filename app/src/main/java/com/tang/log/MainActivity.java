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
import com.tang.log.db.DBHelper;
import com.tang.log.model.LogBean;
import com.tang.log.utils.AESFileUtil;
import com.tang.log.utils.CacheUtil;
import com.tang.log.utils.LogConfig;
import com.tang.log.utils.LogPrint;
import com.tang.log.utils.PermissionUtil;
import com.tang.log.utils.WLog;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

import java.io.File;
import java.util.List;

import static com.tang.log.common.Constants.YAKDATA_PATH;
import static com.tang.log.utils.CacheUtil.getFileNameById;

@ContentView(R.layout.activity_main)
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
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        mPermissionUtil = new PermissionUtil(this, this);
        if (isRequireCheck) {
            if (mPermissionUtil.lacksPermissions(PERMISSIONS)) {
                mPermissionUtil.requestPermissions(PERMISSIONS); // 请求权限
                isRequireCheck = true;
            } else {
                isRequireCheck = false;
            }
        }
        try {
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initView() throws Exception {
        if (dbHelper == null) {
            dbHelper = DBHelper.getInstance(1);
        }
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mBtnShowLog = findViewById(R.id.btn_show_log);
        mBtnEntry = findViewById(R.id.btn_entry);
        mBtnDetry = findViewById(R.id.btn_detry);
        mBtnUpLoad = findViewById(R.id.btn_upload);
        mTxtLog = findViewById(R.id.txt_log);

        String entry = AESFileUtil.encrypt(Constants.AesKey, "123456789asdfgh哈哈哈");
        String detry = AESFileUtil.decrypt(Constants.AesKey, entry);
        Log.e("MainActivity", "123456789asdfgh哈哈哈   entry:" + entry + ",detry:" + detry);

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
                mTxtLog.setText("日志已加密保存");
                try {
                    //解密数据库中的日志
                    descryFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //生成加密文件
                AESFileUtil.encryptFile(Constants.AesKey, YAKDATA_PATH + File.separator + getFileNameById(Constants.originName), YAKDATA_PATH + File.separator + getFileNameById(Constants.encryptName));
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
                try {
                    descryFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTxtLog.setText("解密导出到本地地址：手机目录/Log/cachedataLog");
            }
        });
        mBtnDetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AESFileUtil.decryptFile(Constants.AesKey, YAKDATA_PATH + File.separator + getFileNameById(Constants.encryptName), YAKDATA_PATH + File.separator + getFileNameById(Constants.decryptName));
                mTxtLog.setText("解密文件地址：手机目录/Log/DecreyLog");
            }
        });
        mBtnUpLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    descryFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AESFileUtil.encryptFile(Constants.AesKey, YAKDATA_PATH + File.separator + getFileNameById(Constants.originName), YAKDATA_PATH + File.separator + getFileNameById(Constants.encryptName));
               ////TODO:这里将加密好的文件上传到服务器，上传成功后删除数据库中的日志信息
                mTxtLog.setText("加密文件地址：手机目录/Log/EntryLog");
            }
        });
    }

    /**
     * 解密导出到本地
     *
     * @throws Exception
     */
    public void descryFile() throws Exception {
        StringBuffer stringBuffer = new StringBuffer(getLogByType(LogConfig.ERROR)).append(getLogByType(LogConfig.DEBUG))
                .append(getLogByType(LogConfig.INFO)).append(getLogByType(LogConfig.WARN)).append(getLogByType(LogConfig.VERBOSE));
        CacheUtil.saveRecentSubList(stringBuffer.toString());
    }

    /**
     * 根据级别获取日志信息
     *
     * @param type
     * @return
     * @throws Exception
     */
    public String getLogByType(int type) throws Exception {
        StringBuffer stringBuffer = new StringBuffer();
        List<LogBean> logBeanList = dbHelper.queryDataByType(type);
        String typeStr = "";
        if (type == LogConfig.ERROR) {
            typeStr = "ERROR";
        } else if (type == LogConfig.DEBUG) {
            typeStr = "DEBUG";
        } else if (type == LogConfig.INFO) {
            typeStr = "INFO";
        } else if (type == LogConfig.WARN) {
            typeStr = "WARN";
        } else if (type == LogConfig.VERBOSE) {
            typeStr = "VERBOSE";
        } else {
            typeStr = "OTHER";
        }
        if (logBeanList != null && logBeanList.size() > 0) {
            int count = logBeanList.size();
            stringBuffer.append("\n\nLog Type:" + typeStr + "\rCount:").append(count).append("\n\n");
            for (LogBean logBean : logBeanList) {
                //解密日志信息
                String detryLog = AESFileUtil.decrypt(Constants.AesKey, logBean.getDescription());
                stringBuffer.append(detryLog);
            }
        }
        return stringBuffer.toString();
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
