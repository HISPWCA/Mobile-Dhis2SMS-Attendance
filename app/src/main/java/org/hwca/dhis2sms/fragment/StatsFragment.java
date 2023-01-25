package org.hwca.dhis2sms.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.DataElementValue;
import org.hwca.dhis2sms.entity.DataSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class StatsFragment extends Fragment {
    private Realm realm;
    private boolean displayCharts = false;
    private String selectedPeriods;
    private BarChart barChart;
    private LinearLayout chartSettings;
    private RadioGroup rdgGroup;
    private TextView noData;
    private RadioGroup periodChk;
    private FloatingActionButton fab;
    private DataSet selectedDataSet;

    public StatsFragment() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        barChart = getView().findViewById(R.id.barChart);
        chartSettings = getView().findViewById(R.id.chartSettings);
        rdgGroup = getView().findViewById(R.id.rdgGroup);
        noData = getView().findViewById(R.id.noData);
        periodChk = getView().findViewById(R.id.periodChk);
        fab = getView().findViewById(R.id.fab_btn);
        fab.setVisibility(View.GONE);

        loadDataSets();
        generateBarChartOne();
    }

    private void loadDataSets() {
        final RealmResults<DataSet> dataSets = realm.where(DataSet.class).findAll();

        rdgGroup.removeAllViews();

        for (int i = 0; i < dataSets.size(); i++) {
            final RadioButton radioButton = new RadioButton(getContext());

            if (dataSets.get(i) != null && dataSets.get(i).getName() != null) {
                radioButton.setId(i);
                radioButton.setText(dataSets.get(i).getName());
                radioButton.setTextColor(Color.BLACK);
                radioButton.setPadding(5, 5, 5, 5);

                rdgGroup.addView(radioButton);
            }
        }

        rdgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                final DataSet dataSet = dataSets.get(checkedId);
                final TableRow dataSetTableRow = new TableRow(getContext());
                final TextView dataSetTextView = new TextView(getContext());

                selectedDataSet = dataSet;
                dataSetTextView.setText(dataSet.getName());
                dataSetTextView.setGravity(Gravity.CENTER);
                dataSetTextView.setTextSize(18);
                dataSetTableRow.setGravity(Gravity.CENTER);
                dataSetTableRow.addView(dataSetTextView);
                loadDataElementValues(dataSet.getId());
            }
        });
    }

    private void loadDataElementValues(final String dataSetId) {
        final RealmResults<DataElementValue> dataElementValues = realm.where(DataElementValue.class).equalTo("dataSet", dataSetId).findAll();
        periodChk.removeAllViews();

        if (dataElementValues.isEmpty()) {
            noData.setVisibility(View.VISIBLE);
            periodChk.setVisibility(View.GONE);
        } else {
            periodChk.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        }

        final Set<String> values = new HashSet<>();
        for (final DataElementValue dataElementValue : dataElementValues) {
            values.add(dataElementValue.getPeriod());
        }

        periodChk.removeAllViews();

        int k = 0;
        for (final String value : values) {
            final RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(value);
            radioButton.setChecked(false);
            radioButton.setId(k);
            radioButton.setTextColor(Color.BLACK);
            radioButton.setPadding(5, 5, 5, 5);

            periodChk.addView(radioButton);
            periodChk.setVisibility(View.VISIBLE);
            k++;
        }
        periodChk.setVisibility(View.VISIBLE);
        periodChk.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fab.setVisibility(View.VISIBLE);

                final List<String> customValues = new ArrayList<>(values);
                selectedPeriods = customValues.get(checkedId);
            }
        });
    }

    private void generateBarChartOne() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayCharts = !displayCharts;
                if (displayCharts && !selectedPeriods.isEmpty()) {
                    final String legend = selectedDataSet.getDisplayName().concat("(").concat(selectedPeriods).concat(")");
                    final BarDataSet barDataSet1 = new BarDataSet(uniqueDataValue(), legend);
                    final BarData barData1 = new BarData();
                    barData1.addDataSet(barDataSet1);
                    barChart.setDescription(null);
                    barChart.setData(barData1);
                    barChart.invalidate();
                    barChart.setVisibility(View.VISIBLE);
                    chartSettings.setVisibility(View.GONE);
                    rdgGroup.setVisibility(View.GONE);
                    periodChk.setVisibility(View.GONE);
                    noData.setVisibility(View.GONE);
                } else {
                    barChart.setVisibility(View.GONE);
                    chartSettings.setVisibility(View.VISIBLE);
                    rdgGroup.setVisibility(View.VISIBLE);
                    periodChk.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private List<BarEntry> uniqueDataValue() {
        final List<BarEntry> dataValues = new ArrayList<>();
        final RealmResults<DataElementValue> dataElementValues = realm.where(DataElementValue.class).equalTo("period", selectedPeriods).findAll();
        int i = 1;
        for (final DataElementValue value : dataElementValues) {
            if (!value.getValue().isEmpty()) {
                dataValues.add(new BarEntry(i, Integer.parseInt(value.getValue())));

                i++;
            }
        }

        return dataValues;
    }
}
