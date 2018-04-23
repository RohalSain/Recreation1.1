package com.example.emilence.recreationcenter

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by emilence on 21/3/18.
 */
class BasicAuthInterceptor(user:String,password:String) : Interceptor
{
   var credentials = Credentials.basic(user,password)

    fun BasicAuthInterceptor(user:String, password:String)
    {
    credentials = Credentials.basic(user, password);
    }

    override fun intercept(chain: Interceptor.Chain): Response
    {
        val request:Request = chain.request()
        val authenticatedRequest: Request =request.newBuilder()
                .header("Authorization",credentials)
                .build()
        return chain.proceed(authenticatedRequest)
    }
}