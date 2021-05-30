package com.veganway;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.veganway.DB.DBManager;


// pantalla principal (4 opciones)
public class MainActivity extends Activity {

    //Base de datos
    private DBManager manager;
    private Cursor cursor;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        manager = new DBManager(this);

        manager.escribirDB();

        //borrar bd
        manager.borrarTablas();


        //insertar elem a bd
        insertarTodosBarrios();
        insertarTodosLugares();

    }

    public void insertarTodosLugares() {

        manager.insertarLugar("Alma Resto", "San Lorenzo 336", "Mier-Dom 18:00-1:00", "", "www.facebook.com/pages/Alma-Vegan-Resto/899954406692937", "", -31.4245961, -64.1825868,6,"almaresto");
        manager.insertarLugar("Rincón Verde", "Deán Funes 732", "Lun-Vie 8:00-15:00", "4234331", "www.facebook.com/pages/Rincon-Verde/483556805371", "", -31.413049, -64.1939356,3,"rinconverde");
        manager.insertarLugar("Casa Dórica", "Bolivia 60", "Lun-Vie 12:00-1:00", "156453574", "www.facebook.com/casitadorica", "", -31.4312202, -64.1896077,6,"casadorica");
        manager.insertarLugar("Mundo Verde", "Av. Olmos 189", "Lun-Vie 09:00-20:00", "4212758", "www.facebook.com/mundoverdecordoba", "",-31.414406,-64.1806741,3,"mundoverde");
        manager.insertarLugar("Uriel", "27 de abril 263", "Lun-Vie 12:00-16:00", "", "www.facebook.com/pages/URIEL-restaurante-vegetariano-y-vegano/197003763757369", "", -31.4160682, -64.1881311,3, "uriel");
        manager.insertarLugar("Sol y Luna", "Montevideo 66", "Lun-Sáb 12:00-15:30", "4211863", "www.facebook.com/solylunaonline", "www.solylunaonline.com.ar",-31.4216947,-64.1874412,6, "solyluna");
        manager.insertarLugar("Be Prana", "Dean Funes 743", "Lun-Sáb 12:00-15:30", "156857872", "www.facebook.com/PranaVegetales", "",-31.4130776,-64.1938345,3, "beprana");
        manager.insertarLugar("La Pepa", "Isabel la Católica 664", "Mié-Dom 16:30-0:00", "157152688", "www.facebook.com/lapepacasadearte", "www.lapepa.com.ar",-31.3912579,-64.1832777,2,"lapepa");
        manager.insertarLugar("Vida Plena", "27 de abril 1968", "Lun-Sáb 17:00-23:30", "4871428", "www.facebook.com/pages/Restaurante-Vida-Plena/1491516791123982", "",-31.4090226, -64.210026,1, "vidaplena");
        manager.insertarLugar("Vegan Food", "Velez Sarsfield 1224", "Lun-Vie 12:00-15:00", "153611882", "www.facebook.com/pages/Vegan-Food/693781250664800", "", -31.4293712,-64.1926682,5, "veganfood");
        manager.insertarLugar("Carmen Resto", "Paraná 580", "Lun-Sáb 12:00-15:30", "153020648", "www.facebook.com/pages/Carmen-Resto-Natural/196128157078362", "", -31.4250088,-64.1803313,6, "carmen");
        manager.insertarLugar("Eva Natural", "Avenida Hipólito Yrigoyen 511", "Mar-Dom 10:00-20:00", "4343636", "www.facebook.com/evanatural.rawfood", "",-31.4270874,-64.1855506,6, "evanatural");
        manager.insertarLugar("Verde Siempre Verde", "9 de julio 36", "Lun-Sáb 12:00-15:00", "4218820", "www.facebook.com/verdesiempreverderestaurant", "",-31.414748,-64.1840308,3, "verdesiempre");
        manager.insertarLugar("Quasar", "Corrientes 44", "Lun-Vie 11:30-15:30", "155956273", "www.facebook.com/quasar.cordoba", "",-31.4195548,-64.1849453,3, "quasar");
        manager.insertarLugar("Fénix", "Simón Bolívar 348", "Lun-Vie 12:00-15:30", "", "", "",-31.4178844,-64.1923773,3, "fenix");
        manager.insertarLugar("La Naturaleza y Ud.", "General Manuel Belgrano 222", "Lun-Vie 12:00-16:00", "", "", "",-31.4172789,-64.189188,3, "lanatyud");
        manager.insertarLugar("Bodhi Garden", "Bv. Arturo Illia 437", "Lun-Vie 11:30-15:30", "", "www.facebook.com/pages/Bodhi-Garden-Comedor-vegetariano/1655057468060461", "",-31.4224773,-64.1802307,6, "bodhi");
        manager.insertarLugar("La casa de Carmen", "Rondeau 616", "Lun-Vie 12:00-15:30", "4247975", "", "",-31.424621,-64.1782366,6, "carrot");
        manager.insertarLugar("Palito Rojo", "Rafael Núñez 3635", "Lun-Sáb 12:00-23:45", "4812672", "www.facebook.com/pages/PALITO-ROJO/542905705742975", "",-31.3770389,-64.225712,4, "palitorojo");
    }

    public void insertarTodosBarrios() {
        manager.insertarBarrio("Alberdi");
        manager.insertarBarrio("Alta Córdoba");
        manager.insertarBarrio("Centro");
        manager.insertarBarrio("Cerro de las Rosas");
        manager.insertarBarrio("Güemes");
        manager.insertarBarrio("Nueva Córdoba");
    }

    public void mapClick(View v) {
        Intent i = new Intent(this, MapsActivity.class);
        startActivity(i);
    }

    public void listClick(View v) {
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i);
    }

    public void favClick(View v) {

        Cursor cursorFav;
        DBManager managerFav;

        managerFav = new DBManager(this);
        managerFav.leerDB();

        cursorFav = manager.consultarFavoritos();
        cursorFav.moveToFirst();

        if(cursorFav.getCount() == 0) {

            Toast.makeText(this, "No hay lugares agregados como favoritos", Toast.LENGTH_SHORT).show();

        } else {

            Intent i = new Intent(this, FavActivity.class);
            startActivity(i);

        }

    }

    public void wishClick(View v) {

        Cursor cursorWish;
        DBManager managerFav;

        managerFav = new DBManager(this);
        managerFav.leerDB();

        cursorWish = manager.consultarWishes();
        cursorWish.moveToFirst();

        if(cursorWish.getCount() == 0) {

            Toast.makeText(this, "No hay lugares agregados en Wishlist", Toast.LENGTH_SHORT).show();

        } else {

            Intent i = new Intent(this, WishActivity.class);
            startActivity(i);

        }
    }


}