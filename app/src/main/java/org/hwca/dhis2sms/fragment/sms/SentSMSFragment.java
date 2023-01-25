package org.hwca.dhis2sms.fragment.sms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.hwca.dhis2sms.R;
import org.hwca.dhis2sms.entity.SMSMessage;
import org.hwca.dhis2sms.entity.Settings;
import org.hwca.dhis2sms.utils.SMSUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class SentSMSFragment extends Fragment {
    private static final int REQUEST_PHONE_CALL = 1;
    private List<String> smsList;

    public SentSMSFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sent_sms, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button resendSentSMS = getView().findViewById(R.id.resendSentSMS);
        resendSentSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Settings settings = Realm.getDefaultInstance().where(Settings.class).findFirst();
                final SMSUtils smsUtils = new SMSUtils(getContext(), getActivity());

                for (final String sms : smsList)
                    smsUtils.sendSMS(settings.getGatewayNumber(), sms);
            }
        });

        checkReadSMSPermissionIsGranted();
        populateSentSmsListView();
    }

    private void populateSentSmsListView() {
        smsList = new ArrayList<>();

        final RealmResults<SMSMessage> smsMessages = Realm.getDefaultInstance()
                .where(SMSMessage.class)
                .equalTo("messageSent", "sent").findAll();
        for (final SMSMessage smsMessage : smsMessages)
            smsList.add(smsMessage.getMessage());

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, smsList);
        final ListView listView = getView().findViewById(R.id.sent_sms_listview);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAnimal = smsList.get(position);
                Toast.makeText(getContext(), "Element Selected : " + selectedAnimal, Toast.LENGTH_LONG).show();
            }
        });

        final TextView textView = getView().findViewById(R.id.sent_sms_data_status);
        final LinearLayout linearLayout = getView().findViewById(R.id.sent_sms_action_button);
        if (smsList.isEmpty()) {
            linearLayout.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        }
    }

    private void checkReadSMSPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS}, REQUEST_PHONE_CALL);
    }
}
