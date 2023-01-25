package org.hwca.dhis2sms.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import net.danlew.android.joda.JodaTimeAndroid;

import org.hwca.dhis2sms.R;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CalendarViewActivity extends AppCompatActivity {
    private boolean firstInit = true;
    private Integer maxYear;
    private final List<String> semestreList = new ArrayList<>();
    private final List<String> trimestreList = new ArrayList<>();
    private final String DATE_FORMAT = "dd-MM-yyyy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        JodaTimeAndroid.init(this);

        final Spinner anneesSpinner = findViewById(R.id.anneesSpinner);
        final Spinner weeksSpinner = findViewById(R.id.weeksSpinner);

        final Spinner trimestreAnneeSpinner = findViewById(R.id.trimestreAnneeSpinner);
        final Spinner trimestreSpinner = findViewById(R.id.trimestreSpinner);

        final Spinner semestreAnneeSpinner = findViewById(R.id.semestreAnneeSpinner);
        final Spinner semestreSpinner = findViewById(R.id.semestreSpinner);

        final List<String> years = new ArrayList<>();
        final List<String> weeks = new ArrayList<>();
        final int year = Calendar.getInstance().get(Calendar.YEAR);

        maxYear = year + 21;
        for (int y = year - 20; y < maxYear; y++)
            years.add(String.valueOf(y));

        Collections.sort(years, Collections.reverseOrder());

        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anneesSpinner.setAdapter(dataAdapter);
        anneesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final String userSelectedYear = parent.getItemAtPosition(position).toString();
                int selectedYear = firstInit ? maxYear : Integer.parseInt(userSelectedYear) + 1;

                final SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
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
                    localStartingDate = localStartingDate.minusDays(1);

                    final String start = " - ".concat(df.format(localStartingDate.toDate()));
                    final Date endingDate = i.getEnd().minusMillis(1).toDate();

                    LocalDateTime localEndingDate = LocalDateTime.fromDateFields(endingDate);
                    localEndingDate = localEndingDate.minusDays(1);

                    final String end = " - ".concat(df.format(localEndingDate.toDate()));
                    weeks.add(week.concat(start).concat(end));

                    i = new Interval(i.getStart().plusDays(7), weekPeriod);
                }
                Collections.sort(weeks, Collections.reverseOrder());

                final ArrayAdapter<String> dataAdapterWeek = new ArrayAdapter<>(CalendarViewActivity.this, android.R.layout.simple_spinner_item, weeks);
                dataAdapterWeek.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                weeksSpinner.setAdapter(dataAdapterWeek);

                weeksSpinner.setVisibility(View.VISIBLE);
                firstInit = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //
            }
        });


        final ArrayAdapter<String> trimestreAnneeSpinnerDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        trimestreAnneeSpinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimestreAnneeSpinner.setAdapter(trimestreAnneeSpinnerDataAdapter);

        trimestreList.clear();
        trimestreList.add("01 Jan - 31 Mar");
        trimestreList.add("01 Apr - 30 Jun");
        trimestreList.add("01 Jul - 31 Sept");
        trimestreList.add("01 Oct - 31 Dec");

        final ArrayAdapter<String> trimestreSpinnerDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trimestreList);
        trimestreSpinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trimestreSpinner.setAdapter(trimestreSpinnerDataAdapter);

        final ArrayAdapter<String> semestreAnneeSpinnerDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        semestreAnneeSpinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semestreAnneeSpinner.setAdapter(semestreAnneeSpinnerDataAdapter);

        semestreList.clear();
        semestreList.add("01 Jan - 30 Jun");
        semestreList.add("01 Jul - 31 Dec");

        final ArrayAdapter<String> semestreSpinnerDataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, semestreList);
        semestreSpinnerDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semestreSpinner.setAdapter(semestreSpinnerDataAdapter);

        }
    }

