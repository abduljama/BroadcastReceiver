package com.abduljama.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by abduljama on 5/24/17.
 */

public class IncomingSmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        final SmsManager sms = SmsManager.getDefault();

        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    Toast.makeText(context , "You  have new message from senderNum : "+ message, Toast.LENGTH_LONG).show();

                    //  Generates an  application event
                    Intent registrationComplete = new Intent("newMessageRecieved");
                    registrationComplete.putExtra("sender", senderNum);
                    registrationComplete.putExtra("message", message);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(registrationComplete);
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }

    }
}
