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

package io.openauth.authenticator.core.security.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricAvailability @Inject constructor(
    @ApplicationContext private val context: Context
) {
    enum class Status {
        AVAILABLE,
        NO_HARDWARE,
        NONE_ENROLLED,
        HARDWARE_UNAVAILABLE,
        UNSUPPORTED
    }

    fun status(): Status {
        return when (
            BiometricManager
                .from(context)
                .canAuthenticate(
                    BiometricManager.Authenticators.BIOMETRIC_STRONG
                )
        ) {
            BiometricManager.BIOMETRIC_SUCCESS -> Status.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Status.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Status.NONE_ENROLLED
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Status.HARDWARE_UNAVAILABLE
            else -> Status.UNSUPPORTED
        }
    }

    fun isAvailable(): Boolean = status() == Status.AVAILABLE
}