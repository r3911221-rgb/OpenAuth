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

package io.openauth.authenticator.core.security.session

import io.openauth.authenticator.core.security.lock.AppLockManager
import io.openauth.authenticator.core.security.lock.AppLockTimeout
import io.openauth.authenticator.core.security.lock.LockState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionTimeoutObserver @Inject constructor(
    private val lockManager: AppLockManager
) {
    fun observe(timeout: AppLockTimeout): Flow<LockState> = flow {
        while (true) {
            emit(lockManager.currentState(timeout))
            delay(1000)
        }
    }
}