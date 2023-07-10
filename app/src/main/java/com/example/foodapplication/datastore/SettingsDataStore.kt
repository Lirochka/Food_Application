package com.example.foodapplication.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodapplication.di.BaseApplication
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Singleton
class SettingsDataStore
@Inject
constructor(app: BaseApplication) {

    private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private val datastore: DataStore<Preferences> = app.settingsDataStore

    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        observeDataStore()
    }

    val isDark = mutableStateOf(false)

    fun toggleTheme() {
        scope.launch {
            datastore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY] ?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    private fun observeDataStore() {
        datastore.data.onEach { preferences ->
            preferences[DARK_THEME_KEY]?.let { isDarkTheme ->
                isDark.value = isDarkTheme
            }
        }.launchIn(scope)
    }

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
    }
}