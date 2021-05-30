package com.veganway;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.veganway.DB.DBManager;
import com.veganway.ExpandListAdapter.ExpandListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// barrios
public class GuideActivity extends Activity {

    DBManager manager;

    Cursor cursorBarrios;
    Cursor cursorLugares;

    ExpandListAdapter expandAdapter;
    ExpandableListView expandList;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // get listview
        expandList = (ExpandableListView) findViewById(R.id.expandList);

        //agregar datos de la bd
        fillData();

        expandAdapter = new ExpandListAdapter(this, listDataHeader, listDataChild);

        expandList.setAdapter(expandAdapter);

        expandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                String nombreLugar = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                Intent i = new Intent(GuideActivity.this, InfoActivity.class);
                i.putExtra("activity", 2);
                i.putExtra("nombreLugar", nombreLugar);
                startActivity(i);

                return false;
            }
        });

    }

    public void fillData() {

        manager = new DBManager(this);

        manager.leerDB();

        String nombreBarrio;
        String nombreLugar;
        int i = 0;

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        cursorBarrios = manager.cargarBarrios();
        cursorBarrios.moveToFirst();

        while(!cursorBarrios.isAfterLast()) {

            nombreBarrio = cursorBarrios.getString(1);

            listDataHeader.add(nombreBarrio);
            List<String> tmp = new ArrayList<String>();

            cursorLugares = manager.lugaresporBarrio(i+1);
            cursorLugares.moveToFirst();

            nombreLugar = cursorLugares.getString(1);

            tmp.add(nombreLugar);
            cursorLugares.moveToNext();

            while(!cursorLugares.isAfterLast()) {

                nombreLugar = cursorLugares.getString(1);
                tmp.add(nombreLugar);
                cursorLugares.moveToNext();

            }
            listDataChild.put(listDataHeader.get(i), tmp);
            i++;
            cursorBarrios.moveToNext();
        }
    }

    
    public void retButton(View v) {
        finish();
    }
}
