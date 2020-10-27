package com.example.bluetootharduino;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bluetootharduino.utils.Http;
import com.example.bluetootharduino.utils.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MainActivity extends AppCompatActivity {

    private BluetoothAdapter mBTAdapter;
    private static final int BT_ENABLE_REQUEST = 10; // This is the code we use for BT Enable
    private static final int SETTINGS = 20;
    private UUID mDeviceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private int mBufferSize = 50000; //Default
    public static final String DEVICE_EXTRA = "com.example.bluetoothlight.SOCKET";
    public static final String DEVICE_UUID = "com.example.bluetoothlight.uuid";
    private static final String DEVICE_LIST = "com.example.bluetoothlight.devicelist";
    private static final String DEVICE_LIST_SELECTED = "com.example.bluetoothlight.devicelistselected";
    public static final String BUFFER_SIZE = "com.example.bluetoothlight.buffersize";
    private static final String TAG = "BlueTest5-MainActivity";

    //variabile di controllo cambio pagina (per onStop)
    private boolean cambioPagina = false;

    //ELEMENTI GRAFICI
    private TextView tIdServer;
    private Button bConnettiServer;
    private TextView tStatoServer;
    private Button bCercaBT;
    private Button bConnettiBT;
    private ListView lVBT;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        tIdServer = (TextView) findViewById(R.id.plainTextIdServer);
        bConnettiServer = (Button) findViewById(R.id.buttonConnettiServer);
        tStatoServer = (TextView) findViewById(R.id.textViewStatoServer);
        bCercaBT = (Button) findViewById(R.id.buttonCercaBt);
        bConnettiBT = (Button) findViewById(R.id.buttonConnettiBT);
        lVBT = (ListView) findViewById(R.id.listViewBT);

        bCercaBT.setEnabled(false);
        bConnettiBT.setEnabled(false);
        bCercaBT.setBackground(getDrawable(R.drawable.rounded_gray));
        bConnettiBT.setBackground(getDrawable(R.drawable.rounded_gray));

        if (savedInstanceState != null) {
            ArrayList<BluetoothDevice> list = savedInstanceState.getParcelableArrayList(DEVICE_LIST);
            if (list != null) {
                initList(list);
                MyAdapter adapter = (MyAdapter) lVBT.getAdapter();
                int selectedIndex = savedInstanceState.getInt(DEVICE_LIST_SELECTED);
                if (selectedIndex != -1) {
                    adapter.setSelectedIndex(selectedIndex);
                    bConnettiBT.setEnabled(true);
                }
            } else {
                initList(new ArrayList<BluetoothDevice>());
            }

        } else {
            initList(new ArrayList<BluetoothDevice>());
        }

        bConnettiServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Utilities.aggiornaIndirizzo(tIdServer.getText().toString());
                Http.get(Utilities.url+"token", new Http.Listener() {
                    @SuppressLint("SetTextI18n")
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onHttpResponseAvailable(HttpResponse response) {
                        int code;
                        try {
                            code = response.code();
                        } catch (NullPointerException e) {
                            tStatoServer.setText("impossibile raggiungere il server");
                            code = 417;
                        }
                        if (code != 417) {
                            if (response.code() == HttpURLConnection.HTTP_OK) {
                                try {
                                    JSONObject json = new JSONObject(response.contentAsString());
                                    String token = json.getString("token");
                                    Utilities.token = token;
                                    if (token.equals("Il servizio non è al momento disponibile, attendere!")) {
                                        tStatoServer.setText(token);
                                    } else {
                                        tStatoServer.setText("token: " + token);
                                        bCercaBT.setEnabled(true);
                                        bCercaBT.setBackground(getDrawable(R.drawable.rounded));
                                        bConnettiServer.setEnabled(false);
                                        bConnettiServer.setBackground(getDrawable(R.drawable.rounded_gray));

                                    }
                                } catch (JSONException | IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }
        });

        bCercaBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                bCercaBT.setEnabled(false);
                bCercaBT.setBackground(getDrawable(R.drawable.rounded_gray));

                mBTAdapter = BluetoothAdapter.getDefaultAdapter();

                if (mBTAdapter == null) {
                    msg("Bluetooth non trovato");
                } else if (!mBTAdapter.isEnabled()) {
                    Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBT, BT_ENABLE_REQUEST);
                } else {
                    new SearchDevices().execute();
                }
            }
        });

        bConnettiBT.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                cambioPagina = true;
                BluetoothDevice device = ((MyAdapter) (lVBT.getAdapter())).getSelectedItem();
                Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                intent.putExtra(DEVICE_EXTRA, device);
                intent.putExtra(DEVICE_UUID, mDeviceUUID.toString());
                intent.putExtra(BUFFER_SIZE, mBufferSize);
                startActivity(intent);
            }
        });



    }

    protected void onResume() {
        if(!tStatoServer.getText().equals(getString(R.string.stato_server))){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        if(!cambioPagina) {
            JSONObject content = new JSONObject();
            try {
                content.put("exit", "exit");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Http.post(Utilities.url + "exit", content.toString().getBytes(), new Http.Listener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onHttpResponseAvailable(HttpResponse response) {
                }
            });
        }


        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case BT_ENABLE_REQUEST:
                if (resultCode == RESULT_OK) {
                    msg("Il Bluetooth é stato attivato con successo");
                    new SearchDevices().execute();
                } else {
                    msg("Il Bluetooth non é stato attivato");
                }

                break;
            case SETTINGS:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String uuid = prefs.getString("prefUuid", "Null");
                mDeviceUUID = UUID.fromString(uuid);
                Log.d(TAG, "UUID: " + uuid);
                String bufSize = prefs.getString("prefTextBuffer", "Null");
                mBufferSize = Integer.parseInt(bufSize);

                String orientation = prefs.getString("prefOrientation", "Null");
                Log.d(TAG, "Orientation: " + orientation);
                switch (orientation) {
                    case "Landscape":
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                        break;
                    case "Portrait":
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        break;
                    case "Auto":
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
                        break;
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initList(List<BluetoothDevice> objects) {
        final MyAdapter adapter = new MyAdapter(getApplicationContext(), R.layout.list_item, R.id.lstContent, objects);
        lVBT.setAdapter(adapter);
        lVBT.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bConnettiBT.setEnabled(true);
                bConnettiBT.setBackground(getDrawable(R.drawable.rounded));
                adapter.setSelectedIndex(position);
            }
        });
    }


    @SuppressLint("StaticFieldLeak")
    public class SearchDevices extends AsyncTask<Void, Void, List<BluetoothDevice>> {

        @Override
        protected List<BluetoothDevice> doInBackground(Void... params) {
            Set<BluetoothDevice> pairedDevices = mBTAdapter.getBondedDevices();
            return new ArrayList<>(pairedDevices);

        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onPostExecute(List<BluetoothDevice> listDevices) {
            super.onPostExecute(listDevices);
            if (listDevices.size() > 0) {
                MyAdapter adapter = (MyAdapter) lVBT.getAdapter();
                adapter.replaceItems(listDevices);
            } else {
                msg("Nessun device accoppiato");
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private class MyAdapter extends ArrayAdapter<BluetoothDevice> {
        private int selectedIndex;
        private final Context context;
        private List<BluetoothDevice> myList;

        public MyAdapter(Context ctx, int resource, int textViewResourceId, List<BluetoothDevice> objects) {
            super(ctx, resource, textViewResourceId, objects);
            context = ctx;
            myList = objects;
            selectedIndex = -1;
        }

        public void setSelectedIndex(int position) {
            selectedIndex = position;
            notifyDataSetChanged();
        }

        public BluetoothDevice getSelectedItem() {
            return myList.get(selectedIndex);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public BluetoothDevice getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private class ViewHolder {
            TextView tv;
        }

        public void replaceItems(List<BluetoothDevice> list) {
            myList = list;
            notifyDataSetChanged();
        }

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            if (convertView == null) {
                vi = LayoutInflater.from(context).inflate(R.layout.list_item, null);
                holder = new ViewHolder();

                holder.tv = (TextView) vi.findViewById(R.id.lstContent);

                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }

            if (selectedIndex != -1 && position == selectedIndex) {
                holder.tv.setBackground(getDrawable(R.drawable.rounded_item_selected));
            } else {
                holder.tv.setBackground(getDrawable(R.drawable.rounded_item_gray));
            }
            holder.tv.setTextColor(getColor(R.color.myGray));
            BluetoothDevice device = myList.get(position);
            holder.tv.setText(device.getName() + "\n " + device.getAddress());

            return vi;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
            startActivityForResult(intent, SETTINGS);
        }
        return super.onOptionsItemSelected(item);
    }
}