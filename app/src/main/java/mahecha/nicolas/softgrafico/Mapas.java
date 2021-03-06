package mahecha.nicolas.softgrafico;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;

import android.text.Layout;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;

import mahecha.nicolas.softgrafico.Adaptador.Elemento;


/**
 * A simple {@link Fragment} subclass.
 */
public class Mapas extends Fragment {

    RelativeLayout relativeLayout,relative2;
    private View v;
    Bitmap bMap,bMap2,reziza;
    Drawable mImage,mImage2;
    Paint paint;
    float posx=0,posy=0;


    public Mapas() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        v = null; // now cleaning up!
        mImage = null;
        bMap = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_mapas, container, false);
        relativeLayout = (RelativeLayout)v.findViewById(R.id.rect);
        relativeLayout.addView(new ZoomView(getActivity()));


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


        public ZoomView(Context context) {
            super(context);

            detector = new ScaleGestureDetector(getContext(), new ScaleListener());

            if(getArguments().getString("plano") != null) {
                bMap = BitmapFactory.decodeFile(getArguments().getString("plano"));
            }else{

                bMap = BitmapFactory.decodeFile("/storage/emulated/0/Pictures/piso3.jpg");
             }


            if(getArguments().getString("imagen").contains("HUMO")||getArguments().getString("imagen").contains("SMOKE")) {
                bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.sensorhumo);
            }
            else if(getArguments().getString("imagen").contains("SUPRV")||getArguments().getString("imagen").contains("SUPERV")) {
                bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.pulsador);
            }
            else if(getArguments().getString("imagen").contains("PULSADOR")||
                    getArguments().getString("imagen").contains("PULL_STATION")) {
                bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.avisador);
            }
            else if(getArguments().getString("imagen").contains("BATERIA")||
                    getArguments().getString("imagen").contains("BATTERY")) {
                bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.bateria);
            }
            else if(getArguments().getString("imagen").contains("MONITOR")) {
                bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.monitor);
            }else{bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.averia);}

           // bMap2 = BitmapFactory.decodeResource(getActivity().getResources(),R.drawable.averia);
            mImage = new BitmapDrawable(bMap);
           // mImage2 = getResources().getDrawable(R.drawable.averia);
            reziza = getResizedBitmap(bMap2,30,30);
            paint = new Paint();

            posx = Float.parseFloat(getArguments().getString("posx"));
            posy = Float.parseFloat(getArguments().getString("posy"));

        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                    mode = DRAG;
                    startX = event.getX() - previousTranslateX;
                    startY = event.getY() - previousTranslateY;
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

            mImage.setBounds(0, 0, getWidth(), getHeight());

            canvas.scale(scaleFactor, scaleFactor,getWidth()/2,getHeight()/2);
            canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
            ////****PONER IMAGEN////////
            mImage.draw(canvas);
            canvas.drawBitmap(reziza,posx,posy,paint);
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

}


