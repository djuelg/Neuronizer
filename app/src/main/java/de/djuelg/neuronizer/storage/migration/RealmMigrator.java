package de.djuelg.neuronizer.storage.migration;

import android.annotation.SuppressLint;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by djuelg on 20.10.2016.
 */

public class RealmMigrator implements RealmMigration {

    @SuppressLint("DefaultLocale")
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        if (oldVersion < newVersion) {
            // Unknown migration
            throw new RealmMigrationNeededException(realm.getPath(), String.format("Migration missing from v%d to v%d", oldVersion, newVersion));
        }
    }
}
