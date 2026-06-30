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

package io.openauth.authenticator.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "icons")
data class IconEntity(
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "issuer")
    val issuer: String,

    @ColumnInfo(name = "mime_type")
    val mimeType: String,

    @ColumnInfo(name = "encrypted_data")
    val encryptedData: ByteArray,

    @ColumnInfo(name = "source")
    val source: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is IconEntity) return false
        return id == other.id &&
               encryptedData.contentEquals(other.encryptedData)
    }

    override fun hashCode(): Int {
        return 31 * id.hashCode() + encryptedData.contentHashCode()
    }
}