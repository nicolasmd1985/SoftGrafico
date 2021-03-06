package mahecha.nicolas.softgrafico.Configuracion;



import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.HashMap;

import mahecha.nicolas.softgrafico.MainActivity;
import mahecha.nicolas.softgrafico.R;
import mahecha.nicolas.softgrafico.Sqlite.DBController;


/**
 * A simple {@link Fragment} subclass.
 */
public class Configmapa extends Fragment {

    RelativeLayout relativeLayout,relative2;
    private View v;
    Bitmap bMap,bMap2,reziza;
    Drawable mImage,mImage2;
    Paint paint;
    Button guardar;
    float puntox=0,puntoy=0;
    DBController controller;
    HashMap<String, String> queryValues;

    public Configmapa() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_configmapa, container, false);
        relativeLayout = (RelativeLayout)v.findViewById(R.id.rect);
        relativeLayout.addView(new Configmapa.ZoomView(getActivity()));
        guardar = (Button)v.findViewById(R.id.guardar);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showSimplePopUp();

            }
        });
        return  v;
    }


    public class ZoomView extends View {

        //These two constants specify the minimum and maximum zoom
        private  float MIN_ZOOM = 1f;
        private  float MAX_ZOOM = 10f;
        private float scaleFactor = 1.f;
        private ScaleGestureDetector detector;
        //These constants specify the mode that we're in
        private  int NONE = 0;
        private  int DRAG = 1;
        private  int ZOOM = 2;
        private int mode;


        //These two variables keep track of the X and Y coordinate of the finger when it first
        //touches the screen
        private float startX = 0f;
        private float startY = 0f;
        //These two variables keep track of the amount we need to translate the canvas along the X
        //and the Y coordinate
        private float translateX = 0f;
        private float translateY = 0f;
        //These two variables keep track of the amount we translated the X and Y coordinates, the last time we
        //panned.
        private float previousTranslateX = 0f;
        private float previousTranslateY = 0f;
        private boolean dragged = false;
        private float displayWidth;
        private float displayHeight;

        public ZoomView(Context context) {
            super(context);
            detector = new ScaleGestureDetector(getContext(), new Configmapa.ZoomView.ScaleListener());
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);


            if(getArguments().getString("plano") != null) {
                bMap = BitmapFactory.decodeFile(getArguments().getString("plano"));
            }else{
                bMap = BitmapFactory.decodeFile("null");
            }
            mImage = new BitmapDrawable(bMap);



            paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(16);


            // paint.setT

            // paint.
            //bMap2 = BitmapFactory.decodeFile("android.resource://mahecha.nicolas.softgrafico/drawable/sensorhumo");
            bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.averia);
            mImage2 = getResources().getDrawable(R.drawable.averia);
            reziza = getResizedBitmap(bMap2,30,30);


        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    mode = DRAG;

                    startX = event.getX() - previousTranslateX;
                    puntox=startX;
                    startY = event.getY() - previousTranslateY;
                    puntoy=startY;
                    break;

                case MotionEvent.ACTION_MOVE:

                    translateX = event.getX() - startX;
                    translateY = event.getY() - startY;
                    double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                            Math.pow(event.getY() - (startY + previousTranslateY), 2)
                    );

                    if (distance > 0) {
                        dragged = true;
                    }

                    break;

                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = ZOOM;
                    break;

                case MotionEvent.ACTION_UP:
                    mode = NONE;

                    previousTranslateX = translateX;
                    previousTranslateY = translateY;
                    break;

                case MotionEvent.ACTION_POINTER_UP:
                    mode = DRAG;
                    previousTranslateX = translateX;
                    previousTranslateY = translateY;
                    break;
            }

            detector.onTouchEvent(event);


            invalidate();
            return true;
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            // mImage.setBounds(0, 0, (int)displayWidth, (int)displayHeight);
            mImage.setBounds(0, 0, getWidth(), getHeight());
            mImage2.setBounds(50, 50, 100, 100);
            // mImage2.
            canvas.scale(scaleFactor, scaleFactor,getWidth()/2,getHeight()/2);
            canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
            ////****PONER IMAGEN////////
            mImage.draw(canvas);
            canvas.drawText("x:"+startX+"y:"+startY,getWidth()/2 ,getHeight()/2 , paint);
            // mImage2.draw(canvas);
            canvas.drawBitmap(reziza,startX,startY,paint);
            // canvas.d
            //canvas.
            //canvas.restore();

        }

        private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
                return true;
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }


    ///////////******************POP UP**************//////////////
    private void showSimplePopUp() {

        final AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("GUARDAR!!");

        helpBuilder.setMessage("Desea guardar punto?");

        helpBuilder.setPositiveButton("Si",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        queryValues = new HashMap<String, String>();
                        controller = new DBController(getActivity());
                        queryValues.put("id_dispositivo", getArguments().getString("iddisp"));
                        queryValues.put("puntox", ""+puntox);
                        queryValues.put("puntoy", ""+puntoy);

                        try {
                            controller.uppuntos(queryValues);
                        }catch (Exception e) {
                            // Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                });
        helpBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // System.out.println("no");
            }
        });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }



}


