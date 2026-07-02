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

package io.openauth.authenticator.core.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.openauth.authenticator.core.ui.theme.*

@Composable
fun OtpCodeChip(
    code: String,
    remainingFraction: Float,
    onCopy: () -> Unit,
    onLongPress: () -> Unit = {},
    isDimmed: Boolean = false,
    modifier: Modifier = Modifier,
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isDimmed -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            remainingFraction < 0.15f -> ErrorRed.copy(alpha = 0.15f)
            remainingFraction < 0.30f -> WarningAmber.copy(alpha = 0.15f)
            else -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        },
        label = "OtpBackground"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isDimmed -> MaterialTheme.colorScheme.onSurfaceVariant
            remainingFraction < 0.15f -> ErrorRed
            remainingFraction < 0.30f -> WarningAmber
            else -> MaterialTheme.colorScheme.primary
        },
        label = "OtpTextColor"
    )

    val scale by animateFloatAsState(
        targetValue = if (remainingFraction < 0.15f && !isDimmed) 1.02f else 1f,
        animationSpec = spring(stiffness = 300f),
        label = "OtpScale"
    )

    val formattedCode = formatCodeWithSpacing(code)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onCopy
            )
            .padding(horizontal = 20.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = formattedCode,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    letterSpacing = 4.sp,
                ),
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp),
            )

            Spacer(modifier = Modifier.height(4.dp))

            CountdownProgressRing(
                fraction = remainingFraction,
                size = 28.dp,
                strokeWidth = 2.5.dp,
                color = textColor,
            )
        }
    }
}

private fun formatCodeWithSpacing(code: String): String {
    return when (code.length) {
        6 -> "${code.substring(0, 3)} ${code.substring(3, 6)}"
        7 -> "${code.substring(0, 3)} ${code.substring(3, 7)}"
        8 -> "${code.substring(0, 4)} ${code.substring(4, 8)}"
        else -> code
    }
}