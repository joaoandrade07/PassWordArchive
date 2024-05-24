package com.joaoandrade.passwordarchive.UI.AlterarNome;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.joaoandrade.passwordarchive.R;

import java.util.Objects;

public class AlterarNomePresenter extends ContextWrapper implements AlterarNomeContrato.AlterarNomePresenter{

    private final AlterarNomeContrato.AlterarNomeView view;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public AlterarNomePresenter(Context base, AlterarNomeContrato.AlterarNomeView view){
        super(base);
        this.view = view;
    }

    @Override
    public void PegarNome() {
        if ((auth.getCurrentUser() != null)) {
            String userId = auth.getCurrentUser().getUid();
            DocumentReference documentReference = db.collection("Usuarios").document(userId);
            documentReference.addSnapshotListener( (value, error) -> {
                if(value!=null){
//                    binding.inputNome.setText(value.getString("nome"));
                    view.MostarNome(value);
                }
            });
        }
    }

    @Override
    public void AlterarNome(View v, String nome) {
        db.collection("Usuarios").document(Objects.requireNonNull(auth.getUid())).update("nome", nome).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
//                externalListener.SnackBar(v, getString(R.string.nome_alterado), getColor(R.color.biometria_ativada), null);
                view.MensagemSucesso(v, getString(R.string.nome_alterado));
            }
        });
//        binding.inputNome.clearFocus();
        view.LimparFoco();
    }
}
