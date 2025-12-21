package com.incidenciasapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.text.get
import kotlin.text.set

class UserPreferences(private val context: Context) {

    companion object {
        private val Context.dataStore by preferencesDataStore(name = "user_prefs")

        val TOKEN = stringPreferencesKey("token")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_ROLE = stringPreferencesKey("user_role")
        val USER_ID = stringPreferencesKey("user_id")
    }

    // Guardar token
    suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN] = token
        }
    }

    // Leer token
    fun getToken(): Flow<String?> =
        context.dataStore.data.map { prefs ->
            prefs[TOKEN]
        }

    // Guardar usuario completo
    suspend fun saveUser(
        name: String,
        email: String,
        role: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
            prefs[USER_ROLE] = role
        }
    }

    // Borrar todo
    suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun saveUserId(userId: Int) {
        context.dataStore.edit { prefs ->
            prefs[USER_ID] = userId.toString()
        }
    }

    fun getUserId(): Flow<Int?> =
        context.dataStore.data.map { prefs ->
            prefs[USER_ID]?.toIntOrNull()
        }

    fun getUserName(): Flow<String?> =
        context.dataStore.data.map { it[USER_NAME] }

    fun getUserEmail(): Flow<String?> =
        context.dataStore.data.map { it[USER_EMAIL] }

    fun getUserRole(): Flow<String?> =
        context.dataStore.data.map { it[USER_ROLE] }
}
