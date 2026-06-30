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

package io.openauth.authenticator.core.security.antitamper

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TamperCheckAggregator @Inject constructor(
    private val rootDetector: RootDetector,
    private val emulatorDetector: EmulatorDetector,
    private val debuggerDetector: DebuggerDetector
) {
    data class Result(
        val rooted: Boolean,
        val emulator: Boolean,
        val debugger: Boolean
    ) {
        val compromised: Boolean get() = rooted || emulator || debugger
    }

    fun check(): Result {
        return Result(
            rooted = rootDetector.isRooted(),
            emulator = emulatorDetector.isEmulator(),
            debugger = debuggerDetector.isDebuggerAttached()
        )
    }
}