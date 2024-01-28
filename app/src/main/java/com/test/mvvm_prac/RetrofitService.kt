package com.test.mvvm_prac

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path



    interface ApiService {
        @GET("posts/{postId}")
        suspend fun getPost(@Path("postId") postId: Int): Response<Post> //이 칸에 api 가 요구하는 key값이나 값들을 넣어준다.
                                                                        // header는 따로 설정.
    }
