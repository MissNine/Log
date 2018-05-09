package com.tang.log.model;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Description:日志bean
 * Author:qingxia
 * Created:2018/5/9 14:33
 * Version:onCreated = "sql"：当第一次创建表需要插入数据时候在此写sql语句
 */
@Table(name = "LogInfo")
public class LogBean {

    public LogBean(){
    }

    /**
     * name = "id"：数据库表中的一个字段
     * isId = true：是否是主键
     * autoGen = true：是否自动增长
     * property = "NOT NULL"：添加约束
     */
    @Column(name = "id", isId = true, autoGen = true, property = "NOT NULL")
    private Integer id;
    @Column(name = "type")
    private Integer type;//日志级别
    @Column(name = "description")
    private String description;//日志描述
    @Column(name = "date")
    private long date;//日志生成时间

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
