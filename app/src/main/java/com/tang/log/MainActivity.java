package com.tang.log;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tang.log.utils.CacheUtil;
import com.tang.log.utils.WLog;

public class MainActivity extends Activity {
    private Button mBtnShowLog;
    private TextView mTxtLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnShowLog = findViewById(R.id.btn_show_log);
        mTxtLog = findViewById(R.id.txt_log);
        mBtnShowLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTxtLog.setText(CacheUtil.getRecentSubList());
            }
        });
//        testV();
//        testD();
//        testI();
//        testW();
//        testE();

        try{
            int a = 3;
            int b = 0;
            int c = a / b;
            System.out.println(c);
        }catch (Exception e){
            WLog.w("MainActivity","除法运算异常",e,true);
            WLog.e("MainActivity","这是一条错误信息错误",e);
        }

    }


    private void testV(){
        long endTime=System.currentTimeMillis();
        WLog.v("testV", "测试testV  " + "Verbose 通常用于开发中调试输出，开发结束后不建议留存于代码");
    }
    private void testD(){
        long endTime=System.currentTimeMillis();
        WLog.d("MainActivity", "测试testD  " + "Debug 通常用于在调试中输出，此信息给程序员看");
    }
    private void testI(){
        long endTime=System.currentTimeMillis();
        WLog.i("UserDao", "测试testI  " + "Info 通常用于在输出程序状态信息，此信息给用户看，应该更加友好。例如某账号登录成功");
    }
    private void testW(){

        try{
            int a = 3;
            int b = 0;
            int c = a / b;
            System.out.println(c);
        }catch (Exception e){
            WLog.w("", "除法运算异常");
            e.printStackTrace();
        }
    }
    private void testE(){

        try{
            int a = 3;
            int b = 0;
            int c = a / b;
            System.out.println(c);
        }catch (Exception e){
            Log.e("aa","aa",e);
        }

    }
    private static String generateTag(StackTraceElement stack){
        String tag = "%s.%s(L:%d)";
        String className = stack.getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        tag = String.format(tag, stack.getClassName(),className,stack.getLineNumber());
        tag = "测试测试测试"+":"+tag;
        return tag;
    }

}
