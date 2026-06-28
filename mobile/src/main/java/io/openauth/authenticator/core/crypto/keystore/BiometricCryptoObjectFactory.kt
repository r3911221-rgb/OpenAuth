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

import androidx.biometric.BiometricPrompt
import io.openauth.authenticator.core.crypto.model.CryptoConstants
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

class BiometricCryptoObjectFactory @Inject constructor(
    private val keystoreManager: AndroidKeystoreManager
) {
    fun createEncryptionObject(): BiometricPrompt.CryptoObject {
        val key = keystoreManager.getOrCreateKey(
            KeyAlias.BIOMETRIC_KEY,
            requireAuthentication = true
        )

        val cipher = Cipher.getInstance(CryptoConstants.AES_GCM_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        return BiometricPrompt.CryptoObject(cipher)
    }

    fun createDecryptionObject(iv: ByteArray): BiometricPrompt.CryptoObject {
        require(iv.size == CryptoConstants.GCM_IV_SIZE_BYTES) {
            "Invalid GCM IV size"
        }

        val key = keystoreManager.getOrCreateKey(
            KeyAlias.BIOMETRIC_KEY,
            requireAuthentication = true
        )

        val cipher = Cipher.getInstance(CryptoConstants.AES_GCM_TRANSFORMATION)
        cipher.init(
            Cipher.DECRYPT_MODE,
            key,
            GCMParameterSpec(CryptoConstants.GCM_TAG_SIZE_BITS, iv.copyOf())
        )

        return BiometricPrompt.CryptoObject(cipher)
    }
}