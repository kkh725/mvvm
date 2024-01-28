package com.test.mvvm_prac

data class Model(private val content : String) {
    // api 가 호출되어 Link 값이 return 될때

    fun link(): String{
        return "link$content" // 링크를 반환하게 하는 함수 라던지.
    }
    fun success() : Boolean{
        //만약 json 파싱한 값이 success가 들어있다면 -> 성공적으로 들어왔다면 true, 아니라면 false
        return content == "success"
    }


}

