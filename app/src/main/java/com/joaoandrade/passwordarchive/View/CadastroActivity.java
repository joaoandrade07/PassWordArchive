package com.joaoandrade.passwordarchive.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.ExternalListener;
import com.joaoandrade.passwordarchive.Model.UserModel;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.CadastroBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CadastroActivity extends AppCompatActivity {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CadastroBinding binding;

    UserModel user;

    ExternalListener externalListener = new ExternalListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        user = new UserModel();

        binding.voltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();

            }
        });

        binding.btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = binding.nome.getText().toString();
                String email = binding.email.getText().toString();
                String senha = binding.senha.getText().toString();
                String confirmarSenha = binding.confirmarSenha.getText().toString();

                if(nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()){

                    externalListener.SnackBar(view, getString(R.string.preencha_todos_campos), getColor(R.color.error), null);

                } else if (!senha.equals(confirmarSenha)) {

                    externalListener.SnackBar(view, getString(R.string.senha_diferentes), getColor(R.color.error), null);

                } else {
                    auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SalvarDadosUsuario(nome, email);
                                externalListener.SnackBar(view, getString(R.string.cadastro_sucesso), getColor(R.color.biometria_ativada), null);
                                binding.nome.setText("");
                                binding.email.setText("");
                                binding.senha.setText("");
                                binding.confirmarSenha.setText("");
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
//
                                externalListener.SnackBar(view, msg, getColor(R.color.error), getColor(R.color.white));

                            }
                        }
                    });
                }
            }
        });
    }


    private void SalvarDadosUsuario(String nome, String email){
        user.setNome(nome);
        Log.d("Nome do usuario", user.getNome());
        user.setEmail(email);
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", user.getNome());
        usuarios.put("email", user.getEmail());
        usuarios.put("biometria",false);
        usuarios.put("lingua", null);

        user.setId(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        DocumentReference documentReference = db.collection("Usuarios").document(user.getId());
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "Erro ao salvar os dados " + e.toString());
            }
        });
        
    }

}