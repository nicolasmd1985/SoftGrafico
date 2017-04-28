package mahecha.nicolas.softgrafico;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
public class ListaEventos extends Fragment {

    ListView lista;
    ArrayList<Elemento> arraydir = new ArrayList<Elemento>();
    DBController controller;

    public ListaEventos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lista_eventos, container, false);
        lista = (ListView) v.findViewById(R.id.eventos);
        controller = new DBController(getActivity());
        geteventos();


        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {

                String nn = String.valueOf(arraydir.get(i).getTitulo());
                Toast.makeText(getContext(),nn,Toast.LENGTH_LONG).show();
                //controller.dipsup(nn);
                refresh();
            }
        });

        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String nn = String.valueOf(arraydir.get(i).getidop());
                Toast.makeText(getContext(),nn,Toast.LENGTH_LONG).show();
                controller.upstado(nn);
                refresh();
                return false;
            }
        });

        return v;
    }

    private void geteventos() {
        Elemento item;
        Adaptador adaptador = new Adaptador(getContext(), getActivity(), arraydir);


        ArrayList<HashMap<String, String>> userList = controller.geteventos();
        if (userList.size() != 0) {

            for (HashMap<String, String> hashMap : userList) {
               // Toast.makeText(getContext(),hashMap.get("tipo"),Toast.LENGTH_LONG).show();

                if(hashMap.get("tipo").contains("1") ) {

                    item = new Elemento("https://4.bp.blogspot.com/-Y1BpZCehYVA/V1qtGQMVNtI/AAAAAAAAC2M/5pqiLcqw4koLPaJbChmwDDSlNg4XLQjUQCLcB/s1600/AVERIA.png",hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("id_evento"));
                    arraydir.add(item);
                }
                if(hashMap.get("tipo").contains("2")) {
                    item = new Elemento("https://cdn3.iconfinder.com/data/icons/3d-printing-icon-set/512/Error.png", hashMap.get("nombre"), hashMap.get("id_dispositivo"), hashMap.get("fecha"), hashMap.get("id_evento"));
                    arraydir.add(item);
                }
            }

            lista.setAdapter(adaptador);
        }




    }


    public void refresh()
    {
        ListaEventos map = new ListaEventos();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.lista,map, "tag");
        ft.addToBackStack("tag");
        ft.commit();
    }


}