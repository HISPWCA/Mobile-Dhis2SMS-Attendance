package org.hwca.dhis2sms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.User;
import org.hwca.dhis2sms.utils.AeSimpleSHA1;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class PinCodeConfigActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_config);
        realm = Realm.getDefaultInstance();

        final EditText newPinCode = findViewById(R.id.nouveauCodePin);
        final EditText confirmPinCode = findViewById(R.id.confirmerLePinCode);
        final Button validationBtn = findViewById(R.id.validationConfigPinCodeBtn);
        validationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newPin = newPinCode.getText().toString();
                final String confirmPin = confirmPinCode.getText().toString();

                if (newPin.length() == 0 || confirmPin.length() == 0)
                    Toasty.error(getApplicationContext(), "Please fill both pin code fields", Toast.LENGTH_SHORT, false).show();
                 else if (newPin.length() != 4)
                    Toasty.error(getApplicationContext(), "The Pin Code length should be 4 ", Toast.LENGTH_SHORT, false).show();
                 else if (newPin.contentEquals("0000"))
                    Toasty.error(getApplicationContext(), "Sorry you can not use this reserved Pin Code !", Toast.LENGTH_SHORT, false).show();
                 else if (!newPin.contentEquals(confirmPin))
                    Toasty.error(getApplicationContext(), "The confirmation Pin Code seems to be different, try again please", Toast.LENGTH_SHORT, false).show();
                 else {
                    final Settings settings = realm.where(Settings.class).findFirst();
                    try {
                        realm.beginTransaction();
                        settings.setPinCode(AeSimpleSHA1.SHA1(newPin));
                        realm.commitTransaction();
                    } catch (NoSuchAlgorithmException e) {
                        Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                    } catch (UnsupportedEncodingException e) {
                        Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                    }
                    User user = realm.where(User.class).findFirst();
                    if (user != null) {
                        final Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                        mainIntent.putExtra("username", user.getUsername());
                        mainIntent.putExtra("password", user.getPassword());
                        mainIntent.putExtra("loginResponse", user.getLoginResponse());

                        startActivity(mainIntent);
                        finish();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
