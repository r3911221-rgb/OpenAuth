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

package io.openauth.authenticator.core.crypto.kdf

import io.openauth.authenticator.core.crypto.util.toUtf8ByteArray
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters
import java.util.Arrays
import javax.inject.Inject

class Argon2KeyDerivation @Inject constructor() : KeyDerivationFunction {

    override val algorithm: KdfAlgorithm = KdfAlgorithm.ARGON2ID

    override fun derive(
        password: CharArray,
        salt: ByteArray,
        keyLengthBytes: Int
    ): ByteArray {
        require(keyLengthBytes in MIN_KEY_BYTES..MAX_KEY_BYTES) {
            "Invalid key length"
        }
        require(salt.size >= MIN_SALT_BYTES) { "Invalid salt" }

        val passwordBytes = password.toUtf8ByteArray()
        val saltCopy = salt.copyOf()

        return try {
            val parameters = Argon2Parameters.Builder(
                Argon2Parameters.ARGON2_id
            )
            .withVersion(Argon2Parameters.ARGON2_VERSION_13)
            .withIterations(ITERATIONS)
            .withMemoryAsKB(MEMORY_KB)
            .withParallelism(PARALLELISM)
            .withSalt(saltCopy)
            .build()

            Argon2BytesGenerator()
                .apply { init(parameters) }
                .let { generator ->
                    ByteArray(keyLengthBytes).also {
                        generator.generateBytes(passwordBytes, it)
                    }
                }
        } finally {
            Arrays.fill(passwordBytes, 0)
            Arrays.fill(saltCopy, 0)
        }
    }

    private companion object {
        const val ITERATIONS = 3
        const val MEMORY_KB = 65536
        const val PARALLELISM = 4
        const val MIN_KEY_BYTES = 16
        const val MAX_KEY_BYTES = 64
        const val MIN_SALT_BYTES = 16
    }
}