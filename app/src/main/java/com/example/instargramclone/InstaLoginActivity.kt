package com.example.instargramclone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InstaLoginActivity : AppCompatActivity() {
    var userName: String = ""
    var userPassword: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insta_login)

        val retrofit = Retrofit.Builder()
            .baseUrl("http://mellowcode.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val retrofitService = retrofit.create(RetrofitService::class.java)

        // id 입력
        findViewById<EditText>(R.id.id_input).doAfterTextChanged {
            userName = it.toString()
        }

        // pw 입력
        findViewById<EditText>(R.id.pw_input).doAfterTextChanged {
            userPassword = it.toString()
        }

        // 가입하기
        findViewById<TextView>(R.id.instar_join).setOnClickListener {
            startActivity(Intent(this, InstaJoinActivity::class.java))
        }

        findViewById<TextView>(R.id.login_btn).setOnClickListener {
            val user = HashMap<String, Any>()
            user.put("username", userName)
            user.put("password", userPassword)
            retrofitService.instaLogin(user).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val user = response.body()!!
                        val sharedPreferences =
                            getSharedPreferences("user_info", Context.MODE_PRIVATE)
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.putString("token", user.token)
                        editor.putString("user_id", user.id.toString())
                        editor.commit()
                        startActivity(Intent(this@InstaLoginActivity, InstarMainActivity::class.java))
                    } else {
                        errorMessage()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    errorMessage()
                }
            })
        }
    }

    fun errorMessage() {
        Toast.makeText(this@InstaLoginActivity, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show()
    }
}