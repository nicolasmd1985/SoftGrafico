package mahecha.nicolas.softgrafico;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import mahecha.nicolas.softgrafico.Adaptador.Adaptador;
import mahecha.nicolas.softgrafico.Adaptador.Elemento;
import mahecha.nicolas.softgrafico.Sqlite.DBController;
import android.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaEventos extends android.app.Fragment {

    ListView lista;
    ArrayList<Elemento> arraydir;
    DBController controller;
    String alarma,averia,bateria,pulsador;
    ////////////*******MANAGER**********////////////
    FragmentManager fm;

    public ListaEventos() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  v = inflater.inflate(R.layout.fragment_lista_eventos, container, false);
        lista = (ListView) v.findViewById(R.id.eventos);
        arraydir = new ArrayList<Elemento>();
        controller = new DBController(getActivity());
        geteventos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {

                String nn = String.valueOf(arraydir.get(i).getTitulo());
                Toast.makeText(getActivity(),nn,Toast.LENGTH_LONG).show();
                refresh();
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nn = String.valueOf(arraydir.get(i).getidop());
                Toast.makeText(getActivity(),nn,Toast.LENGTH_LONG).show();
                controller.upstado(nn);
               // v.refreshDrawableState();
                refresh();
                return false;
            }
        });

        return v;
    }

    private void geteventos() {
        Elemento item;
        Adaptador adaptador = new Adaptador(getActivity(), getActivity(), arraydir);


        ArrayList<HashMap<String, String>> userList = controller.geteventos();
        if (userList.size() != 0) {

            for (HashMap<String, String> hashMap : userList) {
               // Toast.makeText(getContext(),hashMap.get("tipo"),Toast.LENGTH_LONG).show();
                alarma="android.resource://mahecha.nicolas.softgrafico/drawable/averia";
                averia="android.resource://mahecha.nicolas.softgrafico/drawable/alarma";
                bateria="android.resource://mahecha.nicolas.softgrafico/drawable/pulsador";
                pulsador="android.resource://mahecha.nicolas.softgrafico/drawable/bateria";


                if(hashMap.get("tipo").contains("1") ) {
                    item = new Elemento(alarma, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("2")) {
                    item = new Elemento(averia, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("3")) {
                    item = new Elemento(bateria, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("4")) {
                    item = new Elemento(pulsador, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("id_evento"));
                    arraydir.add(item);
                }
            }

            lista.setAdapter(adaptador);
        }
    }


    public void refresh()
    {
        ListaEventos listaEventos = new ListaEventos();
        fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.lista,listaEventos).commit();
    }
}