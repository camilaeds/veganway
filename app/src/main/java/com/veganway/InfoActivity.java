package com.veganway;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.veganway.DB.DBManager;

import org.w3c.dom.Text;

import java.util.Map;


public class InfoActivity extends Activity {

    //Base de datos
    int typeAct;
    private DBManager manager;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    String nombre_lugar;
    double latitud_lugar;
    double longitud_lugar;

    TextView telLugar;
    boolean lugarDeseado;
    boolean lugarFavorito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_lugar);

        typeAct = getIntent().getExtras().getInt("activity");

        nombre_lugar = this.getIntent().getExtras().getString("nombreLugar");

        manager = new DBManager(this);

        manager.leerDB();

        cursor = manager.consultarLugar(nombre_lugar);
        cursor.moveToFirst();

        String nombre = cursor.getString(1);
        String direccion = cursor.getString(2);
        String horario = cursor.getString(3);
        String telefono = cursor.getString(4);
        String facebook = cursor.getString(5);
        String paginaWeb = cursor.getString(6);
        latitud_lugar = cursor.getDouble(7);
        longitud_lugar = cursor.getDouble(8);
        String img = cursor.getString(10);
        Log.d("lala", "" +img);


        manager.cerrarConexion();

        ImageView imgLugar = (ImageView) findViewById(R.id.imgFood);
        TextView nombLugar = (TextView) findViewById(R.id.titlePlace);
        TextView dirLugar = (TextView) findViewById(R.id.addressPlace);
        telLugar = (TextView) findViewById(R.id.telephPlace);
        TextView horaLugar = (TextView) findViewById(R.id.hoursPlace);
        TextView faceLugar = (TextView) findViewById(R.id.fbPlace);
        TextView pagWeb = (TextView) findViewById(R.id.webPlace);

        //int imageResource = getResources().getIdentifier(img, null, getPackageName());

        //ContextCompat.getDrawable(this, imageResource);
        //Drawable draw =  ContextCompat.getDrawable(this, imageResource);

        Context context = imgLugar.getContext();
        int id_img = context.getResources().getIdentifier(img, "drawable", context.getPackageName());
        Log.d("lala", "" + id_img);


        imgLugar.setImageResource(id_img);
        nombLugar.setText(nombre);
        dirLugar.setText(direccion);
        horaLugar.setText(horario);
        telLugar.setText(telefono);
        faceLugar.setText(facebook);
        pagWeb.setText(paginaWeb);

        Button btnLlamada = (Button) findViewById(R.id.btnLlamada);

        ImageView imgTel = (ImageView) findViewById(R.id.imgTel);


        if(telefono.equals("")){ //Si el  lugar no tiene telefono
            btnLlamada.setVisibility(View.INVISIBLE);
            imgTel.setVisibility(View.INVISIBLE);

        }

        ImageView imgPagWeb = (ImageView) findViewById(R.id.imgPagWeb);//Si el  lugar no tiene paginaWeb

        if(paginaWeb.equals("")){
            imgPagWeb.setVisibility(View.INVISIBLE);
            pagWeb.setText("");

        }

        ImageView imgFB = (ImageView) findViewById(R.id.imgFB);

        if(facebook.equals("")){ //Si el  lugar no tiene facebook
            imgFB.setVisibility(View.INVISIBLE);
            faceLugar.setText("");
        }


        final ImageButton btn=(ImageButton)findViewById(R.id.wishIcon);
        final ImageButton btn2=(ImageButton)findViewById(R.id.favIcon);

        Cursor c1 = manager.isWished(nombre_lugar);

        if(c1.getCount()!=0){

            btn.setBackgroundResource(R.drawable.fillhearticon);
            lugarDeseado = true;

        }else{

            btn.setBackgroundResource(R.drawable.plushearticon);
            lugarDeseado = false;
        }


        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN && lugarDeseado == false) {
                    btn.setBackgroundResource(R.drawable.fillhearticon);
                    manager.insertarWished(true, nombre_lugar);
                    Toast.makeText(InfoActivity.this.getApplicationContext(),"Lugar agregado a la wishlist",Toast.LENGTH_SHORT).show();
                    lugarDeseado = true;
                }
                else{
                    if (event.getAction() == MotionEvent.ACTION_DOWN && lugarDeseado == true){
                        btn.setBackgroundResource(R.drawable.plushearticon);
                        manager.eliminarWish(nombre_lugar);
                        Toast.makeText(InfoActivity.this.getApplicationContext(),"Lugar eliminado de la wishlist",Toast.LENGTH_SHORT).show();
                        lugarDeseado = false;
                    }
                }
                return false;
            }
        });

        Cursor c2 = manager.esFavorito(nombre_lugar);


        if(c2.getCount()!=0){

            btn2.setBackgroundResource(R.drawable.fillstaricon);
            lugarFavorito = true;

        }else{

            btn2.setBackgroundResource(R.drawable.plusstarticon);
            lugarFavorito = false;
        }

        btn2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN && lugarFavorito == false) {
                    btn2.setBackgroundResource(R.drawable.fillstaricon);
                    manager.insertarFavorito(true, nombre_lugar);
                    Toast.makeText(InfoActivity.this.getApplicationContext(),"Lugar agregado a favorito",Toast.LENGTH_SHORT).show();
                    lugarFavorito = true;
                }
                else{
                    if (event.getAction() == MotionEvent.ACTION_DOWN && lugarFavorito == true){
                        btn2.setBackgroundResource(R.drawable.plusstarticon);
                        manager.eliminarFavorito(nombre_lugar);
                        Toast.makeText(InfoActivity.this.getApplicationContext(),"Lugar eliminado de favoritos",Toast.LENGTH_SHORT).show();
                        lugarFavorito = false;
                    }
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mapButton(View v) {
        Intent i;

        switch (typeAct) {
            case 1:
                i = new Intent(InfoActivity.this, MapsActivity.class);
                i.putExtra("latitud_lugar", latitud_lugar);
                i.putExtra("longitud_lugar", longitud_lugar);
                setResult(RESULT_OK, i);
                finish();
                break;

            case 2:
                i = new Intent(InfoActivity.this,  MapsActivity.class);
                i.putExtra("latitud_lugar", latitud_lugar);
                i.putExtra("longitud_lugar", longitud_lugar);
                i.putExtra("Lugar seleccionado", true );
                startActivity(i);
                finish();
                break;
        }



    }

    public void callButtonClicked(View view)
    {
        String number = (String) telLugar.getText();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);

    }

    public void retButton(View v) {
        finish();
    }


}