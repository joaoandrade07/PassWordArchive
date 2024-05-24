package com.joaoandrade.passwordarchive.UI.AdicionarSenha;

import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.Network.ApiService;
import com.joaoandrade.passwordarchive.Network.Response.EmpresaResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdicionarSenhaPresenter implements AdicionarSenhaContrato.AdicionarSenhaPresenter{

    AdicionarSenhaContrato.AdicionarSenhaView view;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    public AdicionarSenhaPresenter(AdicionarSenhaContrato.AdicionarSenhaView view){
        this.view = view;
    }


    @Override
    public void AtualizarDados(String contaAntiga, String conta, String usuario, String senha, String id) {
        if (contaAntiga.equals(conta)) {
            db.collection("Usuarios").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).
                    collection("senhas").document(id)
                    .update( "usuario", usuario, "senha", senha)
                    .addOnSuccessListener(unused -> {
                        view.Sair();
                    });
        }else{
            AtualizarDados(conta, usuario, senha, id);
        }
    }


    private void AtualizarDados(String conta, String usuario, String senha, String id){
        ApiService.getInstace()
                .obterLogo(conta).enqueue(new Callback<List<EmpresaResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<EmpresaResponse>> call, @NonNull Response<List<EmpresaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String logo = null;
                            if(!response.body().isEmpty()){
                                logo = response.body().get(0).getLogo();
                                for(EmpresaResponse resp : response.body()) {
                                    if (resp.getName().equalsIgnoreCase(conta)){
                                        logo = resp.getLogo();
                                    }
                                }
                            }
                            db.collection("Usuarios").document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).
                                    collection("senhas").document(id)
                                    .update("conta",conta, "usuario", usuario, "senha", senha, "logo", logo)
                                    .addOnSuccessListener(unused -> {
                                        view.Sair();//Fechar a tela
                                    });
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<EmpresaResponse>> call, @NonNull Throwable t) {
                        view.MostrarErro("Erro ao salvar conta!");
                    }
                });
    }

    @Override
    public void SalvarDados(String conta, String usuario, String senha) {
        ApiService.getInstace()
                .obterLogo(conta).enqueue(new Callback<List<EmpresaResponse>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<EmpresaResponse>> call, @NonNull Response<List<EmpresaResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String logo = null;
                            if(!response.body().isEmpty()){
//                                logo = response.body().get(0).getLogo();
                                for(EmpresaResponse resp : response.body()) {
                                    if (resp.getName().equalsIgnoreCase(conta)){
                                        logo = resp.getLogo();
                                    }
                                }
                            }
                            SalvarDados(conta, usuario, senha, logo);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<EmpresaResponse>> call, @NonNull Throwable t) {
                        view.MostrarErro("Erro ao salvar conta!");
                    }
                });


    }

    private void SalvarDados(String conta, String usuario, String senha, String logo){
        Map<String, Object> listModel = new HashMap<>();
        listModel.put("id", null);
        listModel.put("conta", conta);
        listModel.put("usuario", usuario);
        listModel.put("senha", senha);
        listModel.put("logo", logo);
        listModel.put("iv", null);
        listModel.put("salt", null);
        DocumentReference documentReference = db.collection("Usuarios")//referencia um documento no banco de dados
                .document(Objects.requireNonNull(auth.getCurrentUser()).getUid()).collection("senhas").document();

        documentReference.set(listModel).addOnSuccessListener(unused -> {

            db.collection("Usuarios").document(Objects.requireNonNull(auth.getCurrentUser()).getUid())//atualiza um campo dentro do documento criado
                    .collection("senhas").document(documentReference.getId())
                    .update("id",documentReference.getId());
            view.Sair();//Fechar a tela

        });
    }

    @Override
    public void GerarSenha(Boolean isChecked, EditText senha, View viewSenha) {
        if(isChecked){
            senha.setText("");
            senha.setVisibility(View.GONE);
            viewSenha.setVisibility(View.GONE);
        }else{
            senha.setVisibility(View.VISIBLE);
            viewSenha.setVisibility(View.VISIBLE);
        }
    }
}
