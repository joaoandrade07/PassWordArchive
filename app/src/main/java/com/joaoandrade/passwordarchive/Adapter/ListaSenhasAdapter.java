package com.joaoandrade.passwordarchive.Adapter;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.UI.AdicionarSenha.AdicionarSenhaActivity;
import com.joaoandrade.passwordarchive.Model.ListModel;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.UI.TelaPrincipal.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class ListaSenhasAdapter extends RecyclerView.Adapter<ListaSenhasAdapter.ListaSenhasViewHolder> {

    private ArrayList<ListModel> listModel;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final HomeActivity activity;


    public ListaSenhasAdapter(HomeActivity activity){//ArrayList<ListModel> listModel, HomeActivity activity
        this.activity = activity;
    }



    @NonNull
    @Override
    public ListaSenhasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_senha, parent, false);
        return new ListaSenhasViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ListaSenhasViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ListModel item = listModel.get(position);
        holder.conta.setText(item.getConta());
//        if(item.getUsuario().length()> )
        holder.usuario.setText(item.getUsuario());
        holder.senha.setText(item.getSenha());
        if(item.getLogo() != null){
            Picasso.get().load(item.getLogo()).into(holder.logo);
        }else {
            holder.logo.setImageDrawable(activity.getDrawable(R.drawable.image_world));
        }

        holder.verSenha.setOnClickListener(v -> {
            holder.senha.setInputType(InputType.TYPE_CLASS_TEXT);
            holder.verSenha.setVisibility(View.GONE);
            holder.esconderSenha.setVisibility(View.VISIBLE);
        });

        holder.esconderSenha.setOnClickListener(v -> {
            holder.senha.setInputType(129);
            holder.esconderSenha.setVisibility(View.GONE);
            holder.verSenha.setVisibility(View.VISIBLE);
        });

        holder.copiarConta.setOnClickListener(v -> {
            copiarTexto(holder.usuario.getText().toString());
        });

        holder.copiarSenha.setOnClickListener(v -> {
            copiarTexto(holder.senha.getText().toString());
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("RtlHardcoded")
            @Override
            public boolean onLongClick(View v) {
                PopupMenu popupMenu = new PopupMenu(holder.itemView.getContext(), holder.itemView);

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.delete){
                            deleteItem(position);
                        }else {
                            editItem(position);
                        }
                        return true;
                    }
                });
                popupMenu.inflate(R.menu.pop_up_menu);
                popupMenu.setGravity(Gravity.RIGHT);
                popupMenu.show();
                return true;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTask(ArrayList<ListModel> listModel) {
        this.listModel = listModel;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filtro(String text, ArrayList<ListModel> list){//
        if(text.isEmpty()){
            listModel.clear();
            listModel.addAll(list);
        }else{
            ArrayList<ListModel> result = new ArrayList<>();
            text = text.toLowerCase();
            for (ListModel item: list){
                if(item.getConta().toLowerCase().contains(text)){
                    result.add(item);
                }
            }
            listModel.clear();
            listModel.addAll(result);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(listModel != null){
            return listModel.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void deleteItem(int position){
        ListModel item = listModel.get(position);
        listModel.remove(position);
        db.collection("Usuarios").document(Objects.requireNonNull(auth.getCurrentUser())
                        .getUid()).collection("senhas")
                .document(item.getId()).delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Erro ao apagar senha", e.toString());
                    }
                });
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


    private void editItem(int position){
        ListModel item = listModel.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("id",item.getId());
        bundle.putString("conta",item.getConta());
        bundle.putString("usuario", item.getUsuario());
        bundle.putString("senha", item.getSenha());
        AdicionarSenhaActivity fragment = new AdicionarSenhaActivity();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AdicionarSenhaActivity.TAG);
    }

    private void copiarTexto(String texto){
        ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("abc", texto);
        clipboard.setPrimaryClip(clipData);
    }

    static class ListaSenhasViewHolder extends RecyclerView.ViewHolder{
        ImageView logo, copiarConta, copiarSenha, verSenha, esconderSenha;
        TextView conta, usuario, senha;
        public ListaSenhasViewHolder(@NonNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.logo_empresa);
            copiarConta = itemView.findViewById(R.id.copiar_conta);
            copiarSenha = itemView.findViewById(R.id.copiar_senha);
            verSenha = itemView.findViewById(R.id.ver_senha);
            esconderSenha = itemView.findViewById(R.id.esconder_senha);
            conta = itemView.findViewById(R.id.conta);
            usuario = itemView.findViewById(R.id.usuario);
            senha = itemView.findViewById(R.id.senha);
        }
    }
}
