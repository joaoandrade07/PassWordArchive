package com.joaoandrade.passwordarchive.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.Controller.Notificacoes;
import com.joaoandrade.passwordarchive.Model.MessageEventBus;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.SettingsBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private SettingsBinding binding;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Notificacoes notificacoes;


    private View v;

    //Iniciar a activity e pegar o código para escolher a foto
    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        binding.userImage.setImageURI(result.getData().getData());
                        Log.d("Caminho da imgaem", Objects.requireNonNull(result.getData().getData()).toString());
                        EventBus.getDefault().post(new MessageEventBus(getString(R.string.foto_alterada), v));
                        db.collection("Usuarios").document(Objects.requireNonNull(auth.getUid()))
                                .update("foto", result.getData().getData()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Update", "Sucesso!");
                            }
                        });
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificacoes = new Notificacoes(this);

        //voltar para a tela home
        binding.voltarHome.setOnClickListener(view -> {
            startActivity(new Intent(this, HomeActivity.class));
        });

        //selecionar imagem
        binding.selecionarImagem.setOnClickListener(view -> {
            Intent iGAlerry = new Intent(Intent.ACTION_PICK);
            iGAlerry.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mStartForResult.launch(iGAlerry);
            v = view;
        });

        //iniciar tela de alterar nome
        binding.alterarNome.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, AlterarNomeActivity.class));
        });


        //iniciar tela de alterar email
        binding.alterarEmail.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, AlterarEmailActivity.class));
        });


        //sair da conta
        binding.sairConta.setOnClickListener(view -> {
            notificacoes.deleteNotificationChannel(null);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            finish();
        });


        //ativar ou desativar  a biometria
        binding.ativarBiometria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                AtivarBiometria(b);
            }
        });

        //inicar a tela de mudar de idioma
        binding.mudarIdioma.setOnClickListener(view -> {
            startActivity(new Intent(SettingsActivity.this, LanguageActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        BuscaDadosUsuario();//buscar os dados do usuario
        EventBus.getDefault().register(this);//registar um evento no event bus
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);//parar com os registros no event bus
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);//parar com os registros no event bus
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)// fazer uma ação quando o event bus receber uma mensagem
    public void onMessageEvent(MessageEventBus event) {
        ExternalListener externalListener = new ExternalListener();
        externalListener.SnackBar(event.getView(), event.message, getColor(R.color.biometria_ativada), null);
    }

    private void BuscaDadosUsuario(){// buscar os dados do usuário no firebase
        if ((auth.getCurrentUser() != null)) {
            String userId = auth.getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener( (value, error) -> {
                if(value!= null) {
                    if (Objects.requireNonNull(value).getBoolean("biometria") == Boolean.TRUE) {
                        binding.ativarBiometria.setChecked(Boolean.TRUE);
                    }
                    binding.userName.setText(value.getString("nome"));

                    if (Objects.requireNonNull(value).getString("foto") != null) {
                        binding.userImage.setImageURI(Uri.parse(value.getString("foto")));
                    }
                }
            });
        }
    }

    private void AtivarBiometria(boolean b){//ativar ou desativar a biometria, e mudar o estado no bando de dados
        if (b){
            db.collection("Usuarios").document(Objects.requireNonNull(auth.getUid())).update("biometria", Boolean.TRUE).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Update", "Sucesso!");
                }
            });
        }else{
            db.collection("Usuarios").document(Objects.requireNonNull(auth.getUid())).update("biometria", Boolean.FALSE).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Update", "Sucesso!");
                }
            });
        }
    }
}