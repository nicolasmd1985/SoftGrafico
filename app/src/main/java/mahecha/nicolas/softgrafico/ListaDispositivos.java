package mahecha.nicolas.softgrafico;


import android.app.Fragment;
import android.app.FragmentManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import mahecha.nicolas.softgrafico.Adaptador.Adaptador;
import mahecha.nicolas.softgrafico.Adaptador.Elemento;
import mahecha.nicolas.softgrafico.Sqlite.DBController;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaDispositivos extends Fragment {

    ListView lista;
    ArrayList<Elemento> arraydir;
    DBController controller;
    Adaptador adaptador;

    ////////////*******MANAGER**********////////////
    FragmentManager fm;


    Fragment aux;


    public ListaDispositivos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=  inflater.inflate(R.layout.lista_dispositivos, container, false);
        lista = (ListView)v.findViewById(R.id.recep);

        controller = new DBController(getActivity());
        arraydir = new ArrayList<Elemento>();

        geteventos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {

                String plano = String.valueOf(arraydir.get(i).getValor());
                String posx = String.valueOf(arraydir.get(i).getidop());
                String posy = String.valueOf(arraydir.get(i).getIdeven());

               //Toast.makeText(getActivity(),plano+" "+posx+" "+posy,Toast.LENGTH_LONG).show();
                cargamap(plano,posx,posy);

            }
        });

        return v;
    }

    private void geteventos() {
        Elemento item;
        adaptador = new Adaptador(getActivity(),getActivity(), arraydir);


        ArrayList<HashMap<String, String>> userList = controller.getUsers();
        if (userList.size() != 0) {
            String sensorhumo="android.resource://mahecha.nicolas.softgrafico/drawable/sensorhumo";
            String avisador="android.resource://mahecha.nicolas.softgrafico/drawable/avisador";
            String bateria="android.resource://mahecha.nicolas.softgrafico/drawable/bateria";
            String bocina="android.resource://mahecha.nicolas.softgrafico/drawable/bocina";
            String monitor="android.resource://mahecha.nicolas.softgrafico/drawable/monitor";
            String pulsador="android.resource://mahecha.nicolas.softgrafico/drawable/pulsador";

            for (HashMap<String, String> hashMap : userList) {
               if(hashMap.get("nombre").contains("HUMO")) {
                    item = new Elemento(sensorhumo, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"));
                    arraydir.add(item);
                }
                else if(hashMap.get("nombre").contains("SUPRV")) {
                    item = new Elemento(pulsador, hashMap.get("nombre"), hashMap.get("id_dispositivo"),  hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"));
                    arraydir.add(item);
                }
                else if(hashMap.get("nombre").contains("MONITOR") && hashMap.get("nombre").contains("PULSADOR")) {
                    item = new Elemento(avisador, hashMap.get("nombre"), hashMap.get("id_dispositivo"),  hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"));
                    arraydir.add(item);
                }
                else if(hashMap.get("nombre").contains("BATERIA")) {
                    item = new Elemento(bateria, hashMap.get("nombre"), hashMap.get("id_dispositivo"),  hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"));
                    arraydir.add(item);
                }
                else if(hashMap.get("nombre").contains("MONITOR")) {
                    item = new Elemento(monitor , hashMap.get("nombre"), hashMap.get("id_dispositivo"),  hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"));
                    arraydir.add(item);
                }
         }
       }

        lista.setAdapter(adaptador);
   }


   public void cargamap(String plano,String posx, String posy)
   {


       fm = getFragmentManager();
       fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
       aux = getFragmentManager().findFragmentByTag("mapas");
       fm.beginTransaction().remove(aux).commit();
       Bundle bundle = new Bundle();
       bundle.putString("plano",plano);
       bundle.putString("posx",posx);
       bundle.putString("posy",posy);
       aux = new Mapas();
       aux.setArguments(bundle);
       fm = getFragmentManager();
       fm.beginTransaction().replace(R.id.principal,aux,"mapas").commit();
       fm.executePendingTransactions();


   }

}
