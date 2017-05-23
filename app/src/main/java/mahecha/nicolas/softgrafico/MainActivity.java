package mahecha.nicolas.softgrafico;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;

import android.text.format.DateFormat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;

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
    public Configdispositivos Configdispositivos = new Configdispositivos();
    public Configmapa Configmapa = new Configmapa();


   ////////////*******MANAGER**********////////////
    FragmentManager fm;

    ////////////*******FAB*******//////////

    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ////////////*******INICIO DE SERVICIO BIND**********//////////

        final Intent intent = new Intent(MainActivity.this, MiServiceIBinder.class);
        tareaP = new MiTareaAsincrona();
        MainActivity.this.bindService(intent, sConnectionIB, Context.BIND_AUTO_CREATE);

        ////////////////***************ICONO FLOTANTE********/////////////
        fab = (FloatingActionButton) findViewById(R.id.fab);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        fm = getFragmentManager();
        Bundle bundle = new Bundle();
        bundle.putString("plano","/storage/emulated/0/Pictures/piso3.jpg");
        Configmapa.setArguments(bundle);
        fm.beginTransaction().add(R.id.lista,listaEventos,"listeven").add(R.id.principal, Configmapa,"mapas").commit();

        tareaP.execute();



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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(Configdispositivos).remove(Configmapa).commit();
            fm.executePendingTransactions();
            fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();

        } else if (id == R.id.nav_gallery) {

            fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(Configdispositivos).remove(Configmapa).commit();
            fm.executePendingTransactions();
            fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaDispositivos,"listdispo").commit();

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

           try {
                tareaP.cancel(true);
                fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
           }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

            fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(Configdispositivos).remove(Configmapa).commit();
            fm.executePendingTransactions();
            fm.beginTransaction().replace(R.id.principal,fragConfiguracion).commit();


        } else if (id == R.id.nav_share) {
            try {
                fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                tareaP.cancel(true);
                //mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

        } else if (id == R.id.nav_send) {
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

                if(mServiceIBinder.estadoacesorio()==2){
                    mServiceIBinder.resumir();
                    fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                }
                if(mServiceIBinder.estadoacesorio()==0) {
                    fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.verde));
                }

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
            try {
                if (mServiceIBinder != null) {
                    mServiceIBinder.estadoacesorio();
                }
            }catch (Exception e){}
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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();


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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();



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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();



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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                fm.executePendingTransactions();
                fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();



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
        String time = s.toString();
        return time ;
    }


}
