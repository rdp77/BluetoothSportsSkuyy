package com.team.repairbluetoothsportsskuyy;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.aflak.bluetooth.Bluetooth;
import me.aflak.bluetooth.interfaces.BluetoothCallback;

public class MainActivity extends AppCompatActivity {

    DatabaseReference databaseLatihan;

    Bluetooth bluetooth = new Bluetooth(this);
    //Definisi Button
    private  Button es,me,ex;
    private TextView sos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Definisi firebase
        databaseLatihan = FirebaseDatabase.getInstance().getReference("latihan");
        //Definisi Text
        sos = (TextView) findViewById(R.id.stat);
        //Definisi Button
        es = (Button) findViewById(R.id.easy);
        me = (Button) findViewById(R.id.medium);
        ex = (Button) findViewById(R.id.expert);
        //Bluetooth
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        this.registerReceiver(mReceiver,filter);
        //Definisi Bluetooth
        bluetooth.setBluetoothCallback(bluetoothCallback);
        //Floating Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.ACTION_BLUETOOTH_SETTINGS));
            }
        });

        //Button Easy
        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLatihan("Jumping Jacks", 45);
                addLatihan("High Knee Up", 40);
                addLatihan("Mountain Climbers", 40);
                addLatihan("Sit Up", 25);
                addLatihan("Plank Knee Twist", 35);
                addLatihan("Wide Push Up", 25);
                addLatihan("Crunch", 20);
                addLatihan("Push Up", 20);
                addLatihan("Russian Twist", 20);
                addLatihan("Burpess", 55);
                addLatihan("Toe Jump", 25);
                addLatihan("Plank", 50);
            }
        });

        //Button Medium
        me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLatihan("Jumping Jacks", 45);
                addLatihan("High Knee Up",45);
                addLatihan("Mountain Climbers",45);
                addLatihan("Sit Up",30);
                addLatihan("Plank Knee Twist",40);
                addLatihan("Wide Push Up",30);
                addLatihan("Crunch",25);
                addLatihan("Push Up",25);
                addLatihan("Russin Twist",25);
                addLatihan("Burpess",60);
                addLatihan("Toe Jump",30);
                addLatihan("Plank",55);
                addLatihan("Squat",35);
                addLatihan("Sit Up",30);
                addLatihan("Push Up",30);
                addLatihan("Jumping Jacks",50);
            }
        });

        //Button Expert
        ex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLatihan("Jumping Jacks", 57);
                addLatihan("High Knee Up",52);
                addLatihan("Mountain Climbers",52);
                addLatihan("Sit Up",37);
                addLatihan("Plank Knee Twist",47);
                addLatihan("Wide Push Up",37);
                addLatihan("Crunch",32);
                addLatihan("Push Up",32);
                addLatihan("Russin Twist",32);
                addLatihan("Burpess",67);
                addLatihan("Toe Jump",37);
                addLatihan("Plank",62);
                addLatihan("Squat",42);
                addLatihan("Sit Up",37);
                addLatihan("Push Up",37);
                addLatihan("Jumping Jacks",57);
                addLatihan("Burpess",67);
                addLatihan("Plank",63);
            }
        });
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)){
                es.setVisibility(View.VISIBLE);
                me.setVisibility(View.VISIBLE);
                ex.setVisibility(View.VISIBLE);
                sos.setText("Akses diperbolehkan");
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        bluetooth.onStart();
        if(bluetooth.isEnabled()){
            // doStuffWhenBluetoothOn() ...
        } else {
            bluetooth.showEnableDialog(MainActivity.this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bluetooth.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bluetooth.onActivityResult(requestCode, resultCode);
    }

    private BluetoothCallback bluetoothCallback = new BluetoothCallback() {
        @Override public void onBluetoothTurningOn() {}
        @Override public void onBluetoothTurningOff() {}
        @Override public void onBluetoothOff() {
            es.setVisibility(View.INVISIBLE);
            me.setVisibility(View.INVISIBLE);
            ex.setVisibility(View.INVISIBLE);
            sos.setText("Bluetooth dimatikan");
        }

        @Override
        public void onBluetoothOn() {
            sos.setText("Bluetooth diaktifkan");
        }

        @Override
        public void onUserDeniedActivation() {
            sos.setText("Bluetooth dimatikan oleh user");
        }
    };

    public void addLatihan(String gerakan, int kalori) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
        Date date = new Date();

        String id_latihan = databaseLatihan.push().getKey();
        String tanggal = dateFormat.format(date);
        int jumlah_kalori = 400;

        AddFirebase save = new AddFirebase(id_latihan, tanggal, gerakan, kalori);
        databaseLatihan.child(id_latihan).setValue(save);
    }

}
