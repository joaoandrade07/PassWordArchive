package com.joaoandrade.passwordarchive.UI.TelaConfiguracoes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ConfiguracoesPresenter extends  AppCompatActivity implements  ConfiguracoesContrato.ConfiguracoesPresenter {

    ConfiguracoesContrato.ConfiguracoesView view;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public ConfiguracoesPresenter(ConfiguracoesContrato.ConfiguracoesView view) {
        this.view = view;
    }

    @Override
    public void SalvarFoto(Uri foto) {
        db.collection("Usuarios").document(Objects.requireNonNull(auth.getUid()))
                .update("foto", foto).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("Update", "Sucesso!");
            }
        });
    }

    @Override
    public void AtivarBiometria(Boolean biometria) {//ativar ou desativar a biometria, e mudar o estado no bando de dados
        if (biometria){
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

    @Override
    public void BuscarDadosUsuario() {// buscar os dados do usuário no firebase
        if ((auth.getCurrentUser() != null)) {
            String userId = auth.getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener( (value, error) -> {
                if(value!= null) {
                    view.MostarDados(value);
                }
            });
        }
    }
}
