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

package io.openauth.authenticator.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.openauth.authenticator.core.database.converter.InstantTypeConverter
import io.openauth.authenticator.core.database.converter.OtpAlgorithmConverter
import io.openauth.authenticator.core.database.converter.StringListConverter
import io.openauth.authenticator.core.database.dao.AccountDao
import io.openauth.authenticator.core.database.dao.BackupHistoryDao
import io.openauth.authenticator.core.database.dao.GroupDao
import io.openauth.authenticator.core.database.dao.IconDao
import io.openauth.authenticator.core.database.dao.TagDao
import io.openauth.authenticator.core.database.entity.AccountEntity
import io.openauth.authenticator.core.database.entity.AccountGroupCrossRef
import io.openauth.authenticator.core.database.entity.AccountTagCrossRef
import io.openauth.authenticator.core.database.entity.BackupHistoryEntity
import io.openauth.authenticator.core.database.entity.GroupEntity
import io.openauth.authenticator.core.database.entity.IconEntity
import io.openauth.authenticator.core.database.entity.TagEntity

@Database(
    entities = [
        AccountEntity::class,
        TagEntity::class,
        GroupEntity::class,
        IconEntity::class,
        BackupHistoryEntity::class,
        AccountTagCrossRef::class,
        AccountGroupCrossRef::class
    ],
    version = 6,
    exportSchema = true
)
@TypeConverters(
    InstantTypeConverter::class,
    OtpAlgorithmConverter::class,
    StringListConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun tagDao(): TagDao
    abstract fun groupDao(): GroupDao
    abstract fun iconDao(): IconDao
    abstract fun backupHistoryDao(): BackupHistoryDao

    companion object {
        const val NAME = "openauth_secure.db"
    }
}