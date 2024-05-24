package com.joaoandrade.passwordarchive.UI.TelaConfiguracoes;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.Controller.Notificacoes;
import com.joaoandrade.passwordarchive.Model.MessageEventBus;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.UI.AlterarEmail.AlterarEmailActivity;
import com.joaoandrade.passwordarchive.UI.AlterarLingua.IdiomaActivity;
import com.joaoandrade.passwordarchive.UI.AlterarNome.AlterarNomeActivity;
import com.joaoandrade.passwordarchive.UI.Login.LoginActivity;
import com.joaoandrade.passwordarchive.UI.TelaPrincipal.HomeActivity;
import com.joaoandrade.passwordarchive.databinding.SettingsBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

public class ConfiguracoesActivity extends AppCompatActivity implements ConfiguracoesContrato.ConfiguracoesView{

    private SettingsBinding binding;

    private Notificacoes notificacoes;

    private ConfiguracoesContrato.ConfiguracoesPresenter presenter;

    private View v;

    //Iniciar a activity e pegar o código para escolher a foto
    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        assert result.getData() != null;
                        binding.userImage.setImageURI(result.getData().getData());
                        Log.d("Caminho da imgaem", Objects.requireNonNull(result.getData()).toString());
                        EventBus.getDefault().post(new MessageEventBus(getString(R.string.foto_alterada), v));
                        presenter.SalvarFoto(result.getData().getData());

                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        notificacoes = new Notificacoes(this);
        presenter = new ConfiguracoesPresenter(this);

        //voltar para a tela home
        binding.voltarHome.setOnClickListener(view -> {
//            startActivity(new Intent(this, HomeActivity.class));
            finish();
        });

        //selecionar imagem
        binding.selecionarImagem.setOnClickListener(this::SelecionarFoto);

        //iniciar tela de alterar nome
        binding.alterarNome.setOnClickListener(view -> {
            startActivity(new Intent(ConfiguracoesActivity.this, AlterarNomeActivity.class));
        });


        //iniciar tela de alterar email
        binding.alterarEmail.setOnClickListener(view -> {
            startActivity(new Intent(ConfiguracoesActivity.this, AlterarEmailActivity.class));
        });


        //sair da conta
        binding.sairConta.setOnClickListener(view -> {
            notificacoes.deleteNotificationChannel(null);
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(ConfiguracoesActivity.this, LoginActivity.class));
            finish();
        });


        //ativar ou desativar  a biometria
        binding.ativarBiometria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                AtivarBiometria(b);
                presenter.AtivarBiometria(b);
            }
        });

        //inicar a tela de mudar de idioma
        binding.mudarIdioma.setOnClickListener(view -> {
            startActivity(new Intent(ConfiguracoesActivity.this, IdiomaActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.BuscarDadosUsuario();//buscar os dados do usuario
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


    @Override
    public void SelecionarFoto(View view){
        Intent iGAlerry;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R && android.os.ext.SdkExtensions.getExtensionVersion(android.os.Build.VERSION_CODES.R) >= 2) {
//            iGAlerry = new Intent(MediaStore.ACTION_PICK_IMAGES);
//            iGAlerry.setType("image/*");
//        }else{
//            iGAlerry = new Intent(Intent.ACTION_PICK);
//            iGAlerry.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        }
        iGAlerry = new Intent(Intent.ACTION_PICK);
        iGAlerry.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        mStartForResult.launch(iGAlerry);
        v = view;
    }

    @Override
    public void MostarDados(DocumentSnapshot value) {
        if (Objects.requireNonNull(value).getBoolean("biometria") == Boolean.TRUE) {
            binding.ativarBiometria.setChecked(Boolean.TRUE);
        }
        binding.userName.setText(value.getString("nome"));

        if (Objects.requireNonNull(value).getString("foto") != null) {
            binding.userImage.setImageURI(Uri.parse(value.getString("foto")));
        }
    }
}