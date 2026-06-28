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

package io.openauth.authenticator.core.crypto.otp

import io.openauth.authenticator.core.crypto.hashing.HmacProvider
import javax.inject.Inject

class HotpGenerator @Inject constructor(
    private val hmacProvider: HmacProvider
) : OtpGenerator {

    override fun generate(
        secret: ByteArray,
        counter: Long,
        digits: OtpDigits,
        algorithm: OtpAlgorithm
    ): String {
        require(secret.isNotEmpty()) { "OTP secret cannot be empty" }
        require(counter >= 0) { "OTP counter cannot be negative" }

        val counterBytes = counter.toBigEndianBytes()
        val hash = hmacProvider.compute(
            algorithm = algorithm,
            key = secret,
            message = counterBytes
        )

        val binaryCode = dynamicTruncate(hash)
        val otp = binaryCode.toLong() % POW10[digits.value]

        return otp.toString()
            .padStart(digits.value, '0')
    }

    private fun dynamicTruncate(hash: ByteArray): Int {
        require(hash.size >= 16) {
            "HMAC output too short for dynamic truncation"
        }

        val offset = hash.last().toInt() and 0x0F

        require(offset + 3 < hash.size) {
            "Invalid dynamic truncation offset"
        }

        return ((hash[offset].toInt() and 0x7F) shl 24) or
               ((hash[offset + 1].toInt() and 0xFF) shl 16) or
               ((hash[offset + 2].toInt() and 0xFF) shl 8) or
               (hash[offset + 3].toInt() and 0xFF)
    }

    private fun Long.toBigEndianBytes(): ByteArray {
        return ByteArray(8) { index ->
            ((this ushr (56 - index * 8)) and 0xFF).toByte()
        }
    }

    companion object {
        private val POW10 = longArrayOf(
            1, 10, 100, 1_000, 10_000,
            100_000, 1_000_000, 10_000_000, 100_000_000
        )
    }
}