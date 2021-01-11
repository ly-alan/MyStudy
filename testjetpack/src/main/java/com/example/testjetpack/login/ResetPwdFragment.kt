package com.example.testjetpack.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.testjetpack.databinding.FragmentResetPwdBinding

class ResetPwdFragment : Fragment() {
    lateinit var binding: FragmentResetPwdBinding;


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentResetPwdBinding.inflate(inflater, container, false);
        binding.btnRegister.setOnClickListener {
            Navigation.findNavController(it).navigateUp()
        }

        return binding.root;
    }
}