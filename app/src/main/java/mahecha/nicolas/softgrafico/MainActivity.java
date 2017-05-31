package mahecha.nicolas.softgrafico;

import android.app.Fragment;
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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import mahecha.nicolas.softgrafico.Configuracion.Configdispositivos;
import mahecha.nicolas.softgrafico.Configuracion.Configmapa;
import mahecha.nicolas.softgrafico.Configuracion.FragConfiguracion;
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
    Mapas mapas = new Mapas();
    ListaEventos listaEventos = new ListaEventos();
    ListaDispositivos listaDispositivos = new ListaDispositivos();
    Historial historial = new Historial();
    FragConfiguracion fragConfiguracion = new FragConfiguracion();
    mahecha.nicolas.softgrafico.Configuracion.Configdispositivos Configdispositivos = new Configdispositivos();
    mahecha.nicolas.softgrafico.Configuracion.Configmapa Configmapa = new Configmapa();


   ////////////*******MANAGER**********////////////
    FragmentManager fm;
    Fragment aux;

    ////////////*******FAB*******//////////
    FloatingActionButton fab;








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        //getWindow().setFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN, WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        getWindow().setFlags(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN, WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON, WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
//


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enableFullScreen(true);



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



        ArrayList<HashMap<String, String>> userList = controller.geteventos();
        if (userList.size() != 0) {

            HashMap<String, String> hashMap = userList.get(0);
            //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
            fm = getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("plano",hashMap.get("plano"));
            bundle.putString("posx",hashMap.get("posx"));
            bundle.putString("posy",hashMap.get("posy"));
            mapas.setArguments(bundle);
            fm.beginTransaction().add(R.id.lista,listaEventos,"listeven").add(R.id.principal, mapas,"mapas").commit();
        }else{
            fm = getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("plano","");
            bundle.putString("posx","0");
            bundle.putString("posy","0");
            mapas.setArguments(bundle);
            fm.beginTransaction().add(R.id.lista,listaEventos,"listeven").add(R.id.principal, mapas,"mapas").commit();
        }




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

//            fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(Configdispositivos).remove(Configmapa).commit();
//            fm.executePendingTransactions();
//            fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();


            ArrayList<HashMap<String, String>> userList = controller.geteventos();
            if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);
                //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();

            }else{
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","");
                bundle.putString("posx","0");
                bundle.putString("posy","0");
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();
            }


        } else if (id == R.id.nav_gallery) {




            ArrayList<HashMap<String, String>> userList = controller.getUsers();
            if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);
                //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                aux = new Mapas();
                listaDispositivos = new ListaDispositivos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaDispositivos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();

            }else{
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","");
                bundle.putString("posx","0");
                bundle.putString("posy","0");
                aux = new Mapas();
                listaDispositivos = new ListaDispositivos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaDispositivos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();
            }

        } else if (id == R.id.nav_slideshow) {



            ArrayList<HashMap<String, String>> userList = controller.getehistorial();
            if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);
                //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                aux = new Mapas();
                historial = new Historial();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, historial, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();

            }else{
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","");
                bundle.putString("posx","0");
                bundle.putString("posy","0");
                aux = new Mapas();
                historial = new Historial();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, historial, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();
            }



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
            try            {
                fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                tareaP.cancel(true);
                //mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

        } else if (id == R.id.nav_send) {

            try            {
                tareaP = new MiTareaAsincrona();
                //fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                tareaP.execute();
                //mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

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
            //int p = 0;
            enableFullScreen(true);
            if (mServiceIBinder != null) {



                if(mServiceIBinder.estadoacesorio()==0) {
                    fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.verde));
                }else{
                    //mServiceIBinder.onDestroy();
                }

                resultado = String.valueOf(mServiceIBinder.getResultado());
                if(resultado != null) {
                    if(!resultado.contentEquals(""))
                    {
                       // Toast.makeText(MainActivity.this,resultado,Toast.LENGTH_LONG).show();
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
                   // mServiceIBinder.estadoacesorio();
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

//                fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
//                fm.executePendingTransactions();
//                fm.beginTransaction().replace(R.id.principal,mapas,"mapas").replace(R.id.lista,listaEventos,"listeven").commit();


                  ArrayList<HashMap<String, String>> userList = controller.geteventos();
               // if (userList.size() != 0) {

                    HashMap<String, String> hashMap = userList.get(0);

                    fm = getFragmentManager();
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("plano",hashMap.get("plano"));
                    bundle.putString("posx",hashMap.get("posx"));
                    bundle.putString("posy",hashMap.get("posy"));
                    aux = new Mapas();
                    listaEventos = new ListaEventos();
                    aux.setArguments(bundle);
                    fm = getFragmentManager();
                    fm.beginTransaction().replace(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                    fm.executePendingTransactions();

//                }


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

                ArrayList<HashMap<String, String>> userList = controller.geteventos();
                // if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);

                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().add(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();


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

                ArrayList<HashMap<String, String>> userList = controller.geteventos();
                // if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);

                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().add(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();


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

                ArrayList<HashMap<String, String>> userList = controller.geteventos();
                // if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);

                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().add(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();


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
        CharSequence s  = DateFormat.format("d/M/yyyy H:m:s", date.getTime());
        String time = s.toString();
        return time ;
    }



    protected void enableFullScreen(boolean enabled) {
        int newVisibility =  View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

        if(enabled) {
            newVisibility |= View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            //Toast.makeText(this,"inmerso",Toast.LENGTH_LONG).show();
        }

        getDecorView().setSystemUiVisibility(newVisibility);
    }

    private View getDecorView() {
        return getWindow().getDecorView();
    }


}
