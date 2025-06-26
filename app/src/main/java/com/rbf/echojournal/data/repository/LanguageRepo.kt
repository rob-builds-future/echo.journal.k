package com.rbf.echojournal.data.repository

import com.rbf.echojournal.data.remote.model.util.LanguageDto
import kotlinx.coroutines.flow.Flow

interface LanguageRepo {
    fun getLanguages(): Flow<List<LanguageDto>>
}
