package mahecha.nicolas.softgrafico.Rs232;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import java.util.Random;

public class MiServiceIBinder extends Service {
	
	private final IBinder iBinder = new MiBinderIBinder();
	private final Random random = new Random();

	public FT311UARTInterface uartInterface;
	public SharedPreferences sharePrefSettings;
	public handler_thread handlerThread;



	int baudRate; /* baud rate */
	byte stopBit; /* 1:1stop bits, 2:2 stop bits */
	byte dataBit; /* 8:8bit, 7: 7bit */
	byte parity; /* 0: none, 1: odd, 2: even, 3: mark, 4: space */
	byte flowControl; /* 0:none, 1: flow control(CTS,RTS) */


	/* local variables */
	byte[] writeBuffer;
	byte[] readBuffer;
	char[] readBufferToChar;
	int[] actualNumBytes;
	byte status;

	//TextView lectura;


	StringBuffer readSB = new StringBuffer();


	public String act_string;
	public boolean bConfiged = false;
	
	
	public class MiBinderIBinder extends Binder {
		public MiServiceIBinder getService() {
            return MiServiceIBinder.this;
        }



    }
	
	
	@Override
	public IBinder onBind(Intent arg0) {
		Toast.makeText(this, "Service iniciado", Toast.LENGTH_SHORT).show();

		sharePrefSettings = getSharedPreferences("UARTLBPref", 0);


		//lectura = (TextView)findViewById(R.id.lectu);
		writeBuffer = new byte[64];
		readBuffer = new byte[4096];
		readBufferToChar = new char[4096];
		actualNumBytes = new int[1];


		baudRate = 9600;
		stopBit = 1;
		dataBit = 7;
		parity = 0;
		flowControl = 0;



		uartInterface = new FT311UARTInterface(this, sharePrefSettings);
		handlerThread = new handler_thread(handler);
		handlerThread.start();

		uartInterface.ResumeAccessory();
		baudRate = 9600;
		stopBit = 1;
		dataBit = 7;
		parity = 0;
		flowControl = 0;

		uartInterface.SetConfig(baudRate, dataBit, stopBit, parity, flowControl);




		return iBinder;
	}
	
	
	@Override
    public void onDestroy(){
        Toast.makeText(this, "Service finalizado", Toast.LENGTH_SHORT).show();

    }

	
	public StringBuffer getResultado() {

		return readSB;
	}

	public void cleanr()
	{
		readSB.setLength(0);
	}




	final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {

			for (int i = 0; i < actualNumBytes[0]; i++) {
				readBufferToChar[i] = (char) readBuffer[i];
			}
			appendData(readBufferToChar, actualNumBytes[0]);
		}
	};

	/* usb input data handler */
	private class handler_thread extends Thread {
		Handler mHandler;

		/* constructor */
		handler_thread(Handler h) {
			mHandler = h;
		}

		public void run() {
			Message msg;

			while (true) {

				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}

				status = uartInterface.ReadData(4096, readBuffer, actualNumBytes);

				if (status == 0x00 && actualNumBytes[0] > 0) {
					msg = mHandler.obtainMessage();
					mHandler.sendMessage(msg);
				}

			}
		}
	}


	public void appendData(char[] data, int len) {
		if (len >= 1)
			readSB.append(String.copyValueOf(data, 0, len));
		//Toast.makeText(getApplicationContext(),readSB,Toast.LENGTH_LONG).show();
		//lectura.setText(readSB);

	}





}