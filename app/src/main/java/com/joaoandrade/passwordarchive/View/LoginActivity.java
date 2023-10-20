package com.joaoandrade.passwordarchive.View;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;


import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.provider.Settings;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.View;


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
import com.joaoandrade.passwordarchive.Controller.AirPlaneModeReceiver;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.Model.UserModel;
import com.joaoandrade.passwordarchive.Controller.Notificacoes;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.Controller.SingleClickListener;
import com.joaoandrade.passwordarchive.databinding.LoginBinding;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private LoginBinding binding;

    private FirebaseAuth auth;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int REQUEST_CODE = 101010;

    protected ExternalListener externalListener = new ExternalListener();

    protected AirPlaneModeReceiver airPlaneModeReceiver = new AirPlaneModeReceiver();

    private FirebaseUser usuarioAtual;

    private Notificacoes notificacoes ;

    protected UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(
                airPlaneModeReceiver,
                new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        );
        auth = FirebaseAuth.getInstance();
        usuarioAtual = auth.getCurrentUser();
        user = new UserModel();
        notificacoes = new Notificacoes(this);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cadastreSe.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        });

        binding.entrar.setOnClickListener(new SingleClickListener(3000) {
            @Override
            public void performClick(View view) {
                String email = binding.email.getText().toString();
                String senha = binding.senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Log.d("Senha Usuario", Objects.requireNonNull(usuarioAtual.getEmail()));
                    externalListener.SnackBar(view, getString(R.string.preencha_todos_campos), getColor(R.color.error), null);
                } else {
                    Login(email, senha, view);
                }
            }
        });

        binding.entrarUsandoDigital.setOnClickListener(view -> {
            LoginComDigital();
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarUsuarioJaLogado();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(airPlaneModeReceiver);
    }

    private void IniciarTelaPrincipal(){
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        finish();
    }


    private void Login(String email, String senha, View view){ // autentica o usuario no firebase para ver se a senha e email
                                                                //estão certos
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    notificacoes.notificacao(getString(R.string.bem_vindo), getString(R.string.login_sucesso), null);
                    IniciarTelaPrincipal();
                } else {
                    externalListener.SnackBar(view, getString(R.string.erro_ao_fazer_login), getColor(R.color.error), null);
                }
            }
        });
    }

    private void VerificaDigital(String userId){ //verifica se o usuario esta com a digital ativada
        
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null) {
                    if (value.getBoolean("biometria") == Boolean.TRUE) {
                        binding.entrarUsandoDigital.setVisibility(View.VISIBLE);// caso esteja seta o botão para entrar com a
                        LoginComDigital();                                          // digital visivel
                        Log.d("Biometria_teste", "TRUE");
                    } else {
                        Log.d("Biometria_teste", "FALSE");
                    }
                }
            }
        });
    }

    private void VerificarUsuarioJaLogado(){ //Verificar se já tem um usuario logado na instancia do Firebase
        if (usuarioAtual != null ) {            //nesse ap, no celular em que estiver
            Log.d("ID USUARIO", usuarioAtual.getUid());
            String email = usuarioAtual.getEmail();
            binding.email.setText(email);
            VerificaDigital(usuarioAtual.getUid());// verifica se o usuario ta com a digital ativada
            binding.email.addTextChangedListener(new TextWatcher() {// verifica se o usuario esta editando o email
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {// caso esteja faz uma ação

                    if(binding.entrarUsandoDigital.getVisibility() != View.GONE){// se o botao para entrar com a digital estiver visivel
                        binding.entrarUsandoDigital.setVisibility(View.GONE);    // seta o botão para entrar com a digital como gone
                        Log.d("View", "Diferente");                     // para que ele não esteja mais disponivel
                    } else if(binding.entrarUsandoDigital.getVisibility() == View.GONE){
                        Log.d("View", "Igual");
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) { // caso o email digitado for igual ao do usuario logado
                                                                    //vai setar o botao de entrar com a digital como visivel
                    String email = binding.email.getText().toString();
                    if(email.equals(usuarioAtual.getEmail())){
                        VerificaDigital(usuarioAtual.getUid());
                    }
                }
            });

        }
    }


    public void LoginComDigital(){ // faz o login do usuario com a digital
        BiometricManager biometricManager = BiometricManager.from(LoginActivity.this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(LoginActivity.this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                notificacoes.notificacao(getString(R.string.bem_vindo), getString(R.string.login_sucesso), null);
                IniciarTelaPrincipal();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.autenticacao))
                .setNegativeButtonText(getString(R.string.cancel_biometria))
                .build();

        biometricPrompt.authenticate(promptInfo);

    }
}