package com.veganway;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.veganway.DB.DBManager;

import java.util.HashMap;
import java.util.WeakHashMap;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private CameraUpdate mCamara; //Permite el movimiento de la camara

    private HashMap<Marker, String> mHashMap = new HashMap<Marker, String>();
    private HashMap<Marker, String> mHashMap2 = new HashMap<Marker, String>();

    //Base de datos
    private DBManager manager;
    private Cursor cursor;

    private final int REQUEST_ID_LUGAR = 1;
    boolean lugar_seleccionado = false;
    String nombLugar;
    double latitud_lugar;
    double longitud_lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        manager = new DBManager(this);

        if ( getIntent().hasExtra("Lugar seleccionado")) {

            lugar_seleccionado = getIntent().getBooleanExtra("Lugar seleccionado",false);
            latitud_lugar = getIntent().getDoubleExtra("latitud_lugar",0F);
            longitud_lugar = getIntent().getDoubleExtra("longitud_lugar",0F);
            setUpMapIfNeeded();
            mCamara = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud_lugar, longitud_lugar), 20);
            mMap.animateCamera(mCamara);
        } else {
            setUpMapIfNeeded();
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.MenuNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL); // Establecemos el mapa normal
                return true;

            case R.id.MenuHibrido:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); // Establecemos el mapa hibrido
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (lugar_seleccionado == true) {

            mCamara = CameraUpdateFactory.newLatLngZoom(new LatLng(latitud_lugar, longitud_lugar), 20);
            mMap.animateCamera(mCamara);
        } else {
            setUpMapIfNeeded();
        }
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true); //Para mostrar mi ubicacion en el mapa
                //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                setUpMap();

                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String nombreLugar = mHashMap.get(marker);
                        Intent i = new Intent(MapsActivity.this, InfoActivity.class);
                        i.putExtra("activity", (int) 1);
                        i.putExtra("nombreLugar", nombreLugar);
                        startActivityForResult(i, REQUEST_ID_LUGAR);
                    }

                });
            }
        }
    }

    private void setUpMap() {

        manager.leerDB();
        cursor = manager.cargarLugares();
        cursor.moveToFirst();
        int id;
        String titulo;
        String direccion;
        String horario;
        double lat, longitud;
        Marker m;
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(0);
            titulo = cursor.getString(1);
            lat = cursor.getDouble(7);
            longitud = cursor.getDouble(8);
            direccion = cursor.getString(2);
            horario = cursor.getString(3);
            m = setMarker(new LatLng(lat, longitud), titulo, direccion, 0.1F, 0.1F, R.drawable.carrotmarker);
            mHashMap.put(m, titulo);
            mHashMap2.put(m,horario);
            mCamara = CameraUpdateFactory.newLatLngZoom(new LatLng(lat, longitud), 12);
            mMap.animateCamera(mCamara);
            cursor.moveToNext();

            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

                @Override
                public View getInfoWindow(Marker marker) {

                    View v = getLayoutInflater().inflate(R.layout.map_popup, null);

                    TextView title = (TextView) v.findViewById(R.id.nombreLugar);
                    title.setText(marker.getTitle());

                    TextView address = (TextView) v.findViewById(R.id.direccion);
                    address.setText(marker.getSnippet());

                    TextView horario = (TextView) v.findViewById(R.id.horario);
                    String hour = mHashMap2.get(marker);
                    horario.setText(hour);

                    return v;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    return null;
                }
            });
        }
        cursor.close();
    }

    //Agrega y personaliza el marker
    private Marker setMarker(LatLng position, String title, String info, float dimension1, float dimension2, int icon) {

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(info)
                .anchor(dimension1, dimension2)
                .icon(BitmapDescriptorFactory.fromResource(icon)));
        return marker;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ID_LUGAR) {
                latitud_lugar = data.getExtras().getDouble("latitud_lugar");
                longitud_lugar = data.getExtras().getDouble("longitud_lugar");
                lugar_seleccionado = true;
            }

        }
    }
}
