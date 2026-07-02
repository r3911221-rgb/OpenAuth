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

package io.openauth.authenticator.core.ui.util

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HapticFeedbackHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val vibrator: Vibrator? by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            manager?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    fun lightTap() {
        vibrate(VibrationEffect.EFFECT_TICK)
    }

    fun mediumTap() {
        vibrate(VibrationEffect.EFFECT_CLICK)
    }

    fun heavyTap() {
        vibrate(VibrationEffect.EFFECT_HEAVY_CLICK)
    }

    fun success() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrate(VibrationEffect.EFFECT_DOUBLE_CLICK)
        } else {
            vibrate(VibrationEffect.EFFECT_CLICK)
        }
    }

    fun error() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            vibrate(VibrationEffect.EFFECT_THUD)
        } else {
            vibrate(VibrationEffect.EFFECT_DOUBLE_CLICK)
        }
    }

    private fun vibrate(effectId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator?.vibrate(VibrationEffect.createPredefined(effectId))
        } else {
            @Suppress("DEPRECATION")
            vibrator?.vibrate(50)
        }
    }
}