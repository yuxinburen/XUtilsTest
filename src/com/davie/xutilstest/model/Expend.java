package com.davie.xutilstest.model;

/**
 * User: davie
 * Date: 15-4-11
 */

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 支出信息的实体
 */
@Table(name="expends") //代表当前类描述的是一个表DbUtils
public class Expend {

    @Id(column = "_id")//id 成员变量能够引用到 数据库表当中的主键
    private long id;

    //设置外键的部分,代表成员变量的数据从指定类型中加载.通过表内部的column指向到Category表中的_id上面,进行唯一的设置
    @Foreign(column = "cid", foreign = "_id")
    private Category category;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Column(column = "money",defaultValue = "0")
    private float money;

    @Column  //如果不指定名称,根据变量名来进行表的字段的设置.
    //如果采用不写名称的方式,当软件发布时,如果进行了"混淆",成员变量就会被自动改成a,b,c,.......列名也就跟着变了
    //因此,推荐指定列名,不推荐不写列名
    private long currentTime;


    public long getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(long currentTime) {
        this.currentTime = currentTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

}
