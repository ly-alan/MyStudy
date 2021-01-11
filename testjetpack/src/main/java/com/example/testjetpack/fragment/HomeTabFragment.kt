package com.example.testjetpack.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.testjetpack.databinding.FragmentExampleBinding
import com.example.testjetpack.databinding.TextData
import java.text.FieldPosition

class HomeTabFragment : Fragment {

    constructor()


    constructor(index: Int) {
        position = index;
    }

    var position: Int = 0;

    lateinit var binding: FragmentExampleBinding;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExampleBinding.inflate(inflater, container, false)
        binding.fragmentName = TextData("$position");
        return binding.root
    }

}