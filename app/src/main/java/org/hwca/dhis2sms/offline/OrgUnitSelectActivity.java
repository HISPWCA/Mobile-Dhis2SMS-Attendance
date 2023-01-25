package org.hwca.dhis2sms.offline;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.activity.MainActivity;
import org.hwca.dhis2sms.entity.User;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;

public class OrgUnitSelectActivity extends AppCompatActivity {
    private Realm realm;
    private Spinner levelUnOrgUnitSpinner;
    private Spinner levelDeuxOrgUnitSpinner;
    private Spinner levelTroisOrgUnitSpinner;
    private Spinner levelQuatreOrgUnitSpinner;
    private Spinner levelCinqOrgUnitSpinner;
    private String currentSelectedOrgUnitId = null;
    private String currentSelectedOrgUnitDisplayName = null;
    private Button validateButton;
    private CurrentSelectedOrgUnit selectedOrgUnit;
    private GridLayout selectedOrgUnitGridLayout;
    private final List<SelectedOrgUnit> selectedOrgUnitList = new ArrayList<>();
    private LinearLayout.LayoutParams params;
    private ScrollView schoolListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orgunit_select);
        realm = Realm.getDefaultInstance();

        selectedOrgUnitGridLayout = findViewById(R.id.selected_org_list_container);
        schoolListContainer = findViewById(R.id.school_list_container);
        Button addButton = findViewById(R.id.btnAdd);
        validateButton = findViewById(R.id.btnValidate);
        validateButton.setVisibility(View.GONE);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showValidateDialog();
            }
        });
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        selectedOrgUnitList();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedOrgUnitId == null && currentSelectedOrgUnitDisplayName == null)
                    Toasty.error(OrgUnitSelectActivity.this, "Please select the school").show();
                else if (currentSelectedOrgUnitId.equals("Select") && currentSelectedOrgUnitDisplayName.equals("Select"))
                    Toasty.error(OrgUnitSelectActivity.this, "Please select the school").show();
                else {
                    SelectedOrgUnit orgUnit = realm.where(SelectedOrgUnit.class).equalTo("id", currentSelectedOrgUnitId).findFirst();
                    if (orgUnit == null) {
                        realm.beginTransaction();
                        orgUnit = realm.createObject(SelectedOrgUnit.class, currentSelectedOrgUnitId);
                        orgUnit.displayName = currentSelectedOrgUnitDisplayName;
                        realm.commitTransaction();
                    } else
                        Toasty.info(OrgUnitSelectActivity.this, "You have already selected this school").show();

                    selectedOrgUnitList();
                }
            }
        });

        selectedOrgUnit = realm.where(CurrentSelectedOrgUnit.class).equalTo("id", "selectedOrgUnit").findFirst();
        levelUnOrgUnitSpinner = findViewById(R.id.level_un_org_unit_spinner);
        levelDeuxOrgUnitSpinner = findViewById(R.id.level_deux_org_unit_spinner);
        levelTroisOrgUnitSpinner = findViewById(R.id.level_trois_org_unit_spinner);
        levelQuatreOrgUnitSpinner = findViewById(R.id.level_quatre_org_unit_spinner);
        levelCinqOrgUnitSpinner = findViewById(R.id.level_cinq_org_unit_spinner);

        populateLevelUnOrgUnitSpinner();
    }

    private void selectedOrgUnitList() {
        selectedOrgUnitGridLayout.removeAllViews();
        params.setMargins(8, 5, 8, 8);
        RealmResults<SelectedOrgUnit> orgUnits = realm.where(SelectedOrgUnit.class).findAll();
        if (orgUnits.size() > 0) {
            for (SelectedOrgUnit selectedOrgUnit : orgUnits) {
                if (selectedOrgUnit != null) {
                    selectedOrgUnitList.add(selectedOrgUnit);
                    LinearLayout row = (LinearLayout) LinearLayout.inflate(OrgUnitSelectActivity.this, R.layout.select_org_item, null);
                    row.setLayoutParams(params);
                    selectedOrgUnitGridLayout.addView(row);
                    item(row, selectedOrgUnit);
                }
            }

            validateButton.setVisibility(View.VISIBLE);
            schoolListContainer.setVisibility(View.VISIBLE);
        } else {
            schoolListContainer.setVisibility(View.GONE);
            validateButton.setVisibility(View.GONE);
        }
    }

    private void item(LinearLayout row, final SelectedOrgUnit selectedOrgUnit) {
        final TextView selectedDisplayNameTextView;
        final ImageView removeSelectedOrgUnit;
        selectedDisplayNameTextView = row.findViewById(R.id.selected_org_unit_name_text_view);
        removeSelectedOrgUnit = row.findViewById(R.id.remove_selected_org_unit_image_view);
        selectedDisplayNameTextView.setText(selectedOrgUnit.displayName);
        removeSelectedOrgUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDialog(selectedOrgUnit);
            }
        });
    }

    public void populateLevelUnOrgUnitSpinner() {
        final List<OrgUnit> orgUnitNames = new ArrayList<>();
        final RealmResults<OrgUnit> orgUnits = realm.where(OrgUnit.class).isNull("parent").findAll();
        for (final OrgUnit orgUnit : orgUnits)
            if (orgUnit != null) {
                OrgUnit populateOrgUnit = new OrgUnit();
                populateOrgUnit.id = orgUnit.id;
                populateOrgUnit.displayName = orgUnit.displayName;
                populateOrgUnit.parent = orgUnit.parent;
                orgUnitNames.add(populateOrgUnit);
            }

        final ArrayAdapter<OrgUnit> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, orgUnitNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        this.levelUnOrgUnitSpinner.setAdapter(dataAdapter);
        this.levelUnOrgUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrgUnit orgUnit = (OrgUnit) levelUnOrgUnitSpinner.getSelectedItem();
                populateLevelDeuxOrgUnitSpinner(orgUnit.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void populateLevelDeuxOrgUnitSpinner(String parent) {
        final List<OrgUnit> orgUnitNames = new ArrayList<>();
        OrgUnit firstOrg = new OrgUnit();
        firstOrg.id = "id";
        firstOrg.displayName = "Select the region";
        firstOrg.parent = " ";
        firstOrg.level = 0;
        orgUnitNames.add(firstOrg);

        final RealmResults<OrgUnit> orgUnits = realm.where(OrgUnit.class).equalTo("parent", parent).isNotNull("parent").sort("displayName").findAll();
        for (final OrgUnit orgUnit : orgUnits)
            if (orgUnit != null) {
                OrgUnit populateOrgUnit = new OrgUnit();
                populateOrgUnit.id = orgUnit.id;
                populateOrgUnit.displayName = orgUnit.displayName;
                populateOrgUnit.parent = orgUnit.parent;
                orgUnitNames.add(populateOrgUnit);
            }

        if (orgUnits.size() > 0)
            levelDeuxOrgUnitSpinner.setVisibility(View.VISIBLE);

        final ArrayAdapter<OrgUnit> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, orgUnitNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        this.levelDeuxOrgUnitSpinner.setAdapter(dataAdapter);
        this.levelDeuxOrgUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrgUnit orgUnit = (OrgUnit) levelDeuxOrgUnitSpinner.getSelectedItem();
                populateLevelTroisOrgUnitSpinner(orgUnit.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void populateLevelTroisOrgUnitSpinner(String parent) {
        final List<OrgUnit> orgUnitNames = new ArrayList<>();
        OrgUnit firstOrg = new OrgUnit();
        firstOrg.id = "id";
        firstOrg.displayName = "Select";
        firstOrg.parent = " ";
        firstOrg.level = 0;
        orgUnitNames.add(firstOrg);

        final RealmResults<OrgUnit> orgUnits = realm.where(OrgUnit.class).equalTo("parent", parent).isNotNull("parent").sort("displayName").findAll();
        for (final OrgUnit orgUnit : orgUnits)
            if (orgUnit != null) {
                OrgUnit populateOrgUnit = new OrgUnit();
                populateOrgUnit.id = orgUnit.id;
                populateOrgUnit.displayName = orgUnit.displayName;
                populateOrgUnit.parent = orgUnit.parent;
                orgUnitNames.add(populateOrgUnit);
            }

        if (orgUnits.size() > 0)
            levelTroisOrgUnitSpinner.setVisibility(View.VISIBLE);

        final ArrayAdapter<OrgUnit> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, orgUnitNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        this.levelTroisOrgUnitSpinner.setAdapter(dataAdapter);
        this.levelTroisOrgUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrgUnit orgUnit = (OrgUnit) levelTroisOrgUnitSpinner.getSelectedItem();
                populateLevelQuatreOrgUnitSpinner(orgUnit.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void populateLevelQuatreOrgUnitSpinner(String parent) {
        final List<OrgUnit> orgUnitNames = new ArrayList<>();
        OrgUnit firstOrg = new OrgUnit();
        firstOrg.id = "id";
        firstOrg.displayName = "Select";
        firstOrg.parent = " ";
        firstOrg.level = 0;
        orgUnitNames.add(firstOrg);

        final RealmResults<OrgUnit> orgUnits = realm.where(OrgUnit.class).equalTo("parent", parent).isNotNull("parent").sort("displayName").findAll();
        for (final OrgUnit orgUnit : orgUnits)
            if (orgUnit != null) {
                OrgUnit populateOrgUnit = new OrgUnit();
                populateOrgUnit.id = orgUnit.id;
                populateOrgUnit.displayName = orgUnit.displayName;
                populateOrgUnit.parent = orgUnit.parent;
                orgUnitNames.add(populateOrgUnit);
            }

        if (orgUnits.size() > 0)
            levelQuatreOrgUnitSpinner.setVisibility(View.VISIBLE);

        final ArrayAdapter<OrgUnit> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, orgUnitNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        this.levelQuatreOrgUnitSpinner.setAdapter(dataAdapter);
        this.levelQuatreOrgUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OrgUnit orgUnit = (OrgUnit) levelQuatreOrgUnitSpinner.getSelectedItem();
                populateLevelCinqOrgUnitSpinner(orgUnit.id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    public void populateLevelCinqOrgUnitSpinner(String parent) {
        final List<OrgUnit> orgUnitNames = new ArrayList<>();
        OrgUnit firstOrg = new OrgUnit();
        firstOrg.id = "id";
        firstOrg.displayName = "Select";
        firstOrg.parent = " ";
        firstOrg.level = 0;
        orgUnitNames.add(firstOrg);

        final RealmResults<OrgUnit> orgUnits = realm.where(OrgUnit.class).equalTo("parent", parent).isNotNull("parent").sort("displayName").findAll();
        for (final OrgUnit orgUnit : orgUnits)
            if (orgUnit != null) {
                OrgUnit populateOrgUnit = new OrgUnit();
                populateOrgUnit.id = orgUnit.id;
                populateOrgUnit.displayName = orgUnit.displayName;
                populateOrgUnit.parent = orgUnit.parent;
                orgUnitNames.add(populateOrgUnit);
            }

        if (orgUnits.size() > 0)
            levelCinqOrgUnitSpinner.setVisibility(View.VISIBLE);

        final ArrayAdapter<OrgUnit> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, orgUnitNames);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        this.levelCinqOrgUnitSpinner.setAdapter(dataAdapter);
        this.levelCinqOrgUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                validateButton.setVisibility(View.GONE);
                currentSelectedOrgUnitId = null;
                currentSelectedOrgUnitDisplayName = null;
                OrgUnit orgUnit = (OrgUnit) levelCinqOrgUnitSpinner.getSelectedItem();

                if (!orgUnit.displayName.equals("Select")) {
                    currentSelectedOrgUnitId = orgUnit.id;
                    currentSelectedOrgUnitDisplayName = orgUnit.displayName;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void removeDialog(final SelectedOrgUnit selectedOrgUnit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrgUnitSelectActivity.this);
        builder.setTitle("Remove Confirmation")
                .setMessage("Are sure you want to remove this school from the list")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedOrgUnit orgUnit = realm.where(SelectedOrgUnit.class).equalTo("id", selectedOrgUnit.id).findFirst();
                        if (orgUnit != null) {
                            realm.beginTransaction();
                            realm.where(SelectedOrgUnit.class).equalTo("id", selectedOrgUnit.id).findFirst().deleteFromRealm();
                            realm.commitTransaction();
                        }
                        selectedOrgUnitList();
                    }
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void showValidateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrgUnitSelectActivity.this);
        builder.setTitle("Confirmation")
                .setMessage("Are sure you want to validate the school you have selected")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                }).
                setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                }).create().show();
    }
}