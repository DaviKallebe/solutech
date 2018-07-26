package com.example.bruno.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class MainFragment extends Fragment {

    private static final int NUM_PAGES = 3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        FragmentActivity fragmentActivity = getActivity();

        if (fragmentActivity != null) {
            FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();

            ViewPager mPager = rootView.findViewById(R.id.viewPagerLogado);
            PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager);
            mPager.setAdapter(mPagerAdapter);

            TabLayout tabLayout = rootView.findViewById(R.id.sliding_tabs);
            tabLayout.setupWithViewPager(mPager);
        }

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return new HospedadorListagemFragment();
            else if (position == 1) {
                return new ListagemMensagemFragment();
            } else
                return new ListagemMensagemFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Hospedadores";
                case 1:
                    return "Mensagens";
                case 2:
                    return "Comentários";
                default:
                    return "Nada";
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
