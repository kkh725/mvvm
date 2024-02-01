package com.test.mvvm_prac

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.navercorp.nid.NaverIdLoginSDK
import com.test.mvvm_prac.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        binding.myViewModel = viewModel

        //네이버 로그인 과정
        initializeNaverLogin()
        val launcher =
            registerForActivityResult<Intent, ActivityResult>(ActivityResultContracts.StartActivityForResult()) { result ->
               handleNaverLoginResult(result)
            }
        //네이버 로그인 버튼 클릭시 작동
        binding.buttonOAuthLoginImg.setOnClickListener {
            NaverIdLoginSDK.authenticate(
                this,
                launcher
            )
        }


        setupObserver()

        binding.button.setOnClickListener {
            viewModel.getapirecall(
                "http://192.168.35.29:8080/",
                binding.etId.text.toString(),
                binding.etPw.text.toString()
            )
        }


    }

    private fun setupObserver() {
        viewModel.link.observe(this) { // apirecall 변수에 옵저버를 달아두어 변화가 생길때마다 람다식 안의 표현을 수행한다.
            binding.tv1.text = it // 여기서 It 은 live data 즉 apirecall값의 변경된 값을 의미한다.
        }
    }

    /**
     * Naver
     * 로그인 초기화
     * id, client id, naver client name 등을 초기화하는과정
     */

    private fun initializeNaverLogin() {
        val naverClientId = getString(R.string.social_login_info_naver_client_id)
        val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
        val naverClientName = getString(R.string.social_login_info_naver_client_name)
        NaverIdLoginSDK.initialize(this, naverClientId, naverClientSecret, naverClientName)
    }

    /**
     * Naver
     * 네이버 로그인 성공 후 정보가져오기
     */
    private fun handleNaverLoginResult(result: ActivityResult) {
        when (result.resultCode) {
            AppCompatActivity.RESULT_OK -> {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d("Naver_getAccessToken", NaverIdLoginSDK.getAccessToken().toString())
                Log.d("Naver_getRefreshToken", NaverIdLoginSDK.getRefreshToken().toString())
                Log.d("Naver_getExpiresAt", NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("Naver_getTokenType", NaverIdLoginSDK.getTokenType().toString())
                Log.d("Naver_getState", NaverIdLoginSDK.getState().toString())

                startActivity(Intent(this,com.test.mvvm_prac.result::class.java))
            }

            AppCompatActivity.RESULT_CANCELED -> {
                // 실패 or 에러
                Log.d("errorCode", NaverIdLoginSDK.getLastErrorCode().code)
                Log.d("errorDescription", NaverIdLoginSDK.getLastErrorDescription().toString())

                Toast.makeText(this,"로그인에 성공하였습니다!", Toast.LENGTH_SHORT).show()

            }
        }

    }


}