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

package io.openauth.authenticator.core.security.lock

import io.openauth.authenticator.core.crypto.kdf.Argon2KeyDerivation
import io.openauth.authenticator.core.crypto.random.SecureRandomProvider
import io.openauth.authenticator.core.crypto.util.constantTimeEquals
import io.openauth.authenticator.core.crypto.util.hexToByteArray
import io.openauth.authenticator.core.crypto.util.toHex
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinHasher @Inject constructor(
    private val argon2: Argon2KeyDerivation,
    private val random: SecureRandomProvider,
) {
    private companion object {
        const val SALT_SIZE = 16
        const val KEY_SIZE = 32
    }

    fun hash(pin: CharArray): HashedPin {
        val salt = random.nextBytes(SALT_SIZE)
        return try {
            val hash = argon2.derive(pin, salt, KEY_SIZE)
            HashedPin(
                hash = hash.toHex(),
                salt = salt.toHex()
            )
        } finally {
            pin.fill('\u0000')
            salt.fill(0)
        }
    }

    fun verify(pin: CharArray, stored: HashedPin): Boolean {
        val salt = stored.salt.hexToByteArray()
        val expected = stored.hash.hexToByteArray()
        return try {
            val candidate = argon2.derive(pin, salt, KEY_SIZE)
            candidate.constantTimeEquals(expected)
        } finally {
            pin.fill('\u0000')
        }
    }
}

data class HashedPin(
    val hash: String,
    val salt: String
)