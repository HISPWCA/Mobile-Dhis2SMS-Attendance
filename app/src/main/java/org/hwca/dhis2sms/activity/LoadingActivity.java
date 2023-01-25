package org.hwca.dhis2sms.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.DataElement;
import org.hwca.dhis2sms.entity.DataSet;
import org.hwca.dhis2sms.entity.GatewayNumber;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.SmsCommand;
import org.hwca.dhis2sms.entity.User;
import org.hwca.dhis2sms.offline.CurrentSelectedOrgUnit;
import org.hwca.dhis2sms.offline.JsonData;
import org.hwca.dhis2sms.offline.OrgUnit;
import org.hwca.dhis2sms.offline.OrgUnitSelectActivity;
import org.hwca.dhis2sms.utils.Constants;
import org.hwca.dhis2sms.utils.Utilities;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class LoadingActivity extends AppCompatActivity {
    private Realm realm;
    private TextView loadingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        realm = Realm.getDefaultInstance();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        loadingTextView = findViewById(R.id.loading_text_view);

        User user = realm.where(User.class).findFirst();
        if (user != null) {
            final Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            mainIntent.putExtra("username", user.getUsername());
            mainIntent.putExtra("password", user.getPassword());
            mainIntent.putExtra("loginResponse", user.getLoginResponse());

            startActivity(mainIntent);
            finish();
        } else {
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingTextView.setText("Loading personal data");
                    initUser();

                    new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingTextView.setText("Loading settings");
                            initSettings();

                            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    loadingTextView.setText("Loading gateway data");
                                    initGatewayNumber();

                                    new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            loadingTextView.setText("Loading sms command data");
                                            initSMSCommands();

                                            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    loadingTextView.setText("Loading datasets");
                                                    initDataSet();

                                                    new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            loadingTextView.setText("Loading organisations units");
                                                            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    initOrgUnit();
                                                                }
                                                            }, 8000);

                                                        }
                                                    }, 3000);
                                                }
                                            }, 2000);
                                        }
                                    }, 4000);
                                }
                            }, 5000);
                        }
                    }, 3000);
                }
            }, 1000);

        }
    }

    private void initUser() {
        final byte[] response = JsonData.getMeResponse();
        final User currentUser = realm.where(User.class).equalTo("username", Constants.USERNAME).equalTo("password", Constants.PASSWORD).findFirst();

        if (currentUser == null) {
            final String email = "";

            realm.beginTransaction();
            User user = realm.where(User.class).equalTo("id", "user").findFirst();
            if (user == null)
                user = realm.createObject(User.class, "user");
            user.setEmail(email);
            user.setUsername(Constants.USERNAME);
            user.setPassword(Constants.PASSWORD);
            user.setLoginResponse(response);
            realm.commitTransaction();
        }
    }

    private void initSettings() {
        final Settings settings = realm.where(Settings.class).findFirst();
        if (settings == null) {
            realm.beginTransaction();
            Settings setting = realm.createObject(Settings.class, "setting");
            setting.setBaseUrl(Constants.URL);
            setting.setLastConnection(new Date());
            realm.commitTransaction();
        }

        CurrentSelectedOrgUnit selectedOrgUnit = realm.where(CurrentSelectedOrgUnit.class).equalTo("id","selectedOrgUnit").findFirst();
        if(selectedOrgUnit == null){
            realm.beginTransaction();
            selectedOrgUnit= realm.createObject(CurrentSelectedOrgUnit.class,"selectedOrgUnit");
            selectedOrgUnit.displayName=null;
            selectedOrgUnit.uid=null;
            realm.commitTransaction();
        }
    }

    private void initSMSCommands() {
        try {
            final JSONObject smsCommandObject = JsonData.getSmsCommand();
            if (smsCommandObject != null) {
                final JSONArray smsCommands = smsCommandObject.getJSONArray("smsCommands");

                for (int i = 0; i < smsCommands.length(); i++) {
                    final JSONObject jsonSMSCommand = smsCommands.getJSONObject(i);
                    final String smsCommandId = jsonSMSCommand.getString("id");
                    final JSONArray dataElements = jsonSMSCommand.getJSONArray("smsCodes");
                    final String smsCommandDataSet = jsonSMSCommand.getJSONObject("dataset").getString("id");
                    final DataSet dataSetTrouve = realm.where(DataSet.class).equalTo("id", smsCommandDataSet).findFirst();

                    realm.beginTransaction();
                    if (dataSetTrouve == null)
                        realm.createObject(DataSet.class, smsCommandDataSet);
                    realm.commitTransaction();

                    for (int w = 0; w < dataElements.length(); w++) {
                        final JSONObject subElement = dataElements.getJSONObject(w);
                        final String dataElementId = subElement.getJSONObject("dataElement").getString("id");
                        final String dataElementCode = subElement.getString("code");
                        final DataElement dataElementTrouve = realm.where(DataElement.class).equalTo("id", dataElementId).findFirst();

                        realm.beginTransaction();
                        if (dataElementTrouve == null) {
                            DataElement dataElement = realm.createObject(DataElement.class, dataElementId);
                            dataElement.setCode(dataElementCode);
                            dataElement.setDataSet(smsCommandDataSet);
                        } else {
                            dataElementTrouve.setCode(dataElementCode);
                            dataElementTrouve.setDataSet(smsCommandDataSet);
                        }
                        realm.commitTransaction();
                    }

                    final SmsCommand smsCommandTrouve = realm.where(SmsCommand.class).equalTo("id", smsCommandId).findFirst();

                    if (smsCommandTrouve == null) {
                        final String smsCommandName = jsonSMSCommand.getString("name");
                        final String smsCommandDisplayName = jsonSMSCommand.getString("displayName");
                        final String smsCommandSeparator = jsonSMSCommand.getString("separator");

                        realm.beginTransaction();
                        SmsCommand smsCommand = realm.createObject(SmsCommand.class, smsCommandId);
                        smsCommand.setName(smsCommandName);
                        smsCommand.setDisplayName(smsCommandDisplayName);
                        smsCommand.setSeparator(smsCommandSeparator);
                        smsCommand.setDataSet(smsCommandDataSet);
                        realm.commitTransaction();
                    }
                }
            }
        } catch (JSONException e) {
            Toasty.error(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }

    private void initDataSet() {
        try {
            final JSONObject dataSetResponse = JsonData.getDataSet();

            if (dataSetResponse != null) {
                final JSONArray dataSetArray = dataSetResponse.getJSONArray("dataSets");

                if (dataSetArray.length() > 0) {
                    for (int i = 0; i < dataSetArray.length(); i++) {
                        final JSONObject dataSetObject = dataSetArray.getJSONObject(i);
                        final String id = dataSetObject.getString("id");
                        final String name = dataSetObject.getString("name");
                        final String displayName = dataSetObject.getString("displayName");
                        final String periodType = dataSetObject.getString("periodType");
                        final JSONArray dataSetElements = dataSetObject.getJSONArray("dataSetElements");
                        final JSONArray sections = dataSetObject.getJSONArray("sections");
                        final int openFuturePeriods = dataSetObject.getInt("openFuturePeriods");

                        if (sections.length() > 0) {
                            for (int qw = 0; qw < sections.length(); qw++) {
                                final JSONObject section = sections.getJSONObject(qw);
                                final JSONArray sectionsDataElements = section.getJSONArray("dataElements");

                                for (int bh = 0; bh < sectionsDataElements.length(); bh++) {
                                    final JSONObject dataElementTmp = sectionsDataElements.getJSONObject(bh);
                                    final String dataElementId = dataElementTmp.getString("id");
                                    final String dataElementName = dataElementTmp.getString("name");
                                    final String dataElementDisplayName = dataElementTmp.getString("displayName");
                                    final DataElement dataElementTrouve = realm.where(DataElement.class).equalTo("id", dataElementId).findFirst();

                                    realm.beginTransaction();
                                    if (dataElementTrouve == null) {
                                        DataElement dataElement = realm.createObject(DataElement.class, dataElementId);
                                        dataElement.setName(dataElementName);
                                        dataElement.setDisplayName(dataElementDisplayName);
                                        dataElement.setDataSet(id);
                                        dataElement.setOrder(bh);
                                    } else {
                                        dataElementTrouve.setName(dataElementName);
                                        dataElementTrouve.setDataSet(id);
                                        dataElementTrouve.setOrder(bh);
                                        dataElementTrouve.setDisplayName(dataElementDisplayName);
                                    }
                                    realm.commitTransaction();
                                }
                            }
                        } else {
                            for (int b = 0; b < dataSetElements.length(); b++) {
                                final JSONObject jsonDataElement = dataSetElements.getJSONObject(b);
                                final JSONObject subDataElement = jsonDataElement.getJSONObject("dataElement");
                                final String dataElementId = subDataElement.getString("id");
                                final String dataElementName = subDataElement.getString("name");
                                final String dataElementDisplayName = subDataElement.getString("name");
                                final DataElement dataElementTrouve = realm.where(DataElement.class).equalTo("id", dataElementId).findFirst();

                                realm.beginTransaction();
                                if (dataElementTrouve == null) {
                                    DataElement dataElement = realm.createObject(DataElement.class, dataElementId);
                                    dataElement.setName(dataElementName);
                                    dataElement.setDataSet(id);
                                    dataElement.setOrder(b);
                                    dataElement.setDisplayName(dataElementDisplayName);
                                } else {
                                    dataElementTrouve.setName(dataElementName);
                                    dataElementTrouve.setDataSet(id);
                                    dataElementTrouve.setOrder(b);
                                    dataElementTrouve.setDisplayName(dataElementDisplayName);
                                }
                                realm.commitTransaction();
                            }
                        }

                        final User user = realm.where(User.class).equalTo("username", Constants.USERNAME).findFirst();
                        if (user != null) {
                            DataSet dataSet = realm.where(DataSet.class).equalTo("id", id).findFirst();
                            realm.beginTransaction();

                            if (dataSet == null)
                                dataSet = realm.createObject(DataSet.class, id);

                            dataSet.setName(name);
                            dataSet.setDisplayName(displayName);
                            dataSet.setPeriodType(periodType);
                            dataSet.setOpenFuturePeriods(openFuturePeriods);
                            dataSet.setUser(user.getId());

                            realm.commitTransaction();
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Toasty.error(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }

    private void initGatewayNumber() {
        final JSONObject gatewayNumberObject = JsonData.getGatewayNumber();

        if (gatewayNumberObject != null) {
            try {
                final Settings settings = realm.where(Settings.class).findFirst();

                realm.beginTransaction();
                settings.setGatewayNumber(null);
                realm.commitTransaction();

                final JSONArray gatewayNumberArray = gatewayNumberObject.getJSONArray("gatewayNumberArray");

                for (int gn = 0; gn < gatewayNumberArray.length(); gn++) {
                    final String tmpGatewayNumber = gatewayNumberArray.getString(gn);

                    checkAndSaveGatewayNumber(tmpGatewayNumber);
                }
            } catch (final JSONException e) {
                Toasty.error(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG, false).show();
            }
        }
    }

    private void checkAndSaveGatewayNumber(final String gatewayNumber) {
        GatewayNumber gatewayNumberTrouve = realm.where(GatewayNumber.class).equalTo("number", gatewayNumber).findFirst();

        if (gatewayNumberTrouve == null) {
            realm.beginTransaction();

            gatewayNumberTrouve = realm.createObject(GatewayNumber.class, gatewayNumber);
            gatewayNumberTrouve.setNumber(gatewayNumber);

            realm.commitTransaction();
        }
    }

    private void initOrgUnitOld() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String orgUnitString = Utilities.getJsonFromAssets(getApplicationContext(), "orgUnit.json");
                if (orgUnitString != null) {
                    try {
                        JSONObject orgUnitResponseObject = new JSONObject(orgUnitString);
                        JSONArray orgUnitArray = orgUnitResponseObject.getJSONArray("organisationUnits");

                        if (orgUnitArray.length() > 0) {
                            for (int i = 0; i < orgUnitArray.length(); i++) {
                                JSONObject orgUnitObject = orgUnitArray.getJSONObject(i);

                                String id = orgUnitObject.getString("id");
                                String displayName = orgUnitObject.getString("displayName");
                                String parent = null;

                                int level = orgUnitObject.getInt("level");
                                if (orgUnitObject.has("parent"))
                                    parent = orgUnitObject.getJSONObject("parent").getString("id");

                                OrgUnit orgUnit = realm.where(OrgUnit.class).equalTo("id", id).findFirst();
                                if (orgUnit == null) {
                                    realm.beginTransaction();

                                    orgUnit = realm.createObject(OrgUnit.class, id);
                                    orgUnit.displayName = displayName;
                                    orgUnit.level = level;
                                    orgUnit.parent = parent;

                                    realm.commitTransaction();
                                }
                            }
                        }

                        callLoadingActivity();
                    } catch (JSONException e) {
                        Toasty.error(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG, false).show();
                    }
                }
            }
        });
    }

    private void initOrgUnit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String orgUnitString = Utilities.getJsonFromAssets(getApplicationContext(), "orgUnit.json");
                if (orgUnitString != null) {
                    try {
                        JSONObject orgUnitResponseObject = new JSONObject(orgUnitString);
                        JSONArray orgUnitArray = orgUnitResponseObject.getJSONArray("organisationUnits");

                        if (orgUnitArray.length() > 0) {
                            for (int i = 0; i < orgUnitArray.length(); i++) {
                                JSONObject orgUnitObject = orgUnitArray.getJSONObject(i);

                                String id = orgUnitObject.getString("id");
                                String displayName = orgUnitObject.getString("displayName");
                                String parent = null;

                                int level = orgUnitObject.getInt("level");
                                if (orgUnitObject.has("parent"))
                                    parent = orgUnitObject.getJSONObject("parent").getString("id");

                                OrgUnit orgUnit = realm.where(OrgUnit.class).equalTo("id", id).findFirst();
                                if (orgUnit == null) {
                                    realm.beginTransaction();

                                    orgUnit = realm.createObject(OrgUnit.class, id);
                                    orgUnit.displayName = displayName;
                                    orgUnit.level = level;
                                    orgUnit.parent = parent;

                                    realm.commitTransaction();
                                }

                                generateParent(orgUnitObject.getJSONObject("parent"));
                            }
                        }

                        callLoadingActivity();
                    } catch (JSONException e) {
                        Toasty.error(LoadingActivity.this, e.getMessage(), Toast.LENGTH_LONG, false).show();
                    }
                }
            }
        });
    }

    private void callLoadingActivity() {
        final Intent orgUnitIntent = new Intent(getApplicationContext(), OrgUnitSelectActivity.class);

        startActivity(orgUnitIntent);
        finish();
    }

    private void generateParent(JSONObject parentObject){
        try {
            if(parentObject.has("parent") && !parentObject.isNull("parent")){
                String id = parentObject.getString("id");
                String displayName = parentObject.getString("displayName");
                String parent = null;

                int level = parentObject.getInt("level");
                if (parentObject.has("parent"))
                    parent = parentObject.getJSONObject("parent").getString("id");

                OrgUnit orgUnit = realm.where(OrgUnit.class).equalTo("id", id).findFirst();
                if (orgUnit == null) {
                    realm.beginTransaction();

                    orgUnit = realm.createObject(OrgUnit.class, id);
                    orgUnit.displayName = displayName;
                    orgUnit.level = level;
                    orgUnit.parent = parent;

                    realm.commitTransaction();
                }

                generateParent(parentObject.getJSONObject("parent"));

            }else{
                // to create level 1
                String id = parentObject.getString("id");
                String displayName = parentObject.getString("displayName");
                int level = parentObject.getInt("level");

                OrgUnit orgUnit = realm.where(OrgUnit.class).equalTo("id", id).findFirst();
                if (orgUnit == null) {
                    realm.beginTransaction();

                    orgUnit = realm.createObject(OrgUnit.class, id);
                    orgUnit.displayName = displayName;
                    orgUnit.level = level;
                    orgUnit.parent = null;

                    realm.commitTransaction();
                }

            }

        }catch (JSONException e){
            e.printStackTrace();
        }
    }
}