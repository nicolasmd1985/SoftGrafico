package mahecha.nicolas.softgrafico;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mahecha.nicolas.softgrafico.Rs232.MiServiceIBinder;
import mahecha.nicolas.softgrafico.Sqlite.DBController;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragConfiguracion extends Fragment {

    Button Autoconfig;
    private MiServiceIBinder mServiceIBinder;



    public FragConfiguracion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_frag_configuracion, container, false);
        final DBController controller = new DBController(getActivity());
        Autoconfig = (Button)v.findViewById(R.id.button);
        Autoconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Autoconfigura buffer = new Autoconfigura();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.lista,buffer, "tag");
                ft.addToBackStack("tag");
                ft.commit();

            }

        });

        return v;
    }





}
