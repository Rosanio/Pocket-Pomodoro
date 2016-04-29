package com.example.guest.pomodoro.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.guest.pomodoro.models.QA;
import com.example.guest.pomodoro.ui.CardFragment;

import java.util.ArrayList;

public class CardPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<QA> mQas;

    public CardPagerAdapter(FragmentManager fm, ArrayList<QA> qas) {
        super(fm);
        mQas = qas;
    }

    @Override
    public Fragment getItem(int position) {
        return CardFragment.newInstance(mQas.get(position));
    }

    @Override
    public int getCount() {
        return mQas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mQas.get(position).getQuestion();
    }
}
