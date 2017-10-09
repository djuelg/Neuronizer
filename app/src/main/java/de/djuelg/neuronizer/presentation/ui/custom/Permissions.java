package de.djuelg.neuronizer.presentation.ui.custom;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import static de.djuelg.neuronizer.presentation.ui.Constants.READ_EXTERNAL_STORAGE;
import static de.djuelg.neuronizer.presentation.ui.Constants.WRITE_EXTERNAL_STORAGE;

/**
 * Created by Domi on 05.09.2017.
 *
 * This class simplifies working with dynamic permission handling
 */

public class Permissions {

    private final Fragment fragment;

    public Permissions(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     *
     * @return the full name of the permission if granted, or empty string
     */
    public String getGrantedPermission(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case READ_EXTERNAL_STORAGE:
                    return Manifest.permission.READ_EXTERNAL_STORAGE;
                case WRITE_EXTERNAL_STORAGE:
                    return Manifest.permission.WRITE_EXTERNAL_STORAGE;
                default:
                    break;
            }
        }
        return "";
    }

    /**
     * Check if the app has the requested permission
     *
     * @param permission from  Manifest.permission constants
     */
    public boolean appHasPermission(String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(fragment.getContext(), permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Request a new permission from user
     *
     * @param permission from  Manifest.permission constants
     */
    public void requestPermission(String permission) {
        int requestCode;
        switch (permission) {
            case Manifest.permission.READ_EXTERNAL_STORAGE:
                requestCode = READ_EXTERNAL_STORAGE;
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                requestCode = WRITE_EXTERNAL_STORAGE;
                break;
            default:
                throw new RuntimeException("Permission not legal");
        }

        fragment.requestPermissions(new String[]{permission}, requestCode);
    }

    /**
     * Request a new permission from user if it is necessary
     * @return whether it was necessary
     */
    public boolean requestPermissionIfNecessary(String permission) {
        if (appHasPermission(permission)) return false;
        requestPermission(permission);
        return true;
    }
}
