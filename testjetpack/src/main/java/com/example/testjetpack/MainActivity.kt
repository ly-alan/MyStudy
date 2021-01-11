package com.example.testjetpack

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.testjetpack.databinding.ActivityMainBinding
import com.example.testjetpack.fragment.HomeTabFragment
import com.example.testjetpack.home.HomeFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initTabValue();
    }

    private fun initTabValue() {
        binding.vp2Main.adapter = object : FragmentStateAdapter(this) {
            private var tabFragmentList: List<Fragment> = listOf(
                    HomeFragment(),
                    HomeTabFragment(1),
                    HomeTabFragment(2),
                    HomeTabFragment(3)
            );

            override fun getItemCount(): Int {
                return tabFragmentList.size;
            }

            override fun createFragment(position: Int): Fragment {
                return tabFragmentList[position]
            }
        }
        TabLayoutMediator(binding.tabsMain, binding.vp2Main) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach();
    }

    private fun getTabTitle(position: Int): String? {
        return "tab$position";
    }

}
