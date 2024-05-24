package com.joaoandrade.passwordarchive.UI.AlterarNome;

import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

public interface AlterarNomeContrato {

    interface AlterarNomeView{
        void LimparFoco();

        void MostarNome(DocumentSnapshot value);

        void MensagemSucesso(View view, String mensagem);
    }

    interface AlterarNomePresenter{
        void PegarNome();

        void AlterarNome(View view, String nome);
    }
}
