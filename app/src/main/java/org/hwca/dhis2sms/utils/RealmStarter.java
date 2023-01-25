package org.hwca.dhis2sms.utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmStarter extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(RealmStarter.this);
        final RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name("default")
                .schemaVersion(3)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
