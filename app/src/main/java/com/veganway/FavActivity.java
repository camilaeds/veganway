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

import com.veganway.ListAdapter;
import com.veganway.DB.DBManager;

import java.util.ArrayList;
import java.util.HashMap;


public class FavActivity extends Activity {

    ArrayList<String> array;

    private DBManager manager;
    private Cursor cursorFav;
    private ListView listaFavoritos;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        listaFavoritos = (ListView) findViewById(R.id.favList);

        registerForContextMenu(listaFavoritos);

        fillFav();

        listaFavoritos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String nombreLugar;

                Intent intent = new Intent(FavActivity.this, InfoActivity.class);
                nombreLugar = adapter.getItem(position);

                intent.putExtra("activity", 4);

                intent.putExtra("nombreLugar", nombreLugar);
                startActivity(intent);
            }
        });

    }

    public void fillFav() {

        String nombreLugar;
        array = new ArrayList<String>();
        manager = new DBManager(this);
        manager.leerDB();

        cursorFav = manager.consultarFavoritos();
        cursorFav.moveToFirst();

        while(cursorFav.getCount()!=0 && !cursorFav.isAfterLast()) {
            nombreLugar = cursorFav.getString(2);
            array.add(nombreLugar);
            cursorFav.moveToNext();
        }
        adapter = new ListAdapter(this, array);

        listaFavoritos.setAdapter(adapter);
    }

    public void retButton(View v) {
        finish();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,View view,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Eliminar Favorito");
        menu.add(0, view.getId(), 0, "Eliminar");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Eliminar") {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int posicionFavorito = info.position;
            String nombreLugar = adapter.getItem(posicionFavorito);
            manager.eliminarFavorito(nombreLugar);
            fillFav();
            return true;
        }else{
            return false;
        }
    }

}