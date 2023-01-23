/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.biometrics.activeunlock;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.preference.PreferenceScreen;

import com.android.settings.Utils;
import com.android.settings.biometrics.BiometricStatusPreferenceController;
import com.android.settingslib.RestrictedPreference;

/**
 * Preference controller for active unlock settings within the biometrics settings page, that
 * controls the ability to unlock the phone with watch authentication.
 */
public class ActiveUnlockStatusPreferenceController
        extends BiometricStatusPreferenceController implements LifecycleObserver {
    /**
     * Preference key.
     *
     * This must match the key found in security_settings_combined_biometric.xml
     **/
    public static final String KEY_ACTIVE_UNLOCK_SETTINGS = "biometric_active_unlock_settings";
    @Nullable private RestrictedPreference mPreference;
    @Nullable private PreferenceScreen mPreferenceScreen;
    private final ActiveUnlockStatusUtils mActiveUnlockStatusUtils;

    public ActiveUnlockStatusPreferenceController(@NonNull Context context) {
        this(context, KEY_ACTIVE_UNLOCK_SETTINGS);
    }

    public ActiveUnlockStatusPreferenceController(
            @NonNull Context context, @NonNull String key) {
        super(context, key);
        mActiveUnlockStatusUtils = new ActiveUnlockStatusUtils(context);
    }

    /** Resets the preference reference on resume. */
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        if (mPreferenceScreen != null) {
            displayPreference(mPreferenceScreen);
        }
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mPreferenceScreen = screen;
        mPreference = screen.findPreference(mPreferenceKey);
        updateState(mPreference);
    }

    @Override
    public int getAvailabilityStatus() {
        return mActiveUnlockStatusUtils.getAvailability();
    }

    @Override
    protected boolean isDeviceSupported() {
        // This should never be called, as getAvailabilityStatus() will return the exact value.
        // However, this is an abstract method in BiometricStatusPreferenceController, and so
        // needs to be overridden.
        return mActiveUnlockStatusUtils.isAvailable();
    }

    @Override
    protected boolean isHardwareSupported() {
        // This should never be called, as getAvailabilityStatus() will return the exact value.
        // However, this is an abstract method in BiometricStatusPreferenceController, and so
        // needs to be overridden.
        return Utils.hasFaceHardware(mContext) || Utils.hasFingerprintHardware(mContext);
    }

    @Override
    protected String getSummaryText() {
        // TODO(b/264812018): set the summary from the ContentProvider
        return "";
    }

    @Override
    protected String getSettingsClassName() {
        // TODO(b/264813445): direct user to face & fingerprint setup
        return null;
    }
}
