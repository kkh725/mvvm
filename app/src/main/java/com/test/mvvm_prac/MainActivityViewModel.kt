package com.test.mvvm_prac

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 일반적으로 local db나 api 데이터를 호출해온다.
// 이번 예제에서는 간단히 repository의 프로퍼티 값을 호출하는 방식으로 작성해보았다.

class MainActivityViewModel : ViewModel() {

    private var _link = MutableLiveData<String>()
    val link : LiveData<String> = _link


    fun getapirecall(url : String){
        /**
         * view에서 버튼을 누르거나, 동작을 수행할 때 viewmodel 의 메서드를 실행을시킨다. ->
         * 메서드를 실행을시키면 model 의 메서드를 다시 수행하고, vm의 데이터값을 변화시킨다.
         * 그 후 자연스레 livedata도 변환이되고, 변화가된다면 옵저버에 확인되어 ui에 변화를 줄 수 있게 만든다.
         */
        //api 호출하는 코드. 호출한 다음 Model 에 contents값을 넣어 파싱.

        val retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()).build();

        val apiService = retrofit.create(ApiService::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 호출
                val postId = 1
                val response: Response<Post> = apiService.getPost(postId) //1을 넣고 api 호출한것

                // UI 업데이트는 Main 스레드에서 수행
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val post: Post? = response.body()
                        if (post != null) {
                            // API 호출이 성공하고, 응답 데이터를 post 변수에 얻어옴
                            // post 변수를 이용하여 필요한 처리 수행

                            Log.d("post", post.toString())
                            Log.d("post2",response.toString())

                            //여기서 model 의 success 함수를 표현할 수 있을듯.

                            _link.value = Model(post.toString()).link()
                        }
                    } else {
                        // API 호출이 실패한 경우
                        // response.errorBody() 등을 이용하여 실패 이유를 확인할 수 있음
                    }
                }
            } catch (e: Exception) {
                // 예외 처리
            }
        }



    }

}