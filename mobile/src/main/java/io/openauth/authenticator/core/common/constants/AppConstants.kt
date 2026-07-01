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

package io.openauth.authenticator.core.common.constants

object AppConstants {
    const val APP_NAME = "OpenAuth"
    const val DATABASE_NAME = "openauth.db"
    const val BACKUP_FILE_EXTENSION = ".openauth"
    const val OTP_REFRESH_INTERVAL_MS = 500L
    const val SEARCH_DEBOUNCE_MS = 300L
    const val CLIPBOARD_CLEAR_DELAY_MS = 30_000L
    const val NOTIFICATION_CHANNEL_ID = "openauth_notifications"
}