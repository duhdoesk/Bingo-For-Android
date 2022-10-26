package com.example.sorteadordebingo.data

import com.example.sorteadordebingo.model.Theme
import com.example.sorteadordebingo.util.JsonFileReader
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ThemeLocalDataSource @Inject constructor(private val jsonFileReader: JsonFileReader) {

    companion object {
        private const val THEMES_JSON = "themes.json"
    }

    fun loadThemes() : List<Theme> {
        val rawJson = jsonFileReader.readJsonAsset(THEMES_JSON)
            ?: return emptyList()

        val jsonReader = Json { isLenient = true }

        return jsonReader.decodeFromString(rawJson)
    }
}