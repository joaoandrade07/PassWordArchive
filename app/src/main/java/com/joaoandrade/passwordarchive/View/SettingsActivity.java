package com.joaoandrade.passwordarchive.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.joaoandrade.passwordarchive.Model.UserModel;
import com.joaoandrade.passwordarchive.databinding.SettingsBinding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private SettingsBinding binding;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

//    private UserModel user;

    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        user = new UserModel();


        binding.voltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        binding.alterarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                startActivity(new Intent(SettingsActivity.this, AlterarNomeActivity.class));
            }
        });

        binding.alterarEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this, AlterarEmailActivity.class));
            }
        });

        binding.sairConta
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.ativarBiometria.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
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
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if ((auth.getCurrentUser() != null)) {
            String userId = auth.getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener( (value, error) -> {
                if(Objects.requireNonNull(value).getBoolean("biometria") == Boolean.TRUE){
                    binding.ativarBiometria.setChecked(Boolean.TRUE);
                }
                binding.userName.setText(value.getString("nome"));
            });
        }
    }
}