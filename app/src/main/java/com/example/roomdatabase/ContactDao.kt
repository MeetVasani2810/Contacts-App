package com.example.roomdatabase

import android.icu.text.MessagePattern.ArgType.SELECT
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Upsert
    suspend fun upsertContact(contacts: Contacts)

    @Delete
    suspend fun deleteContact(contacts: Contacts)

    @Query("SELECT * FROM contacts ORDER by firstname ASC" )
    fun getContactsOrderedByFirstName(): Flow<List<Contacts>>

    @Query("SELECT * FROM contacts ORDER by lastname ASC" )
    fun getContactsOrderedByLastName(): Flow<List<Contacts>>

    @Query("SELECT * FROM contacts ORDER by phoneNumber ASC" )
    fun getContactsOrderedByPhoneNumber(): Flow<List<Contacts>>

}