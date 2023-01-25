package org.hwca.dhis2sms.utils;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import es.dmoral.toasty.Toasty;

public class SMSUtils {
    private Context context;
    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private Activity activity;

    public SMSUtils(final Context context, final Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void sendSMS(final String phoneNumber, final String message) {

        if (isSimCardExists() && checkPermission(Manifest.permission.SEND_SMS)) {
            final String SENT = "SMS_SENT";
            final String DELIVERED = "SMS_DELIVERED";
            final PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, new Intent(SENT), 0);
            final PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0, new Intent(DELIVERED), 0);
            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toasty.success(context, "SMS Sent successfully", Toast.LENGTH_SHORT, false).show();
                            break;

                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                            Toasty.error(context, "Generic failure", Toast.LENGTH_SHORT, false).show();
                            break;

                        case SmsManager.RESULT_ERROR_NO_SERVICE:
                            Toasty.error(context, "No service", Toast.LENGTH_SHORT, false).show();
                            break;

                        case SmsManager.RESULT_ERROR_NULL_PDU:
                            Toasty.error(context, "Null PDU", Toast.LENGTH_SHORT, false).show();
                            break;

                        case SmsManager.RESULT_ERROR_RADIO_OFF:
                            Toasty.error(context, "Radio off", Toast.LENGTH_SHORT, false).show();
                            break;
                    }
                }
            }, new IntentFilter(SENT));

            context.registerReceiver(new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent arg1) {
                    switch (getResultCode()) {
                        case Activity.RESULT_OK:
                            Toasty.success(context, "SMS delivered", Toast.LENGTH_SHORT, false).show();
                            break;

                        case Activity.RESULT_CANCELED:
                            Toasty.success(context, "SMS not delivered", Toast.LENGTH_SHORT, false).show();
                            break;
                    }
                }
            }, new IntentFilter(DELIVERED));

            try {
                final SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
            } catch (Exception e) {
                Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT, false).show();
            }
        } else {
//            Toasty.error(context, "There was an error while sending SMS due, please retry", Toast.LENGTH_SHORT, false).show();
            Toasty.info(context, "There was an error while sending SMS due, please retry", Toast.LENGTH_SHORT, false).show();
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission(String permission) {
        int check = ContextCompat.checkSelfPermission(context, permission);

        return check == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isSimCardExists() {
        final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        int SIM_STATE = telephonyManager.getSimState();

        if (SIM_STATE == TelephonyManager.SIM_STATE_READY)
            return true;
        else {
            switch (SIM_STATE) {
                case TelephonyManager.SIM_STATE_ABSENT: //SimState = "No Sim Found!";
                    Toasty.error(context, "No Sim Found", Toast.LENGTH_SHORT, false).show();
                    break;

                case TelephonyManager.SIM_STATE_NETWORK_LOCKED: //SimState = "Network Locked!";
                    Toasty.error(context, "Network Locked", Toast.LENGTH_SHORT, false).show();
                    break;

                case TelephonyManager.SIM_STATE_PIN_REQUIRED: //SimState = "PIN Required to access SIM!";
                    Toasty.error(context, "PIN Required to access SIM", Toast.LENGTH_SHORT, false).show();
                    break;

                case TelephonyManager.SIM_STATE_PUK_REQUIRED: //SimState = "PUK Required to access SIM!"; // Personal Unblocking Code
                    Toasty.error(context, "PUK Required to access SIM", Toast.LENGTH_SHORT, false).show();
                    break;

                case TelephonyManager.SIM_STATE_UNKNOWN: //SimState = "Unknown SIM State!";
                    Toasty.error(context, "Unknown SIM State", Toast.LENGTH_SHORT, false).show();
                    break;
            }
            return false;
        }
    }
}
