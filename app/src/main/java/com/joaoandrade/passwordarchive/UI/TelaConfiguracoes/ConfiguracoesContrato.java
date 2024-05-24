package com.joaoandrade.passwordarchive.UI.TelaConfiguracoes;

import android.net.Uri;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;

public interface ConfiguracoesContrato {

    interface ConfiguracoesView{
//        void Mensagem(String msg, View v);

        void MostarDados(DocumentSnapshot value);

        void SelecionarFoto(View view);
    }

    interface ConfiguracoesPresenter{
        void SalvarFoto(Uri foto);

        void AtivarBiometria(Boolean biometria);

        void BuscarDadosUsuario();
    }
}
