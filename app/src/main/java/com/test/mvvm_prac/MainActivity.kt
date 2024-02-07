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
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.common.util.Utility
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.test.mvvm_prac.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val keyHash = Utility.getKeyHash(this)
        Log.d("Hash", keyHash)
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

        // KaKao SDK  초기화
        // 초기화할때는 그냥 api key 그대로 숫자만 가져다 쓰고 menifest에 등록할때는 kakao 붙여쓴다.
        KakaoSdk.init(this, getString(R.string.kakao_app_key))
        // KaKao SDK  초기화

        //카카오톡으로 로그인실패 후 카카오계정으로 로그인할 시
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Toast.makeText(this, "접근이 거부 됨(동의 취소)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Toast.makeText(this, "유효하지 않은 앱", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Toast.makeText(this, "인증 수단이 유효하지 않아 인증할 수 없는 상태", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Toast.makeText(this, "요청 파라미터 오류", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Toast.makeText(this, "유효하지 않은 scope ID", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Toast.makeText(this, "설정이 올바르지 않음(android key hash)", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Toast.makeText(this, "서버 내부 에러", Toast.LENGTH_SHORT).show()
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Toast.makeText(this, "앱이 요청 권한이 없음", Toast.LENGTH_SHORT).show()
                    }
                    else -> { // Unknown
                        Toast.makeText(this, "기타 에러", Toast.LENGTH_SHORT).show()
                        Log.d("error2222",error.toString())
                    }
                }
            }
            //얘는 연동 처음할때 코드 @@@@@@@@@@@@
            else if (token != null) { //연동하고 다시 돌아왔을때 설정.
                Toast.makeText(this, "카카오 로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, result::class.java)
                intent.putExtra("success","kakao")
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                Log.d("kakao token.accessToken",token.accessToken)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        binding.btnKakao.setOnClickListener {

            //@@@@@@@@@ 이전에 로그인했던 토큰이 있는지 확인
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                //토큰이 없고 에러가난다면 에러를 로그로찍음
                if (error != null) {
                    Log.d("error", error.toString())
                    Toast.makeText(this, "토큰 정보 보기 실패", Toast.LENGTH_SHORT).show()

                    //토큰이 없다면 카카오톡이나 카카오계정으로 로그인 / 회원가입?

                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                        UserApiClient.instance.loginWithKakaoTalk(this, callback = callback)
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    }

                }

                //카카오 로그인을 통해 토큰 정보를 받아오면 다음페이지로 이동.
                //한번 로그인하면 다음부터는 계속 바로 로그인된다.
                else if (tokenInfo != null) {
                    Toast.makeText(this, "토큰 정보 보기 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, result::class.java)
                    Log.d("token333",tokenInfo.toString())
                    intent.putExtra("success", "kakao")
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }



            }
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
            RESULT_OK -> {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                Log.d("Naver_getAccessToken", NaverIdLoginSDK.getAccessToken().toString())
                Log.d("Naver_getRefreshToken", NaverIdLoginSDK.getRefreshToken().toString())
                Log.d("Naver_getExpiresAt", NaverIdLoginSDK.getExpiresAt().toString())
                Log.d("Naver_getTokenType", NaverIdLoginSDK.getTokenType().toString())
                Log.d("Naver_getState", NaverIdLoginSDK.getState().toString())

                intent = Intent(this,com.test.mvvm_prac.result::class.java)
                intent.putExtra("success","naver")
                startActivity(intent)
            }

            RESULT_CANCELED -> {
                // 실패 or 에러
                Log.d("errorCode", NaverIdLoginSDK.getLastErrorCode().code)
                Log.d("errorDescription", NaverIdLoginSDK.getLastErrorDescription().toString())

                Toast.makeText(this,"로그인에 성공하였습니다!", Toast.LENGTH_SHORT).show()

            }
        }

    }


    private fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.d("errer","연결실패")
            } else if (token != null) {
                //TODO: 최종적으로 카카오로그인 및 유저정보 가져온 결과
                UserApiClient.instance.me { user, error ->
                    Toast(this@MainActivity).show()
                }
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.d("errer","연결실패")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

}