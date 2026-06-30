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

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.openauth.authenticator.core.database.dao.AccountDao
import org.junit.After
import org.junit.Before
import org.junit.Test

class AccountDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: AccountDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        )
        .allowMainThreadQueries()
        .build()

        dao = database.accountDao()
    }

    @After
    fun close() {
        database.close()
    }

    @Test
    fun databaseCreated() {
        assert(database.isOpen)
    }
}