package mahecha.nicolas.softgrafico.Configuracion;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mahecha.nicolas.softgrafico.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfiguacionManual extends Fragment {


    public ConfiguacionManual() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuacion_manual, container, false);
    }

}
