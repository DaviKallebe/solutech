package com.example.bruno.myapplication;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruno.myapplication.retrofit.Hospedagem;
import com.example.bruno.myapplication.retrofit.Pet;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class HospedadorServicoDetalheFragment extends DialogFragment {

    MainActivityViewModel mViewModel;
    OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentActivity fragmentActivity = getActivity();

        if (fragmentActivity != null)
            mViewModel = ViewModelProviders.of(fragmentActivity).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hospedador_servico_detalhe, container, false);

        Context context = getContext();
        Bundle bundle = getArguments();

        if (context != null && bundle != null) {
            try {
                JSONObject json = new JSONObject(bundle.getString("servico"));
                ViewPager pager = rootView.findViewById(R.id.hospedador_servico_detalhe_pager);
                TextView totalPets = rootView.findViewById(R.id.hospedador_servico_detalhe_total);

                SharedPreferences prefs = context.getSharedPreferences("userfile", MODE_PRIVATE);
                Integer id_user = prefs.getInt("id_user", 0);

                CircleImageView perfil = rootView.findViewById(R.id.hospedador_servico_detalhe_perfil);
                TextView nome = rootView.findViewById(R.id.hospedador_servico_detalhe_nome);
                TextView dataInicio = rootView.findViewById(R.id.hospedador_servico_detalhe_datainicio);
                TextView dataFim = rootView.findViewById(R.id.hospedador_servico_detalhe_datafim);

                nome.setText(String.format("%1$s %2$s",
                        json.getString("primeiroNome"),
                        json.getString("ultimoNome")));
                dataInicio.setText(String.format("Inicio: %1$s", json.getString("dataInicio")));
                dataFim.setText(String.format("Fim: %1$s", json.getString("dataFim")));

                if (json.has("imagem") && !json.isNull("imagem"))
                    Picasso.get().load(json.getString("imagem")).into(perfil);

                Button confirmar = rootView.findViewById(R.id.hospedador_servico_detalhe_confirmar);
                Button rejeitar = rootView.findViewById(R.id.hospedador_servico_detalhe_rejeitar);
                CheckBox aceito = rootView.findViewById(R.id.hospedador_servico_detalhe_aceitar);

                aceito.setOnCheckedChangeListener((buttonView, isChecked) -> confirmar.setEnabled(isChecked));

                confirmar.setEnabled(aceito.isChecked());
                confirmar.setOnClickListener((View v) -> {
                    try {
                        Hospedagem hospedagem = new Hospedagem();

                        hospedagem.setId(json.getInt("id"));
                        hospedagem.setStatus(2);

                        Disposable disposable = mViewModel
                                .atualizarHospedagem(hospedagem)
                                .observeOn(Schedulers.io())
                                .subscribeOn(AndroidSchedulers.mainThread())
                                .subscribe((Hospedagem hosp) -> {
                                    if (mListener != null)
                                        mListener.refreshAdpter(hospedagem.getId(),
                                                hospedagem.getStatus());
                                    if (getActivity() != null)
                                        getActivity().runOnUiThread(() -> Toast.makeText(context,
                                                "Serviço aceito com sucesso",
                                                Toast.LENGTH_LONG).show());

                                    dismiss();
                                }, (Throwable e) -> {
                                    if (getActivity() != null)
                                        getActivity().runOnUiThread(() -> Toast.makeText(context,
                                                "Erro, não foi possivel aceitar",
                                                Toast.LENGTH_LONG).show());
                                });

                        CompositeDisposable compositeDisposable = new CompositeDisposable();
                        compositeDisposable.add(disposable);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                });

                rejeitar.setOnClickListener((View v) -> {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Confirmar a ação")
                            .setMessage("Deseja rejeitar esse serviço?")
                            .setPositiveButton("Sim", (dialog, which) -> {
                                try {
                                    Hospedagem hospedagem = new Hospedagem();

                                    hospedagem.setId(json.getInt("id"));
                                    hospedagem.setStatus(3);

                                    Disposable disposable = mViewModel
                                            .atualizarHospedagem(hospedagem)
                                            .observeOn(Schedulers.io())
                                            .subscribeOn(AndroidSchedulers.mainThread())
                                            .subscribe((Hospedagem hosp) -> {
                                                if (getActivity() != null)
                                                    if (mListener != null)
                                                        mListener.refreshAdpter(hospedagem.getId(),
                                                                hospedagem.getStatus());
                                                    getActivity().runOnUiThread(() -> Toast.makeText(context,
                                                            "Serviço rejeitado com sucesso",
                                                            Toast.LENGTH_LONG).show());

                                                dismiss();
                                            }, (Throwable e) -> {
                                                e.printStackTrace();
                                                if (getActivity() != null)
                                                    getActivity().runOnUiThread(() -> Toast.makeText(context,
                                                            "Erro, não foi possivel cancelar",
                                                            Toast.LENGTH_LONG).show());
                                            });

                                    CompositeDisposable compositeDisposable = new CompositeDisposable();
                                    compositeDisposable.add(disposable);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            })
                            .setNegativeButton("Não", null);

                    AlertDialog alert = builder.create();
                    alert.show();
                });

                if (json.has("id_pets") && !json.isNull("id_pets"))
                    setPager(json.getString("id_pets"), pager, totalPets);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setHasOptionsMenu(true);
        return rootView;
    }

    public void setPager(String id_pets, ViewPager viewPager, TextView totalPets) {
        WeakReference<ViewPager> viewPagerWeakReference = new WeakReference<>(viewPager);
        WeakReference<TextView> editTextWeakReference = new WeakReference<>(totalPets);

        Disposable disposable = new RetrofitConfig()
                .getHospedagemService()
                .selecionarPetHospedagem(id_pets)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe((List<Pet> pets) -> {
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> {
                            PagerAdapter mPagerAdapter =
                                    new ScreenSlidePagerAdapter(
                                            getChildFragmentManager(), pets);

                            editTextWeakReference.get().setText(String.format(
                                    new Locale("pt", "BR"),
                                    "Quantidade de pets: %d",
                                    pets.size()));

                            viewPagerWeakReference.get().setAdapter(mPagerAdapter);
                        });
                    }
                }, Throwable::printStackTrace);

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(disposable);
    }

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null)
            activity.setTitle(getResources().getString(R.string.fragment_servico_detalhe));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
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
        List<Pet> pets;

        ScreenSlidePagerAdapter(FragmentManager fragmentManager, List<Pet> pets) {
            super(fragmentManager);
            this.pets = pets;
        }

        @Override
        public Fragment getItem(int position) {
            HospedadorServicoDetalhePetFragment hospedadorServicoDetalhePetFragment =
                    new HospedadorServicoDetalhePetFragment();

            try {
                Bundle bundle = new Bundle();
                bundle.putString("pet", pets.get(position).getFieldsJson().toString());
                bundle.putBoolean("proximo", position < pets.size() - 1);
                bundle.putBoolean("anterior", position > 0);

                hospedadorServicoDetalhePetFragment.setArguments(bundle);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            return hospedadorServicoDetalhePetFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return pets.size();
        }
    }

    public interface OnFragmentInteractionListener {
        void refreshAdpter(Integer id, Integer status);
    }
}
