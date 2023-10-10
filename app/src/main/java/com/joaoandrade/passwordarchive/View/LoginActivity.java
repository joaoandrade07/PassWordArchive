package com.joaoandrade.passwordarchive.View;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;


import android.annotation.SuppressLint;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joaoandrade.passwordarchive.AirPlaneModeReceiver;
import com.joaoandrade.passwordarchive.ExternalListener;
import com.joaoandrade.passwordarchive.Model.UserModel;
import com.joaoandrade.passwordarchive.Notificacoes;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.LoginBinding;

import java.util.Objects;
import java.util.concurrent.Executor;

public class LoginActivity extends AppCompatActivity {

    private LoginBinding binding;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final int REQUEST_CODE = 101010;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    ExternalListener externalListener = new ExternalListener();

    AirPlaneModeReceiver airPlaneModeReceiver = new AirPlaneModeReceiver();

    private final FirebaseUser usuarioAtual = auth.getCurrentUser();

    DocumentReference documentReference;

    Notificacoes notificacoes ;

    UserModel user;

//    String CHANNEL_ID = "Ola_sei_lá_oq";
//    int notificationId = 12345678;
//
//    CharSequence name = "Nome canal";
//    String description = "Descricao do canal";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerReceiver(
                airPlaneModeReceiver,
                new IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
        );
        user = new UserModel();
        notificacoes = new Notificacoes(this);
        binding = LoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.cadastreSe.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, CadastroActivity.class));
        });

        binding.entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.email.getText().toString();
                String senha = binding.senha.getText().toString();

                if (email.isEmpty() || senha.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, R.string.preencha_todos_campos, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.show();
                } else {
                    auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                IniciarTelaPrincipal();
                                notificacoes.notificacao(getString(R.string.bem_vindo), getString(R.string.login_sucesso));
                            } else {
                                Snackbar snackbar = Snackbar.make(view, R.string.erro_ao_fazer_login, Snackbar.LENGTH_SHORT);
                                snackbar.setBackgroundTint(Color.RED);
                                snackbar.setTextColor(Color.WHITE);
                                snackbar.show();
                            }
                        }
                    });
                }
            }
        });

        binding.entrarUsandoDigital.setOnClickListener(view -> {
            LoginComDigital();
        });


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




    @Override
    protected void onStart() {
        super.onStart();
        VerificarUsuarioJaLogado();
    }




    private void VerificaDigital(){

        String userId = usuarioAtual.getUid();
        DocumentReference documentReference = db.collection("Usuarios").document(userId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (Objects.requireNonNull(value).getBoolean("biometria") == Boolean.TRUE){
                    binding.entrarUsandoDigital.setVisibility(View.VISIBLE);
                    LoginComDigital();
                    Log.d("Biometria_teste", "TRUE");
                }else{
                    Log.d("Biometria_teste", "FALSE");
                }
            }
        });
    }

    private void VerificarUsuarioJaLogado(){

        if ((usuarioAtual != null)) {
            String email = Objects.requireNonNull(auth.getCurrentUser()).getEmail();
            binding.email.setText(email);
            VerificaDigital();
            binding.email.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    if(binding.entrarUsandoDigital.getVisibility() != View.GONE){
                        binding.entrarUsandoDigital.setVisibility(View.GONE);
                        Log.d("View", "Diferente");
                    } else if(binding.entrarUsandoDigital.getVisibility() == View.GONE){
                        Log.d("View", "Igual");
                    }
//                String dados = String.valueOf(binding.entrarUsandoDigital.getVisibility());
//                Log.d("Estado da View",dados );

                }

                @Override
                public void afterTextChanged(Editable editable) {

                    String email = binding.email.getText().toString();
                    if(email.equals(usuarioAtual.getEmail())){
                        VerificaDigital();
                    }
                }
            });

        }
    }


    public void LoginComDigital(){
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

        executor = ContextCompat.getMainExecutor(LoginActivity.this);
        biometricPrompt = new BiometricPrompt(LoginActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getApplicationContext(),
//                                "Authentication error: " + errString, Toast.LENGTH_SHORT)
//                        .show();


//                externalListener.IniciarToast( getApplicationContext(),"Operação cancelada");
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                IniciarTelaPrincipal();
                notificacoes.notificacao(getString(R.string.bem_vindo), getString(R.string.login_sucesso));
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
//                Toast.makeText(getApplicationContext(), "Authentication failed",
//                                Toast.LENGTH_SHORT)
//                        .show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.autenticacao))
                .setNegativeButtonText(getString(R.string.cancel_biometria))
                .build();

        biometricPrompt.authenticate(promptInfo);

    }

//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is not in the Support Library.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this.
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }
//
//
//
//
//    @SuppressLint("MissingPermission")
//    private void notificacao(String titulo, String descricao) {
//        notificationId = notificationId + 1;
//        Intent intent = new Intent(this, HomeActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle(titulo)
//                .setContentText(descricao)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText(descricao))
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(LoginActivity.this);
//// notificationId is a unique int for each notification that you must define.
//        notificationManager.notify(notificationId, builder.build());
//    }



}