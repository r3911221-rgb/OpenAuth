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

package io.openauth.authenticator.core.security.model

object SecurityConstants {
    const val MAX_PIN_FAILED_ATTEMPTS = 5
    const val BASE_LOCK_DURATION_SECONDS = 5L
    const val MAX_LOCK_DURATION_SECONDS = 1800L
    const val SESSION_TIMEOUT_SECONDS = 300L
    const val CLIPBOARD_CLEAR_DELAY_MS = 30_000L
    const val BIOMETRIC_KEY_ALIAS = "openauth_biometric_master_key"
    const val STRICT_SECURITY_MODE = true
}