package mahecha.nicolas.softgrafico;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import mahecha.nicolas.softgrafico.Adaptador.Adaptador;
import mahecha.nicolas.softgrafico.Adaptador.Elemento;
import mahecha.nicolas.softgrafico.Sqlite.DBController;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaEventos extends Fragment {

    ListView lista;
    ArrayList<Elemento> arraydir = new ArrayList<Elemento>();
    DBController controller;

    public ListaEventos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_eventos, container, false);
        lista = (ListView) v.findViewById(R.id.eventos);
        controller = new DBController(getActivity());
        geteventos();
        return v;
    }

    private void geteventos() {
        Elemento item;
        Adaptador adaptador = new Adaptador(getContext(), getActivity(), arraydir);


        ArrayList<HashMap<String, String>> userList = controller.getUsers();
        if (userList.size() != 0) {

            for (HashMap<String, String> hashMap : userList) {
                item = new Elemento("http://www.codigofuego.com/productos/big/SENALAMIENTO-ALARMA-668685.jpg", hashMap.get("nombre"), hashMap.get("id_dispositivo"), "Mahecha", "1");
                arraydir.add(item);

            }


        }

        lista.setAdapter(adaptador);


    }
}