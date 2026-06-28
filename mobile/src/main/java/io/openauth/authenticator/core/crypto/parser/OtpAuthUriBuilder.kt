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

package io.openauth.authenticator.core.crypto.parser

import io.openauth.authenticator.core.crypto.otp.OtpAlgorithm
import io.openauth.authenticator.core.crypto.otp.OtpDigits
import io.openauth.authenticator.core.crypto.otp.OtpPeriod
import io.openauth.authenticator.core.crypto.otp.OtpType
import io.openauth.authenticator.core.crypto.util.Base32Encoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object OtpAuthUriBuilder {

    private fun encode(value: String): String {
        return URLEncoder
            .encode(value, StandardCharsets.UTF_8.name())
            .replace("+", "%20")
    }

    fun build(
        type: OtpType,
        secret: ByteArray,
        accountName: String,
        issuer: String? = null,
        algorithm: OtpAlgorithm = OtpAlgorithm.DEFAULT,
        digits: OtpDigits = OtpDigits.DEFAULT,
        period: OtpPeriod = OtpPeriod.DEFAULT,
        counter: Long = 0L
    ): String {
        require(accountName.isNotBlank())

        val secretCopy = secret.clone()

        try {
            val label = if (!issuer.isNullOrBlank()) {
                "${issuer.trim()}:$accountName"
            } else {
                accountName
            }

            val query = buildString {
                append("secret=")
                append(Base32Encoder.encode(secretCopy))
                append("&algorithm=")
                append(algorithm.name)
                append("&digits=")
                append(digits.value)

                if (!issuer.isNullOrBlank()) {
                    append("&issuer=")
                    append(encode(issuer.trim()))
                }

                when (type) {
                    OtpType.TOTP -> {
                        append("&period=")
                        append(period.seconds)
                    }
                    OtpType.HOTP -> {
                        append("&counter=")
                        append(counter)
                    }
                    OtpType.STEAM -> Unit
                }
            }

            return buildString {
                append("otpauth://")
                append(type.name.lowercase())
                append("/")
                append(encode(label))
                append("?")
                append(query)
            }
        } finally {
            secretCopy.secureWipe()
        }
    }
}