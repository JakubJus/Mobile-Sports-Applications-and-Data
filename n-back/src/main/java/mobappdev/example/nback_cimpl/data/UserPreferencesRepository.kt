package mobappdev.example.nback_cimpl.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val HIGHSCORE = intPreferencesKey("highscore")
        val NBACK = intPreferencesKey("nback")
        val SIZE_OF_GAME = intPreferencesKey("sizeOfGame")
        val MAX_SIZE = intPreferencesKey("maxSize")
        const val TAG = "UserPreferencesRepo"
    }

    val highscore: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[HIGHSCORE] ?: 0
        }

    val nback: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[NBACK] ?: 2  // Default to 2 if no value exists
        }

    val sizeOfGame: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[SIZE_OF_GAME] ?: 3  // Default size
        }

    val maxSize: Flow<Int> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[MAX_SIZE] ?: 5  // Default max size
        }

    suspend fun saveHighScore(score: Int) {
        dataStore.edit { preferences ->
            preferences[HIGHSCORE] = score
        }
    }

    suspend fun saveNback(nbackValue: Int) {
        dataStore.edit { preferences ->
            preferences[NBACK] = nbackValue
        }
    }

    suspend fun saveSizeOfGame(size: Int) {
        dataStore.edit { preferences ->
            preferences[SIZE_OF_GAME] = size
        }
    }

    suspend fun saveMaxSize(maxSizeValue: Int) {
        dataStore.edit { preferences ->
            preferences[MAX_SIZE] = maxSizeValue
        }
    }
}
