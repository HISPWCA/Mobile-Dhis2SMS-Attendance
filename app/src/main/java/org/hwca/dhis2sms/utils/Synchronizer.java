package org.hwca.dhis2sms.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.hwca.dhis2sms.entity.DataElement;
import org.hwca.dhis2sms.entity.DataSet;
import org.hwca.dhis2sms.entity.GatewayNumber;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.SmsCommand;
import org.hwca.dhis2sms.entity.User;
import org.hwca.dhis2sms.offline.OrgUnit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Synchronizer {
    private Realm realm;
    private Context context;
    private String username;
    private String password;
    private byte[] response;
    private String baseURL;
    private final OkHttpClient client = new OkHttpClient();

    public Synchronizer(Context context, String username, String password, byte[] response) {
        this.realm = Realm.getDefaultInstance();

        this.context = context;
        this.username = username;
        this.password = password;
        this.response = response;

        final Settings settings = realm.where(Settings.class).findFirst();
        this.baseURL = settings.getBaseUrl();
    }

    public void sync() throws JSONException {
        assert response != null;
        final JSONObject loginResponse = new JSONObject(new String(response));
        final String displayName = loginResponse.getString("displayName");
        final JSONArray dataSets = loginResponse.getJSONArray("dataSets");
        final User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
        assert user != null;

        realm.beginTransaction();
        user.setDisplayName(displayName);
        realm.commitTransaction();

        syncDataSets(dataSets);
        syncGatewayNumber();
        loadSMSCommands(dataSets);
    }

    public void syncDataSets(JSONArray dataSets) {
        for (int i = 0; i < dataSets.length(); i++) {
            try {
                String dataSetId = dataSets.getString(i);
                syncDataSet(dataSetId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void loadSMSCommands(final JSONArray dataSets) {
        final String url = this.baseURL.concat(URLConstants.SMS_COMMANDS);
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                final JSONArray smsCommands = jsonResponse.getJSONArray("smsCommands");

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

                                    syncDataSet(smsCommandDataSet);

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

                                    if (smsCommandTrouve == null && dataSets.toString().contains(smsCommandDataSet)) {
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
                            } catch (JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void syncDataSet(final String dataSetId) {
        final String url = this.baseURL.concat(URLConstants.DATASETS).concat("/").concat(dataSetId).concat(".json?fields=id,name,displayName,periodType,openFuturePeriods,dataSetElements[dataElement[id,name,displayName]],sections[id,displayName,sortOrder,greyedFields,dataElements[id,name,displayName]]");
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e != null)
                    Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                final String id = jsonResponse.getString("id");
                                final String name = jsonResponse.getString("name");
                                final String displayName = jsonResponse.getString("displayName");
                                final String periodType = jsonResponse.getString("periodType");
                                final int openFuturePeriods = jsonResponse.getInt("openFuturePeriods");
                                final JSONArray dataSetElements = jsonResponse.getJSONArray("dataSetElements");
                                final JSONArray sections = jsonResponse.getJSONArray("sections");

                                if (sections.length() > 0) {
                                    for (int qw = 0; qw < sections.length(); qw++) {
                                        final JSONObject section = sections.getJSONObject(qw);
                                        final JSONArray sectionsDataElements = section.getJSONArray("dataElements");

                                        for (int bh = 0; bh < sectionsDataElements.length(); bh++) {
                                            final JSONObject dataElementTmp = sectionsDataElements.getJSONObject(bh);
                                            final String dataElementId = dataElementTmp.getString("id");
                                            final String dataElementName = dataElementTmp.getString("name");
                                            final DataElement dataElementTrouve = realm.where(DataElement.class).equalTo("id", dataElementId).findFirst();

                                            realm.beginTransaction();
                                            if (dataElementTrouve == null) {
                                                DataElement dataElement = realm.createObject(DataElement.class, dataElementId);
                                                dataElement.setName(dataElementName);
                                                dataElement.setDataSet(dataSetId);
                                                dataElement.setOrder(bh);
                                            } else {
                                                dataElementTrouve.setName(dataElementName);
                                                dataElementTrouve.setDataSet(dataSetId);
                                                dataElementTrouve.setOrder(bh);
                                            }
                                            realm.commitTransaction();

                                            syncDataElements(dataElementId);
                                        }
                                    }
                                } else {
                                    for (int b = 0; b < dataSetElements.length(); b++) {
                                        final JSONObject jsonDataElement = dataSetElements.getJSONObject(b);
                                        final JSONObject subDataElement = jsonDataElement.getJSONObject("dataElement");
                                        final String dataElementId = subDataElement.getString("id");
                                        final String dataElementName = subDataElement.getString("name");
                                        final DataElement dataElementTrouve = realm.where(DataElement.class).equalTo("id", dataElementId).findFirst();

                                        realm.beginTransaction();
                                        if (dataElementTrouve == null) {
                                            DataElement dataElement = realm.createObject(DataElement.class, dataElementId);
                                            dataElement.setName(dataElementName);
                                            dataElement.setDataSet(dataSetId);
                                            dataElement.setOrder(b);
                                        } else {
                                            dataElementTrouve.setName(dataElementName);
                                            dataElementTrouve.setDataSet(dataSetId);
                                            dataElementTrouve.setOrder(b);
                                        }
                                        realm.commitTransaction();

                                        syncDataElements(dataElementId);
                                    }
                                }
                                final User user = realm.where(User.class).equalTo("username", username).findFirst();
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
                            } catch (JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void syncDataElements(final String dataElementId) {
        final String url = this.baseURL.concat(URLConstants.DATA_ELEMENTS).concat("/").concat(dataElementId).concat(".json?fields=id,name,displayName");
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e != null)
                    Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                final String id = jsonResponse.getString("id");
                                final String name = jsonResponse.getString("name");
                                final String displayName = jsonResponse.getString("displayName");
                                final DataElement dataElementTrouve = realm.where(DataElement.class).equalTo("id", id).findFirst();

                                realm.beginTransaction();
                                if (dataElementTrouve == null) {
                                    DataElement dataElement = realm.createObject(DataElement.class, id);
                                    dataElement.setName(name);
                                    dataElement.setDisplayName(displayName);
                                } else {
                                    dataElementTrouve.setName(name);
                                    dataElementTrouve.setDisplayName(displayName);
                                }
                                updateDataElement();
                                realm.commitTransaction();

                            } catch (JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void syncGatewayNumber() {
        final String url = this.baseURL.concat(URLConstants.GATEWAY_SETTINGS);
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e != null)
                    Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                final RealmResults<GatewayNumber> numbers = realm.where(GatewayNumber.class).findAll();
                                final Settings settings = realm.where(Settings.class).findFirst();

                                realm.beginTransaction();
                                settings.setGatewayNumber(null);
                                numbers.deleteAllFromRealm();
                                realm.commitTransaction();

                                final JSONArray gatewayNumberArray = jsonResponse.getJSONArray("gatewayNumberArray");

                                for (int gn = 0; gn < gatewayNumberArray.length(); gn++) {
                                    final String tmpGatewayNumber = gatewayNumberArray.getString(gn);

                                    checkAndSaveGatewayNumber(tmpGatewayNumber);
                                }
                            } catch (final JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public void updateDataElement() {
        final String url = this.baseURL.concat(URLConstants.BASE_URL).concat("/smsCommands.json?paging=false&fields=smsCodes");
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e != null)
                    Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                try {
                    final JSONObject jsonResponse = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                final JSONArray smsCommands = jsonResponse.getJSONArray("smsCommands");

                                for (int o = 0; o < smsCommands.length(); o++) {
                                    final JSONObject firstJSONObject = smsCommands.getJSONObject(o);
                                    final JSONArray smsCodes = firstJSONObject.getJSONArray("smsCodes");

                                    for (int t = 0; t < smsCodes.length(); t++) {
                                        final JSONObject jsonDatElement = smsCodes.getJSONObject(t);
                                        final String code = jsonDatElement.getString("code");
                                        final String id = jsonDatElement.getJSONObject("dataElement").getString("id");
                                        final DataElement dataElement = realm.where(DataElement.class).equalTo("id", id).findFirst();

                                        if (dataElement != null) {
                                            realm.beginTransaction();
                                            dataElement.setCode(code);
                                            realm.commitTransaction();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void syncOrganisationUnitsOld() {
        final String url = this.baseURL.concat(URLConstants.ORGANISATION_UNITS);
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e != null)
                    Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                try {
                    final JSONObject orgUnitResponseObject = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
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

                            } catch (JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void syncOrganisationUnits() {
        final String url = this.baseURL.concat(URLConstants.ORGANISATION_UNITS);
        final Request request = new Request.Builder().url(url).addHeader("Authorization", Credentials.basic(username, password)).build();

        Call response = client.newCall(request);
        response.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                if (e != null)
                    Toasty.error(context, (e.getMessage() != null) ? e.getMessage() : "The server is not responding", Toast.LENGTH_SHORT, false).show();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                try {
                    final JSONObject orgUnitResponseObject = new JSONObject(new String(response.body().bytes()));
                    ((Activity) context).runOnUiThread(new Runnable() {
                        public void run() {
                            try {
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

                            } catch (JSONException e) {
                                Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
                            }
                        }
                    });
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
