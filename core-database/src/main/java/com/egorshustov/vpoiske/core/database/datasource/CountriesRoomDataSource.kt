package com.egorshustov.vpoiske.core.database.datasource

import com.egorshustov.vpoiske.core.common.network.AppDispatchers.IO
import com.egorshustov.vpoiske.core.common.network.Dispatcher
import com.egorshustov.vpoiske.core.database.dao.CountryDao
import com.egorshustov.vpoiske.core.database.model.CountryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CountriesRoomDataSource @Inject constructor(
    private val countryDao: CountryDao,
    @Dispatcher(IO) private val ioDispatcher: CoroutineDispatcher
) : CountriesDatabaseDataSource {

    override fun getCountriesStream(): Flow<List<CountryEntity>> =
        countryDao.getCountriesStream().flowOn(ioDispatcher)

    override suspend fun upsertCountries(entities: List<CountryEntity>) =
        withContext(ioDispatcher) { countryDao.upsertCountries(entities) }
}