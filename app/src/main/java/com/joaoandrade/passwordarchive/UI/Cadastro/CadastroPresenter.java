package com.joaoandrade.passwordarchive.UI.Cadastro;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CadastroPresenter extends ContextWrapper implements CadastroContrato.CadastroPresenter{

    private final CadastroContrato.CadastroView view;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();


    public CadastroPresenter(Context base, CadastroContrato.CadastroView view) {
        super(base);
        this.view = view;
    }
    @Override
    public void Casdastrar(String email, String nome, String senha, String confirmarSenha, View v) {
        if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()){

            view.Mensagem(v,getString(R.string.preencha_todos_campos), getColor(R.color.error));

        } else if (!senha.equals(confirmarSenha)) {

            view.Mensagem(v,getString(R.string.senha_diferentes), getColor(R.color.error));

        } else {
            auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        auth.updateCurrentUser(Objects.requireNonNull(auth.getCurrentUser()));
                        SalvarDadosUsuario(email, nome);
                        view.LimparCampos();
                        view.Mensagem(v,getString(R.string.cadastro_sucesso), getColor(R.color.biometria_ativada));
                    }else{
                        String msg;
                        try {
                            throw Objects.requireNonNull(task.getException());
                        }catch (FirebaseAuthWeakPasswordException e){
                            msg = getString(R.string.erro_senha_pequena);
                        } catch (FirebaseAuthUserCollisionException e){
                            msg = getString(R.string.conta_ja_cadastrada);
                        }catch (FirebaseAuthInvalidCredentialsException e){
                            msg = getString(R.string.email_invalido);
                        } catch (FirebaseNetworkException e){
                            msg = getString(R.string.sem_internet);
                        }catch (Exception e) {
                            msg = getString(R.string.erro_cadastro_generico);
                        }
                        view.Mensagem(v,msg, getColor(R.color.error));

                    }
                }
            });
        }
    }


    private void SalvarDadosUsuario(String email, String nome) {
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarios.put("email", email);
        usuarios.put("biometria",false);
        usuarios.put("foto", null);

        DocumentReference documentReference = db.collection("Usuarios").document(Objects.requireNonNull(auth.getCurrentUser()).getUid());
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("db", "Sucesso ao salvar os dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db", "Erro ao salvar os dados " + e);
                    }
                });
    }
}
