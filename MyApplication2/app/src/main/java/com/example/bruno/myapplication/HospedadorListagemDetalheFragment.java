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
                HospedadorListagemDetalhePerfilFragment hospedadorListagemDetalhePerfilFragment =
                        new HospedadorListagemDetalhePerfilFragment();
                HospedadorListagemDetalheLocalFragment hospedadorListagemDetalheLocalFragment =
                        new HospedadorListagemDetalheLocalFragment();

                List<FragmentOption> fragmentOptions = new ArrayList<>();

                //Fragment and Name
                fragmentOptions.add(new FragmentOption(hospedadorListagemDetalhePerfilFragment,
                        getResources().getString(R.string.fragment_hospedador_listagem_detalhe_perfil)));
                fragmentOptions.add(new FragmentOption(hospedadorListagemDetalheLocalFragment,
                        getResources().getString(R.string.fragment_hospedador_listagem_detalhe_local)));

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
        private Fragment fragment;
        private String fragmentName;

        public FragmentOption(Fragment fragment, String fragmentName) {
            this.fragment = fragment;
            this.fragmentName = fragmentName;
        }

        public String getFragmentName() {
            return fragmentName;
        }

        public Fragment getFragment() {
            return fragment;
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
