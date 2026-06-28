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

import android.security.keystore.StrongBoxUnavailableException
import io.openauth.authenticator.core.crypto.model.CryptoConstants
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidKeystoreManager @Inject constructor(
    private val specFactory: KeyGenerationSpecFactory
) {
    private val keyStore: KeyStore = KeyStore
        .getInstance(CryptoConstants.ANDROID_KEYSTORE_PROVIDER)
        .apply { load(null) }

    @Synchronized
    fun hasKey(alias: KeyAlias): Boolean {
        return keyStore.containsAlias(alias.value)
    }

    @Synchronized
    fun getOrCreateKey(
        alias: KeyAlias,
        requireAuthentication: Boolean = false
    ): SecretKey {
        getExistingKey(alias)?.let { return it }
        return createKey(alias, requireAuthentication)
    }

    @Synchronized
    fun deleteKey(alias: KeyAlias) {
        if (hasKey(alias)) {
            keyStore.deleteEntry(alias.value)
        }
    }

    private fun getExistingKey(alias: KeyAlias): SecretKey? {
        val entry = keyStore.getEntry(alias.value, null)
        return (entry as? KeyStore.SecretKeyEntry)?.secretKey
    }

    private fun createKey(
        alias: KeyAlias,
        requireAuthentication: Boolean
    ): SecretKey {
        return try {
            generate(alias, requireAuthentication, true)
        } catch (_: StrongBoxUnavailableException) {
            generate(alias, requireAuthentication, false)
        }
    }

    private fun generate(
        alias: KeyAlias,
        requireAuthentication: Boolean,
        strongBox: Boolean
    ): SecretKey {
        return KeyGenerator.getInstance(
            CryptoConstants.AES_ALGORITHM,
            CryptoConstants.ANDROID_KEYSTORE_PROVIDER
        ).apply {
            init(specFactory.create(alias, requireAuthentication, strongBox))
        }.generateKey()
    }
}