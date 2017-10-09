package de.djuelg.neuronizer;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static de.djuelg.neuronizer.storage.RepositoryManager.createConfiguration;
import static io.realm.Realm.DEFAULT_REALM_NAME;

public class AndroidApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        // initiate Realm
        Realm.init(this);
        RealmConfiguration realmConfiguration = createConfiguration(DEFAULT_REALM_NAME);
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
