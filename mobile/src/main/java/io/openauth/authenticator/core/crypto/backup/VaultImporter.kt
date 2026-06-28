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

package io.openauth.authenticator.core.crypto.backup

import io.openauth.authenticator.core.crypto.cipher.CipherProvider
import io.openauth.authenticator.core.crypto.exception.CryptoException
import io.openauth.authenticator.core.crypto.kdf.Argon2KeyDerivation
import io.openauth.authenticator.core.crypto.kdf.KdfAlgorithm
import io.openauth.authenticator.core.crypto.kdf.KeyDerivationFunction
import io.openauth.authenticator.core.crypto.kdf.Pbkdf2KeyDerivation
import io.openauth.authenticator.core.crypto.kdf.ScryptKeyDerivation
import io.openauth.authenticator.core.crypto.model.CryptoConstants
import javax.inject.Inject

class VaultImporter @Inject constructor(
    private val cipher: CipherProvider,
    private val pbkdf2: Pbkdf2KeyDerivation,
    private val scrypt: ScryptKeyDerivation,
    private val argon2: Argon2KeyDerivation
) {
    fun import(spec: BackupCipherSpec, password: CharArray): ByteArray {
        return try {
            spec.use { backup ->
                val kdf = resolveKdf(backup.kdfAlgorithm)
                val key = kdf.derive(
                    password, backup.salt,
                    CryptoConstants.AES_KEY_SIZE_BYTES
                )

                try {
                    cipher.decrypt(key, backup.encryptionResult)
                } finally {
                    key.fill(0)
                }
            }
        } catch (e: CryptoException) {
            throw e
        } catch (e: Exception) {
            throw CryptoException("Vault import failed", e)
        }
    }

    private fun resolveKdf(algorithm: KdfAlgorithm): KeyDerivationFunction {
        return when (algorithm) {
            KdfAlgorithm.PBKDF2 -> pbkdf2
            KdfAlgorithm.SCRYPT -> scrypt
            KdfAlgorithm.ARGON2ID -> argon2
        }
    }
}