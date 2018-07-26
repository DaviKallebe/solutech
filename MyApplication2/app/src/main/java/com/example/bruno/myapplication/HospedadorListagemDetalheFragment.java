package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HospedadorListagemDetalheFragment extends Fragment {

    private MainActivityViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();

        if (activity != null)
            mViewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_hospedador_listagem_detalhe, container, false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {

            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                //Fragments Settings
                List<FragmentOption> fragmentOptions = new ArrayList<>();

                fragmentOptions.add(new FragmentOption(HospedadorListagemDetalhePerfilFragment.class,
                        getResources().getString(R.string.fragment_hospedador_listagem_detalhe_perfil), getArguments()));
                fragmentOptions.add(new FragmentOption(HospedadorListagemDetalheLocalFragment.class,
                        getResources().getString(R.string.fragment_hospedador_listagem_detalhe_local), getArguments()));

                //PageViewer and TabLyout configuration
                ViewPager mPager = rootView.findViewById(R.id.fragment_hospedador_listagem_detalhe_viewpager);
                PagerAdapter mPagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, fragmentOptions);
                mPager.setAdapter(mPagerAdapter);

                TabLayout tabLayout = rootView.findViewById(R.id.fragment_hospedador_listagem_detalhe_tablayout);
                tabLayout.setupWithViewPager(mPager);
            }
        }

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_hospedador_listagem_detalhe));

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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
