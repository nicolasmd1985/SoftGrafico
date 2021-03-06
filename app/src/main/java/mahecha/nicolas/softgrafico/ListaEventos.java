package mahecha.nicolas.softgrafico;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import mahecha.nicolas.softgrafico.Adaptador.Adaptador;
import mahecha.nicolas.softgrafico.Adaptador.Elemento;
import mahecha.nicolas.softgrafico.Sqlite.DBController;
import android.app.Fragment;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaEventos extends Fragment {

    ListView lista;
    ArrayList<Elemento> arraydir;
    DBController controller;
    String alarma,averia,bateria,pulsador;
    ////////////*******MANAGER**********////////////
    FragmentManager fm;
    Fragment aux;

    int posic=0;

    View v;

    public ListaEventos() {
        // Required empty public constructor
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        v = null; // now cleaning up!
        arraydir = null;
        lista = null;
       // bMap = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        v = inflater.inflate(R.layout.fragment_lista_eventos, container, false);
        lista = (ListView) v.findViewById(R.id.eventos);
        arraydir = new ArrayList<Elemento>();
        controller = new DBController(getActivity());
        geteventos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {
               // Toast.makeText(getActivity(),""+i ,Toast.LENGTH_LONG).show();
                String plano = String.valueOf(arraydir.get(i).getPlano());
                String posx = String.valueOf(arraydir.get(i).getPosx());
                String posy = String.valueOf(arraydir.get(i).getPosy());
                String imagen = String.valueOf(arraydir.get(i).getNombre());

                //Toast.makeText(getActivity(),plano+" "+posx+" "+posy,Toast.LENGTH_LONG).show();
                cargamap(plano,posx,posy,imagen);
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nn = String.valueOf(arraydir.get(i).getId_evento());
               // Toast.makeText(getActivity(),nn,Toast.LENGTH_LONG).show();
                controller.upstado(nn);
                refresh();
                return false;
            }
        });





//
//            String plano = String.valueOf(arraydir.get(0).getPlano());
//            String posx = String.valueOf(arraydir.get(0).getPosx());
//            String posy = String.valueOf(arraydir.get(0).getPosy());
//
//            //Toast.makeText(getActivity(),plano+" "+posx+" "+posy,Toast.LENGTH_LONG).show();
//            cargamap(plano, posx, posy);
//

        return v;
    }

    private void geteventos() {
        posic=0;
        Elemento item;
        Adaptador adaptador = new Adaptador(getActivity(), getActivity(), arraydir);


        ArrayList<HashMap<String, String>> userList = controller.geteventos();

        if (userList.size() != 0) {

            for (HashMap<String, String> hashMap : userList) {
                posic++;
               // Toast.makeText(getContext(),hashMap.get("tipo"),Toast.LENGTH_LONG).show();
                alarma="android.resource://mahecha.nicolas.softgrafico/drawable/averia";
                averia="android.resource://mahecha.nicolas.softgrafico/drawable/alarma";
                bateria="android.resource://mahecha.nicolas.softgrafico/drawable/pulsador";
                pulsador="android.resource://mahecha.nicolas.softgrafico/drawable/bateria";


                if(hashMap.get("tipo").contains("1") ) {
                    item = new Elemento(alarma, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"),hashMap.get("plano"),  hashMap.get("posx"),hashMap.get("posy"),hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("2")) {
                    item = new Elemento(averia, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("plano"),  hashMap.get("posx"),hashMap.get("posy"),hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("3")) {
                    item = new Elemento(bateria, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("plano"),  hashMap.get("posx"),hashMap.get("posy"),hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("4")) {
                    item = new Elemento(pulsador, hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("plano"),  hashMap.get("posx"),hashMap.get("posy"),hashMap.get("id_evento"));
                    arraydir.add(item);
                }
            }



            lista.setAdapter(adaptador);




        }
    }


    public void refresh()
    {
        aux =null;
        aux = getFragmentManager().findFragmentByTag("listeven");
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(aux);
        ft.attach(aux);
        ft.commit();
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