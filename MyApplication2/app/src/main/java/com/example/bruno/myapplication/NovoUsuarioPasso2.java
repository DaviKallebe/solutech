package com.example.bruno.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.bruno.myapplication.commons.BrPhoneNumberFormatter;

import java.lang.ref.WeakReference;


public class NovoUsuarioPasso2 extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_novo_usuario_passo2, container, false);

        EditText telephone = view.findViewById(R.id.editPhone);
        BrPhoneNumberFormatter addLineNumberFormatter = new BrPhoneNumberFormatter(new WeakReference<>(telephone));
        telephone.addTextChangedListener(addLineNumberFormatter);

        FloatingActionButton fab_arrow_forward = view.findViewById(R.id.arrow_forward_novo_usuario2);
        fab_arrow_forward.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.arrow_forward_novo_usuario2) {
            final EditText editPhone = getView().findViewById(R.id.editPhone);
            final EditText editFirstName = getView().findViewById(R.id.editFirstName);
            final EditText editLastName = getView().findViewById(R.id.editLastName);

            final String phoneText = editPhone.getText().toString();
            final String firstName = editFirstName.getText().toString();
            final String lastName = editLastName.getText().toString();

            if (phoneText.equals("") || firstName.equals("") || lastName.equals("")) {
                Snackbar.make(view, "Os campos são obrigatórios!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                return;
            }
            mListener.NovoUsuarioPasso2SaveToActivity(phoneText, firstName, lastName);
            //mListener.NovoUsuarioPasso2SaveToActivity("000000000", "OMEGA", "LUL");

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.onFragmentInteraction(uri);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void NovoUsuarioPasso2SaveToActivity(String phone, String firstName, String lastName);
    }
}
