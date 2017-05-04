package mahecha.nicolas.softgrafico;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.text.format.DateFormat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import android.app.Fragment;

import mahecha.nicolas.softgrafico.Rs232.MiServiceIBinder;
import mahecha.nicolas.softgrafico.Sqlite.DBController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MiServiceIBinder mServiceIBinder;
    private MiTareaAsincrona tareaP;
    String resultado;
    DBController controller = new DBController(this);
    HashMap<String, String> queryValues;

    EnvioDatos envioDatos = new EnvioDatos(this);

    ////////////***FRAGMENTOS***///////////////
    public Mapas mapas = new Mapas();
    public ListaEventos listaEventos = new ListaEventos();
    public ListaDispositivos listaDispositivos = new ListaDispositivos();
    public FragConfiguracion fragConfiguracion = new FragConfiguracion();


   ////////////*******MANAGER**********////////////
    FragmentManager fm;
    FragmentTransaction ft;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = getFragmentManager();
        fm.beginTransaction().add(R.id.lista,listaEventos).add(R.id.principal,mapas).commit();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
            fm.executePendingTransactions();
            fm.beginTransaction().replace(R.id.principal,mapas).replace(R.id.lista,listaEventos).commit();

        } else if (id == R.id.nav_gallery) {

            fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
            fm.executePendingTransactions();
            fm.beginTransaction().replace(R.id.principal,mapas).replace(R.id.lista,listaDispositivos).commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

            fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
            fm.executePendingTransactions();
            fm.beginTransaction().replace(R.id.principal,fragConfiguracion).commit();


        } else if (id == R.id.nav_share) {
            try {
                tareaP.cancel(true);
                mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

        } else if (id == R.id.nav_send) {

            Intent intent = new Intent(this, MiServiceIBinder.class);
            this.bindService(intent, sConnectionIB, Context.BIND_AUTO_CREATE);
            tareaP = new MiTareaAsincrona();
            tareaP.execute();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    // CONFIGURACION INTERFACE SERVICECONNECTION IBINDER
    private ServiceConnection sConnectionIB = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MiServiceIBinder.MiBinderIBinder binder = (MiServiceIBinder.MiBinderIBinder) service;
            mServiceIBinder = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };


//////////************TAREA ASINCRONA********//////////

    private class MiTareaAsincrona extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {

            while(true) {
                tareaLarga();
                publishProgress();
                if(isCancelled())
                    break;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (mServiceIBinder != null) {
                resultado = String.valueOf(mServiceIBinder.getResultado());
                if(resultado != null) {
                    if(!resultado.contentEquals(""))
                    {
                        saltos();
                        envioDatos.enviar(resultado);

                    }
                    mServiceIBinder.cleanr();
                }
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
                Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(MainActivity.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }


    public void saltos() {
        String[] split = resultado.split("\n");
        StringBuilder sb = new StringBuilder();
        String sSubCadena = "";

//        Toast.makeText(this,resultado,Toast.LENGTH_LONG).show();
  try{
        for (int i = 0; i < split.length; i++) {

            if (split[i].contains("AVERIA")) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.alarma1);
                mp.start();
                queryValues = new HashMap<String, String>();
                sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                queryValues.put("id_dispositivo", sSubCadena);
                queryValues.put("fecha", tiempo());
                queryValues.put("tipo","1");
                controller.inserevento(queryValues);

                fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(listaEventos).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.lista,listaEventos).commit();

            }

            if (split[i].contains("ALARM:")) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.alarma1);
                mp.start();
                queryValues = new HashMap<String, String>();
                sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                queryValues.put("id_dispositivo", sSubCadena);
                queryValues.put("fecha", tiempo());
                queryValues.put("tipo","2");
                controller.inserevento(queryValues);
                fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(listaEventos).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.lista,listaEventos).commit();

            }

            if (split[i].contains("ACTIVA")) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.alarma1);
                mp.start();
                queryValues = new HashMap<String, String>();
                sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                queryValues.put("id_dispositivo", sSubCadena);
                queryValues.put("fecha", tiempo());
                queryValues.put("tipo","3");
                controller.inserevento(queryValues);
                fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(listaEventos).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.lista,listaEventos).commit();

            }

            if (split[i].contains("AVERIA") && split[i].contains("BATERIAS")) {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.alarma1);
                mp.start();
                queryValues = new HashMap<String, String>();
                sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                queryValues.put("id_dispositivo", "BATERIA");
                queryValues.put("fecha", tiempo());
                queryValues.put("tipo","4");
                controller.inserevento(queryValues);
                fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(listaEventos).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.lista,listaEventos).commit();
            }




            }

        }catch(Exception e){}
    }

    /////////*******TAREA LARGA*********/////////

    private void tareaLarga()
    {
        try {
            Thread.sleep(10000);
        } catch(InterruptedException e) {}
    }



    ////////////////////***************OBTIENE TIEMPO**************///////////////////
    public String tiempo()
    {
        Date date = new Date();
        CharSequence s  = DateFormat.format("d/M/yyyy H:m", date.getTime());
        // System.out.println (s);
        String time = s.toString();
        return time ;
    }









}
