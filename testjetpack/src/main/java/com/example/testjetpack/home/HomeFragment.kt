package com.example.testjetpack.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testjetpack.databinding.FragmentExampleBinding
import com.example.testjetpack.databinding.TextData

class HomeFragment : Fragment() {

    lateinit var binding: FragmentExampleBinding;

    lateinit var homeViewModel: HomeViewModel;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        homeViewModel = HomeViewModel();
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        homeViewModel.getHomeLiveData().observe(this, Observer<String> {
            Log.i("liao", it);
            binding.fragmentName = TextData(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExampleBinding.inflate(inflater, container, false);
        return binding.root;
    }

}