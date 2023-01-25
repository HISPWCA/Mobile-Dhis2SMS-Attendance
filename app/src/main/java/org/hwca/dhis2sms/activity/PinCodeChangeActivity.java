package org.hwca.dhis2sms.activity;

import static org.hwca.dhis2sms.utils.AeSimpleSHA1.SHA1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.Settings;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class PinCodeChangeActivity extends AppCompatActivity {
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code_change);
        realm = Realm.getDefaultInstance();

        final EditText currentPinCode = findViewById(R.id.codePinActuel);
        final EditText newPinCode = findViewById(R.id.nouveauCodePinAUtiliser);
        final EditText confirmPinCode = findViewById(R.id.leCodeDeConfirmation);

        final Button saveButton = findViewById(R.id.saveAndClose);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String current = currentPinCode.getText().toString();
                final String newPin = newPinCode.getText().toString();
                final String confirm = confirmPinCode.getText().toString();

                if (current.length() == 0 || newPin.length() == 0 || confirm.length() == 0)
                    Toasty.error(getApplicationContext(), "You should fill all the fields before processing, Please !", Toast.LENGTH_SHORT, false).show();
                 else if (newPin.length() != 4)
                    Toasty.error(getApplicationContext(), "You should You should provide 4 caracters as Pin Code  !", Toast.LENGTH_SHORT, false).show();
                 else if (newPin.contentEquals("0000"))
                    Toasty.error(getApplicationContext(), "Sorry you can not use this reserved Pin Code !", Toast.LENGTH_SHORT, false).show();
                 else if (current.contentEquals(newPin) || current.contentEquals(confirm)) {
                    Toasty.error(getApplicationContext(), "You should not fill current pin code and other field with the same value", Toast.LENGTH_SHORT, false).show();
                } else {
                    Settings settings = realm.where(Settings.class).findFirst();
                    try {
                        if (SHA1(current).contentEquals(settings.getPinCode())) {
                            if (newPin.contentEquals(confirm)) {
                                final String pinCode = SHA1(newPin);
                                realm.beginTransaction();
                                settings.setPinCode(pinCode);
                                realm.commitTransaction();

                                Toasty.success(getApplicationContext(), "Your Pin Code is successfully updated", Toast.LENGTH_SHORT, false).show();
                                finish();
                            } else
                                Toasty.error(getApplicationContext(), "It seems like your new Pin code is differrent from the confirmation one ", Toast.LENGTH_SHORT, false).show();
                        } else
                            Toasty.error(getApplicationContext(), "The current Pin code you've provided is not correct!", Toast.LENGTH_SHORT, false).show();
                    } catch (NoSuchAlgorithmException e) {
                        Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                    } catch (UnsupportedEncodingException e) {
                        Toasty.error(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT, false).show();
                    }
                }
            }
        });
    }
}
