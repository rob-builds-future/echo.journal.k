package com.example.echojournal.data.repository

import com.example.echojournal.data.remote.LibreTranslateService
import com.example.echojournal.data.remote.model.util.LanguageDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LanguageRepoImpl(
    private val api: LibreTranslateService
) : LanguageRepo {
    override fun getLanguages(): Flow<List<LanguageDto>> = flow {
        emit(api.getLanguages())
    }
}