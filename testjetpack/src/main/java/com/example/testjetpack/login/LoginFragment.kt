package com.example.testjetpack.login

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.testjetpack.MainActivity
import com.example.testjetpack.R
import com.example.testjetpack.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.btnLogin.setOnClickListener {
            openMainActivity();
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

        return binding.root;
    }


    private fun openMainActivity() {
//        var intent = Intent(activity, MainActivity::class.java)
//        startActivity(intent);
//        activity?.finish();
        var url = "kidscontent://vod.detail?id=100039042&pageId=1"
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent);

    }
}


