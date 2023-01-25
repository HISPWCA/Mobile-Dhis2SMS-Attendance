package org.hwca.dhis2sms.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.SMSMessage;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.fragment.HistFragment;
import org.hwca.dhis2sms.fragment.HomeFragment;
import org.hwca.dhis2sms.fragment.StatsFragment;
import org.hwca.dhis2sms.offline.OrgUnitSelectActivity;
import org.hwca.dhis2sms.utils.Utilities;

import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {
    private HomeFragment homeFragment;
    private Toolbar toolbar;
    private Context context;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = MainActivity.this;
        realm = Realm.getDefaultInstance();

        if (ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") != PackageManager.PERMISSION_GRANTED) {
            final int REQUEST_CODE_ASK_PERMISSIONS = 123;

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS"}, REQUEST_CODE_ASK_PERMISSIONS);
        } else
            new Timer().schedule(new SMSChecker(), 200, 5000);

        homeFragment = new HomeFragment();
        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(bottmNavigationListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener bottmNavigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.nav_hist:
                    selectedFragment = new HistFragment();
                    break;

                case R.id.nav_stat:
                    selectedFragment = new StatsFragment();
                    break;
                default:
                    break;
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logUserOut();
                break;
            case R.id.clearDataBase:
                final String alertMessage =
                        "Following actions will be performed: \n\n" +
                                "All your local data related to this software will be removed \n\n" +
                                "You will be disconnected after the process finished.\n\n " +
                                "Please do you still want to perform this operation ? \n\n";
                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Sensitive action need your attention");
                builder.setMessage(alertMessage);
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent intent = new Intent(getBaseContext(), ConfirmDataBaseDeletionByPinCodeActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.info(
                                getApplicationContext(),
                                "Great! No change has been made on your local database",
                                Toast.LENGTH_LONG,
                                false
                        ).show();
                    }
                });

                builder.show();
                break;
            case R.id.changeZone:
                final String warningMessage = "Changing your Zone can make the System unstable sometimes \n\n" +
                        "Do you want to continue anyway ?";
                final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Sensitive action detected");
                alertBuilder.setMessage(warningMessage);
                alertBuilder.setCancelable(false);
                alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final Intent zoneActivity = new Intent(context, ZoneActivity.class);
                        startActivity(zoneActivity);
                    }
                });

                alertBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.info(getApplicationContext(), "Great! Your current Zone has not changed ", Toast.LENGTH_LONG, false).show();
                    }
                });
                alertBuilder.show();
                break;

            case R.id.modifyPinCode:
                final Intent changePinCodeIntent = new Intent(context, PinCodeChangeActivity.class);
                startActivity(changePinCodeIntent);
                break;

            case R.id.profileInfos:
                Intent profileIntent = new Intent(context, ProfileActivity.class);
                startActivity(profileIntent);
                break;

            case R.id.aboutUs:
                Intent aboutUs = new Intent(context, AboutUs.class);
                startActivity(aboutUs);
                break;

            case R.id.sync:
                homeFragment.doSynchronization();
                break;

            case R.id.changeSelectedSchool:
                Intent selectedSchoolIntent = new Intent(context, OrgUnitSelectActivity.class);
                startActivity(selectedSchoolIntent);
                finish();
                break;

            case R.id.syncMetaData:
                if (Utilities.isConnected(MainActivity.this)) homeFragment.doMetaDataSynchronization();
                else Toasty.info(MainActivity.this,"No internet connection",Toasty.LENGTH_SHORT).show();
                break;

            case R.id.syncConfig:
                if (Utilities.isConnected(MainActivity.this)) homeFragment.doConfigurationSynchronization();
                else Toasty.info(MainActivity.this,"No internet connection",Toasty.LENGTH_SHORT).show();
                break;

            case R.id.syncOrganisationUnits:
                if (Utilities.isConnected(MainActivity.this)) homeFragment.doOrgUnitSynchronization();
                else Toasty.info(MainActivity.this,"No internet connection",Toasty.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logUserOut() {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Settings settings = realm.where(Settings.class).findFirst();
        settings.setLastConnection(null);
        realm.commitTransaction();

        final Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        finish();
        startActivity(intent);
    }

    public class SMSChecker extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    checkSenSMS();
                    checkFailedSMS();
                    checkUndeliverededSMS();
                }
            });
        }
    }

    private void checkSenSMS() {
        try {
            final Cursor smsInboxCursor = getContentResolver().query(Uri.parse("content://sms/sent"), null, null, null, null);
            if (smsInboxCursor != null) {
                int indexBody = smsInboxCursor.getColumnIndex("body");
                if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
                do {
                    final String sms = smsInboxCursor.getString(indexBody);
                    final SMSMessage smsMessage = realm.where(SMSMessage.class).equalTo("message", sms).findFirst();
                    if (smsMessage != null) {
                        realm.beginTransaction();
                        smsMessage.setMessageSent("sent");
                        realm.commitTransaction();
                    }
                } while (smsInboxCursor.moveToNext());
            }
        } catch (Exception e) {
            Toasty.error(context, e.getMessage()).show();
        }
    }

    private void checkFailedSMS() {
        try {
            final Cursor smsInboxCursor = getContentResolver().query(Uri.parse("content://sms/failed"), null, null, null, null);
            if (smsInboxCursor != null) {
                int indexBody = smsInboxCursor.getColumnIndex("body");
                if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
                do {
                    final String sms = smsInboxCursor.getString(indexBody);
                    final SMSMessage smsMessage = realm.where(SMSMessage.class).equalTo("message", sms).findFirst();
                    if (smsMessage != null) {
                        realm.beginTransaction();
                        smsMessage.setMessageSent("failed");
                        realm.commitTransaction();
                    }
                } while (smsInboxCursor.moveToNext());
            }
        } catch (Exception e) {
            Toasty.error(context, e.getMessage()).show();
        }
    }

    private void checkUndeliverededSMS() {
        try {
            final Cursor smsInboxCursor = getContentResolver().query(Uri.parse("content://sms/undelivered"), null, null, null, null);
            if (smsInboxCursor != null) {
                int indexBody = smsInboxCursor.getColumnIndex("body");
                if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;
                do {
                    final String sms = smsInboxCursor.getString(indexBody);
                    final SMSMessage smsMessage = realm.where(SMSMessage.class).equalTo("message", sms).findFirst();
                    if (smsMessage != null) {
                        realm.beginTransaction();
                        smsMessage.setMessageSent("undelivered");
                        realm.commitTransaction();
                    }
                } while (smsInboxCursor.moveToNext());
            }
        } catch (Exception e) {
            Toasty.error(context, e.getMessage()).show();
        }
    }
}
