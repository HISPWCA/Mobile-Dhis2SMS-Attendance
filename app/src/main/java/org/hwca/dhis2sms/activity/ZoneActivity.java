package org.hwca.dhis2sms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.GatewayNumber;
import org.hwca.dhis2sms.entity.Settings;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class ZoneActivity extends AppCompatActivity {
    private Realm realm;
    private String gatewayNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zone);

        realm = Realm.getDefaultInstance();

        final RealmResults<GatewayNumber> gatewayNumbers = realm.where(GatewayNumber.class).findAll();
        if (gatewayNumbers.isEmpty()) {
            finish();
        } else if (gatewayNumbers.size() == 1) {
            final String uniqueNumber = gatewayNumbers.get(0).getNumber();

            realm.beginTransaction();
            final Settings settings = realm.where(Settings.class).findFirst() ;
            settings.setGatewayNumber(uniqueNumber);
            realm.commitTransaction();

            finish();
        }

        final List<String> zones = new ArrayList<>();
        for (int i = 0; i < gatewayNumbers.size(); i++) {
            final String zoneName = i < 9 ? "Gateway 0".concat(String.valueOf(i + 1)).concat(" (").concat(gatewayNumbers.get(i).getNumber()).concat(")") : "Gateway ".concat(String.valueOf(i)).concat(" (").concat(gatewayNumbers.get(i).getNumber()).concat(")");

            zones.add(zoneName);
        }

        final Spinner zoneSelectionSpinner = findViewById(R.id.zoneSelectionSpinner);
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(ZoneActivity.this, R.layout.spinner_item, zones);

        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        zoneSelectionSpinner.setAdapter(dataAdapter);
        zoneSelectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gatewayNumber = gatewayNumbers.get(position).getNumber();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Button button = findViewById(R.id.zoneSelectionValidationButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Settings settings = realm.where(Settings.class).findFirst();
                realm.beginTransaction();
                settings.setGatewayNumber(gatewayNumber);
                realm.commitTransaction();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.success(getApplicationContext(), "Your Zone has been updated successfully", Toast.LENGTH_SHORT, false).show();
                    }
                });
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toasty.error(getApplicationContext(), "Select your Zone please", Toast.LENGTH_SHORT, false).show();
    }
}
