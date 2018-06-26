package com.example.bruno.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bruno.myapplication.retrofit.Mensagem;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListMessageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListMessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListMessageFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ListMessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListMessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListMessageFragment newInstance(String param1, String param2) {
        ListMessageFragment fragment = new ListMessageFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_list_message, container, false);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            final ListView listView = rootView.findViewById(R.id.listViewLogadoMessage);
            listView.setOnItemClickListener(this);

            Call<List<Mensagem>> call = new RetrofitConfig().getUsuarioService().getMessages(bundle.getInt("id_user"));
            call.enqueue(new Callback<List<Mensagem>>() {
                @Override
                public void onResponse(Call<List<Mensagem>> call, Response<List<Mensagem>> response) {
                    if (response.code() == 200) {
                        listView.setAdapter(new CustomAdapter(response.body()));
                    }
                }

                @Override
                public void onFailure(Call<List<Mensagem>> call, Throwable t) {
                    Snackbar.make(getView().findViewById(R.id.fragment_List_message), "Verifique sua conexão!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //
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
        //
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textViewNome;
        TextView textViewMessage;
        TextView textViewData;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.imageViewMessage);
            textViewNome = view.findViewById(R.id.textViewNomeMessage);
            textViewData = view.findViewById(R.id.textViewDataMessage);
            textViewMessage = view.findViewById(R.id.textViewMessagemMessage);
        }
    }

    class CustomAdapter extends BaseAdapter {

        private List<Mensagem> mensagens;

        public CustomAdapter(List<Mensagem> mensagens) {
            this.mensagens = mensagens;
        }

        @Override
        public int getCount() {
            return this.mensagens.size();
        }

        @Override
        public Object getItem(int position) {
            return this.mensagens.get(position);
        }

        @Override
        public long getItemId(int position) {
            return this.mensagens.get(position).getRemetente();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListMessageFragment.ViewHolder viewHolder;
            Mensagem msg = mensagens.get(position);
            SimpleDateFormat hour_minute = new SimpleDateFormat("hh:mm");
            SimpleDateFormat no_time = new SimpleDateFormat("dd/M/yyyy");
            Date data1;
            Date data2;

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_message, parent, false);
                viewHolder = new ListMessageFragment.ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ListMessageFragment.ViewHolder) convertView.getTag();
            }


            try {
                data1 = no_time.parse(no_time.format(msg.getData()));
                data2 = no_time.parse(no_time.format(Calendar.getInstance().getTime()));

                long diff = data2.getTime() - data1.getTime();

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long elapsedDays = diff / daysInMilli;

                //viewHolder.imageView.setImageResource(imagem);
                viewHolder.textViewNome.setText(msg.getFullName());
                viewHolder.textViewMessage.setText(msg.getMensagem());

                if (elapsedDays == 0)
                    viewHolder.textViewData.setText(hour_minute.format(msg.getData()));
                else
                if (elapsedDays == 1)
                    viewHolder.textViewData.setText("Ontem");
                else
                if (elapsedDays > 1)
                    viewHolder.textViewData.setText(DateFormat.getDateInstance().format(msg.getData()));

            } catch (ParseException e) {
                Log.e("PARSE", e.toString());
            }

            return convertView;
        }
    }
}
