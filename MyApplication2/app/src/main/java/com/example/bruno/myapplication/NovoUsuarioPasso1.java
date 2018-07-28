package com.example.bruno.myapplication;

import android.content.Context;
import android.os.Bundle;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class NovoUsuarioPasso1 extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NovoUsuarioPasso1() {
        // Required empty public constructor
    }

    public static NovoUsuarioPasso1 newInstance(String param1, String param2) {
        NovoUsuarioPasso1 fragment = new NovoUsuarioPasso1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /*EditText telephone = getView().findViewById(R.id.editPhone);
        BrPhoneNumberFormatter addLineNumberFormatter = new BrPhoneNumberFormatter(new WeakReference<EditText>(telephone));
        telephone.addTextChangedListener(addLineNumberFormatter);*/
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_novo_usuario_passo1, container, false);

        final EditText editMail = view.findViewById(R.id.editMail);
        editMail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);


        editMail.addTextChangedListener(new TextWatcher() {
            Long beforeTime;
            Long afterTime;
            Handler handler;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTime = SystemClock.uptimeMillis();

                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);
                    editMail.setError(null);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //
            }

            @Override
            public void afterTextChanged(Editable s) {
                handler = new Handler();
                handler.postDelayed(() -> {
                    afterTime = SystemClock.uptimeMillis();

                    if (getContext() != null) {
                        if (afterTime - beforeTime >= 1000 && !isEmailValid(editMail.getText().toString().trim()))
                            editMail.setError(getResources().getString(R.string.error_invalid_email));
                        else
                            editMail.setError(null);
                    }

                    handler.removeCallbacksAndMessages(null);
                }, 1500);
            }
        });

        editMail.setOnFocusChangeListener((View v, boolean hasFocus) -> {
            if (!hasFocus) {
                //check if email is valid
            }
        });

        FloatingActionButton fab_arrow_forward = view.findViewById(R.id.arrow_forward);
        fab_arrow_forward.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        //clicked in float button
        if (viewId == R.id.arrow_forward && getView() != null) {
            final EditText editEmail = getView().findViewById(R.id.editMail);
            final EditText editPass1 = getView().findViewById(R.id.editPass1);
            final EditText editPass2 = getView().findViewById(R.id.editPass2);

            final String emailText = editEmail.getText().toString();
            final String pass1Text = editPass1.getText().toString();
            final String pass2Text = editPass2.getText().toString();

            if (emailText.equals("") || pass1Text.equals("") || pass2Text.equals("")) {
                Snackbar.make(view, "Os campos email e senha são obrigatórios!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                return;
            }
            if (!pass1Text.equals(pass2Text)) {
                Snackbar.make(view, "Senhas não são iguais!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                editPass1.setText("");
                editPass2.setText("");

                return;
            }
            mListener.NovoUsuarioPasso1SaveToActivity(emailText, pass1Text);

            //mListener.NovoUsuarioPasso1SaveToActivity("myemail", "mypass");
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void NovoUsuarioPasso1SaveToActivity(String email, String pass);
    }
}
