package com.zebra.jamesswinton.togglenullkeyboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.symbol.emdk.EMDKManager;
import com.symbol.emdk.EMDKResults;
import com.symbol.emdk.ProfileManager;

public class MainActivity extends AppCompatActivity implements EMDKManager.EMDKListener {

    // Debugging
    private static final String TAG = "MainActivity";

    // Constants
    private static final String LAUNCHER_PREFS = "keyboard-prefs";
    private static final String LAUNCHER_STATE = "keyboard-state";

    // Static Variables
    private EMDKManager mEmdkManager = null;
    private ProfileManager mProfileManager = null;

    // Non-Static Variables
    private Boolean mIsCustomState = null;
    private SharedPreferences mSharedPrefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Validate First Run
        if (savedInstanceState == null) {
            // Init Shared Prefs
            mSharedPrefs = getSharedPreferences(LAUNCHER_PREFS, 0);
            mIsCustomState = mSharedPrefs.getBoolean(LAUNCHER_STATE, false);

            // Init EMDK
            EMDKResults emdkManagerResults = EMDKManager.getEMDKManager(this,
                    this);

            // Verify EMDK Manager
            if (emdkManagerResults == null ||
                    emdkManagerResults.statusCode != EMDKResults.STATUS_CODE.SUCCESS) {
                Log.e(TAG, "onCreate: Failed to get EMDK Manager -> " +
                        (emdkManagerResults == null ? "No Results Returned"
                                : emdkManagerResults.statusCode));
                Toast.makeText(this, "Failed to get EMDK Manager!", Toast.LENGTH_LONG)
                        .show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    /******************
     * EMDK Callbacks *
     ******************/

    @Override
    public void onOpened(EMDKManager emdkManager) {
        // Assign EMDK Reference
        mEmdkManager = emdkManager;

        // Get Profile & Version Manager Instances
        mProfileManager = (ProfileManager) mEmdkManager.getInstance(EMDKManager.FEATURE_TYPE.PROFILE);

        // Apply Profile
        if (mProfileManager != null) {
            applyLauncherToggleProfile();
        } else {
            Log.e(TAG, "Error Obtaining ProfileManager!");
            Toast.makeText(this, "Error Obtaining ProfileManager!", Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onClosed() {
        // Release EMDK Manager Instance
        if (mEmdkManager != null) {
            mEmdkManager.release();
            mEmdkManager = null;
        }
    }

    /*******************
     * Process Profile *
     *******************/

    private void applyLauncherToggleProfile() {
        String[] params = new String[1];
        if (mIsCustomState) {
            params[0] = App.DEFAULT_KEYBOARD_PROFILE_XML;
            new ProcessProfile(App.DEFAULT_KEYBOARD_PROFILE_NAME, mProfileManager, onProfileApplied)
                    .execute(params);
        } else {
            params[0] = App.NULL_KEYBOARD_PROFILE_XML;
            new ProcessProfile(App.NULL_KEYBOARD_PROFILE_NAME, mProfileManager, onProfileApplied)
                    .execute(params);
        }
    }

    private ProcessProfile.OnProfileApplied onProfileApplied = new ProcessProfile.OnProfileApplied() {
        @Override
        public void profileApplied() {
            // Notify User
            Toast.makeText(MainActivity.this, (mIsCustomState ?  "Default Keyboard Applied"
                    : "Null Keyboard Applied"), Toast.LENGTH_SHORT).show();

            // Update Pref
            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.putBoolean(LAUNCHER_STATE, !mIsCustomState);

            // Store Pref
            if (!editor.commit()) {
                Log.e(TAG, "Couldn't save state to shared prefs");
            }

            // Exit App
            finish();
        }

        @Override
        public void profileError() {
            Log.e(TAG, "Error Processing Profile!");
            Toast.makeText(MainActivity.this, "Error Applying Profile!",
                    Toast.LENGTH_SHORT).show();

            // Applied -> Exit App
            finish();
        }
    };
}