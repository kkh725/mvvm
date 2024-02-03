package com.test.mvvm_prac

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.test.mvvm_prac.databinding.ActivityResultBinding

class result : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_result)
         //네이버 로그아웃과, 연동해제는 따로 만들어서 사용할것.

        //들어오는게 네이버로 로그인할시
        when (intent.getStringExtra("success")){
            "naver" -> {
                binding.tvNaver.visibility = View.VISIBLE
                binding.button2.setOnClickListener {startNaverDeleteToken()}
            } ;
            //카카오로 로그인 할 시
            "kakao" -> {
                binding.tvKakao.visibility = View.VISIBLE
                binding.button2.setOnClickListener {startKakaoDeleteToken()}
            }

        }
    }


    //로그아웃
    private fun startNaverLogout(){
        NaverIdLoginSDK.logout()
        Toast.makeText(this, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
    }

    /**
     * 네이버 토큰 삭제
     * 연동 해제.
     */
    private fun startNaverDeleteToken(){
        NidOAuthLogin().callDeleteTokenApi( object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                Toast.makeText(applicationContext, "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
                finish()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }

    private fun startKakaoDeleteToken(){
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                Log.e(TAG, "연결 끊기 실패", error)
                Toast.makeText(applicationContext, "카카오 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
                finish()
            }
            else {
                Log.i(TAG, "연결 끊기 성공. SDK에서 토큰 삭제 됨")
            }
        }
    }
}

