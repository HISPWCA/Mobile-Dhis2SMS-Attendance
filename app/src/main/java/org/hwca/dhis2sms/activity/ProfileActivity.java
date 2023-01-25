package org.hwca.dhis2sms.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.GatewayNumber;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.User;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProfileActivity extends Activity {
    private Realm realm;
    private boolean displayNotification = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.realm = Realm.getDefaultInstance();
        initdata();

        final RealmResults<GatewayNumber> gatewayNumbers = realm.where(GatewayNumber.class).findAll();
        final Settings settings = realm.where(Settings.class).findFirst();

        final TextView zone = findViewById(R.id.zone);
        zone.setVisibility(View.GONE);
        if (!gatewayNumbers.isEmpty()) {
            for (int i = 0; i < gatewayNumbers.size(); i++) {
                final String zoneName = i < 9 ? "Gateway 0".concat(String.valueOf(i + 1)) : "Gateway ".concat(String.valueOf(i));
                try {
                    final GatewayNumber tmpGateway = gatewayNumbers.get(i);

                    if (settings.getGatewayNumber().contentEquals(tmpGateway.getNumber())) {
                        final String zoneText = zoneName.concat("(").concat(settings.getGatewayNumber()).concat(")");

                        zone.setText(zoneText);
                        zone.setVisibility(View.VISIBLE);

                        break;
                    }
                } catch (NullPointerException e) {
                    if (displayNotification) {
                        Toasty.warning(getApplicationContext(), "Please you should choose your Gateway first ", Toast.LENGTH_LONG, false).show();

                        displayNotification = false;
                    }
                }
            }
        }

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        getWindow().setLayout(width, height);

        final WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = -20;

        getWindow().setAttributes(layoutParams);
    }

    private void initdata() {
        final TextView displayName = findViewById(R.id.displayNameP);
        final TextView username = findViewById(R.id.usernameP);

        final User user = realm.where(User.class).findFirst();

        username.setText("Username: ".concat(user.getUsername()));
        displayName.setText(user.getDisplayName());
    }

    public void closeModal(View view) {
        finish();
    }
}
