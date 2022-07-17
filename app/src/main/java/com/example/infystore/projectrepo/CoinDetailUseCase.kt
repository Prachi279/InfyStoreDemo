package com.example.infystore.projectrepo

import com.example.cryptocurrencydemo.common.Resource
import com.example.cryptocurrencydemo.data.remote.dto.APIInterface
import com.example.cryptocurrencydemo.domain.model.CoinDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class CoinDetailUseCase @Inject constructor(private val apiInterface: APIInterface) {
    operator fun invoke(coinId:String): Flow<Resource<CoinDetail>> = flow {
        try {
            emit(Resource.Loading<CoinDetail>())
            val coinDetail = apiInterface.getCoinById(coinId)
            emit(Resource.Success<CoinDetail>(coinDetail))
        } catch (e: HttpException) {
            emit(Resource.Error<CoinDetail>(e.localizedMessage ?: "An unexpected error occured"))
        } catch (e: IOException) {
            emit(Resource.Error<CoinDetail>("Check your internet connection"))
        }
    }
}