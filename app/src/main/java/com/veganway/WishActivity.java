package com.veganway;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.veganway.WishListAdapter;
import com.veganway.DB.DBManager;

import java.util.ArrayList;
import java.util.HashMap;


public class WishActivity extends Activity {

    ArrayList<String> array;

    private DBManager manager;
    private Cursor cursorWish;
    private ListView listaWish;
    private WishListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        listaWish = (ListView) findViewById(R.id.wishList);

        registerForContextMenu(listaWish);

        fillWish();


        listaWish.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nombreLugar;

                Intent intent = new Intent(WishActivity.this, InfoActivity.class);
                nombreLugar = adapter.getItem(position);

                intent.putExtra("activity", 3);

                intent.putExtra("nombreLugar", nombreLugar);
                startActivity(intent);
            }
        });

    }

    public void fillWish() {

        manager = new DBManager(this);
        manager.leerDB();

        cursorWish = manager.consultarWishes();
        cursorWish.moveToFirst();

        String nombreLugar;
        array = new ArrayList<String>();

        while(cursorWish.getCount()!=0 && !cursorWish.isAfterLast()) {
            nombreLugar = cursorWish.getString(2);
            array.add(nombreLugar);
            cursorWish.moveToNext();
        }

        adapter = new WishListAdapter(this, array);

        listaWish.setAdapter(adapter);
    }

    public void retButton(View v) {
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View view,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Eliminar Wish");
        menu.add(0, view.getId(), 0, "Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Eliminar") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int posicionWish = info.position;
            String nombreLugar = adapter.getItem(posicionWish);
            manager.eliminarWish(nombreLugar);
            fillWish();
            return true;
        }else{
            return false;
        }
    }


}
