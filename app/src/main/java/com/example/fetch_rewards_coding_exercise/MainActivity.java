package com.example.fetch_rewards_coding_exercise;

import androidx.fragment.app.Fragment;


public class MainActivity extends FragmentActivity {
    @Override
    public Fragment createFragment() {
        return new ListFragment();
    }

}