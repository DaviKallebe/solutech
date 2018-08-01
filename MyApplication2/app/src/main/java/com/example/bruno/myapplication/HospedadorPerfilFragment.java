package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HospedadorPerfilFragment extends Fragment {

    private MainActivityViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();

        if (activity != null)
            mViewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hospedador_perfil, container, false);

        List<FragmentOption> fragmentOptions = new ArrayList<>();

        fragmentOptions.add(new FragmentOption(DetalheHospedador1.class,
                "Perfil", null));
        fragmentOptions.add(new FragmentOption(DetalheHospedador2.class,
                "Informações", null));


        ViewPager mPager = rootView.findViewById(R.id.fragmento_hospedador_pefil_viewpager);
        PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager(), fragmentOptions);
        mPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = rootView.findViewById(R.id.fragmento_hospedador_pefil_tablayout);
        tabLayout.setupWithViewPager(mPager);

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.menu_perfil_hospedador));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menu_id = item.getItemId();

        switch (menu_id) {
            case android.R.id.home:
                AppCompatActivity activity = (AppCompatActivity) getActivity();

                if (activity != null) {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                    if (fragmentManager != null)
                        fragmentManager.popBackStackImmediate();
                }

                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    private class FragmentOption {
        private Class classFragment;
        private String fragmentName;
        private Bundle bundle;

        private <T extends Fragment> FragmentOption(Class<T> classFragment, String fragmentName) {
            this.classFragment = classFragment;
            this.fragmentName = fragmentName;
        }

        private <T extends Fragment> FragmentOption(Class<T> classFragment, String fragmentName, Bundle bundle) {
            this.classFragment = classFragment;
            this.fragmentName = fragmentName;
            this.bundle = bundle;
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public Fragment getFragment() {
            try {
                Fragment fragment = (Fragment) classFragment.newInstance();
                fragment.setArguments(bundle);

                return fragment;
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        List<FragmentOption> fragmentOptions;

        ScreenSlidePagerAdapter(FragmentManager fragmentManager, List<FragmentOption> fragmentOptions) {
            super(fragmentManager);
            this.fragmentOptions = fragmentOptions;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentOptions.get(position).getFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentOptions.get(position).getFragmentName();
        }

        @Override
        public int getCount() {
            return fragmentOptions.size();
        }
    }
}
