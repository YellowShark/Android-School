package ru.yellowshark.surfandroidschool.data.repository

import ru.yellowshark.surfandroidschool.data.network.MemesApi
import ru.yellowshark.surfandroidschool.data.network.auth.request.AuthRequest
import ru.yellowshark.surfandroidschool.data.network.auth.response.AuthResponse

class Repository(
    private val memesApi: MemesApi
) {
    suspend fun login (authRequest: AuthRequest): AuthResponse? {

        val response = memesApi.userAuth(authRequest)

        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }

        /*MemesApi.getInstance()
            .userAuth(AuthRequest(login = "qwerty", password = "qwerty")).enqueue(object :
                Callback<AuthResponse> {
                override fun onResponse(
                    call: Call<AuthResponse>,
                    response: Response<AuthResponse>
                ) {
                    Log.d("TAG", "onResponse:\n" +
                            "code: ${response.code()}\n" +
                            "message: ${response.message()}\n" +
                            "body: ${response.body()}")

                    if (response.isSuccessful && response.body()?.accessToken != null) {

                    } else {

                    }
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                }

            })*/
    }
}