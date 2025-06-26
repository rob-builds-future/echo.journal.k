package com.rbf.echojournal.data.repository

import com.rbf.echojournal.data.remote.LibreTranslateService
import com.rbf.echojournal.data.remote.model.util.LanguageDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LanguageRepoImpl(
    private val api: LibreTranslateService
) : LanguageRepo {
    override fun getLanguages(): Flow<List<LanguageDto>> = flow {
        emit(api.getLanguages())
    }
}