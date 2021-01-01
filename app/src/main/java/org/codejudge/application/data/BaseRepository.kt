package org.codejudge.application.data


import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.keffys.android.domain.exceptions.MyException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

open class BaseRepository {

    suspend fun <R> either(service : suspend () -> R): Either<MyException, R> {

        return try {
            val response = service.invoke()
            Either.Right(response)
        } catch (e: Exception) {
            e.printStackTrace()
            Either.Left(transformException(e))
        }
    }


    private fun transformException(e: Exception): MyException {
        if (e is HttpException) {
            when (e.code()) {
                422,
                502 -> return MyException.JsonException(e)
                500 -> return MyException.JsonException(e)
                404 -> return MyException.ServerNotAvailableException(e)
                else -> return MyException.UnknownException(e)
            }
        } else {
            if (e is CancellationException) {
                return MyException.NetworkCallCancelException(e)
            } else if (e is ConnectException || e is UnknownHostException || e is SocketTimeoutException || e is SocketException) {
                return MyException.InternetConnectionException(e)
            } else if (e is IllegalStateException || e is JsonSyntaxException || e is MalformedJsonException) {
                return MyException.JsonException(e)
            }
        }
        return MyException.UnknownException(e)
    }


}