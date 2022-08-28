package com.example.instargramclone

import retrofit2.Call
import retrofit2.http.FieldMap
import retrofit2.http.POST
import retrofit2.http.FormUrlEncoded

class UserToken(
    val userName: String,
    val token: String
)

interface RetrofitService {

    @POST("user/signup/")
    @FormUrlEncoded
    fun instarJoin(
        @FieldMap params: HashMap<String, Any>
    ): Call<UserToken>

    @POST("user/login/")
    @FormUrlEncoded
    fun instarLogin(
        @FieldMap params: HashMap<String, Any>
    ): Call<UserToken>
}