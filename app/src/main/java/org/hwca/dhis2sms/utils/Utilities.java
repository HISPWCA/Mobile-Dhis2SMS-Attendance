package org.hwca.dhis2sms.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.hwca.dhis2sms.entity.DataElement;
import org.hwca.dhis2sms.entity.DataElementValue;
import org.hwca.dhis2sms.entity.DataSet;
import org.hwca.dhis2sms.entity.GatewayNumber;
import org.hwca.dhis2sms.entity.Period;
import org.hwca.dhis2sms.entity.SMSMessage;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.SmsCommand;
import org.hwca.dhis2sms.entity.User;
import org.hwca.dhis2sms.offline.CurrentSelectedOrgUnit;
import org.hwca.dhis2sms.offline.OrgUnit;
import org.hwca.dhis2sms.offline.SelectedOrgUnit;

import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmResults;

public class Utilities {
private Realm realm;

    public Utilities(Realm realm) {
        this.realm = realm;
    }

    public  void clearAllDataFromDatabase() {
        RealmResults<DataElement> dataElements = realm.where(DataElement.class).findAll();
        RealmResults<DataElementValue> dataElementValues = realm.where(DataElementValue.class).findAll();
        RealmResults<DataSet> dataSets = realm.where(DataSet.class).findAll();
        RealmResults<GatewayNumber> gatewayNumbers = realm.where(GatewayNumber.class).findAll();
        RealmResults<Period> periods = realm.where(Period.class).findAll();
        RealmResults<Settings> settings = realm.where(Settings.class).findAll();
        RealmResults<SmsCommand> smsCommands = realm.where(SmsCommand.class).findAll();
        RealmResults<SMSMessage> smsMessages = realm.where(SMSMessage.class).findAll();
        RealmResults<User> users = realm.where(User.class).findAll();
        RealmResults<CurrentSelectedOrgUnit> currentSelectedOrgUnits = realm.where(CurrentSelectedOrgUnit.class).findAll();
        RealmResults<OrgUnit> orgUnits = realm.where(OrgUnit.class).findAll();
        RealmResults<SelectedOrgUnit> selectedOrgUnits = realm.where(SelectedOrgUnit.class).findAll();

        realm.beginTransaction();
        dataElements.deleteAllFromRealm();
        dataElementValues.deleteAllFromRealm();
        dataSets.deleteAllFromRealm();
        gatewayNumbers.deleteAllFromRealm();
        periods.deleteAllFromRealm();
        settings.deleteAllFromRealm();
        smsCommands.deleteAllFromRealm();
        smsMessages.deleteAllFromRealm();
        users.deleteAllFromRealm();
        currentSelectedOrgUnits.deleteAllFromRealm();
        orgUnits.deleteAllFromRealm();
        selectedOrgUnits.deleteAllFromRealm();
        realm.commitTransaction();
    }

   public static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    public static String removeZeroOnStart(final String value) {
        try {
            if (value != null) {
                final String s = value.trim();
                if (s.isEmpty()) {
                    return "";
                }
                if (s.length() == 1) {
                    return s;
                }
                return s.replaceFirst("^0+(?!$)", "");
            }
        } catch (Exception e) {
            return "";
        }

        return "";
    }

    public static boolean isConnected(Context context){
        final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}
