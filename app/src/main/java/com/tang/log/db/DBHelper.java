package com.tang.log.db;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.tang.log.model.LogBean;

import org.xutils.DbManager;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.db.table.TableEntity;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Description:数据库帮助类
 * Author:qingxia
 * Created:2017/9/22 15:09
 * Version:
 */
public class DBHelper<T> {
    private DbManager db;

    /**
     * 构造方法
     *
     * @param version 数据库版本号
     */
    public DBHelper(int version) {
        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                //设置数据库名，默认xutils.db
                .setDbName("LogDB.db")
                //设置表创建的监听
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity table) {
                        Log.i("JAVA", "onTableCreated：" + table.getName());
                    }
                })
                //设置是否允许事务，默认true
                //.setAllowTransaction(true)
                //设置数据库路径，默认安装程序路径下
                //.setDbDir(new File("/mnt/sdcard/"))
                //设置数据库的版本号
                //.setDbVersion(1)
                //设置数据库更新的监听
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion,
                                          int newVersion) {
                    }
                })
                //设置数据库打开的监听
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        //开启数据库支持多线程操作，提升性能
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
        db = x.getDb(daoConfig);
    }

    private static DBHelper instance;

    public static synchronized DBHelper getInstance(int version) {
        if (null == instance) {
            instance = new DBHelper(version);
        }
        return instance;
    }

    /**
     * 插入一条数据
     *
     * @param
     */

    public void insertData(T t) {
        try {
            //数据库中没有存在对应逐渐的数据，则保存数据
            db.save(t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<T> queryData(Class<T> emtity) {
        try {
            List<T> messageList = db.findAll(emtity);
            return messageList;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自定义查询
     *
     * @return
     */
    public List<LogBean> queryAllData() {
        List<LogBean> tList = null;
        try {
            tList = db.selector(LogBean.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return tList;
    }

    /**
     * 根据级别查询日志
     *
     * @param type
     * @return
     */
    public List<LogBean> queryDataByType(int type) {
        List<LogBean> tList = null;
        try {
            tList = db.selector(LogBean.class).where("type", "=", type).findAll();
            ;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return tList;
    }

    /**
     * 查询指定级别日志总条数
     *
     * @param type
     * @return
     */
    public int queryCountByType(int type) {
        long count = 0;
        try {
            count = db.selector(LogBean.class).where("type", "=", type).count();
            ;
        } catch (DbException e) {
            e.printStackTrace();
        }
        return (int) count;
    }

    /**
     * 修改某个字段
     *
     * @param t
     * @param colum
     */
    public void updateData(T t, String colum) {
        try {
            db.update(t, colum);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除数据库
     */
    public void dropDB() {
        try {
            db.dropDb();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除某个表
     *
     * @param t
     */
    public void deleteAllData(Class<T> t) {
        try {
            db.dropTable(t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id删除t表的一条数据
     *
     * @param t
     * @param id
     */
    public void deleteOneData(Class<T> t, int id) {
        try {
            db.deleteById(t, id);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除某调数据
     *
     * @param t
     */
    public void deleteOneData(T t) {
        try {
            db.delete(t);
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据类型删除数据
     *
     * @param t
     * @param type
     */
    public void deleteOneDataByImei(Class<T> t, String type) {
        try {
            db.delete(t, WhereBuilder.b("type", "=", type));
        } catch (DbException e) {
            e.printStackTrace();
        }
    }


}
