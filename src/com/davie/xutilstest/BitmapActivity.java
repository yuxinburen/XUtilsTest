package com.davie.xutilstest;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ListView;
import com.davie.xutilstest.adapter.BookAdapter;
import com.davie.xutilstest.model.Book;

import java.util.LinkedList;

/**
 * User: davie
 * Date: 15-4-11
 */
public class BitmapActivity extends Activity {

    private LinkedList<Book> books;
    private BookAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitmap);

        ListView listView = (ListView) findViewById(R.id.book_list);

        if (listView != null) {
            books = new LinkedList<>();
            adapter = new BookAdapter(this,books);
            listView.setAdapter(adapter);

        }
    }
}