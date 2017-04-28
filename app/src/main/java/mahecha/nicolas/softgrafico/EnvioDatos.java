package mahecha.nicolas.softgrafico;

import android.content.Context;
import android.text.format.DateFormat;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by nicolas on 27/04/2017.
 */

public class EnvioDatos {

    Context context;
    HashMap<String, String> queryValues;

    public EnvioDatos(Context context){
        super();
        this.context = context;
    }

    public void enviar(String resultado)
    {

        queryValues = new HashMap<String, String>();
        queryValues.put("fkidusuario", "3");
        queryValues.put("reporte", String.valueOf(resultado));
        queryValues.put("tiempo", tiempo());
        enviarepo();
    }


    private void enviarepo() {

        Gson gson = new GsonBuilder().create();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        wordList.add(queryValues);



        if(wordList.size()!=0)
        {
            final String jrep = gson.toJson(wordList);
            params.put("jrep", jrep);
            client.post("http://elca.sytes.net:5537/testELCA_APP/detalles_pedidov7/reportmant.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    Toast.makeText(context, "Enviado!!!",
                            Toast.LENGTH_LONG).show();
                    //System.out.println(jrep);
                    //System.out.println(response);

                }
                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    if (statusCode == 404) {
                        Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
                    } else if (statusCode == 500) {
                        Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Dispositivo Sin Conexión a Internet",
                                Toast.LENGTH_LONG).show();
                    }

                }
            });


        }else{Toast.makeText(context, "Nada de nada",
                Toast.LENGTH_LONG).show();}

    }


    ////////////////////***************OBTIENE TIEMPO**************///////////////////
    public String tiempo()
    {
        Date date = new Date();
        CharSequence s  = DateFormat.format("yyyy/M/d H:m", date.getTime());
        String time = s.toString();
        return time ;
    }





}
