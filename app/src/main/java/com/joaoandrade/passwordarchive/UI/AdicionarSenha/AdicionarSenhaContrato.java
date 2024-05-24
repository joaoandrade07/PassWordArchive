package com.joaoandrade.passwordarchive.UI.AdicionarSenha;

import android.view.View;
import android.widget.EditText;

public interface AdicionarSenhaContrato {

    interface AdicionarSenhaView{
        void MostrarErro(String message);

        void Sair();
    }

    interface AdicionarSenhaPresenter{
        void AtualizarDados(String contaAntiga, String conta, String usuario, String senha, String id);

        void SalvarDados(String conta, String usuario, String senha);

        void GerarSenha(Boolean b, EditText senha, View viewSenha);

    }
}
