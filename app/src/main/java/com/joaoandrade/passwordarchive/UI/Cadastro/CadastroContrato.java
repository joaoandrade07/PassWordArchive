package com.joaoandrade.passwordarchive.UI.Cadastro;

import android.view.View;

public class CadastroContrato {

    interface CadastroView{
        void Mensagem(View view, String mensagem, int color);

        void LimparCampos();
    }

    interface CadastroPresenter{
        void Casdastrar(String email, String nome, String senha, String confirmarSenha, View view);

    }
}
