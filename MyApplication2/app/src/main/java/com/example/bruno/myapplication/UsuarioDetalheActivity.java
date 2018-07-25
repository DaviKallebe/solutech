package com.example.bruno.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bruno.myapplication.retrofit.Comentario;
import com.example.bruno.myapplication.retrofit.RetrofitConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioDetalheActivity extends AppCompatActivity  implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_detalhe);

        Intent it = this.getIntent();

        TextView viewNome = findViewById(R.id.textViewNome);
        viewNome.setText(it.getStringExtra("nome"));
        TextView viewTelefone = findViewById(R.id.textViewTelefone);
        viewTelefone.setText(it.getStringExtra("telefone"));
        TextView viewDescricao = findViewById(R.id.textViewDescricaoUsuario);
        viewDescricao.setText(it.getStringExtra("descricao"));

        Call<List<Comentario>> call = new RetrofitConfig().getUsuarioService().getComments(it.getIntExtra("id_user", 0));

        call.enqueue(new Callback<List<Comentario>>() {
            @Override
            public void onResponse(Call<List<Comentario>> call, Response<List<Comentario>> response) {
                if (response.code() == 200) {
                    ListView listViewComentario = findViewById(R.id.fragment_hospedador_listagem_detalhe_perfil_recycler);
                    listViewComentario.setAdapter(new UsuarioDetalheActivity.CustomAdapter(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<Comentario>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //
    }

    static class ViewHolder {
        ImageView imageView;
        TextView textComentario;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.imageViewUsuarioDetalhadoPerfilComentario);
            textComentario = view.findViewById(R.id.textViewUsuarioDetalhadoComentario);
        }
    }

    class CustomAdapter extends BaseAdapter {

        private List<Comentario> comentarios;

        public CustomAdapter(List<Comentario> comentarios) {
            this.comentarios = comentarios;
        }

        @Override
        //numero de pets(?)
        public int getCount() {
            return this.comentarios.size();
        }

        @Override
        public Object getItem(int position) {
            return comentarios.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            Comentario comentario = comentarios.get(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_hospedador_listagem_detalhe_perfil_recycler, parent, false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            Spannable comment = new SpannableString(comentario.getFullName() + ' ' + comentario.getComentario());
            comment.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, comentario.getFullName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            //viewHolder.imageView.setImageResource(imagem);
            viewHolder.textComentario.setText(comment, TextView.BufferType.SPANNABLE);

            return convertView;
        }
    }
}
