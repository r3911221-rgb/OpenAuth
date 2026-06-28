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

package io.openauth.authenticator.core.crypto.cipher

import io.openauth.authenticator.core.crypto.model.CryptoConstants
import io.openauth.authenticator.core.crypto.random.SecureRandomProvider
import java.security.GeneralSecurityException
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AesGcmCipher @Inject constructor(
    private val random: SecureRandomProvider
) : CipherProvider {

    override fun encrypt(key: ByteArray, plaintext: ByteArray): EncryptionResult {
        require(key.size == CryptoConstants.AES_KEY_SIZE_BYTES)

        val keyCopy = key.clone()

        try {
            val iv = random.nextBytes(CryptoConstants.GCM_IV_SIZE_BYTES)
            val cipher = Cipher.getInstance(CryptoConstants.AES_GCM_TRANSFORMATION)
            cipher.init(
                Cipher.ENCRYPT_MODE,
                SecretKeySpec(keyCopy, CryptoConstants.AES_ALGORITHM),
                GCMParameterSpec(CryptoConstants.GCM_TAG_SIZE_BITS, iv)
            )

            return EncryptionResult(
                ciphertext = cipher.doFinal(plaintext),
                iv = iv
            )
        } finally {
            keyCopy.secureWipe()
        }
    }

    override fun decrypt(key: ByteArray, result: EncryptionResult): ByteArray {
        require(key.size == CryptoConstants.AES_KEY_SIZE_BYTES)

        val keyCopy = key.clone()

        try {
            val cipher = Cipher.getInstance(CryptoConstants.AES_GCM_TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                SecretKeySpec(keyCopy, CryptoConstants.AES_ALGORITHM),
                GCMParameterSpec(CryptoConstants.GCM_TAG_SIZE_BITS, result.iv)
            )

            return cipher.doFinal(result.ciphertext)
        } catch (e: GeneralSecurityException) {
            throw DecryptionException(cause = e)
        } finally {
            keyCopy.secureWipe()
        }
    }
}