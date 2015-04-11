package com.davie.xutilstest.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.ConditionVariable;
import android.os.Environment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.davie.xutilstest.R;
import com.davie.xutilstest.model.Book;
import com.lidroid.xutils.BitmapUtils;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.util.EventListener;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.zip.Inflater;

/**
 * User: davie
 * Date: 15-4-11
 */
public class BookAdapter extends BaseAdapter{
    private Context context;

    private List<Book> list;
    private LayoutInflater inflater;

    /**
     * 图片加载工具
     */
    private BitmapUtils bitmapUtils;

    public BookAdapter(Context context, List<Book> list) {
        if(context==null){
            throw new IllegalArgumentException("Context must not null ");
        }
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
        //bitmapUtils 构造有多种形式
        //1、采用默认的形式
//        bitmapUtils  = new BitmapUtils(context);
        //2、采用两个参数的构造,指定文件缓存目录
        File cacheFolder = null;
        String state = Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            File cacheDir = context.getExternalCacheDir();//获取外部缓存目录.外部文件缓存
            cacheFolder = new File(cacheDir,"images");
        }else {
            File cacheDir = context.getCacheDir();
            cacheFolder = new File(cacheDir,"images");
        }

        if(!cacheFolder.exists()){
            //完整创建 文件夹
            cacheFolder.mkdir();
        }
        //创建BitMaputils并且指定文件的缓存路径
        bitmapUtils = new BitmapUtils(context,cacheFolder.getAbsolutePath());

        //设置默认的"加载中"的图片
        bitmapUtils.configDefaultLoadingImage(R.drawable.ic_launcher);

        //设置"加载失败"的图片
        bitmapUtils.configDefaultLoadFailedImage(R.drawable.error);

    }

    @Override
    public int getCount() {
       return list.size();
    }

    @Override
    public Object getItem(int i) {
        Object ret = null;
        if (ret != null) {
            ret = list.get(i);
            return ret;
        }
        return ret;
    }

    /**
     * 当数据库显示的数据CursorAdapter时,这个方法返回记录的ID
     * 另一种情况:ListView显示纯静态的内容,并且ListView设置为多选的情况下,这个ID才有作用
     * @param
     * @return
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View ret = null;
        if(convertView==null){
            ret = convertView;
        }else {
            //inflate方法,推荐使用三个参数的.第二个参数是parent,第三个参数代表是否自动添加到父容器当中
            ret = inflater.inflate(R.layout.item_book, viewGroup,false);
        }

        //ViewHolder 部分
        ViewHolder holder = (ViewHolder) ret.getTag();
        if(holder==null){
            holder = new ViewHolder();
            holder.imgCover = (ImageView) ret.findViewById(R.id.item_book_cover);
            holder.txtTitle = (TextView) ret.findViewById(R.id.item_book_title);
            holder.txtAuthor = (TextView) ret.findViewById(R.id.item_book_auther);

            ret.setTag(holder);
        }

        //数据显示的部分

        Book book = list.get(position);

        String author = book.getAuthor();
        if (author != null) {
            holder.txtAuthor.setText(author);
        }

        //图片网址
        String cover = book.getCover();
        if (cover != null) {
            //TODO 加载图片
            //简单的形式,通过网络加载图片.并显示到ImageView控件上
            //1、第一个参数是一个泛型,支持所有的view控件
            //如果第一个参数是ImageView或者ImageButton,那么会直接设置setImageBitmap相当于src属性
            //如果是其他的控件,会自动设置background.
            bitmapUtils.display(holder.imgCover,cover);
        }

        //书名
        String title = book.getTitle();
        if (title != null) {
            holder.txtTitle.setText(title);
        }

        return ret;
    }

    /**
     * static类,在Adapter类加载的时候，自动的被虚拟机加载了,速度会快一些
     */
    private static class ViewHolder{
        public ImageView imgCover;
        public TextView txtTitle;
        public TextView txtAuthor;
    }
}
