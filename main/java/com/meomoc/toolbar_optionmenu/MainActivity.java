package com.meomoc.toolbar_optionmenu;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseAdapter db = null;
    private MyAdapter adapter = null;
    private ArrayList<String[]> rows = null;
    private static final String BUSINESS = "business";
    private ArrayList<Integer> id_array = new ArrayList<Integer>();
    private ArrayList<String> title_array = new ArrayList<String>();
    private ArrayList<String> timeline_array = new ArrayList<String>();
    private ArrayList<String> content_array  = new ArrayList<String>();
    final Context context = this;
    EditText my_title;
    EditText my_type; EditText my_content;

    String[] business = new String[] {};
    String[] timeline = new String[] {};
    String[] content = new String [] {};
    String[] CONTEXT_MENU = new String[] {"Edit", "Delete"};

    private ListView listview;
    //private ListAdapter myAdapter;

    public static final int MENU_ADD = 1;
    public static final int MENU_NAME = 2;
    public static final int MENU_TIME = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseAdapter(MainActivity.this);
        db.open();
        int business_rows = db.countRows(BUSINESS);
        if(business_rows == 0){
            db.setupBusiness(business, timeline, content);
        }
        populate_data();

        listview = (ListView) findViewById(R.id.list);
        adapter = new MyAdapter(this,title_array,timeline_array,content_array);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //String value = adapterView.getItemAtPosition(i).toString();
                //Toast.makeText(MainActivity.this,adapterView.getItemAtPosition(i).getClass().getName(), Toast.LENGTH_SHORT).show();
                Intent intent;
                intent = new Intent(MainActivity.this, ViewSelectedEntry.class);
                intent.putExtra("Title", title_array.get(i));
                intent.putExtra("Date", timeline_array.get(i));
                intent.putExtra("Content", content_array.get(i));
                startActivity(intent);
                //Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.game_menu, menu);
        menu.add(Menu.NONE, MENU_ADD, Menu.NONE, "Add new entry");
        menu.add(Menu.NONE, MENU_NAME, Menu.NONE, "Order by name");
        menu.add(Menu.NONE, MENU_TIME, Menu.NONE, "Order by date");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case MENU_ADD:
                addDialog();
                return true;
            case MENU_NAME:
                orderByName();

                return true;

            case MENU_TIME:
                orderByDate();
                return true;

        }
        return false;
    }

    public void orderByName(){
        id_array.removeAll(id_array);
        title_array.removeAll(title_array);
        timeline_array.removeAll(timeline_array);
        content_array.removeAll(content_array);

        rows = db.orderByName(BUSINESS);
        for (String[]p : rows){
            id_array.add(Integer.parseInt(p[0]));
            title_array.add(p[1]);
            timeline_array.add(p[2]);
            content_array.add(p[3]);
        }

        adapter = new MyAdapter(this,title_array,timeline_array,content_array);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);

    }

    public void orderByDate(){
        id_array.removeAll(id_array);
        title_array.removeAll(title_array);
        timeline_array.removeAll(timeline_array);
        content_array.removeAll(content_array);

        rows = db.orderByDate(BUSINESS);
        for (String[]p : rows){
            id_array.add(Integer.parseInt(p[0]));
            title_array.add(p[1]);
            timeline_array.add(p[2]);
            content_array.add(p[3]);
        }

        adapter = new MyAdapter(this,title_array,timeline_array,content_array);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);
    }


    public void addDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_list);
        dialog.setTitle("CREATE NEW ENTRY");
        my_title = (EditText) dialog.findViewById(R.id.tittle);
        my_type = (EditText) dialog.findViewById(R.id.date);
        my_content = (EditText) dialog.findViewById(R.id.content);
        my_title.setHint("New title here");
        my_type.setHint("Date of entry('YYYY-MM-DD')");
        my_content.setHint("Content");

        Button add = (Button) dialog.findViewById(R.id.btnAdd);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.insertBusiness(my_title.getText().toString(),my_type.getText().toString(), my_content.getText().toString());
                if (id_array.isEmpty()) {
                    id_array.add(1);
                }
                id_array.add(id_array.get(id_array.size()-1)+1);
                title_array.add(my_title.getText().toString());
                timeline_array.add(my_type.getText().toString());
                content_array.add(my_content.getText().toString());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        menu.setHeaderTitle(title_array.toArray(business)[info.position]);
        menu.add(0,v.getId(),0,CONTEXT_MENU[0]);
        menu.add(0,v.getId(),0,CONTEXT_MENU[1]);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (item.getTitle()==CONTEXT_MENU[0]) {
            edit(info.position);
        }else if (item.getTitle()==CONTEXT_MENU[1]) {
            delete(CONTEXT_MENU[1], info.position);
        }else { return false;}
        return true;
    }

    public void edit(int i){
        //int i= m-1;

        statusDialog(title_array.get(i), timeline_array.get(i), content_array.get(i),  id_array.get(i), i);
        adapter.notifyDataSetChanged();
    }

    public void delete(String tittle, int i){

        //int i = m-1;
        //Toast.makeText(this, "You selected :"+title_array.get(i)+","+tittle, Toast.LENGTH_LONG).show();

        db.delete_business(id_array.get(i));
        id_array.remove(i);
        title_array.remove(i);
        timeline_array.remove(i);
        content_array.remove(i);
        adapter.notifyDataSetChanged();
    }

    private void populate_data(){
        rows = db.get_all_from_table(BUSINESS);
        for(String[] p : rows){
            id_array.add(Integer.parseInt(p[0]));
            title_array.add(p[1]);
            timeline_array.add(p[2]);
            content_array.add(p[3]);
        }
    }

    private void statusDialog(String tittle, String type, String content, int id, int i){
        final int _id = id;
        final int _i = i;
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.edit_list);
        dialog.setTitle("EDIT");
        my_title = (EditText) dialog.findViewById(R.id.tittle);
        my_type = (EditText) dialog.findViewById(R.id.type);
        my_content = (EditText) dialog.findViewById(R.id.content);

        my_title.setText(tittle);
        my_type.setText(type);
        my_content.setText(content);

        Button update = (Button) dialog.findViewById(R.id.btnUpdate);
        Button cancel = (Button) dialog.findViewById(R.id.btnCancel);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title_array.set(_i, my_title.getText().toString());
                timeline_array.set(_i, my_type.getText().toString());
                content_array.set(_i, my_content.getText().toString());
                db.updateBusiness(_id, my_title.getText().toString(), my_type.getText().toString(), my_content.getText().toString());

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                }
        });

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.FILL_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        dialog.show();
    }






}
