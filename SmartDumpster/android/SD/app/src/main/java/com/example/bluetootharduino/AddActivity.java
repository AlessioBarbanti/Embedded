package com.example.bluetootharduino;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.bluetootharduino.utils.Http;
import com.example.bluetootharduino.utils.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.UUID;

public class AddActivity extends Activity {
    private UUID mDeviceUUID;
    private BluetoothSocket mBTSocket;
    private ReadInput mReadThread = null;

    private boolean mIsBluetoothConnected = false;


    private BluetoothDevice mDevice;

    final static String A="A\n";
    final static String B="B\n";
    final static String C="C\n";
    final static String DISCONNECT="d\n";
    final static String TIMER="E\n";

    private Button buttonA;
    private Button buttonB;
    private Button buttonC;
    private Button buttonTimer;

    private TextView textViewTimer;

    private CountDownTimer countDownTimer;
    private long timeLeftMS = 60000;
    private final long extraTimeMS = 30000;

    private ProgressDialog progressDialog;

    private int onstopdone = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ActivityHelper.initialize(this);

        buttonA = (Button) findViewById(R.id.buttonA);
        buttonB = (Button) findViewById(R.id.buttonB);
        buttonC = (Button) findViewById(R.id.buttonC);
        buttonTimer = (Button) findViewById(R.id.buttonTimer);

        textViewTimer = (TextView) findViewById(R.id.textViewTimer);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        mDevice = b.getParcelable(MainActivity.DEVICE_EXTRA);
        mDeviceUUID = UUID.fromString(b.getString(MainActivity.DEVICE_UUID));

        int mMaxChars = b.getInt(MainActivity.BUFFER_SIZE);

        buttonTimer.setTextColor(0xFFFFFFFF);//TESTO BIANCO
        buttonTimer.setEnabled(false);
        buttonTimer.setBackground(getDrawable(R.drawable.rounded_gray));


        Utilities.trash_type = "vuoto";


        if (mBTSocket == null || !mIsBluetoothConnected) {
            new ConnectBT().execute();
        }

        buttonA.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                startTimer();

                buttonA.setVisibility(View.GONE);
                buttonC.setVisibility(View.GONE);
                buttonB.setBackground(getDrawable(R.drawable.rounded_item_selected));
                buttonB.setText("Rifiuto di tipo A");
                buttonB.setTextSize(25);
                findViewById(R.id.textView).setVisibility(View.GONE);


                buttonTimer.setBackground(getDrawable(R.drawable.rounded));
                buttonTimer.setEnabled(true);

                Utilities.trash_type = A;

                try {
                    mBTSocket.getOutputStream().write(A.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                startTimer();

                buttonA.setVisibility(View.GONE);
                buttonC.setVisibility(View.GONE);
                buttonB.setBackground(getDrawable(R.drawable.rounded_item_selected));
                buttonB.setText("Rifiuto di tipo B");
                buttonB.setTextSize(25);
                findViewById(R.id.textView).setVisibility(View.GONE);

                buttonTimer.setBackground(getDrawable(R.drawable.rounded));
                buttonTimer.setEnabled(true);

                Utilities.trash_type = B;

                try {
                    mBTSocket.getOutputStream().write(B.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                startTimer();

                buttonA.setVisibility(View.GONE);
                buttonC.setVisibility(View.GONE);
                buttonB.setBackground(getDrawable(R.drawable.rounded_item_selected));
                buttonB.setText("Rifiuto di tipo C");
                buttonB.setTextSize(25);
                findViewById(R.id.textView).setVisibility(View.GONE);

                buttonTimer.setBackground(getDrawable(R.drawable.rounded));
                buttonTimer.setEnabled(true);

                Utilities.trash_type = C;

                try {
                    mBTSocket.getOutputStream().write(C.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        buttonTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timeLeftMS += 30000;
                countDownTimer.cancel();
                startTimer();

                try {
                    mBTSocket.getOutputStream().write(TIMER.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private class ReadInput implements Runnable {

        private boolean bStop = false;
        private Thread t;

        public ReadInput() {
            t = new Thread(this, "Input Thread");
            t.start();
        }

        public boolean isRunning() {
            return t.isAlive();
        }

        @Override
        public void run() {
            InputStream inputStream;

            try {
                inputStream = mBTSocket.getInputStream();
                while (!bStop) {
                    byte[] buffer = new byte[256];
                    if (inputStream.available() > 0) {
                        inputStream.read(buffer);
                        int i = 0;
                        for (i = 0; i < buffer.length && buffer[i] != 0; i++) {
                        }
                        final String strInput = new String(buffer, 0, i);
                    }
                    Thread.sleep(500);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

        }

        public void stop() {
            bStop = true;
        }

    }

    protected void onResume() {
        if(onstopdone == 1){
            Intent resumeIntent = new Intent(this, MainActivity.class);
            startActivity(resumeIntent);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        onstopdone = 1;
        if (mBTSocket != null && mIsBluetoothConnected) {
            new DisConnectBT().execute();
        }
        JSONObject content = new JSONObject();
        try {
            content.put("exit", "exit");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Http.post(Utilities.url+"exit", content.toString().getBytes(),new Http.Listener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onHttpResponseAvailable(HttpResponse response) {
            }
        });

        try {
            mBTSocket.getOutputStream().write(DISCONNECT.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void startTimer(){
        boolean isTimerRunning = true;
        countDownTimer = new CountDownTimer(timeLeftMS, 1000){

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMS = millisUntilFinished;
                updateTimer();
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onFinish() {

                if(Utilities.trash_type.equals("vuoto")){
                    msg("Selezionare un tipo di rifiuto");
                } else {
                    if (mBTSocket != null && mIsBluetoothConnected) {
                        new DisConnectBT().execute();
                    }
                    JSONObject content = new JSONObject();
                    try {
                        content.put("m", Utilities.trash_type);
                        content.put("t", Utilities.token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Http.post(Utilities.url+"app", content.toString().getBytes(),new Http.Listener() {

                        @Override
                        public void onHttpResponseAvailable(HttpResponse response) {
                            int code;
                            try {
                                code = response.code();
                            } catch (NullPointerException e) {
                                code = 417;
                            }
                            if (code != 417) {
                                if (response.code() == HttpURLConnection.HTTP_OK) {
                                    try {
                                        JSONObject json = new JSONObject(response.contentAsString());
                                        String risposta = json.getString("risposta");
                                        if (risposta.equals("OK")) {
                                            msg("Deposito effettuato con successo");
                                            countDownTimer.cancel();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            msg("Qualcosa é andato storo");
                                        }
                                    } catch (JSONException | IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    msg("Qualcosa é andato super storto");
                                }
                            }
                        }
                    });
                }
            }
        }.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateTimer(){
        int minutes = (int) timeLeftMS/60000;
        int seconds = (int) timeLeftMS % 60000/1000;
        String timeLeftString;

        timeLeftString = "" + minutes + ":";
        if(seconds < 10) timeLeftString += "0";
        timeLeftString += seconds;

        textViewTimer.setText(timeLeftString);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void msg(String s) {
        Toast toast = Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG);
        toast.getView().getBackground().setColorFilter(getColor(R.color.myGray), PorterDuff.Mode.SRC_IN);

        TextView text = toast.getView().findViewById(android.R.id.message);
        text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        text.setTextColor(getColor(R.color.WHITE));
        toast.show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean mConnectSuccessful = true;

        @Override
        protected void onPreExecute() {

            progressDialog = ProgressDialog.show(AddActivity.this, "Dammi un secondo", "connettendo...");// http://stackoverflow.com/a/11130220/1287554

        }

        @Override
        protected Void doInBackground(Void... devices) {

            try {
                if (mBTSocket == null || !mIsBluetoothConnected) {
                    mBTSocket = mDevice.createInsecureRfcommSocketToServiceRecord(mDeviceUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    mBTSocket.connect();
                }
            } catch (IOException e) {
                mConnectSuccessful = false;
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (!mConnectSuccessful) {
                msg("Impossibile connettersi");
                finish();
            } else {
                mIsBluetoothConnected = true;
                mReadThread = new ReadInput(); // Kick off input reader
            }

            progressDialog.dismiss();
        }

    }

    private class DisConnectBT extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (mReadThread != null) {
                mReadThread.stop();
                while (mReadThread.isRunning())
                    ; // Wait until it stops
                mReadThread = null;

            }

            try {
                mBTSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mIsBluetoothConnected = false;
            boolean mIsUserInitiatedDisconnect = false;
            if (mIsUserInitiatedDisconnect) {
                finish();
            }
        }

    }
}