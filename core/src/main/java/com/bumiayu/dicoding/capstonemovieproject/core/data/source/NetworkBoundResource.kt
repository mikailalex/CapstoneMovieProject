package com.bumiayu.dicoding.capstonemovieproject.core.data.source

import com.bumiayu.dicoding.capstonemovieproject.core.data.source.remote.network.ApiResponse
import com.bumiayu.dicoding.capstonemovieproject.core.domain.model.Resource
import kotlinx.coroutines.flow.*

abstract class NetworkBoundResource<ResultType, RequestType> {

    private var result: Flow<Resource<ResultType>> = flow {
        emit(Resource.Loading())
        val dbSource = loadFromDB().first()
        if (shouldFetch(dbSource)) {
            emit(Resource.Loading())
            when (val apiResponse = createCall().first()) {
                is ApiResponse.Success -> {
                    saveCallResult(apiResponse.data)
                    emitAll(loadFromDB().map {
                        Resource.Success(
                            it
                        )
                    })
                }
                is ApiResponse.Empty -> {
                    emitAll(loadFromDB().map {
                        Resource.Success(
                            it
                        )
                    })
                }
                is ApiResponse.Error -> {
                    onFetchFailed()
                    emit(
                        //Generic type is needed, don't delete
                        Resource.Error<ResultType>(
                            apiResponse.errorMessage
                        )
                    )
                }
            }
        } else {
            emitAll(loadFromDB().map {
                Resource.Success(
                    it
                )
            })
        }
    }

    protected open fun onFetchFailed() {}

    protected abstract fun loadFromDB(): Flow<ResultType?>

    protected abstract fun shouldFetch(data: ResultType?): Boolean

    protected abstract suspend fun createCall(): Flow<ApiResponse<RequestType>>

    protected abstract suspend fun saveCallResult(data: RequestType)

    fun asFlow(): Flow<Resource<ResultType>> = result
}

//abstract class NetworkBoundResource<ResultType, RequestType> {
//
//    private val result = MediatorLiveData<Resource<ResultType>>()
//
//    init {
//        result.value = Resource.Loading(null)
//
//        @Suppress("LeakingThis")
//        val dbSource = loadFromDB()
//        result.addSource(dbSource) { data ->
//            result.removeSource(dbSource)
//            if (shouldFetch(data)) {
//                fetcFromNetwork(dbSource)
//            } else {
//                result.addSource(dbSource) { newData ->
//                    result.value = Resource.Success(newData)
//                }
//            }
//        }
//    }
//
//    protected open fun onFetchFailed() {}
//
//    protected abstract fun loadFromDB(): LiveData<ResultType>
//
//    protected abstract fun shouldFetch(data: ResultType?): Boolean
//
//    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
//
//    protected abstract suspend fun saveCallResult(data: RequestType)
//
//    private fun fetcFromNetwork(dbSource: LiveData<ResultType>) {
//        val apiResponse = createCall()
//
//        result.addSource(dbSource) { newData ->
//            result.value = Resource.Loading(newData)
//        }
//        result.addSource(apiResponse) { response ->
//            result.removeSource(apiResponse)
//            result.removeSource(dbSource)
//            when (response) {
//                is ApiResponse.Success ->
//                    CoroutineScope(Dispatchers.IO).launch {
//                        response.data?.let { saveCallResult(it) }
//
//                        withContext(Dispatchers.Main) {
//                            result.addSource(loadFromDB()) { newData ->
//                                result.value = Resource.Success(newData)
//                            }
//                        }
//                    }
//                is ApiResponse.Error -> {
//                    onFetchFailed()
//                    CoroutineScope(Dispatchers.Main).launch {
//                        result.addSource(loadFromDB()) { oldData ->
//                            result.value = Resource.Error(response.errorMessage, oldData)
//                        }
//                    }
//                }
//                else -> result.value = Resource.Error("Something Error", null)
//            }
//        }
//    }
//
//    fun asLiveData(): LiveData<Resource<ResultType>> = result
//}