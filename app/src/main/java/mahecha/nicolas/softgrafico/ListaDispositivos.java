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

                String plano = String.valueOf(arraydir.get(i).getPlano());
                String posx = String.valueOf(arraydir.get(i).getPosx());
                String posy = String.valueOf(arraydir.get(i).getPosy());
                String imagen = String.valueOf(arraydir.get(i).getNombre());

               //Toast.makeText(getActivity(),plano+" "+posx+" "+posy,Toast.LENGTH_LONG).show();
                cargamap(plano,posx,posy,imagen);

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
               if(
                       hashMap.get("nombre").contains("HUMO") ||
                       hashMap.get("nombre").contains("SMOKE")
                       ) {
                    item = new Elemento(sensorhumo, hashMap.get("nombre"), hashMap.get("id_dispositivo"),"",hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"),"");
                    arraydir.add(item);
                }
                else if(hashMap.get("nombre").contains("SUPRV") ||
                       hashMap.get("nombre").contains("SUPERV")) {
                    item = new Elemento(pulsador, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "",hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"),"");
                    arraydir.add(item);
                }
                else if(
                             hashMap.get("nombre").contains("PULSADOR") ||
                             hashMap.get("nombre").contains("PULL_STATION")
                       ) {
                    item = new Elemento(avisador, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "",hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"),"");
                    arraydir.add(item);
                }
                else if(
                                hashMap.get("nombre").contains("BATERIA") ||
                                hashMap.get("nombre").contains("BATTERY")
                       ) {
                    item = new Elemento(bateria, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "",hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"),"");
                    arraydir.add(item);
                }
                else if(hashMap.get("nombre").contains("MONITOR")) {
                    item = new Elemento(monitor , hashMap.get("nombre"), hashMap.get("id_dispositivo"), "",hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"),"");
                    arraydir.add(item);
                }
                else if(
                                hashMap.get("nombre").contains("HEAT") ||
                                hashMap.get("nombre").contains("CALOR")
                        ) {
                    item = new Elemento(sensorhumo, hashMap.get("nombre"), hashMap.get("id_dispositivo"),"",hashMap.get("plano"), hashMap.get("posx"),hashMap.get("posy"),"");
                    arraydir.add(item);
                }
         }
       }

        lista.setAdapter(adaptador);
   }


   public void cargamap(String plano,String posx, String posy,String imagen)
   {


       fm = getFragmentManager();
       fm.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
       aux = getFragmentManager().findFragmentByTag("mapas");
       fm.beginTransaction().remove(aux).commit();
       Bundle bundle = new Bundle();
       bundle.putString("plano",plano);
       bundle.putString("posx",posx);
       bundle.putString("posy",posy);
       bundle.putString("imagen",imagen);
       aux = new Mapas();
       aux.setArguments(bundle);
       fm = getFragmentManager();
       fm.beginTransaction().replace(R.id.principal,aux,"mapas").commit();
       fm.executePendingTransactions();


   }

}
