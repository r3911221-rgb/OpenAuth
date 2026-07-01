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

package io.openauth.authenticator.core.common.logging

import android.util.Log
import javax.inject.Inject

class AppLogger @Inject constructor() {
    fun d(tag: String, message: String) { if (isDebug()) Log.d(tag, message) }
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (throwable != null) Log.e(tag, message, throwable) else Log.e(tag, message)
    }
    fun w(tag: String, message: String) { Log.w(tag, message) }
    fun i(tag: String, message: String) { Log.i(tag, message) }
    private fun isDebug(): Boolean = Log.isLoggable("OpenAuth", Log.DEBUG)
}