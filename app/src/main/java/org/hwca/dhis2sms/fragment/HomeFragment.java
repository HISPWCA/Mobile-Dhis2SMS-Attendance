package org.hwca.dhis2sms.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.whiteelephant.monthpicker.MonthPickerDialog;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.activity.PinCodeConfigActivity;
import org.hwca.dhis2sms.activity.PinCodeLoginActivity;
import org.hwca.dhis2sms.activity.ZoneActivity;
import org.hwca.dhis2sms.entity.DataElement;
import org.hwca.dhis2sms.entity.DataElementValue;
import org.hwca.dhis2sms.entity.DataSet;
import org.hwca.dhis2sms.entity.SMSMessage;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.entity.SmsCommand;
import org.hwca.dhis2sms.entity.User;
import org.hwca.dhis2sms.offline.CurrentSelectedOrgUnit;
import org.hwca.dhis2sms.offline.OrgUnitSelectActivity;
import org.hwca.dhis2sms.offline.SelectedOrgUnit;
import org.hwca.dhis2sms.utils.Constants;
import org.hwca.dhis2sms.utils.DynamincViews;
import org.hwca.dhis2sms.utils.SMSUtils;
import org.hwca.dhis2sms.utils.Synchronizer;
import org.hwca.dhis2sms.utils.Utilities;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HomeFragment extends Fragment {
    private Realm realm;
    private List<DataSet> dataSetList;
    private List<DataElement> dataElementList;
    private Synchronizer synchronizer;
    private LinearLayout linearLayout;
    private DataSet selectedDataSet;
    private Button btnSendSMS;
    private DynamincViews dynamincViews;
    private Spinner spinner;
    private String displayName;
    private static final String DEFAULT_ITEM = "Please choose any the DataSet here";
    private String username;
    private String password;
    private boolean isFirstLoad = true;
    private EditText period;
    private String selectedPeriod;
    private byte[] response;
    private Button launchSync;
    private Button launchDataSetSync;
    private ProgressBar progressBar;
    private TextView syncLoadingText;
    private RelativeLayout mainContainer;
    private LinearLayout mainWrapper;
    private TextView schoolNameTextView;
    private boolean firstInit = true;
    private int maxYear = 2099;
    private CurrentSelectedOrgUnit currentSelectedOrgUnit;
    private Spinner orgUnitSpinner;
    private Context context;
    private final List<SelectedOrgUnit> userSelectedOrgUnitList = new ArrayList<>();

    public HomeFragment() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        syncLoadingText = view.findViewById(R.id.syncLoadingText);
        progressBar = view.findViewById(R.id.progressBar);
        mainWrapper = view.findViewById(R.id.mainWrapper);
        schoolNameTextView = view.findViewById(R.id.selected_org_unit_display_name_text_view);
        orgUnitSpinner = view.findViewById(R.id.select_org_unit_spinner);
        launchSync = view.findViewById(R.id.launchSync);
        launchDataSetSync = view.findViewById(R.id.launchDataSetSync);
        progressBar.setVisibility(View.GONE);
        syncLoadingText.setVisibility(View.GONE);
        context = view.getContext();
        launchSync.setVisibility(View.VISIBLE);
        launchSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Settings settings = realm.where(Settings.class).isNull("gatewayNumber").findFirst();
                if (settings != null && settings.getGatewayNumber() == null) {
                    final Intent zonedActivity = new Intent(context, ZoneActivity.class);

                    startActivity(zonedActivity);
                }
                progressBar.setVisibility(View.VISIBLE);
                syncLoadingText.setVisibility(View.VISIBLE);
                launchSync.setVisibility(View.GONE);

//                makeSynchronization();
                swithBetweenStates();
            }
        });
        mainContainer = view.findViewById(R.id.mainContainer);
        launchDataSetSync.setVisibility(View.GONE);
        launchDataSetSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                populateSpinner();
            }
        });

        final Settings settings = realm.where(Settings.class).findFirst();
        currentSelectedOrgUnit = realm.where(CurrentSelectedOrgUnit.class).equalTo("id", "selectedOrgUnit").findFirst();

        long countOrgUnit = realm.where(SelectedOrgUnit.class).count();

        if (countOrgUnit == 0) {
            final Intent orgIntent = new Intent(context, OrgUnitSelectActivity.class);
            startActivity(orgIntent);
            getActivity().finish();
        } else if (settings != null && settings.getPinCode() == null) {
            final Intent pinCodeIntent = new Intent(context, PinCodeConfigActivity.class);
            startActivity(pinCodeIntent);
            getActivity().finish();
        } else {
            final Settings setting = realm.where(Settings.class).isNull("gatewayNumber").findFirst();
            if (setting != null && setting.getGatewayNumber() == null) {
                final Intent zonedActivity = new Intent(context, ZoneActivity.class);

                startActivity(zonedActivity);
            } else {
                final Date settingDate = settings.getLastConnection() == null ? new Date() : settings.getLastConnection();
                final long nbrMinutes = (int) ((new Date().getTime() / 60000) - (settingDate.getTime() / 60000));
                if (nbrMinutes > 10 || settings.getLastConnection() == null) {
                    final Intent pinCodeLoginIntent = new Intent(context, PinCodeLoginActivity.class);

                    startActivity(pinCodeLoginIntent);
                }
            }
        }

        if (currentSelectedOrgUnit != null && currentSelectedOrgUnit.displayName != null)
            schoolNameTextView.setText(currentSelectedOrgUnit.displayName);

        this.linearLayout = view.findViewById(R.id.littleContainer);
        linearLayout.setVisibility(View.GONE);
        spinner = view.findViewById(R.id.spinner);

        populateOrgUnitSpinner();
        retrieveTheLastSelectedOrgUnit();
        initUserCredentials();
        initActionButton();
        initPeriod();

        if (currentSelectedOrgUnit != null && currentSelectedOrgUnit.displayName != null)
            populateSpinner();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void initUserCredentials() {
        try {
            this.username = getActivity().getIntent().getExtras().getString("username");
            this.password = getActivity().getIntent().getExtras().getString("password");
            final User user = realm.where(User.class).findFirst();
            this.response = user.getLoginResponse();
            final JSONObject jsonResponse = new JSONObject(new String(response));
            this.displayName = jsonResponse.getString("displayName");
            this.synchronizer = new Synchronizer(context, this.username, this.password, this.response);
        } catch (final JSONException e) {
            Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }

    public void makeSynchronization() {
        try {
            synchronizer = new Synchronizer(context, username, password, response);
            synchronizer.sync();
        } catch (final JSONException e) {
            Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }

    private void disableSoftInputFromAppearing(EditText editText) {
        editText.setTextIsSelectable(true);
        editText.setRawInputType(InputType.TYPE_NULL);
        editText.setFocusable(true);
    }

    public void doSynchronization() {
        progressBar.setVisibility(View.VISIBLE);
        syncLoadingText.setVisibility(View.VISIBLE);
        launchSync.setVisibility(View.GONE);
        launchDataSetSync.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        mainWrapper.setVisibility(View.GONE);

        makeSynchronization();

        final CountDownTimer toastCountDown = new CountDownTimer(60000, 1 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                syncLoadingText.setVisibility(View.GONE);
                launchSync.setVisibility(View.GONE);
                launchDataSetSync.setVisibility(View.VISIBLE);

                final RelativeLayout mainContainer = getView().findViewById(R.id.mainContainer);
                mainContainer.setVisibility(View.VISIBLE);

                final LinearLayout mainWrapper = getView().findViewById(R.id.mainWrapper);
                mainWrapper.setVisibility(View.GONE);
            }
        };
        toastCountDown.start();
    }

    public void populateOrgUnitSpinner() {
        userSelectedOrgUnitList.clear();
        SelectedOrgUnit firstOrg = new SelectedOrgUnit();
        firstOrg.id = "Select";
        firstOrg.displayName = "Please select a School here";
        userSelectedOrgUnitList.add(firstOrg);

        final RealmResults<SelectedOrgUnit> orgUnits = realm.where(SelectedOrgUnit.class).findAll();
        for (final SelectedOrgUnit orgUnit : orgUnits) {

            if (orgUnit != null) {
                SelectedOrgUnit populateOrgUnit = new SelectedOrgUnit();
                populateOrgUnit.id = orgUnit.id;
                populateOrgUnit.displayName = orgUnit.displayName;

                userSelectedOrgUnitList.add(populateOrgUnit);
            }
        }

        if (orgUnits.size() > 0)
            orgUnitSpinner.setVisibility(View.VISIBLE);

        final ArrayAdapter<SelectedOrgUnit> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, userSelectedOrgUnitList);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        this.orgUnitSpinner.setAdapter(dataAdapter);
        this.orgUnitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectedOrgUnit orgUnit = (SelectedOrgUnit) orgUnitSpinner.getSelectedItem();
                if (!orgUnit.id.equals("Select")) {
                    realm.beginTransaction();
                    currentSelectedOrgUnit.uid = orgUnit.id;
                    currentSelectedOrgUnit.displayName = orgUnit.displayName;
                    realm.commitTransaction();

                    populateSpinner();
                } else {
                    spinner.setVisibility(View.GONE);
                    realm.beginTransaction();
                    currentSelectedOrgUnit.uid = null;
                    currentSelectedOrgUnit.displayName = null;
                    realm.commitTransaction();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initPeriod() {
        period = getView().findViewById(R.id.period);
        disableSoftInputFromAppearing(period);

        period.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String periodType = selectedDataSet.getPeriodType();
                if (periodType.startsWith("Weekly")) {
                    period.setVisibility(View.VISIBLE);
                    assert getView() != null;
                    linearLayout.setVisibility(View.VISIBLE);

                    populateWeeklySpinner();
                } else if (periodType.contentEquals("SixMonthly")) {
                    period.setVisibility(View.VISIBLE);
                    assert getView() != null;
                    linearLayout.setVisibility(View.VISIBLE);

                    populateSixMonthsSpinner();
                } else if (periodType.contentEquals("Quarterly")) {
                    period.setVisibility(View.VISIBLE);
                    assert getView() != null;
                    linearLayout.setVisibility(View.VISIBLE);

                    populateQuarterlySpinner();
                } else if (periodType.contentEquals("Daily")) {
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                    showDioalogForDailyPeriod(year, month, dayOfMonth);
                } else if (periodType.contentEquals("Monthly")) {
                    final Calendar today = Calendar.getInstance();

                    final MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(context,
                            new MonthPickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(int selectedMonth, int selectedYear) {
                                    selectedMonth = selectedMonth + 1;
                                    selectedPeriod = selectedMonth < 10 ? "0".concat(String.valueOf(selectedMonth)) : String.valueOf(selectedMonth);
                                    retrieveDataFromDataBaseToFields(selectedPeriod);
                                    period.setText("Period Type: Monthly - Selected Month: ".concat(selectedPeriod.concat("/").concat(String.valueOf(selectedYear))));
                                    linearLayout.setVisibility(View.VISIBLE);
                                    retrieveDataFromDataBaseToFields(selectedPeriod);
                                }
                            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                    builder.setMinYear(1900)
                            .setTitle("Select month")
                            .setActivatedYear(2019)
                            .setMaxYear(2090)
                            .setMinMonth(Calendar.JANUARY)
                            .setMonthRange(Calendar.JANUARY, Calendar.DECEMBER)
                            .build()
                            .show();
                } else if (periodType.contentEquals("Yearly")) {
                    final Calendar today = Calendar.getInstance();
                    final MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(context,
                            new MonthPickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(int selectedMonth, int selectedYear) {
                                    final String currentPeriod = String.valueOf(selectedYear);
                                    selectedPeriod = currentPeriod;
                                    retrieveDataFromDataBaseToFields(currentPeriod);
                                    period.setText("Period Type: Yearly - Selected Year: ".concat(currentPeriod));
                                    linearLayout.setVisibility(View.VISIBLE);
                                    retrieveDataFromDataBaseToFields(selectedPeriod);
                                }
                            }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));
                    builder.setMinYear(1900)
                            .setActivatedYear(2019)
                            .setMaxYear(2030)
                            .setTitle("Select a Year")
                            .showYearOnly()
                            .build()
                            .show();
                }
            }
        });
    }

    private void hideSpinners(int p, int p2, int p3) {
        final LinearLayout semestrePeriodType = getView().findViewById(p);
        final Spinner semestreSpinner = getView().findViewById(p2);
        final Spinner semestreAnneeSpinner = getView().findViewById(p3);

        semestrePeriodType.setVisibility(View.GONE);
        semestreSpinner.setVisibility(View.GONE);
        semestreAnneeSpinner.setVisibility(View.GONE);
    }

    private void populateWeeklySpinner() {
        final List<String> years = new ArrayList<>();
        final List<String> weeks = new ArrayList<>();
        final int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int y = year - 20; y < maxYear; y++)
            years.add(String.valueOf(y));

        Collections.sort(years, Collections.reverseOrder());

        final LinearLayout weekPeriodType = getView().findViewById(R.id.weekPeriodType);
        final Spinner wWeeksSpinner = getView().findViewById(R.id.wWeeksSpinner);
        final Spinner wYearSpinner = getView().findViewById(R.id.wYearSpinner);

        weekPeriodType.setVisibility(View.GONE);
        wWeeksSpinner.setVisibility(View.GONE);
        wYearSpinner.setVisibility(View.GONE);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, years);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        wYearSpinner.setAdapter(dataAdapter);
        wYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String userSelectedYear = parent.getItemAtPosition(position).toString();
                int selectedYear = firstInit ? maxYear : Integer.parseInt(userSelectedYear) + 1;

                final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                final Period weekPeriod = new Period().withWeeks(1);
                final DateTime startDate = new DateTime(selectedYear - 1, 1, 1, 0, 0, 0, 0);
                final DateTime endDate = new DateTime(selectedYear, 1, 1, 0, 0, 0, 0);
                Interval i = new Interval(startDate, weekPeriod);

                weeks.clear();
                while (i.getEnd().isBefore(endDate)) {
                    final int weekNumber = i.getStart().getWeekOfWeekyear();
                    final String weekNumberString = weekNumber < 10 ? "0".concat(String.valueOf(weekNumber)) : String.valueOf(weekNumber);
                    final String week = "Week ".concat(weekNumberString);
                    final Date startingDate = i.getStart().toDate();
                    LocalDateTime localStartingDate = LocalDateTime.fromDateFields(startingDate);
                    localStartingDate = localStartingDate.minusDays(2);
                    final String start = " - ".concat(df.format(localStartingDate.toDate()));

                    final Date endingDate = i.getEnd().minusMillis(1).toDate();
                    LocalDateTime localEndingDate = LocalDateTime.fromDateFields(endingDate);
                    localEndingDate = localEndingDate.minusDays(2);
                    final String end = " - ".concat(df.format(localEndingDate.toDate()));
                    weeks.add(week.concat(start).concat(end));

                    i = new Interval(i.getStart().plusDays(7), weekPeriod);
                }
                Collections.sort(weeks, Collections.reverseOrder());

                final ArrayAdapter<String> dataAdapterWeek = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, weeks);
                dataAdapterWeek.setDropDownViewResource(R.layout.spinner_item);
                wWeeksSpinner.setAdapter(dataAdapterWeek);
                wWeeksSpinner.setVisibility(View.VISIBLE);
                wWeeksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String userSelectedWeek = parent.getItemAtPosition(position).toString();
                        final String lastOccurence = userSelectedWeek.split("-")[2];

                        period.setText("Period Type: Weekly - Selected ".concat(userSelectedWeek));

                        final String[] dateArray = lastOccurence.trim().split("/");
                        selectedPeriod = dateArray[0].concat(dateArray[1]);

                        retrieveDataFromDataBaseToFields(selectedPeriod);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                firstInit = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        weekPeriodType.setVisibility(View.VISIBLE);
        wWeeksSpinner.setVisibility(View.VISIBLE);
        wYearSpinner.setVisibility(View.VISIBLE);
    }

    private void populateSixMonthsSpinner() {
        final List<String> years = new ArrayList<>();
        final List<String> sixMonths = new ArrayList<>();
        final int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int y = year - 20; y < maxYear; y++)
            years.add(String.valueOf(y));

        Collections.sort(years, Collections.reverseOrder());

        final LinearLayout semestrePeriodType = getView().findViewById(R.id.semestrePeriodType);
        final Spinner semestreSpinner = getView().findViewById(R.id.semestreSpinner);
        final Spinner semestreAnneeSpinner = getView().findViewById(R.id.semestreAnneeSpinner);
        semestrePeriodType.setVisibility(View.GONE);
        semestreSpinner.setVisibility(View.GONE);
        semestreAnneeSpinner.setVisibility(View.GONE);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, years);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);
        semestreAnneeSpinner.setAdapter(dataAdapter);
        semestreAnneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String userSelectedYear = parent.getItemAtPosition(position).toString();
                int selectedYear = firstInit ? maxYear : Integer.parseInt(userSelectedYear) + 1;

                sixMonths.clear();
                sixMonths.add("01 Jan ".concat(String.valueOf(selectedYear)).concat(" - ").concat("30 Jun ").concat(String.valueOf(selectedYear)));
                sixMonths.add("01 Jul ".concat(String.valueOf(selectedYear)).concat(" - ").concat("31 Dec ").concat(String.valueOf(selectedYear)));

                final ArrayAdapter<String> dataAdapterWeek = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, sixMonths);
                dataAdapterWeek.setDropDownViewResource(R.layout.spinner_item);
                semestreSpinner.setAdapter(dataAdapterWeek);
                semestreSpinner.setVisibility(View.VISIBLE);
                semestreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String userSelectedPeriod = parent.getItemAtPosition(position).toString();

                        period.setText("Period Type: SixMonths - \n Selected period: ".concat(userSelectedPeriod));

                        selectedPeriod = position == 0 ? "3006" : "3112";
                        retrieveDataFromDataBaseToFields(selectedPeriod);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                firstInit = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        semestrePeriodType.setVisibility(View.VISIBLE);
        semestreAnneeSpinner.setVisibility(View.VISIBLE);
        semestreSpinner.setVisibility(View.VISIBLE);
    }

    private void populateQuarterlySpinner() {
        final List<String> years = new ArrayList<>();
        final List<String> quarter = new ArrayList<>();
        final int year = Calendar.getInstance().get(Calendar.YEAR);

        for (int y = year - 20; y < maxYear; y++)
            years.add(String.valueOf(y));

        Collections.sort(years, Collections.reverseOrder());

        final LinearLayout trimestrePeriodType = getView().findViewById(R.id.trimestrePeriodType);
        final Spinner trimestreSpinner = getView().findViewById(R.id.trimestreSpinner);
        final Spinner trimestreAnneeSpinner = getView().findViewById(R.id.trimestreAnneeSpinner);
        trimestrePeriodType.setVisibility(View.GONE);
        trimestreSpinner.setVisibility(View.GONE);
        trimestreAnneeSpinner.setVisibility(View.GONE);

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, years);
        dataAdapter.setDropDownViewResource(R.layout.spinner_item);

        trimestreAnneeSpinner.setAdapter(dataAdapter);
        trimestreAnneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String userSelectedYear = parent.getItemAtPosition(position).toString();
                int selectedYear = firstInit ? maxYear : Integer.parseInt(userSelectedYear) + 1;

                quarter.clear();
                quarter.add("01 Jan ".concat(String.valueOf(selectedYear)).concat(" - ").concat("31 Mar ").concat(String.valueOf(selectedYear)));
                quarter.add("01 Apr ".concat(String.valueOf(selectedYear)).concat(" - ").concat("30 Jun ").concat(String.valueOf(selectedYear)));
                quarter.add("01 Jul ".concat(String.valueOf(selectedYear)).concat(" - ").concat("30 Sep ").concat(String.valueOf(selectedYear)));
                quarter.add("01 Oct ".concat(String.valueOf(selectedYear)).concat(" - ").concat("31 Dec ").concat(String.valueOf(selectedYear)));

                final ArrayAdapter<String> dataAdapterWeek = new ArrayAdapter<>(getActivity(), R.layout.spinner_item, quarter);
                dataAdapterWeek.setDropDownViewResource(R.layout.spinner_item);
                trimestreSpinner.setAdapter(dataAdapterWeek);
                trimestreSpinner.setVisibility(View.VISIBLE);
                trimestreSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        final String userSelectedPeriod = parent.getItemAtPosition(position).toString();
                        period.setText("Period Type: Quarterly - \n Selected period: ".concat(userSelectedPeriod));

                        switch (position) {
                            case 0:
                                selectedPeriod = "3103";
                                break;

                            case 1:
                                selectedPeriod = "3006";
                                break;

                            case 2:
                                selectedPeriod = "3009";
                                break;

                            case 3:
                                selectedPeriod = "3112";
                                break;

                            default:
                                selectedPeriod = null;
                                break;
                        }
                        retrieveDataFromDataBaseToFields(selectedPeriod);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                firstInit = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        trimestrePeriodType.setVisibility(View.VISIBLE);
        trimestreAnneeSpinner.setVisibility(View.VISIBLE);
        trimestreSpinner.setVisibility(View.VISIBLE);
    }

    private void retrieveDataFromDataBaseToFields(final String period) {
        if (period != null && period.length() > 0) {
            for (int i = 0; i < dataElementList.size(); i++) {
                final EditText editText = getView().findViewById(i);
                final DataElement dataElement = dataElementList.get(i);

                final DataElementValue dataElementValue = realm.where(DataElementValue.class).equalTo("period", period).equalTo("dataSet", selectedDataSet.getId()).equalTo("dataElement", dataElement.getId()).findFirst();

                if (dataElementValue != null)
                    editText.setText(dataElementValue.getValue());
                else
                    editText.setText(null);
            }
        }
    }

    private void retrieveTheLastSelectedOrgUnit() {
        if (currentSelectedOrgUnit != null && currentSelectedOrgUnit.uid != null) {
            String orgUnitId = currentSelectedOrgUnit.uid;
            int idx = -1;
            for (int i = 0; i < userSelectedOrgUnitList.size(); i++) {
                if (userSelectedOrgUnitList.get(i).id.equals(orgUnitId)) {
                    idx = i;
                }
            }
            orgUnitSpinner.setSelection(idx);
        }
    }

    private void showDioalogForDailyPeriod(final int year, final int month, final int dayOfMonth) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        final String currentMonth = month < 10 ? "0".concat(String.valueOf(month)) : String.valueOf(month);
                        final String currentDayOfMonth = day < 10 ? "0".concat(String.valueOf(day)) : String.valueOf(day);
                        final String currentPeriod = currentDayOfMonth.concat("/").concat(currentMonth).concat("/").concat(String.valueOf(year));
                        period.setText("Period Type: Daily - Selected Date: ".concat(currentPeriod));
                        linearLayout.setVisibility(View.VISIBLE);

                        selectedPeriod = String.valueOf(year).concat(currentMonth).concat(currentDayOfMonth);
                        retrieveDataFromDataBaseToFields(selectedPeriod);
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
    }

    public void generateFieldsForSelectedDataSet(final int position) {
        final DataSet dataSet = this.dataSetList.get(position - 1);
        this.period.setVisibility(View.VISIBLE);
        this.selectedDataSet = dataSet;
        this.linearLayout.removeAllViews();

        final RealmResults<DataElement> dataElements = realm.where(DataElement.class).equalTo("dataSet", dataSet.getId()).sort("order", Sort.ASCENDING).findAll();

        this.dataElementList = new ArrayList<>();
        dynamincViews = new DynamincViews();
        linearLayout.removeAllViews();

        for (int i = 0; i < dataElements.size(); i++) {
            this.dataElementList.add(dataElements.get(i));

            LinearLayout embededLinearLayout = new LinearLayout(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 10, 0, 5);
            embededLinearLayout.setLayoutParams(layoutParams);
            embededLinearLayout.setOrientation(LinearLayout.VERTICAL);
            embededLinearLayout.setPadding(10, 5, 10, 5);
            embededLinearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
            embededLinearLayout.addView(dynamincViews.generateTextView(context, dataElements.get(i).getName()));
            embededLinearLayout.addView(dynamincViews.generateEditText(context, i, "Please enter value here"));

            linearLayout.addView(embededLinearLayout);
        }

        autoSaveDataValue();
    }

    public void swithBetweenStates() {
        final CountDownTimer toastCountDown = new CountDownTimer(60000, 1) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                syncLoadingText.setVisibility(View.GONE);
                launchSync.setVisibility(View.GONE);
                launchDataSetSync.setVisibility(View.VISIBLE);
            }
        };

        toastCountDown.start();
    }

    public void populateSpinner() {
        if (currentSelectedOrgUnit != null && currentSelectedOrgUnit.displayName != null) {
            RealmResults<DataSet> dataSets;

            String filterValue = "";
            if (currentSelectedOrgUnit.displayName.contains("LBS")) {
                filterValue = "LBE";
            }
            if (currentSelectedOrgUnit.displayName.contains("UBS")) {
                filterValue = "UBE";
            }
            if (currentSelectedOrgUnit.displayName.contains("SSS")) {
                filterValue = "SSE";
            }

            dataSets = realm.where(DataSet.class).isNotNull("displayName").isNotEmpty("displayName").contains("displayName", filterValue)
                    .and().contains("displayName","Daily").or().contains("displayName","daily").findAll();

            if (currentSelectedOrgUnit.displayName.contains("BCS"))
                dataSets = realm.where(DataSet.class).isNotNull("displayName").isNotEmpty("displayName").contains("displayName", "LBE").or().contains("displayName", "UBE")
                        .and().contains("displayName","Daily").or().contains("displayName","daily").findAll();

            if (dataSets.isEmpty())
                dataSets = realm.where(DataSet.class).isNotNull("displayName").isNotEmpty("displayName").and().contains("displayName","Daily").or().contains("displayName","daily").findAll();

            this.dataSetList = new ArrayList<>();
            final List<String> dataSetNames = new ArrayList<>();
            dataSetNames.add("Please select a DataSet here ");

            for (final DataSet dataSet : dataSets) {
                this.dataSetList.add(dataSet);
                dataSetNames.add(dataSet.getDisplayName());
            }
            final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(context, R.layout.spinner_item, dataSetNames);
            dataAdapter.setDropDownViewResource(R.layout.spinner_item);

            this.spinner.setAdapter(dataAdapter);
            this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    hideSpinners(R.id.weekPeriodType, R.id.wWeeksSpinner, R.id.wYearSpinner);
                    hideSpinners(R.id.semestrePeriodType, R.id.semestreSpinner, R.id.semestreAnneeSpinner);
                    hideSpinners(R.id.trimestrePeriodType, R.id.trimestreSpinner, R.id.trimestreAnneeSpinner);

                    linearLayout.setVisibility(View.GONE);
                    period.setText(null);
                    selectedPeriod = null;

                    if (isFirstLoad)
                        isFirstLoad = false;
                    else {
                        if (parent.getItemAtPosition(position).equals(DEFAULT_ITEM)) {
                            Toasty.error(context, "Please select a dataSet", Toast.LENGTH_LONG, false).show();
                        } else {
                            linearLayout.removeAllViews();
                            if (position > 0)
                                generateFieldsForSelectedDataSet(position);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            progressBar.setVisibility(View.GONE);
            syncLoadingText.setVisibility(View.GONE);
            launchSync.setVisibility(View.GONE);
            launchDataSetSync.setVisibility(View.GONE);

            final RelativeLayout mainContainer = getView().findViewById(R.id.mainContainer);
            mainContainer.setVisibility(View.GONE);

            final LinearLayout mainWrapper = getView().findViewById(R.id.mainWrapper);
            mainWrapper.setVisibility(View.VISIBLE);

            this.spinner.setVisibility(View.VISIBLE);
        }
    }

    public void initActionButton() {
        btnSendSMS = getView().findViewById(R.id.btnSendSMS);
        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentSelectedOrgUnit != null && currentSelectedOrgUnit.uid == null && currentSelectedOrgUnit.displayName == null)
                    Toasty.error(context, "Please you should select the school", Toast.LENGTH_LONG, false).show();
                else if (selectedPeriod == null)
                    Toasty.error(context, "Please you should pick period before going on", Toast.LENGTH_LONG, false).show();
                else {
                    buildSMS();
                }
            }
        });
    }

    private void buildSMS() {
        String smsBody = " ";
        final SmsCommand smsCommand = realm.where(SmsCommand.class).equalTo("dataSet", this.selectedDataSet.getId()).findFirst();

        if (smsCommand == null)
            Toasty.error(context, "No sms command found for related DataSet.  Please try sync again", Toast.LENGTH_LONG, false).show();
        else {
            if (this.currentSelectedOrgUnit != null && this.currentSelectedOrgUnit.uid != null && !this.currentSelectedOrgUnit.uid.isEmpty()) {
                String orgUnit = this.currentSelectedOrgUnit.uid;
                String displayName = this.currentSelectedOrgUnit.displayName;

                final String separator = smsCommand.getSeparator();
                String message = "Period: ".concat(this.selectedPeriod);
                message = message.concat("\n");
                message = message.concat("School Name: ".concat(displayName));
                smsBody = smsBody.concat("orgUnit");
                smsBody = smsBody.concat(separator);
                smsBody = smsBody.concat(orgUnit);
                smsBody = smsBody.concat(separator);

                for (int i = 0; i < dataElementList.size(); i++) {
                    final EditText editText = getView().findViewById(i);
                    final DataElement dataElement = dataElementList.get(i);

                    final String content = Utilities.removeZeroOnStart(editText.getText().toString());
                    if (content.length() > 0 && dataElement != null) {
                        final String sName = dataElement.getDisplayName();
                        message = message.concat("\n");
                        message = message.concat(sName);
                        message = message.concat(" = ").concat(content);
                        smsBody = smsBody.concat(dataElement.getCode());
                        smsBody = smsBody.concat(separator);
                        smsBody = smsBody.concat(content);
                        smsBody = smsBody.concat(separator);

                        final String dataElementValueId = selectedDataSet.getId().concat(dataElement.getId()).concat(selectedPeriod);
                        DataElementValue dataElementValue = realm.where(DataElementValue.class).equalTo("id", dataElementValueId).equalTo("period", selectedPeriod).equalTo("dataSet", selectedDataSet.getId()).equalTo("dataElement", dataElement.getId()).findFirst();

                        realm.beginTransaction();

                        if (dataElementValue == null)
                            dataElementValue = realm.createObject(DataElementValue.class, dataElementValueId);
                        dataElementValue.setValue(content);
                        dataElementValue.setPeriod(selectedPeriod);
                        dataElementValue.setDataSet(selectedDataSet.getId());
                        dataElementValue.setDataElement(dataElement.getId());

                        realm.commitTransaction();
                    }
                }

                smsBody = smsBody.substring(0, smsBody.length() - 1);

                if (smsBody.length() > 2) {
                    smsSenderConfirmation(smsBody, smsCommand, message);
                } else
                    Toasty.error(context, "Please enter data ", Toast.LENGTH_LONG, false).show();
            } else
                Toasty.error(context, "Please select the school name ", Toast.LENGTH_LONG, false).show();
        }
    }

    private void smsSenderConfirmation(final String smsBody,
                                       final SmsCommand smsCommand, String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("DATA ENTRY SUMMARY");
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Send SMS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String finalSMS = smsCommand.getName().concat(" ").concat(selectedPeriod).concat(smsBody).replaceAll(" ", ".");

                realm.beginTransaction();
                SMSMessage smsMessage = realm.where(SMSMessage.class).equalTo("message", finalSMS).findFirst();
                if (smsMessage == null) {
                    final Number currentMaxId = realm.where(SMSMessage.class).max("id");
                    final int smsMessageId = currentMaxId == null ? 1 : currentMaxId.intValue() + 1;

                    smsMessage = realm.createObject(SMSMessage.class, smsMessageId);
                    smsMessage.setMessage(finalSMS);
                    smsMessage.setMessageSent("saved");
                }
                realm.commitTransaction();

                final Settings settings = realm.where(Settings.class).findFirst();
                final SMSUtils smsUtils = new SMSUtils(context, getActivity());

                smsUtils.sendSMS(settings.getGatewayNumber(), finalSMS);
            }
        });

        builder.setNegativeButton("No Just Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toasty.success(context, "All data are successfully saved", Toast.LENGTH_LONG, false).show();
            }
        });
        builder.show();
    }

    private void autoSaveDataValue() {
        for (int i = 0; i < dataElementList.size(); i++) {
            final EditText editText = getView().findViewById(i);
            final DataElement dataElement = dataElementList.get(i);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    final String content = Utilities.removeZeroOnStart(s.toString());

                    if (dataElement != null) {

                        final String dataElementValueId = selectedDataSet.getId().concat(dataElement.getId()).concat(selectedPeriod);
                        DataElementValue dataElementValue = realm.where(DataElementValue.class)
                                .equalTo("id", dataElementValueId)
                                .equalTo("period", selectedPeriod)
                                .equalTo("dataSet", selectedDataSet.getId())
                                .equalTo("dataElement", dataElement.getId())
                                .findFirst();

                        realm.beginTransaction();

                        if (dataElementValue == null)
                            dataElementValue = realm.createObject(DataElementValue.class, dataElementValueId);
                        dataElementValue.setValue(content);
                        dataElementValue.setPeriod(selectedPeriod);
                        dataElementValue.setDataSet(selectedDataSet.getId());
                        dataElementValue.setDataElement(dataElement.getId());

                        realm.commitTransaction();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

        }
    }

    // sync value

    public void doMetaDataSynchronization() {
        progressBar.setVisibility(View.VISIBLE);
        syncLoadingText.setVisibility(View.VISIBLE);
        launchSync.setVisibility(View.GONE);
        launchDataSetSync.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        mainWrapper.setVisibility(View.GONE);

        makeMetaDataSynchronization();

        final CountDownTimer toastCountDown = new CountDownTimer(60000, 1 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                syncLoadingText.setVisibility(View.GONE);
                launchSync.setVisibility(View.GONE);
                launchDataSetSync.setVisibility(View.VISIBLE);

                final RelativeLayout mainContainer = getView().findViewById(R.id.mainContainer);
                mainContainer.setVisibility(View.VISIBLE);

                final LinearLayout mainWrapper = getView().findViewById(R.id.mainWrapper);
                mainWrapper.setVisibility(View.GONE);
            }
        };
        toastCountDown.start();
    }

    public void doConfigurationSynchronization() {
        progressBar.setVisibility(View.VISIBLE);
        syncLoadingText.setVisibility(View.VISIBLE);
        launchSync.setVisibility(View.GONE);
        launchDataSetSync.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        mainWrapper.setVisibility(View.GONE);

        makeConfigurationSynchronization();

        final CountDownTimer toastCountDown = new CountDownTimer(60000, 1 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                syncLoadingText.setVisibility(View.GONE);
                launchSync.setVisibility(View.GONE);
                launchDataSetSync.setVisibility(View.VISIBLE);

                final RelativeLayout mainContainer = getView().findViewById(R.id.mainContainer);
                mainContainer.setVisibility(View.VISIBLE);

                final LinearLayout mainWrapper = getView().findViewById(R.id.mainWrapper);
                mainWrapper.setVisibility(View.GONE);
            }
        };
        toastCountDown.start();
    }

    public void doOrgUnitSynchronization() {
        progressBar.setVisibility(View.VISIBLE);
        syncLoadingText.setVisibility(View.VISIBLE);
        launchSync.setVisibility(View.GONE);
        launchDataSetSync.setVisibility(View.GONE);
        mainContainer.setVisibility(View.VISIBLE);
        mainWrapper.setVisibility(View.GONE);

        makeOrgUnitSynchronization();

        final CountDownTimer toastCountDown = new CountDownTimer(60000, 1 /*Tick duration*/) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
                syncLoadingText.setVisibility(View.GONE);
                launchSync.setVisibility(View.GONE);
                launchDataSetSync.setVisibility(View.VISIBLE);

                final RelativeLayout mainContainer = getView().findViewById(R.id.mainContainer);
                mainContainer.setVisibility(View.VISIBLE);

                final LinearLayout mainWrapper = getView().findViewById(R.id.mainWrapper);
                mainWrapper.setVisibility(View.GONE);
            }
        };
        toastCountDown.start();
    }

    public void makeMetaDataSynchronization() {
        synchronizer = new Synchronizer(context, username, password, response);
        try {
            final JSONObject loginResponse = new JSONObject(new String(response));
            final String displayName = loginResponse.getString("displayName");
            final JSONArray dataSets = loginResponse.getJSONArray("dataSets");
            final User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
            assert user != null;

            realm.beginTransaction();
            user.setDisplayName(displayName);
            realm.commitTransaction();

            synchronizer.syncDataSets(dataSets);
            synchronizer.loadSMSCommands(dataSets);
        } catch (final JSONException e) {
            Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }

    public void makeConfigurationSynchronization() {
        synchronizer = new Synchronizer(context, username, password, response);
        try {
            final JSONObject loginResponse = new JSONObject(new String(response));
            final String displayName = loginResponse.getString("displayName");
            final User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
            assert user != null;

            realm.beginTransaction();
            user.setDisplayName(displayName);
            realm.commitTransaction();

            synchronizer.syncGatewayNumber();
        } catch (final JSONException e) {
            Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }

    public void makeOrgUnitSynchronization() {
        synchronizer = new Synchronizer(context, username, password, response);
        try {
            final JSONObject loginResponse = new JSONObject(new String(response));
            final String displayName = loginResponse.getString("displayName");
            final User user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
            assert user != null;

            realm.beginTransaction();
            user.setDisplayName(displayName);
            realm.commitTransaction();

            synchronizer.syncOrganisationUnits();
        } catch (final JSONException e) {
            Toasty.error(context, e.getMessage(), Toast.LENGTH_LONG, false).show();
        }
    }


}
