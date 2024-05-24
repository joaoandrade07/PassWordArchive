package com.joaoandrade.passwordarchive.UI.AlterarNome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.UI.TelaPrincipal.HomeActivity;
import com.joaoandrade.passwordarchive.databinding.AlterarNomeBinding;

import java.util.Objects;

public class AlterarNomeActivity extends AppCompatActivity implements AlterarNomeContrato.AlterarNomeView{

    private AlterarNomeBinding binding;

    private ExternalListener externalListener;

    private AlterarNomeContrato.AlterarNomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AlterarNomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        externalListener = new ExternalListener();
        presenter = new AlterarNomePresenter(this, this);

        binding.voltarSettings.setOnClickListener(view -> {
            finish();
        });

        binding.voltarHome.setOnClickListener(view -> {
            startActivity(new Intent(AlterarNomeActivity.this, HomeActivity.class));
            finish();
        });

        binding.alterarNome.setOnClickListener(view -> {
            presenter.AlterarNome(view, binding.inputNome.getText().toString());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.PegarNome();
    }

    @Override
    public void LimparFoco() {
        binding.inputNome.clearFocus();
    }

    @Override
    public void MostarNome(DocumentSnapshot value) {
        binding.inputNome.setText(value.getString("nome"));
    }

    @Override
    public void MensagemSucesso(View view, String mensagem) {
        externalListener.SnackBar(view, mensagem, getColor(R.color.biometria_ativada), null);
    }
}