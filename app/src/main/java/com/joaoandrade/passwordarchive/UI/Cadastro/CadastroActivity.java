package com.joaoandrade.passwordarchive.UI.Cadastro;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.databinding.CadastroBinding;

public class CadastroActivity extends AppCompatActivity implements CadastroContrato.CadastroView{

    private CadastroBinding binding;

    private CadastroContrato.CadastroPresenter presenter;

//    UserModel user;

    ExternalListener externalListener = new ExternalListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CadastroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        presenter = new CadastroPresenter(this, this);

//        user = new UserModel();

        binding.voltarLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        binding.btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               presenter.Casdastrar(binding.email.getText().toString(), binding.nome.getText().toString(),
                       binding.senha.getText().toString(),binding.confirmarSenha.getText().toString(), view);
            }
        });
    }

    @Override
    public void Mensagem(View view, String mensagem, int color) {
        externalListener.SnackBar(view, mensagem, color, null);
    }

    @Override
    public void LimparCampos() {
        binding.nome.setText("");
        binding.email.setText("");
        binding.senha.setText("");
        binding.confirmarSenha.setText("");
    }
}