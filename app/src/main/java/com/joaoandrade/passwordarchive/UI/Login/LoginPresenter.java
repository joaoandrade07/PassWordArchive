package com.joaoandrade.passwordarchive.UI.Login;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.LoginBinding;


public class LoginPresenter extends ContextWrapper implements LoginContrato.LoginPresenter{

    private final LoginContrato.LoginView view;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public LoginPresenter(Context base, LoginContrato.LoginView view) {
        super(base);
        this.view = view;
    }
    @Override
    public void Login(String email, String senha, View v) {
        if (email.isEmpty() || senha.isEmpty()) {
//                    externalListener.SnackBar(view, getString(R.string.preencha_todos_campos), getColor(R.color.error), null);
            view.MostrarError(v, getString(R.string.preencha_todos_campos));
        } else {
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        view.IniciarTelaPrincipal();
                    } else {
                        view.MostrarError(v, getString(R.string.erro_ao_fazer_login));
                    }
                }
            });
        }

    }

    @Override
    public void VerficaDigital(String userId, LinearLayout digital) {
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    if (value.getBoolean("biometria") == Boolean.TRUE) {
                        digital.setVisibility(View.VISIBLE);// caso esteja seta o botão para entrar com a
                        view.LoginComDigital();                                          // digital visivel
                        Log.d("Biometria_teste", "TRUE");
                    } else {
                        Log.d("Biometria_teste", "FALSE");
                    }
                }
            }
        });
    }

    @Override
    public void VerificarUsuarioJaLogado(FirebaseUser usuarioAtual, EditText campoEmail, LinearLayout digital) {
        if (usuarioAtual != null ) {            //nesse ap, no celular em que estiver
            Log.d("ID USUARIO", usuarioAtual.getUid());
            String email = usuarioAtual.getEmail();
            campoEmail.setText(email);
            VerficaDigital(usuarioAtual.getUid(), digital);
            campoEmail.addTextChangedListener(new TextWatcher() {// verifica se o usuario esta editando o email
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {// caso esteja faz uma ação

                    if(digital.getVisibility() != View.GONE){// se o botao para entrar com a digital estiver visivel
                        digital.setVisibility(View.GONE);    // seta o botão para entrar com a digital como gone
                        Log.d("View", "Diferente");                     // para que ele não esteja mais disponivel
                    } else if(digital.getVisibility() == View.GONE){
                        Log.d("View", "Igual");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) { // caso o email digitado for igual ao do usuario logado
                    //vai setar o botao de entrar com a digital como visivel
                    String email = campoEmail.getText().toString();
                    if(email.equals(usuarioAtual.getEmail())){
                        VerficaDigital(usuarioAtual.getUid(), digital);
                    }
                }
            });

        }
    }
}
