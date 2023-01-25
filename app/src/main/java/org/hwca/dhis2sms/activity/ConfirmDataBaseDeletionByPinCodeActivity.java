package org.hwca.dhis2sms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.utils.AeSimpleSHA1;
import org.hwca.dhis2sms.utils.Utilities;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class ConfirmDataBaseDeletionByPinCodeActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_data_base_deletion_by_pin_code);

        realm = Realm.getDefaultInstance();
    }

    public void proceesDbDeletion(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        final EditText pinCodeField = findViewById(R.id.dbDropConfirmationPinCode);
        final String pinCode = pinCodeField.getText().toString();
        if (pinCode.length() != 4)
            Toasty.error(getApplicationContext(), "You should enter a valid pin code", Toast.LENGTH_LONG, false).show();
         else {
            final Settings settings = realm.where(Settings.class).equalTo("pinCode", AeSimpleSHA1.SHA1(pinCode)).findFirst();

            if (settings == null)
                Toasty.error(getApplicationContext(), "Incorrect pin code , Please try again", Toast.LENGTH_LONG, false).show();
             else {
                new Utilities(realm).clearAllDataFromDatabase();
                logUserOut();
                Toasty.success(getApplicationContext(), "Great! Your local database is cleaned now", Toast.LENGTH_LONG, false).show();

                finish();
            }
        }
    }

    private void logUserOut() {
        final Intent intent = new Intent(getBaseContext(), LoadingActivity.class);
        finish();
        startActivity(intent);
    }
}
