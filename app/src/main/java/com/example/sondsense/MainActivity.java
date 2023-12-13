package com.example.sondsense;

import static com.example.sondsense.R.id.Modo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sondsense.controlador.DispositivosVinculados;
import com.example.sondsense.controlador.PagerController;
import com.example.sondsense.controlador.Sensibilidad;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    TabItem tab1, tab2, tab3;
    PagerController pageAdapter;
    private Sensibilidad sensibilidad;
    private TextView texto;
    private TextView modo;
    private SeekBar seek;
    private String address;
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_CONNECT_PERMISSION = 3;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 2;
    private static final String TAG = "MainActivity";
    private BluetoothDevice DispositivoSeleccionado = null;
    private BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    private ConnectedThread MyConexionBT;

    public static ConnectedThread EXConnectedThread;
    private BluetoothSocket btSocket;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Permisos
        requestBluetoothConnectPermission();
        requestLocationPermission();
        //Inicio de datos
        address = getIntent().getStringExtra(DispositivosVinculados.EXTRA_DEVICE_ADDRESS);
        //carga de preferencias
        cargarPreferencias();
        Window window = getWindow();
        //window.setBackgroundDrawable(new ColorDrawable(Color.GREEN));

        if (address != null) {
            editor.putString("BTAddress", address);
            editor.apply();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, " ----->>>>> ActivityCompat.checkSelfPermission");
                DispositivoSeleccionado = mBtAdapter.getRemoteDevice(address);
                Intent intend = new Intent(this, Sensibilidad.class);
                ConectarDispBT();
                intend.putExtra("conexion_bt", MyConexionBT);
                MyConexionBT.write('a');

                // Crear un canal de notificación (solo necesario en Android 8.0 y versiones posteriores)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("mi_canal_id", "Nombre del Canal", NotificationManager.IMPORTANCE_DEFAULT);
                    NotificationManager notificationManager = getBaseContext().getSystemService(NotificationManager.class);
                    notificationManager.createNotificationChannel(channel);
                }

                // Crear la notificación
                NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(), "mi_canal_id")
                        .setSmallIcon(R.drawable.icono_alertas)
                        .setContentTitle("CONEXION EXITOSA")
                        .setContentText("Conexion exitosa con el dispositivo: " + address)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // Mostrar la notificación
                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getBaseContext());
                notificationManagerCompat.notify(/*ID de la notificación*/1, builder.build());
            }
            //Toast.makeText(getBaseContext(), address, Toast.LENGTH_SHORT).show();
            Toast.makeText(getBaseContext(), "Se ha vinculado el dispositivo correctamente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "Aun no hay un dispositivo vinculado", Toast.LENGTH_SHORT).show();
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.ViewPager);

        tab1 = findViewById(R.id.tabSensibilidad);
        tab2 = findViewById(R.id.tabMetricas);
        tab3 = findViewById(R.id.tabAlertas);

        pageAdapter = new PagerController(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0) {
                    pageAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 1) {
                    pageAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 2) {
                    pageAdapter.notifyDataSetChanged();
                }
                if(tab.getPosition() == 3) {
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
    private void requestBluetoothConnectPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_CONNECT_PERMISSION);
    }
    // Agrega este método para solicitar el permiso
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION_PERMISSION);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_CONNECT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permiso concedido, ahora puedes utilizar funciones de Bluetooth que requieran BLUETOOTH_CONNECT");
            } else {
                Log.d(TAG, "Permiso denegado, debes manejar este caso según tus necesidades");
            }
        }
    }

    private void cargarPreferencias() {
        preferences = getSharedPreferences("Preferencias", Context.MODE_PRIVATE);
        editor = preferences.edit();
        //Direccion MAC del dispositivo
        if (address == null) {
            //address = preferences.getString("BTAddress", null);
        }
    }
    private void ConectarDispBT() {
        Toast.makeText(getBaseContext(),"Si",Toast.LENGTH_SHORT).show();
        if (DispositivoSeleccionado == null) {
            Toast.makeText(getBaseContext(),"no",Toast.LENGTH_SHORT).show();

            showToast("Selecciona un dispositivo Bluetooth.");
            return;
        }

        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getBaseContext(),"si2",Toast.LENGTH_SHORT).show();
                //return;
            }
            Toast.makeText(getBaseContext(),"si3",Toast.LENGTH_SHORT).show();
            btSocket = DispositivoSeleccionado.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
            btSocket.connect();
            MyConexionBT = new ConnectedThread(btSocket);
            MyConexionBT.start();
            showToast("Conexión exitosa.");
        } catch (IOException e) {
            showToast("Error al conectar con el dispositivo.");
        }
    }
    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
    public class ConnectedThread extends Thread implements Serializable {
        private final OutputStream mmOutStream;
        ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                showToast("Error al crear el flujo de datos.");
            }

            mmOutStream = tmpOut;
        }
        public void write(char input) {
            //byte msgBuffer = (byte)input;
            try {
                mmOutStream.write((byte)input);
            } catch (IOException e) {
                Toast.makeText(getBaseContext(), "La Conexión fallo", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    public ConnectedThread getMyConexionBT() {
        return MyConexionBT;
    }
}