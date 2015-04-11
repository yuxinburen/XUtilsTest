package com.davie.xutilstest;


import android.media.JetPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.davie.xutilstest.model.Category;
import com.davie.xutilstest.model.Expend;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.DbModelSelector;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfoBuilder;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * User: davie
 * Date: 15-4-9
 */
public class TestFragment extends Fragment {

    @ViewInject(R.id.button1)
    private Button button1;
    @ViewInject(R.id.button2)
    private Button button2;
    @ViewInject(R.id.button3)
    private Button button3;

    @ViewInject(R.id.listview)
    private ListView listView;
    private DbUtils dbUtils;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //第一个参数：Context
        //第二个参数: 数据库名称
        //第三个参数:数据库版本号
        //第四个参数:数据库升级接口,等同于SQLiteOpenHelper中的onUpgrade
        dbUtils = DbUtils.create(
                getActivity(),
                "myrecord.db",
//                1 增加了分类信息,因此进行升级
                3,
                new DbUtils.DbUpgradeListener()
                {
            @Override
            public void onUpgrade(DbUtils db, int oldVersion, int newVersion) {
                //对新旧版本进行简单判断
//                if(oldVersion==1 && newVersion==2){
//                    //执行非查询的操作
//                    try {
//                        db.execNonQuery(" alter table expends add column cid long default 0 ");
//                    } catch (DbException e) {
//                        e.printStackTrace();
//                    }
//                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_test, container, false);

        //对于在Fragment当中的成员变量的UI控件.采用inject(Object object,View view)方法即可
        //实际上Obj就是自身的Fragment
        ViewUtils.inject(this, ret);

        button1.setText("登陆");
        button2.setText("注册");
        button3.setText("忘记密码");

        return ret;
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            //findAll(Class)这个方法,让DbUtils查询指定实体类代表的表的记录
            //对应的SQL语句select * from expends;
            List<Expend> expends = dbUtils.findAll(Expend.class);

            //expends 就是所有的数据了
            if (expends != null){
                for (Expend expend : expends) {
                    long currentTime = expend.getCurrentTime();
                    float money = expend.getMoney();
                    Log.i("DbUtils", "Record: " + currentTime + "," + money);
                }
            }
            //TODO 查询特定的一条数据

            //根据表中 主键的值,来进行查找,未找到 返回null
            Expend t1 = dbUtils.findById(Expend.class, 1L);
            if (t1 != null) {
                Log.i("DbUtils","ID 1 :"+t1.getMoney());
            }

            //查找特定范围内的记录
            //select * from expends where money between 100 and 1000
            //对于上面的 SQL 采用 findAll(Selector)
            //Selector 采用构建器模式
            WhereBuilder builder = WhereBuilder.b();
            //直接书写表达式的方式
            builder.expr(" money between 100 and 1000 ");

            List<Expend> all = dbUtils.findAll(Selector.from(Expend.class)
//                    .where("id","=",1L));k
                    .where(builder));
            if (all != null) {
                for (Expend expend: all){
                    Log.i("DbUtils","All:"+expend.getMoney());
                }
            }

            //findFirst(Selector)通过条件 查找第一条记录
            //例如：按照排序之后的记录查找,查找第一个记录,相当于取出最大值或者最小值。
            Expend money = dbUtils.findFirst(Selector.from(Expend.class)
                    .orderBy("money", true));//orderby(string columnName,boolean desc)
            if (money != null) {
                Log.i("DbUtils","money:"+money.getMoney());
            }

            //TODO 获取特定的一列数据
            DbModel dbModelFirst = dbUtils.findDbModelFirst(
                    DbModelSelector.from(Expend.class)
                            .orderBy("money", true)
                            .select("money", "_id")
            );

            if (dbModelFirst != null) {
                Log.i("DbUtils","money:"+dbModelFirst.getFloat("money"));
            }

            //案例:1、获取记录个数 select count(*) from xxx 返回是数字 个数
            DbModel dbModelFirst1 = dbUtils.findDbModelFirst(
                    DbModelSelector.from(Expend.class)
                            .select(" count(*)")
            );

            if (dbModelFirst1 != null) {
                //获取列的名称与数据
                HashMap<String, String> dataMap = dbModelFirst1.getDataMap();
                for (String key:dataMap.keySet()){
                    String value = dataMap.get(key);
                    Log.i("DbUtils",value);
                }
            }


            if (money != null) {
                //修改
                //1、第一种方式 ->update tableName set columnName1 = value1 ..... where ...
                //这种方法,隐含了id的判断
                money.setMoney(2000);
                dbUtils.update(money,"money");//直接存储对象的数据
            }

            //测试没有_id ,直接更新一条新的数据能否进行更新操作.
            Expend expend = new Expend();
            expend.setMoney(4000);
            expend.setCurrentTime(System.currentTimeMillis());
            dbUtils.update(expend,"money","currentTime");

        } catch (DbException e) {
            e.printStackTrace();
        }

        //采用where语句的方式,进行数据的更新
        //TODO 检查更新一条和多条的情况
        Expend expend = new Expend();
        expend.setMoney(1500);
        //使用wherebuilder
        WhereBuilder builder = WhereBuilder.b("money", "=", "998");
        try {
//            dbUtils.update(expend,builder,"money");
            dbUtils.update(expend,(WhereBuilder)null,"money");
        } catch (DbException e) {
            e.printStackTrace();
        }

        ///updateAll
        //UpdateAll使用WhereBuider的情况
        List<Expend> es = new LinkedList<>();
        Expend object = new Expend();
        object.setMoney(10);
        es.add(object);
        object = new Expend();
        object.setMoney(100);
        es.add(object);
        try {
            dbUtils.updateAll(es,(WhereBuilder)null,"money");
        } catch (DbException e) {
            e.printStackTrace();
        }

        //删除操作
//        dbUtils.deleteById();

    }

    @OnClick(R.id.button2)
    public void btn2OnClick(View v){

        //TODO 存数据

        //1、准备数据
        Expend todayExpend1 = new Expend();

        todayExpend1.setCurrentTime(System.currentTimeMillis());
        todayExpend1.setMoney(998);

        //TODO 设置分类信息
        Category category = new Category();
        category.setcName("出行");
        try {
            dbUtils.save(category);
        } catch (DbException e) {
            e.printStackTrace();
        }


        //2、执行存储方法
        try {
            dbUtils.save(todayExpend1);
            //添加或者修改
            dbUtils.saveOrUpdate(todayExpend1);
            //批量添加多条记录
            dbUtils.saveAll(new LinkedList<Expend>());

        } catch (DbException e) {
            e.printStackTrace();
        }

        //3、关闭数据库
        dbUtils.close();

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.button1)
    public void btn1OnClick(View v) {
        Toast.makeText(getActivity(), "Btn OnClick", Toast.LENGTH_SHORT).show();
        //HttpUtils进行网络的加载
        //必须new一个httpUtils才可以进行网络的加载
        //通常可以作为成员变量,new一次就好了,
        //无参构造默认15s超时,一个参数的,是用来设置超时时间的
//        HttpUtils httpUtils = new HttpUtils();
        HttpUtils httpUtils = new HttpUtils(1000 * 10);
        //设置能够同时并发访问网络的线程的数量
        httpUtils.configRequestThreadPoolSize(5);
        httpUtils.configUserAgent("ThisIsAclient");
        HttpHandler<String> httpHandler = httpUtils.send(
                HttpRequest.HttpMethod.GET,
                "http://www.baidu.com",
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i("HttpUtils","success"+responseInfo.result);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        Log.i("HttpUtils","fial : "+msg);
                    }
                });

        //实现post请求
        //使用带有RequestParams这个send方法,就可以进行数据提交
        //请求的参数 use=admin&password=123
        RequestParams params = new RequestParams();
        //1.实现key = value & key = value 的形式 (标准浏览器提交数据的形式)
        //post请求基本的数据提交形式
        params.addBodyParameter("user", "admin");
        params.addBodyParameter("password","123");


        httpUtils.send(
                HttpRequest.HttpMethod.POST,
                "http://www.baidu.com/",
                params,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });

        //实现文件上传的post请求
        //Http文件上传功能 提交的内部格式：multipart/form-data
        /**
         * 格式的案例：
         * 如果普通的提交采用:key=value&key=value
         * 那么multipart/form-data格式:
         * Content-Type:multipart/form-data; boundcy=AbCDe
         *
         * 以下是数据
         * -AbCDe
         * Content-Type: name=user
         *
         * admin
         * -AbCDe
         * Content-Type: name=password
         *
         * 123
         * --AbCDe--
         *
         */
        //实现以上格式,对于xUtils当中的HttpUtils只要在RequestParams添加一个文件(File)的参数
        RequestParams params1 = new RequestParams();
        //实现文件上传功能
        //可以使用File类对象作为参数传递 File必须可读,必须是文件,不能设置目录上传
        params1.addBodyParameter("file",new File("具体需要上传的文件"));
        httpUtils.send(HttpRequest.HttpMethod.POST,
                "http://www.baidu.com",
                params1,
                new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {

                    }
                });


        ////////////////////////////////////////////
        /**
         * 文件的下载
         * download
         */

        //获取存储卡文件位置
        String state = Environment.getExternalStorageState();

        if(Environment.MEDIA_MOUNTED.equals(state)){
            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory,"img.png");
            httpUtils.download("https://www.baidu.com/img/bdlogo.png", file.getAbsolutePath(), new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                }

                @Override
                public void onFailure(HttpException error, String msg) {

                }
            });
        }



    }

    @OnItemClick(R.id.listview)
    public void listItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}