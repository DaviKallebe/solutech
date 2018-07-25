package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.bruno.myapplication.adapter.PetListagemAdapter;
import com.example.bruno.myapplication.commons.SimpleDividerItemDecoration;
import com.example.bruno.myapplication.commons.Status;
import com.example.bruno.myapplication.retrofit.Pet;

import java.util.List;


public class PetListagemFragment extends Fragment implements PetListagemAdapter.OnItemClicked {

    private RecyclerView mRecyclerView;
    private PetListagemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MainActivityViewModel mViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity activity = getActivity();

        if (activity != null)
            mViewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pet_listagem, container, false);

        setHasOptionsMenu(true);

        Context context = this.getContext();

        if (context != null) {
            mRecyclerView = rootView.findViewById(R.id.listagem_pet_recycler);
            mLayoutManager = new LinearLayoutManager(context);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));


            mViewModel.getPetList().observe(this, resource -> {
                if (resource != null && resource.status == Status.SUCCESS) {
                    List<Pet> petList = resource.data;

                    mAdapter = new PetListagemAdapter(petList, context, this);

                    mRecyclerView.setAdapter(mAdapter);
                }
            });
        }

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_pet_listagem_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_pet));
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_perfil));
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
            case R.id.fragment_pet_detalhe_menu_novo:
                goToFragment(new PetCadastroFragment());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mAdapter != null)
            goToFragment(PetDetalheFragment.newInstance(mAdapter.getItem(position).getId_pet()));
    }

    public void goToFragment(Fragment fragmentDestination) {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = activity.getSupportFragmentManager();

            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fate_out)
                        .replace(R.id.fragment_pet_listagem,
                                fragmentDestination,
                                fragmentDestination.getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
