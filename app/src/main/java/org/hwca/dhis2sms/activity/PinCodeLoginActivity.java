package org.hwca.dhis2sms.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.SMSMessage;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.utils.AeSimpleSHA1;
import org.hwca.dhis2sms.utils.SMSUtils;
import org.hwca.dhis2sms.utils.Utilities;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class PinCodeLoginActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_login);

        realm = Realm.getDefaultInstance();

        final EditText pinCode = findViewById(R.id.unCodePin);
        final Button loginButton = findViewById(R.id.loginByPinCode);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String pin = pinCode.getText().toString();

                if (pin.length() == 0)
                    Toasty.error(getApplicationContext(), "Please enter a pin code", Toast.LENGTH_SHORT, false).show();
                 else if (pin.contentEquals("0000")) {
                    final String message = "You have entered '0000' \n" +
                            "It will remove all data from your local database \n\n" +
                            "You can send all saved data before droping database  \n\n" +
                            "You can just remove all data without sending anything \n\n" +
                            "NOTICE: You will be logged out after the process is ended \n\n\n" +
                            "Do you really want to proceed  ?";
                    final AlertDialog.Builder builder = new AlertDialog.Builder(PinCodeLoginActivity.this);
                    builder.setTitle("SENSITIVE ACTION DETECTED");
                    builder.setMessage(message);
                    builder.setCancelable(true);
                    builder.setPositiveButton("Just Clean", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            new Utilities(realm).clearAllDataFromDatabase();
                            logUserOut();
                            finish();
                        }
                    });
                    builder.setNegativeButton("Send & Clean", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Utilities(realm).clearAllDataFromDatabase();

                            resendAllDataBeforeCleanDB();
                            logUserOut();
                            Toasty.success(getApplicationContext(), "All local Data are sent and database is cleaned", Toast.LENGTH_LONG, false).show();
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    final Settings settings = realm.where(Settings.class).findFirst();
                    try {
                        if (settings.getPinCode().contentEquals(AeSimpleSHA1.SHA1(pin))) {

                            realm.beginTransaction();
                            settings.setLastConnection(new Date());
                            realm.commitTransaction();

                            finish();
                        } else
                            Toasty.error(getApplicationContext(), "Incorrect pin code, try again please", Toast.LENGTH_SHORT, false).show();
                    } catch (NoSuchAlgorithmException e) {
                        Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                    } catch (UnsupportedEncodingException e) {
                        Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                    }
                }
            }
        });
    }

    private void resendAllDataBeforeCleanDB() {
        final SMSUtils smsUtils = new SMSUtils(getApplicationContext(), this);
        final Settings settings = Realm.getDefaultInstance().where(Settings.class).findFirst();
        final RealmResults<SMSMessage> failedSMS = Realm.getDefaultInstance().where(SMSMessage.class).equalTo("messageSent", "failed").findAll();

        for (final SMSMessage smsMessage : failedSMS)
            smsUtils.sendSMS(settings.getGatewayNumber(), smsMessage.getMessage());

        final RealmResults<SMSMessage> undeliveredSMS = Realm.getDefaultInstance().where(SMSMessage.class).equalTo("messageSent", "undelivered").findAll();

        for (final SMSMessage smsMessage : undeliveredSMS)
            smsUtils.sendSMS(settings.getGatewayNumber(), smsMessage.getMessage());
    }

    @Override
    public void onBackPressed() {
        Toasty.error(getApplicationContext(), "Please enter Pin Code", Toast.LENGTH_SHORT, false).show();
    }

    private void logUserOut() {
        final Intent intent = new Intent(getBaseContext(), LoginActivity.class);

        finish();
        startActivity(intent);
    }
}
