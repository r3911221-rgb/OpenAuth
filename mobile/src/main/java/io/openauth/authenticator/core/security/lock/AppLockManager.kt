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

import java.time.Clock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLockManager @Inject constructor(
    private val hasher: PinHasher,
    private val validator: PinValidator,
    private val tracker: FailedAttemptTracker,
    private val policy: LockoutPolicy,
    private val timer: AutoLockTimer,
    private val storage: PinStorage,
    private val clock: Clock,
) {
    suspend fun setPin(pin: CharArray): PinValidationResult {
        val result = validator.validate(pin)
        if (result == PinValidationResult.Valid) {
            storage.setHashedPin(hasher.hash(pin))
            tracker.reset()
            storage.setLockoutEndsAt(null)
        } else {
            pin.fill('\u0000')
        }
        return result
    }

    suspend fun getState(timeout: AppLockTimeout): LockState {
        storage.getLockoutEndsAt()?.let { end ->
            val remaining = java.time.Duration.between(clock.instant(), end)
            return when {
                !remaining.isPositive -> {
                    storage.setLockoutEndsAt(null)
                    LockState.Unlocked
                }
                else -> LockState.TemporarilyLocked(remaining)
            }
        }

        return if (timer.isLockExpired(timeout)) {
            LockState.Locked
        } else {
            LockState.Unlocked
        }
    }

    suspend fun unlock(pin: CharArray): Boolean {
        val state = getState(AppLockTimeout.NEVER)

        if (state is LockState.TemporarilyLocked) {
            pin.fill('\u0000')
            return false
        }

        val stored = storage.getHashedPin() ?: run {
            pin.fill('\u0000')
            return false
        }

        val success = hasher.verify(pin, stored)

        if (success) {
            tracker.reset()
            storage.setLockoutEndsAt(null)
            timer.onForegrounded()
        } else {
            val attempts = tracker.recordFailure()
            val lockDuration = policy.lockoutDurationFor(attempts)

            if (!lockDuration.isZero) {
                storage.setLockoutEndsAt(clock.instant().plus(lockDuration))
            }
        }

        return success
    }
}