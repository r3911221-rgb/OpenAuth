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

package io.openauth.authenticator.core.model

import java.time.Instant

data class Account(
    val id: String,
    val issuer: String?,
    val accountName: String,
    val secret: String,
    val type: AccountType,
    val algorithm: OtpAlgorithmType,
    val digits: Int,
    val periodSeconds: Long,
    val counter: Long,
    val iconId: String?,
    val isFavorite: Boolean,
    val sortPosition: Int,
    val createdAt: Instant,
    val lastUsedAt: Instant?,
    val usageCount: Long,
    val note: String?,
)