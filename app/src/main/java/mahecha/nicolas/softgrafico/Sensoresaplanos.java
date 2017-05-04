package mahecha.nicolas.softgrafico;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mahecha.nicolas.softgrafico.Adaptador.Adaptador;
import mahecha.nicolas.softgrafico.Adaptador.Elemento;
import mahecha.nicolas.softgrafico.Sqlite.DBController;


/**
 * A simple {@link Fragment} subclass.
 */
public class Sensoresaplanos extends Fragment {


    ListView listadispo;
    ArrayList<Elemento> arraydir;
    DBController controller;
    Adaptador adaptador;
    File[] lista;
    List<String> listItems;


    public Sensoresaplanos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sensoresaplanos, container, false);

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        lista= path.listFiles();
        listItems = new ArrayList<String>();

        for(int i=0;i<lista.length;i++) {
            //Toast.makeText(getActivity(), lista[i].toString(), Toast.LENGTH_LONG).show();
            listItems.add(lista[i].toString());

        }

        listadispo = (ListView)v.findViewById(R.id.asigpla);

        controller = new DBController(getActivity());
        arraydir = new ArrayList<Elemento>();
        geteventos();

        listadispo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int i, long l) {
                String nn = String.valueOf(arraydir.get(i).getTitulo());
                Toast.makeText(getActivity(),nn,Toast.LENGTH_LONG).show();
                showSimplePopUp();
            }
        });

//        listadispo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String nn = String.valueOf(arraydir.get(i).getidop());
//                Toast.makeText(getActivity(),nn,Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });

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
                    item = new Elemento(sensorhumo, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "Pruebas", "1");
                    arraydir.add(item);
                }
                if(hashMap.get("nombre").contains("SUPRV")) {
                    item = new Elemento(pulsador, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "Pruebas", "1");
                    arraydir.add(item);
                }
                if(hashMap.get("nombre").contains("PULSADOR") && hashMap.get("nombre").contains("MONITOR")) {
                    item = new Elemento(avisador, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "Pruebas", "1");
                    arraydir.add(item);
                }
                if(hashMap.get("nombre").contains("MONITOR") && hashMap.get("nombre").contains("DIRECCION")) {
                    item = new Elemento(monitor, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "Pruebas", "1");
                    arraydir.add(item);
                }
                if(hashMap.get("nombre").contains("BATERIA")) {
                    item = new Elemento(bateria, hashMap.get("nombre"), hashMap.get("id_dispositivo"), "Pruebas", "1");
                    arraydir.add(item);
                }


            }


        }

        listadispo.setAdapter(adaptador);




    }



    ///////////******************POP UP**************//////////////
    private void showSimplePopUp() {

        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());

        helpBuilder.setTitle(R.string.pick_color).setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });


        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }




}
