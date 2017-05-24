package mahecha.nicolas.softgrafico.Configuracion;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mahecha.nicolas.softgrafico.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragConfiguracion extends Fragment {

    ////////////*******MANAGER**********////////////
    FragmentManager fm;





    Button Autoconfig,asigsensor;




    public FragConfiguracion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_frag_configuracion, container, false);

        Autoconfig = (Button)v.findViewById(R.id.autoconf);
        fm = getFragmentManager();


        Autoconfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Autoconfigura buffer = new Autoconfigura();
                fm.beginTransaction().replace(R.id.principal,buffer).commit();

            }

        });

        asigsensor = (Button)v.findViewById(R.id.asigsensor);
        asigsensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Sensoresaplanos asigsensor = new Sensoresaplanos();
                fm.beginTransaction().replace(R.id.principal,asigsensor,"asigsensor").commit();
            }
        });

        return v;
    }





}
