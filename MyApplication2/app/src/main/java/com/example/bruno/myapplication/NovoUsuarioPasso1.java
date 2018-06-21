package com.example.bruno.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NovoUsuarioPasso1.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NovoUsuarioPasso1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NovoUsuarioPasso1 extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NovoUsuarioPasso1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NovoUsuarioPasso1.
     */
    // TODO: Rename and change types and number of parameters
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_novo_usuario_passo1, container, false);

        final EditText editMail = view.findViewById(R.id.editMail);
        editMail.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        FloatingActionButton fab_arrow_forward = view.findViewById(R.id.arrow_forward);
        fab_arrow_forward.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        //clicked in float button
        if (viewId == R.id.arrow_forward) {
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
        void NovoUsuarioPasso1SaveToActivity(String email, String pass);
    }
}
