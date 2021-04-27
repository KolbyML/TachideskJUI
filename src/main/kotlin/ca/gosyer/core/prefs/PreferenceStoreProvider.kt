/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package ca.gosyer.core.prefs

import ca.gosyer.common.prefs.PreferenceStore
import com.russhwolf.settings.JvmPreferencesSettings
import java.util.prefs.Preferences

class PreferenceStoreFactory {

    fun create(name: String? = null): PreferenceStore {
        val userPreferences: Preferences = Preferences.userRoot()
        val jvmPreferences = if (!name.isNullOrBlank()) {
            JvmPreferencesSettings(userPreferences.node(name))
        } else {
            JvmPreferencesSettings(userPreferences)
        }
        return JvmPreferenceStore(jvmPreferences)
    }
}
