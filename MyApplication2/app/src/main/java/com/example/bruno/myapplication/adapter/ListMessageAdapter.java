package com.example.bruno.myapplication.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bruno.myapplication.R;
import com.example.bruno.myapplication.retrofit.Mensagem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ListMessageAdapter extends RecyclerView.Adapter<ListMessageAdapter.CustomViewHolder> {

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    private List<Mensagem> mensagens;
    private Context context;
    private OnItemClicked onClick;

    public ListMessageAdapter(List<Mensagem> mensagens, Context context) {
        this.mensagens = mensagens;
        this.context = context;
    }

    public ListMessageAdapter(List<Mensagem> mensagens, Context context, OnItemClicked onClick) {
        this.mensagens = mensagens;
        this.context = context;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_message_recycler, parent, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Mensagem msg = mensagens.get(position);

        SimpleDateFormat hour_minute = new SimpleDateFormat("hh:mm");
        SimpleDateFormat no_time = new SimpleDateFormat("dd/M/yyyy");
        Date data1;
        Date data2;

        try {
            data1 = no_time.parse(no_time.format(msg.getData()));
            data2 = no_time.parse(no_time.format(Calendar.getInstance().getTime()));

            long diff = data2.getTime() - data1.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = diff / daysInMilli;

            //holder.imageView.setImageResource(imagem);
            holder.textViewNome.setText(msg.getFullName());
            holder.textViewMessage.setText(msg.getMensagem());

            if (elapsedDays == 0)
                holder.textViewData.setText(hour_minute.format(msg.getData()));
            else
            if (elapsedDays == 1)
                holder.textViewData.setText("Ontem");
            else
            if (elapsedDays > 1)
                holder.textViewData.setText(DateFormat.getDateInstance().format(msg.getData()));

        } catch (ParseException e) {
            Log.e("PARSE", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textViewNome;
        TextView textViewMessage;
        TextView textViewData;

        public CustomViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageViewMessage);
            textViewNome = itemView.findViewById(R.id.textViewNomeMessage);
            textViewData = itemView.findViewById(R.id.textViewDataMessage);
            textViewMessage = itemView.findViewById(R.id.textViewMessagemMessage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            if (onClick != null)
                onClick.onItemClick(view, position);
        }
    }
}
