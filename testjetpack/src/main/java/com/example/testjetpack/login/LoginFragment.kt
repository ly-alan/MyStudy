package com.example.testjetpack.login

import androidx.navigation.Navigation
import com.example.testjetpack.BuildConfig
import com.example.testjetpack.R
import com.example.testjetpack.base.MVVMBaseFragment
import com.example.testjetpack.databinding.FragmentLoginBinding

class LoginFragment : MVVMBaseFragment<LoginViewModel, FragmentLoginBinding>() {


    override fun getBindingVariable(): Int {
        return com.example.testjetpack.BR._all
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        if (BuildConfig.DEBUG) {
            binding.etLoginInputAccount.setText("roger001@ipwangxin.cn")
            binding.etLoginInputPwd.setText("123")
        }

    }

    override fun initData() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.etLoginInputAccount.text.toString(),
                binding.etLoginInputAccount.text.toString(),
                binding.etLoginInputPwd.text.toString()
            )
        }
        binding.tvRegister.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.registerFragment)
//            var url = "mfccontent://vod.detail?id=100039042&pageId=1"
//            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            startActivity(intent);
        }
        binding.tvResetPwd.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.resetPwdFragment)
        }
    }

    override fun initObserver() {

    }

    private fun openMainActivity() {
//        var intent = Intent(activity, MainActivity::class.java)
//        startActivity(intent);
//        activity?.finish();
    }
}


