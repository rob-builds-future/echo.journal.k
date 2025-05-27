package com.example.echojournal.data.repository

import com.example.echojournal.data.remote.model.util.LanguageDto
import kotlinx.coroutines.flow.Flow

interface LanguageRepo {
    fun getLanguages(): Flow<List<LanguageDto>>
}
