package com.amazing.android.autopompomme.linking;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.amazing.android.autopompomme.R;
import com.amazing.android.autopompomme.linking.adapter.LinkingAdapter;
import com.amazing.android.autopompomme.databinding.ActivityLinkingBinding;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class LinkingActivity extends AppCompatActivity implements Listener{
    ActivityLinkingBinding binding;

    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    BluetoothAdapter bluetoothAdapter;

    Set<BluetoothDevice> pairedDevice = new HashSet<>();
    BluetoothLeAdvertiser pairDevice;
    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> deviceAddressArray;
    BluetoothSocket btSocket;

    Listener listener;

    ConnectedThread connectedThread;

    private final static int REQUEST_ENABLE_BT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLinkingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        listener = this;

        if (!bluetoothAdapter.isEnabled()) {
            binding.btnLinking.setText("허용하러 가기");
            //onBluetooth();


        } else {
            //블루트스 활성화
            binding.btnLinking.setText("오토팜 찾기");
            Log.d("TEST", "Blutooth on");
        }

        btArrayAdapter = new ArrayAdapter<>(this, R.layout.linking_item);
        deviceAddressArray = new ArrayList<>();
        //binding.rvLinking.setAdapter(btArrayAdapter);


        binding.btnLinking.setOnClickListener(v -> onClickButtonSearch());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager((Context) this);
        binding.rvLinking.setLayoutManager(linearLayoutManager);

        //LinkingAdapter linkingAdapter = new LinkingAdapter(deviceAddressArray);
        //binding.rvLinking.setAdapter(linkingAdapter);
        Log.d("TEST", "w/" + deviceAddressArray);
        Log.d("TEST", "wwww/" + btArrayAdapter);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); //MAC
                Log.d("TEST","devi"+deviceName);

                if(deviceName!= null){
                    deviceAddressArray.add(deviceName);
                }

                LinkingAdapter linkingAdapter = new LinkingAdapter(context,btArrayAdapter,deviceAddressArray,listener);
                binding.rvLinking.setAdapter(linkingAdapter);
            }
        }
    };



    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
    }


    private void connect() {
        // Check if we have the necessary permissions, and if not, request them.
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);
                return; // Return here to prevent the rest of onCreate() from executing without permissions.
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.BLUETOOTH},
                        MY_PERMISSIONS_REQUEST_BLUETOOTH);
                return; // Return here to prevent the rest of onCreate() from executing without permissions.
            }
        }*/



       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // API level 31 and above
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_ENABLE_BT);
            }
        } else {
            // Below API level 31
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);
            }
        }*/

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.BLUETOOTH_CONNECT},
                    REQUEST_ENABLE_BT);
        }


        Log.d("TEST", "5");
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null) {
            Log.d("TEST", "1");
            // Device doesn't support Bluetooth
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBluetoothResultLauncher.launch(enableBtIntent);
        }

    }

    ActivityResultLauncher<Intent> enableBluetoothResultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            // Bluetooth is now enabled.
                        } else if (result.getResultCode() == RESULT_CANCELED) {
                            // The user chose not to enable Bluetooth.
                        }
                    });

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ENABLE_BT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay!
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
            // Other 'case' lines to check for other permissions this app might request.
        }
    }

    public void onClickButtonSearch() {

        //Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.d("TEST", "3");
            if (ContextCompat.checkSelfPermission(
                    getBaseContext(), Manifest.permission.BLUETOOTH_CONNECT) ==
                    PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("TEST", "블투 연결 됨");
            } else {
                requestPermissionLauncher.launch(
                        Manifest.permission.BLUETOOTH_CONNECT);
                Log.d("TEST", "블투 연결 안됨");
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_SCAN}, 10);
            }
            if (checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 10);

                Log.d("TEST", "BLUETOOTH_CONNECT");
            }

        } else {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            }
        }

        onBluetooth();
        search();
    }

    private void search() {
        Log.d("TEST", "검색 코드시작");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        bluetoothAdapter.startDiscovery();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        } else {
            if (bluetoothAdapter.isEnabled()) {
                Log.d("TEST", "검색 시작하기");
                bluetoothAdapter.startDiscovery();
                Log.d("TEST", "검색/" + btArrayAdapter);
                deviceAddressArray.clear();
                if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
                    deviceAddressArray.clear();
                }
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(receiver, filter);

                pairedDevice = bluetoothAdapter.getBondedDevices();
                if (pairedDevice.size() > 0) {
                    for (BluetoothDevice device : pairedDevice) {
                        String deviceName = device.getName();
                        String deviceHardwareAddres = device.getAddress();
                        Log.d("TEST", "deviceName/" + deviceName);
                        Log.d("TEST", "deviceHardware/" + deviceHardwareAddres);
                        btArrayAdapter.addAll(deviceName);
                        deviceAddressArray.add(deviceHardwareAddres);
                        Log.d("TEST", "e/" + deviceAddressArray);


                    }
                }
                //pairDevice = bluetoothAdapter.getBluetoothLeAdvertiser();
                //for (BluetoothDevice device : pairDevice) {
                 //   String deviceN = device.getName();

                //}
                //Log.d("TEST", "r/" + pairDevice);
            } else {
                //블투 꺼짐
            }
        }

    }


    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Log.d("TEST","Granted");
                }else {
                    Log.d("TEST","not Granted");
                }
            });

    private void onBluetooth() {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivityForResult(intent, 1);
    }

    public void sendData(String name, String passWord) {

    }

    @Override
    public void wifi(String wifiName, String wifiPw, String device) throws IOException {
        btSocket = createBluetoothSocket(bluetoothAdapter.getRemoteDevice(device));
        //btSocket = device.
        connectedThread = new ConnectedThread(btSocket);
        connectedThread.start();
        Log.d("TEST","w"+wifiName);
        if(connectedThread!= null) {
            connectedThread.write(wifiName,wifiPw);
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }
}