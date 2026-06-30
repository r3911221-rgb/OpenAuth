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

package io.openauth.authenticator.core.security.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.PersistableBundle
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureClipboardManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val clipboardManager: ClipboardManager? = context.getSystemService()

    fun copy(
        label: String,
        value: String,
        sensitive: Boolean = true
    ) {
        val clip = ClipData.newPlainText(label, value)

        if (sensitive && Build.VERSION.SDK_INT >= 33) {
            clip.description.extras = PersistableBundle().apply {
                putBoolean(
                    "android.content.extra.IS_SENSITIVE",
                    true
                )
            }
        }

        clipboardManager?.setPrimaryClip(clip)
    }

    fun clear() {
        clipboardManager?.clearPrimaryClip()
    }

    fun hasContent(): Boolean {
        return clipboardManager?.hasPrimaryClip() == true
    }
}