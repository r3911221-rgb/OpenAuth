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

package io.openauth.authenticator.core.crypto.keystore

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import io.openauth.authenticator.core.crypto.model.CryptoConstants
import javax.inject.Inject

class KeyGenerationSpecFactory @Inject constructor() {

    fun create(
        alias: KeyAlias,
        requireAuthentication: Boolean,
        preferStrongBox: Boolean
    ): KeyGenParameterSpec {
        return KeyGenParameterSpec.Builder(
            alias.value, KEY_PURPOSES
        )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(CryptoConstants.AES_KEY_SIZE_BYTES * 8)
        .setRandomizedEncryptionRequired(true)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setUnlockedDeviceRequired(true)
            }
            configureAuthentication(this, requireAuthentication)
            if (preferStrongBox &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                setIsStrongBoxBacked(true)
            }
        }
        .build()
    }

    private fun configureAuthentication(
        builder: KeyGenParameterSpec.Builder,
        enabled: Boolean
    ) {
        if (!enabled) return

        builder.setUserAuthenticationRequired(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            builder.setUserAuthenticationParameters(
                AUTH_TIMEOUT_SECONDS,
                KeyProperties.AUTH_BIOMETRIC_STRONG
            )
        } else {
            @Suppress("DEPRECATION")
            builder.setUserAuthenticationValidityDurationSeconds(
                AUTH_TIMEOUT_SECONDS
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setInvalidatedByBiometricEnrollment(true)
        }
    }

    private companion object {
        const val AUTH_TIMEOUT_SECONDS = -1
        val KEY_PURPOSES = KeyProperties.PURPOSE_ENCRYPT or
                           KeyProperties.PURPOSE_DECRYPT
    }
}