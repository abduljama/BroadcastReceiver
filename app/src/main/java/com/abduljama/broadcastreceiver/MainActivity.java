package com.abduljama.broadcastreceiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button  btn_okay;
    Switch isOn;
    EditText phoneNo;
    SharedPreferences sharedpreferences;

    TextView txtSender , txtMessage;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 101;
    private static final int PERMISSION_SEND_SMS = 123;

    public  static final int PERMISSIONS_MULTIPLE_REQUEST = 1;

    int PERMISSION_ALL = 1;


    String[] PERMISSIONS = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtMessage = (TextView)findViewById(R.id.message);
        txtSender = (TextView)findViewById(R.id.sender);

        checkPhoneStatePermission();
      //  checkSMSPermissions();




        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("newMessageReceived"));
        // On Broadcast Received  the Application event
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("newMessageRecieved")) {
                    String orign = intent.getStringExtra("sender");
                    String message = intent.getStringExtra("message");
                    
                    txtSender.setText(orign);
                    txtMessage.setText(message);
                    Toast.makeText( getApplicationContext() ," Sender " +  orign + "Message "+ message , Toast.LENGTH_LONG).show();
                }
            }
        };


    }


    @Override
    protected void onResume() {
        super.onResume();
        // Register to receive broadcast
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter("newMessageReceived"));


    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();

    }


    //  Requests for Persimission at runtime

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
                return;
            }
        }
    }


    public   void  checkPhoneStatePermission (){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{
                                Manifest.permission.READ_PHONE_STATE ,
                                Manifest.permission.READ_SMS},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        }
    }

    public  void  checkSMSPermissions (){
        // check permission is given
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

            }else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS);
            }
        }
    }

}
