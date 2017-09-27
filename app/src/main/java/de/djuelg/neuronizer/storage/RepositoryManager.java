package de.djuelg.neuronizer.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import de.djuelg.neuronizer.storage.migration.RealmMigrator;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Domi on 07.09.2017.
 *
 * Realm import/export: https://realm.io/docs/java/latest/#faq-backup-restore
 * Save files:          https://developer.android.com/training/basics/data-storage/files.html
 * Overall tutorial:    https://medium.com/glucosio-project/example-class-to-export-import-a-realm-database-on-android-c429ade2b4ed
 */

public class RepositoryManager {
    public final static String FALLBACK_REALM = "default.realm";
    public final static String REPOSITORY_EXTENSION = ".realm";
    public final static long SCHEMA_VERSION = 10L;

    public static CharSequence[] readRepositoryNames(File privateFilesDir) {
        File[] realmFiles = privateFilesDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.endsWith(REPOSITORY_EXTENSION);
            }
        });

        CharSequence[] repositoryNames = new CharSequence[realmFiles.length];
        for (int i=0; i < realmFiles.length; i++) {
            repositoryNames[i] = realmFiles[i].getName();
        }
        return repositoryNames;
    }

    public static boolean importRepository(File privateFilesDir, File importFile) {
        File realmFile = new File(privateFilesDir, importFile.getName());
        try {
            FileOutputStream outputStream = new FileOutputStream(realmFile);
            FileInputStream inputStream = new FileInputStream(importFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean exportRepository(File destination, String activeRealm) {
        if(!destination.exists()) {

            RealmConfiguration configuration = createConfiguration(activeRealm);
            Realm realm = Realm.getInstance(configuration);
            realm.writeCopyTo(destination);
            realm.close();
            return true;
        }
        return false;
    }

    public static RealmConfiguration createConfiguration(String realmName) {
        return new RealmConfiguration.Builder()
                .name(realmName)
                .schemaVersion(SCHEMA_VERSION)
                .migration(new RealmMigrator())
                .build();
    }
}
