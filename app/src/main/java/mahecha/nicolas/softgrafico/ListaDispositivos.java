package mahecha.nicolas.softgrafico;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
    ArrayList<Elemento> arraydir = new ArrayList<Elemento>();
    DBController controller;
    Adaptador adaptador;

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
        geteventos();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {

                String nn = String.valueOf(arraydir.get(i).getTitulo());
                Toast.makeText(getContext(),nn,Toast.LENGTH_LONG).show();
                controller.dipsup(nn);
                refresh();
            }
        });

        return v;
    }

    private void geteventos() {
        Elemento item;
        adaptador = new Adaptador(getContext(),getActivity(), arraydir);


        ArrayList<HashMap<String, String>> userList = controller.getUsers();
        if (userList.size() != 0) {

            for (HashMap<String, String> hashMap : userList) {
                item = new Elemento("http://www.codigofuego.com/productos/big/SENALAMIENTO-ALARMA-668685.jpg", hashMap.get("nombre"),hashMap.get("id_dispositivo"),"Pruebas","1");
                arraydir.add(item);

            }


        }

        lista.setAdapter(adaptador);




    }


    public void refresh()
    {
        ListaDispositivos map = new ListaDispositivos();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.lista,map, "tag");
        ft.addToBackStack("tag");
        ft.commit();
    }




}
