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

package io.openauth.authenticator.core.crypto.util

import java.util.Locale

object Base32Decoder {

    private const val ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567"

    private val TABLE = IntArray(128) { -1 }.apply {
        ALPHABET.forEachIndexed { index, char ->
            this[char.code] = index
            this[char.lowercaseChar().code] = index
        }
    }

    fun decode(input: String): ByteArray {
        val clean = input
            .uppercase(Locale.ROOT)
            .filterNot { it.isWhitespace() || it == '=' }

        require(clean.isNotEmpty()) { "Base32 secret cannot be empty" }
        require(clean.all { it.code < 128 && TABLE[it.code] >= 0 }) {
            "Invalid Base32 secret"
        }

        val output = ByteArray(clean.length * 5 / 8)
        var buffer = 0
        var bits = 0
        var index = 0

        for (char in clean) {
            buffer = (buffer shl 5) or TABLE[char.code]
            bits += 5

            if (bits >= 8) {
                bits -= 8
                output[index++] = ((buffer shr bits) and 0xFF).toByte()
            }
        }

        if (bits > 0) {
            val remainingMask = (1 shl bits) - 1
            require((buffer and remainingMask) == 0) {
                "Invalid Base32 padding"
            }
        }

        return output.copyOf(index)
    }
}