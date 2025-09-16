package com.example.testjetpack.login

import android.content.Intent
import android.net.Uri
import androidx.navigation.Navigation
import com.example.testjetpack.R
import com.example.testjetpack.base.MVVMBaseFragment
import com.example.testjetpack.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.et_login_input_account
import kotlinx.android.synthetic.main.fragment_login.et_login_input_pwd

class LoginFragment : MVVMBaseFragment<LoginViewModel, FragmentLoginBinding>() {


    override fun getBindingVariable(): Int {
        return com.example.testjetpack.BR._all
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {

    }

    override fun initData() {
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                et_login_input_account.text.toString(),
                et_login_input_account.text.toString(),
                et_login_input_pwd.text.toString()
            )
        }
        binding.tvRegister.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.registerFragment)
            var url = "mfccontent://vod.detail?id=100039042&pageId=1"
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent);
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


