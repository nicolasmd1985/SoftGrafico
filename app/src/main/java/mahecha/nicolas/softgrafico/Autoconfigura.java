package mahecha.nicolas.softgrafico;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    TextView Buffertext,Buffertex2;
    private MiServiceIBinder mServiceIBinder;
    String resultado;
    private MiTareaAsincrona tarea1;
    HashMap<String, String> queryValues;
    DBController controller;
    public Autoconfigura() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_buffer, container, false);
        Buffertext = (TextView)v.findViewById(R.id.bufferte);
        Buffertex2 = (TextView)v.findViewById(R.id.buffer2);
        controller = new DBController(getActivity());

        Intent intent = new Intent(getContext(), MiServiceIBinder.class);
        getContext().bindService(intent, sConnectionIB, Context.BIND_AUTO_CREATE);


        tarea1 = new MiTareaAsincrona();
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
            // int progreso = values[0].intValue();
            //texto.setText(""+progreso);
            if (mServiceIBinder != null) {
                resultado = String.valueOf(mServiceIBinder.getResultado());


                if(resultado != null) {
                    //Buffertext.setText("Su resuldato es: " + resultado);



                    if(!resultado.contentEquals(""))
                    {
                        saltos();
//                        enviar();
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
                Toast.makeText(getContext(), "Tarea finalizada!", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onCancelled() {
            Toast.makeText(getContext(), "Tarea cancelada!", Toast.LENGTH_SHORT).show();
        }
    }


    public void saltos() {
        String[] split = resultado.split("\n");
        StringBuilder sb = new StringBuilder();
        int num=0;
        String sSubCadena = "";
        String sSubCadena2 = "";


        for (int i = 0; i < split.length; i++) {
            //sb.append(split[i]);
//            if (i != split.length - 1) {
//                sb.append(" ");
//            }

               if (split[i].contains("NORMAL")) {
                   queryValues = new HashMap<String, String>();
                   sSubCadena = split[i].substring(split[i].length()-7,split[i].length());
                   sSubCadena2 = split[i].substring(7,49);
//                   queryValues.put("id_dispositivo", split[i].substring(split[i].length() - 7, split[i].length()).toString());
//                   queryValues.put("nombre", split[i].substring(7, 49).toString());

                    queryValues.put("id_dispositivo", sSubCadena);
                    queryValues.put("nombre", sSubCadena2);

                   controller.inserdispo(queryValues);
                   //sb.append(i+" "+split[i]+"\n");
                   //sSubCadena = sSubCadena+" "+split[i].substring(split[i].length()-7,split[i].length())+"\n";
                   //sSubCadena2 = sSubCadena2+" "+split[i].substring(7,49)+"\n";

               }


        }


            ArrayList<HashMap<String, String>> userList = controller.getUsers();
            if (userList.size() != 0) {
                String ids = "";
                String nombres ="";
                for (HashMap<String, String> hashMap : userList) {
                    ids= ids+" "+hashMap.get("id_dispositivo")+"\n";
                    nombres= nombres+" "+hashMap.get("nombre")+"\n";

                }

               // Buffertex2.setText(ids);
                Buffertext.setText(ids+" "+nombres);
            }

            tarea1.cancel(true);
            mServiceIBinder.onDestroy();

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


}
