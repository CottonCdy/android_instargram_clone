package com.example.instargramclone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstarJoinActivity : AppCompatActivity() {

    var userName: String = ""
    var userPassword1: String = ""
    var userPassword2: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instar_join)

        // 로그인 버튼
        findViewById<TextView>(R.id.instar_login).setOnClickListener {
            startActivity(Intent(this, InstarLoginActivity::class.java))
        }

        // id 입력
        findViewById<EditText>(R.id.id_input).doAfterTextChanged { userName = it.toString() }

        // pw1 입력
        findViewById<EditText>(R.id.pw_input1).doAfterTextChanged { userPassword1 = it.toString() }

        // pw2 입력
        findViewById<EditText>(R.id.pw_input2).doAfterTextChanged { userPassword2 = it.toString() }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        findViewById<TextView>(R.id.join_btn).setOnClickListener {
            // 비밀번호가 일치하지 않을 경우
            if (!(userPassword1.equals(userPassword2))) {
                Toast.makeText(this@InstarJoinActivity, "비밀번호가 동일하지 않습니다", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            val user = HashMap<String, Any>()
            user.put("username", userName)
            user.put("password1", userPassword1)
            user.put("password2", userPassword2)

            retrofitService.instarJoin(user).enqueue(object : Callback<UserToken> {
                override fun onResponse(call: Call<UserToken>, response: Response<UserToken>) {
                    if (response.isSuccessful) {
                        val userToken = response.body()!!
                        if (userToken.token == null) {
                            // 아이디가 이미 존재하는 경우
                            Toast.makeText(
                                this@InstarJoinActivity,
                                "아이디가 이미 존재합니다",
                                Toast.LENGTH_SHORT
                            ).show()
                            return
                        }

                        val sharedPreferences =
                            getSharedPreferences("user_info", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("token", userToken.token)
                        editor.commit()
                    }
                }

                override fun onFailure(call: Call<UserToken>, t: Throwable) {

                }
            })
        }
    }
}