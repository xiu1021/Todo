package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ListView listView;
    Button add;//添加按钮
    TextView note_id;//向其他界面传值
    ArrayList<HashMap<String, String>> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);
        add = (Button) findViewById(R.id.btn_add);

        add.setOnClickListener(this);

        //通过list获取数据库表中的所有id和title，通过ListAdapter给listView赋值
        final NoteOperator noteOperator = new NoteOperator(MainActivity.this);
        list = noteOperator.getNoteList();
        final ListAdapter listAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.item,
                new String[]{"id", "title"}, new int[]{R.id.note_id, R.id.note_title});
        listView.setAdapter(listAdapter);

        //通过添加界面传来的值判断是否要刷新listView
        Intent intent = getIntent();
        int flag = intent.getIntExtra("Insert", 0);
        if (flag == 1) {
            list = noteOperator.getNoteList();
            listView.setAdapter(listAdapter);
        }

        if (list.size() != 0) {
            //点击listView的任何一项跳到详情页面
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long i) {

                    String id = list.get(position).get("id");
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, DetailActivity.class);
                    intent.putExtra("note_id", Integer.parseInt(id));
                    startActivity(intent);
                }
            });

            //长按实现对列表的删除
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(final AdapterView<?> adapterView, View view, final int position, long l) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("确定删除？");
                    builder.setTitle("提示");

                    //添加AlterDialog.Builder对象的setPositiveButton()方法
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            String id = list.get(position).get("id");
                            noteOperator.delete(Integer.parseInt(id));
                            list.remove(position);
                            //listAdapter.notify();
                            listView.setAdapter(listAdapter);
                        }
                    });

                    //添加AlterDialog.Builder对象的setNegativeButton()方法
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.create().show();
                    return true;
                }
            });
        } else {
            Toast.makeText(this, "暂无待办事项，请添加", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AddActivity.class);
        MainActivity.this.startActivity(intent);
    }
}