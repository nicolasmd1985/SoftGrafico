package mahecha.nicolas.softgrafico;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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


    ///POP UP////
    EditText getInput; //NEW
    String myValue = "" ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
            fm = getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("plano",hashMap.get("plano"));
            bundle.putString("posx",hashMap.get("posx"));
            bundle.putString("posy",hashMap.get("posy"));
            bundle.putString("imagen",hashMap.get("nombre"));
            mapas.setArguments(bundle);
            fm.beginTransaction().add(R.id.lista,listaEventos,"listeven").add(R.id.principal, mapas,"mapas").commit();
        }else{
            fm = getFragmentManager();
            Bundle bundle = new Bundle();
            bundle.putString("plano","/storage/emulated/0/Pictures/soft_normal.jpg");
            bundle.putString("posx","-50");
            bundle.putString("posy","-50");
            bundle.putString("imagen","");
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
            try            {
                tareaP = new MiTareaAsincrona();
                //fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                tareaP.execute();
                //mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

            ArrayList<HashMap<String, String>> userList = controller.geteventos();
            if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);
                //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                bundle.putString("imagen",hashMap.get("nombre"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();

            }else{
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","/storage/emulated/0/Pictures/soft_normal.jpg");
                bundle.putString("posx","-50");
                bundle.putString("posy","-50");
                bundle.putString("imagen","");
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();
            }


        } else if (id == R.id.nav_gallery) {

            try            {
                tareaP = new MiTareaAsincrona();
                //fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                tareaP.execute();
                //mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}


            ArrayList<HashMap<String, String>> userList = controller.getUsers();
            if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);
                //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                bundle.putString("imagen",hashMap.get("nombre"));
                aux = new Mapas();
                listaDispositivos = new ListaDispositivos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaDispositivos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();

            }else{
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","");
                bundle.putString("posx","0");
                bundle.putString("posy","0");
                bundle.putString("imagen","");
                aux = new Mapas();
                listaDispositivos = new ListaDispositivos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaDispositivos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();
            }

        } else if (id == R.id.nav_slideshow) {

            try            {
                tareaP = new MiTareaAsincrona();
                //fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                tareaP.execute();
                //mServiceIBinder.onDestroy();
            }catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();}

            ArrayList<HashMap<String, String>> userList = controller.getehistorial();
            if (userList.size() != 0) {

                HashMap<String, String> hashMap = userList.get(0);
                //Toast.makeText(this,hashMap.get("plano"),Toast.LENGTH_LONG ).show();
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                bundle.putString("imagen",hashMap.get("nombre"));
                aux = new Mapas();
                historial = new Historial();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, historial, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();

            }else{
                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","");
                bundle.putString("posx","0");
                bundle.putString("posy","0");
                bundle.putString("imagen","");
                aux = new Mapas();
                historial = new Historial();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, historial, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();
            }



        } else if (id == R.id.nav_manage) {


            showSimplePopUp();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    // CONFIGURACION INTERFACE SERVICE CONNECTION IBINDER
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

            if (
                    ////////**********español*******////////////
                    split[i].contains("AVERIA MONITOR") ||
                    split[i].contains("AVERIA HUMO") ||
                    split[i].contains("AVERIA SUPRV") ||
                    //////////************ingles********/////////
                    split[i].contains("TROUBL MONITOR") ||
                    split[i].contains("TROUBL SMOKE") ||
                    split[i].contains("TROUBL PULL")

                    )

            {
                MediaPlayer mp = MediaPlayer.create(this, R.raw.alarma1);
                mp.start();
                queryValues = new HashMap<String, String>();
                sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                queryValues.put("id_dispositivo", sSubCadena);
                queryValues.put("fecha", tiempo());
                queryValues.put("tipo","1");
                controller.inserevento(queryValues);


                  ArrayList<HashMap<String, String>> userList = controller.geteventos();
               // if (userList.size() != 0) {

                    HashMap<String, String> hashMap = userList.get(0);

                    fm = getFragmentManager();
                    fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                    Bundle bundle = new Bundle();
                    bundle.putString("plano",hashMap.get("plano"));
                    bundle.putString("posx",hashMap.get("posx"));
                    bundle.putString("posy",hashMap.get("posy"));
                    bundle.putString("imagen",hashMap.get("nombre"));
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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                bundle.putString("imagen",hashMap.get("nombre"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().add(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();


            }

            if (split[i].contains("ACTIVA")||split[i].contains("ACTIVE")) {
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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                bundle.putString("imagen",hashMap.get("nombre"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().add(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();


            }

            if (
                    (split[i].contains("AVERIA") && split[i].contains("BATERIAS")) ||
                    (split[i].contains("TROUBLE") && split[i].contains("BATERIAS"))

                    ) {
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
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano",hashMap.get("plano"));
                bundle.putString("posx",hashMap.get("posx"));
                bundle.putString("posy",hashMap.get("posy"));
                bundle.putString("imagen",hashMap.get("nombre"));
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().add(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
                fm.executePendingTransactions();


            }


            if ((split[i].contains("SISTEMA NORMAL")) || (split[i].contains("SYSTEM NORMAL"))) {


                ArrayList<HashMap<String, String>> userList = controller.geteventos();
                if (userList.size() != 0) {
                    for (HashMap<String, String> hashMap : userList) {
                        controller.upstado(hashMap.get("id_evento"));
                    }
                }

                fm = getFragmentManager();
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                Bundle bundle = new Bundle();
                bundle.putString("plano","/storage/emulated/0/Pictures/soft_normal.jpg");
                bundle.putString("posx","-50");
                bundle.putString("posy","-50");
                bundle.putString("imagen","");
                aux = new Mapas();
                listaEventos = new ListaEventos();
                aux.setArguments(bundle);
                fm = getFragmentManager();
                fm.beginTransaction().replace(R.id.lista, listaEventos, "listeven").replace(R.id.principal, aux, "mapas").commit();
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
        CharSequence s  = DateFormat.format("yyyy/M/d HH:mm:ss", date.getTime());
        String time = s.toString();
       // Toast.makeText(this,s,Toast.LENGTH_LONG).show();
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




    ///////////******************POP UP**************//////////////
    private void showSimplePopUp() {

        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(this);
        helpBuilder.setTitle("PRECAUCION!!");
        LayoutInflater inflater = getLayoutInflater();
        helpBuilder.setMessage("Desea Continuar?");
        final View checkboxLayout = inflater.inflate(R.layout.popuplayout, null);
        getInput = (EditText) checkboxLayout.findViewById(R.id.contra); //MISTAKE

        helpBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String contras = getInput.getText().toString();
                            if (contras.contains("1234")) {

                                tareaP.cancel(true);
                                tareaP.onCancelled();
                                fab.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.gris));
                                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                fm.beginTransaction().remove(listaDispositivos).remove(mapas).remove(listaEventos).remove(fragConfiguracion).remove(historial).commit();
                                fm.executePendingTransactions();
                                fm.beginTransaction().replace(R.id.principal, fragConfiguracion).commit();
                            }else
                                {Toast.makeText(getApplicationContext(),"CONTRASEÑA INCORRECTA",Toast.LENGTH_LONG).show();}
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
        helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
               // System.out.println("no");
            }
        });

        helpBuilder.setView(checkboxLayout);
        helpBuilder.show();

    }



//        AlertDialog helpDialog = helpBuilder.create();
//        helpDialog.show();




}
