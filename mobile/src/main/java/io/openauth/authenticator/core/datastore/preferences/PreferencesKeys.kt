/*
 * Copyright (c) 2026 OpenAuth Authenticator
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.openauth.authenticator.core.datastore.preferences

import androidx.datastore.preferences.core.*

object PreferencesKeys {
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val SORT_ORDER = stringPreferencesKey("sort_order")
    val APP_LOCK_METHOD = stringPreferencesKey("app_lock_method")
    val AUTO_LOCK_TIMEOUT = stringPreferencesKey("auto_lock_timeout")
    val SCREENSHOT_BLOCKED = booleanPreferencesKey("screenshot_blocked")
    val CLIPBOARD_CLEAR_DELAY_MS = longPreferencesKey("clipboard_clear_delay_ms")
    val SHOW_OTP_DIGITS_GROUPED = booleanPreferencesKey("show_otp_digits_grouped")
    val COPY_ON_TAP = booleanPreferencesKey("copy_on_tap")
    val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
    val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    val LAST_SELECTED_GROUP_ID = stringPreferencesKey("last_selected_group_id")
    val BACKUP_REMINDER_INTERVAL_DAYS = intPreferencesKey("backup_reminder_interval_days")
}