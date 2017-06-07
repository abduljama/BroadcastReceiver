package com.abduljama.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by abduljama on 5/15/17.
 */

public class IncomingCallBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        //  Access the Telephony API
        TelephonyManager tmgr = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            Log.w("MY_DEBUG_TAG", state);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumber = extras
                        .getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d( "Incoming call", " You have an incoming  call from " + phoneNumber);
                assert phoneNumber != null;
                if (phoneNumber.equals("0708419086")){

                    Class classTelephony = null;
                    try {
                        classTelephony = Class.forName(tmgr.getClass().getName());
                        Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");
                        methodGetITelephony.setAccessible(true);
                        Object telephonyInterface = methodGetITelephony.invoke(tmgr);
                        Class telephonyInterfaceClass =
                                Class.forName(telephonyInterface.getClass().getName());
                        Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");
                        methodEndCall.invoke(telephonyInterface);
                        // Notificaation View
                        NewNotification.notify(context, " Call declined ", 2);
                        Log.d( "Call Terminated ", "  As  per your request we have automatically terminated call from "+ phoneNumber);

                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
                Log.w("MY_DEBUG_TAG", phoneNumber);
            }
        }



    }



}
