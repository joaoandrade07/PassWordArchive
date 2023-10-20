package com.joaoandrade.passwordarchive.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.databinding.AlterarNomeBinding;

import java.util.Objects;

public class AlterarNomeActivity extends AppCompatActivity {

    private AlterarNomeBinding binding;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private ExternalListener externalListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AlterarNomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        externalListener = new ExternalListener();

        binding.voltarSettings.setOnClickListener(view -> {
            finish();
        });

        binding.voltarHome.setOnClickListener(view -> {
            startActivity(new Intent(AlterarNomeActivity.this, HomeActivity.class));
            finish();
        });

        binding.alterarNome.setOnClickListener(view -> {
            alterarNome(view, binding.inputNome.getText().toString());
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pegarNome();
    }

    private void alterarNome(View v , String nome){
        db.collection("Usuarios").document(Objects.requireNonNull(auth.getUid())).update("nome", nome).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                externalListener.SnackBar(v, "Nome alterado com sucesso", getColor(R.color.biometria_ativada), null);
            }
        });
        binding.inputNome.clearFocus();
    }

    private void pegarNome(){
        if ((auth.getCurrentUser() != null)) {
            String userId = auth.getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener( (value, error) -> {
                assert value != null;
                binding.inputNome.setText(value.getString("nome"));
            });
        }
    }
}