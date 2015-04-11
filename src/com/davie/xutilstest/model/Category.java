package com.davie.xutilstest.model;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * User: davie
 * Date: 15-4-11
 */
@Table(name="categories")
public class Category {
    @Id(column = "_id")
    private long id;

    @Column(column = "cname")
    private String cName;

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
