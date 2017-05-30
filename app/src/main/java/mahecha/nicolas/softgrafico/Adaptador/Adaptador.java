package mahecha.nicolas.softgrafico.Adaptador;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import mahecha.nicolas.softgrafico.R;


public class Adaptador extends BaseAdapter {

    Context context;

    protected Activity activity;
    //ARRAYLIST CON TODOS LOS ITEMS
    protected ArrayList<Elemento> items;

    //CONSTRUCTOR
    public Adaptador(Context context, Activity activity, ArrayList<Elemento> items) {
        this.activity = activity;
        this.items = items;
        this.context = context;
    }



    //CUENTA LOS ELEMENTOS
    @Override
    public int getCount() {
        return items.size();
    }
    //DEVUELVE UN OBJETO DE UNA DETERMINADA POSICION
    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }
    //DEVUELVE EL ID DE UN ELEMENTO
    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }
    //METODO PRINCIPAL, AQUI SE LLENAN LOS DATOS
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // SE GENERA UN CONVERTVIEW POR MOTIVOS DE EFICIENCIA DE MEMORIA
        //ES UN NIVEL MAS BAJO DE VISTA, PARA QUE OCUPEN MENOS MEMORIA LAS IMAGENES
        View v = convertView;
        //ASOCIAMOS LA VISTA AL LAYOUT DEL RECURSO XML DONDE ESTA LA BASE DE CADA ITEM
        if(convertView == null){
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.itemlista, null);
        }

        Elemento dir = items.get(position);
        //RELLENAMOS LA IMAGEN Y EL TEXTO
        ImageView foto = (ImageView) v.findViewById(R.id.imageItem);
        Picasso.with(context).load(dir.getImagen()).into(foto);

        //foto.setImageResource(dir.getImagen());
        TextView id_dispostivo = (TextView) v.findViewById(R.id.id_dispositivo);
        id_dispostivo.setText(dir.getId_dispositivo());

        //foto.setImageResource(dir.getImagen());
        TextView nombre = (TextView) v.findViewById(R.id.descripcion);
        nombre.setText(dir.getNombre());

        //foto.setImageResource(dir.getImagen());
        TextView fecha = (TextView) v.findViewById(R.id.fecha);
        fecha.setText(dir.getFecha());

        //foto.setImageResource(dir.getImagen());
        TextView plano = (TextView) v.findViewById(R.id.plano);
        plano.setText(dir.getPlano());

        // DEVOLVEMOS VISTA
        return v;
    }
}