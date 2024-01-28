package com.test.mvvm_prac

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.test.mvvm_prac.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.myViewModel = viewModel
        //view 모델에서는 vm 의 api recall 값을 받아오고 observe 를 활용해서 바뀔때마다 xml 에서도 실행되게끔 구현
        //정말 잘 모르겠다 다시 해결해보기

        setupObserver()

        binding.button.setOnClickListener{
            viewModel.getapirecall("https://jsonplaceholder.typicode.com/")
        }



    }

    private fun setupObserver(){
        viewModel.link.observe(this) { // apirecall 변수에 옵저버를 달아두어 변화가 생길때마다 람다식 안의 표현을 수행한다.
            binding.tv1.text=it // 여기서 It 은 live data 즉 apirecall값의 변경된 값을 의미한다.
        }
    }


}