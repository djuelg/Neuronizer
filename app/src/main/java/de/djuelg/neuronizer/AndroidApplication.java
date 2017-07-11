package de.djuelg.neuronizer;

import android.app.Application;

import de.djuelg.neuronizer.storage.migration.RealmMigrator;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import timber.log.Timber;
import timber.log.Timber.DebugTree;

public class AndroidApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // initiate Timber
        Timber.plant(new DebugTree());

        // initiate Realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .migration(new RealmMigrator())
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
