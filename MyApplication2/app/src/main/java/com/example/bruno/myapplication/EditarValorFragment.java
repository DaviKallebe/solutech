package com.example.bruno.myapplication;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bruno.myapplication.commons.BrPhoneNumberFormatter;
import com.example.bruno.myapplication.commons.PerfilOpcoes;

import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class EditarValorFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private PerfilOpcoes mOption;
    private EditText mTextValue;
    private DatePicker mDatePickerValue;
    private MainActivityViewModel mViewModel;
    private Context mContext;

    private OnFragmentInteractionListener mListener;

    public EditarValorFragment() {
        // Required empty public constructor
    }

    public static EditarValorFragment newInstance(String param1, String param2) {
        EditarValorFragment fragment = new EditarValorFragment();
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

        mViewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fragment_editar_valor,
                container, false);
        setHasOptionsMenu(true);

        mContext = this.getContext();
        mTextValue = rootView.findViewById(R.id.fragment_edit_edittext_value);
        mDatePickerValue = rootView.findViewById(R.id.fragment_edit_datePicker_value);

        if (mOption != null) {
            TextView textView = rootView.findViewById(R.id.fragment_edit_textview_title);
            textView.setText(mOption.getName());
            Integer type = mOption.getTypeField();

            if (type == 1 || type == 2 || type == 3) {
                mTextValue.setText(mOption.getValueAsString());
                mTextValue.setLines(1);
                mTextValue.setSingleLine(true);
                mTextValue.setVisibility(View.VISIBLE);
            }

            if (type == 4) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                Calendar calendar = Calendar.getInstance();
                Date date = null;

                try {
                    date = format.parse((String)mOption.getValue());
                } catch (ParseException e) {
                    Log.e("DATEPARSEERROR", e.getMessage());
                }

                mDatePickerValue.setVisibility(View.VISIBLE);

                if (date != null)
                    calendar.setTime(date);

                mDatePickerValue.updateDate(calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
            }

            if (type == 5) {
                BrPhoneNumberFormatter addLineNumberFormatter = new BrPhoneNumberFormatter(
                        new WeakReference<>(mTextValue));

                mTextValue.addTextChangedListener(addLineNumberFormatter);
                mTextValue.setText(mOption.getValueAsString());
                mTextValue.setVisibility(View.VISIBLE);
            }

            if (type == 6) {
                mTextValue.setText(mOption.getValueAsString());
                mTextValue.setSingleLine(false);
                mTextValue.setVisibility(View.VISIBLE);
            }

            Button button1 = rootView.findViewById(R.id.fragment_edit_button_confirm);
            Button button2 = rootView.findViewById(R.id.fragment_edit_button_cancel);

            button1.setOnClickListener(this);
            button2.setOnClickListener(this);
        }

        return rootView;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            //mListener.saveFieldOnBackend(uri);
        }
    }

    public void setOption(PerfilOpcoes mOption) {
        this.mOption = mOption;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_perfil).setVisible(false);
        menu.findItem(R.id.action_deslogar).setVisible(false);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }

            activity.setTitle(getResources().getString(R.string.fragment_perfil));
        }

        super.onCreateOptionsMenu(menu, inflater);
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

                    hideSoftKeyboard(getActivity(), getView());
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
        //
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();

            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowHomeEnabled(false);
            }
            activity.setTitle(getResources().getString(R.string.app_label));
        }
    }

    public void close() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (activity != null) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            if (fragmentManager != null)
                fragmentManager.popBackStackImmediate();
        }
    }

    public void hideSoftKeyboard(Activity activity, View view) {
        if (activity == null || view == null)
            return;

        InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                Context.INPUT_METHOD_SERVICE);

        if (imm != null && view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();

        switch (vid) {
            case R.id.fragment_edit_button_cancel:
                hideSoftKeyboard(getActivity(), getView());
                close();
                break;

            case R.id.fragment_edit_button_confirm:
                if (mListener != null) {
                    Integer type = mOption.getTypeField();

                    if (type == 1 || type == 2 || type == 3 || type == 5 || type == 6) {
                        mOption.setValue(mTextValue.getText().toString());
                    }

                    if (type == 4) {
                        mOption.setValue(new GregorianCalendar(mDatePickerValue.getYear(),
                                mDatePickerValue.getMonth(),
                                mDatePickerValue.getDayOfMonth()).getTime());
                    }

                    mListener.saveFieldOnBackend(mOption.getField(), mOption.getValue());

                    hideSoftKeyboard(getActivity(), getView());
                    close();
                }

                break;
        }
    }

    public interface OnFragmentInteractionListener {
        <T extends Object> void saveFieldOnBackend(String fieldName, T fieldValue);
    }
}
