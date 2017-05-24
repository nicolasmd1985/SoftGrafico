package mahecha.nicolas.softgrafico;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import mahecha.nicolas.softgrafico.Rs232.MiServiceIBinder;
import mahecha.nicolas.softgrafico.Sqlite.DBController;


/**
 * A simple {@link Fragment} subclass.
 */
public class Autoconfigura extends Fragment {

    private MiServiceIBinder mServiceIBinder;
    String resultado;
    private MiTareaAsincrona tarea1;
    HashMap<String, String> queryValues;
    DBController controller;
    EnvioDatos envioDatos;
    private ProgressBar pbarProgreso;
    private ProgressDialog pDialog;

    ////////////*******MANAGER**********////////////
    FragmentManager fm;

    public Autoconfigura() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_buffer, container, false);

        controller = new DBController(getActivity());
        envioDatos = new EnvioDatos(getActivity());
        Intent intent = new Intent(getActivity(), MiServiceIBinder.class);
        getActivity().bindService(intent, sConnectionIB, Context.BIND_AUTO_CREATE);
        tarea1 = new MiTareaAsincrona();

        fm = getFragmentManager();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setMessage("Procesando...");
        pDialog.setCancelable(true);
        pDialog.setMax(100);
        pDialog.setProgress(0);
        pDialog.show();
        pDialog.setProgress(10);


        tarea1.execute();
        return v;
    }
    /////////*******TAREA LARGA*********/////////
    private void tareaLarga()
    {
        try {
            Thread.sleep(10000);
        } catch(InterruptedException e) {}
    }
    ///////////////************TAREA ASINCRONA*************///////////
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
                pDialog.setProgress(30);

                resultado = String.valueOf(mServiceIBinder.getResultado());
                if(resultado != null) {
                    pDialog.setProgress(30);
                    pDialog.setMessage("Esperando informacion de central...");
                    if(!resultado.contentEquals(""))
                    {
                        pDialog.setProgress(80);

                        saltos();
                        envioDatos.enviar(resultado);

                        tarea1.cancel(true);
                        pDialog.setProgress(100);
                        pDialog.hide();
                    }


                }
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Boolean result) {


        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getActivity(), "Tarea cancelada!", Toast.LENGTH_SHORT).show();

                Sensoresaplanos asigsensor = new Sensoresaplanos();
                fm.beginTransaction().replace(R.id.principal,asigsensor,"asigsensor").commit();

        }
    }

    //////////////////************************SALTOS***************/////////////
    public void saltos() {

        String[] split = resultado.split("\n");
        String sSubCadena = "";
        String sSubCadena2 = "";
        ArrayList<HashMap<String, String>> userList = controller.getUsers();


       ///////////////////***********ENTRA SI ENCUENTRA DATOS EN LA LISTA**********////////////
        try {
            if (userList.size()!= 0){
             //   Toast.makeText(getActivity(),"entra x",Toast.LENGTH_LONG).show();
                for (HashMap<String, String> hashMap : userList) {

                    for (int i = 0; i < split.length; i++) {

                        if (split[i].contains("NORMAL")) {
                            queryValues = new HashMap<String, String>();
                            sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                            sSubCadena2 = split[i].substring(7, 49);
                            if (hashMap.get("id_dispositivo").contains(sSubCadena)) {
                                queryValues.put("id_dispositivo", sSubCadena);
                                queryValues.put("nombre", sSubCadena2);
                                controller.updips(queryValues);
                            } else {
                                queryValues.put("id_dispositivo", sSubCadena);
                                queryValues.put("nombre", sSubCadena2);
                                controller.inserdispo(queryValues);
                            }
                        }
                    }
                }


            }else {
                for (int i = 0; i < split.length; i++) {
                   // Toast.makeText(getActivity(),"entra y",Toast.LENGTH_LONG).show();
                    if (split[i].contains("NORMAL")) {
                        queryValues = new HashMap<String, String>();
                        sSubCadena = split[i].substring(split[i].length() - 7, split[i].length());
                        sSubCadena2 = split[i].substring(7, 49);
                        queryValues.put("id_dispositivo", sSubCadena);
                        queryValues.put("nombre", sSubCadena2);
                        controller.inserdispo(queryValues);
                    }
                    queryValues.put("id_dispositivo", "BATERIA");
                    queryValues.put("nombre", "BATERIA");
                    controller.inserdispo(queryValues);

                }
            }


        }catch (Exception e){Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();}





//
//        controller = new DBController(getActivity());
//        ArrayList<HashMap<String, String>> userList2 = controller.getUsers();
//        try {
//        if (userList.size() != 0) {
//                String ids = "";
//                String nombres ="";
//                for (HashMap<String, String> hashMap : userList2) {
//                    ids= ids+" "+hashMap.get("id_dispositivo")+"\n";
//                    nombres= nombres+" "+hashMap.get("nombre")+"\n";
//                }
//                Buffertext.setText(ids+" "+nombres);
//            }
//                tarea1.cancel(true);
//            }catch (Exception e){Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG).show();}



    }

    //************ CONFIGURACION INTERFACE SERVICECONNECTION IBINDER******/////////////
    private ServiceConnection sConnectionIB = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MiServiceIBinder.MiBinderIBinder binder = (MiServiceIBinder.MiBinderIBinder) service;
            mServiceIBinder = binder.getService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {}
    };

}
