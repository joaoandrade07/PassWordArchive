package com.joaoandrade.passwordarchive.UI.Login;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseUser;

public interface LoginContrato {

    interface LoginView{

        void LoginComDigital();

        void IniciarTelaPrincipal();

        void MostrarError(View view, String error);


    }

    interface LoginPresenter{
        void Login(String email, String senha, View view);

        void VerficaDigital(String userId, LinearLayout digital);

        void VerificarUsuarioJaLogado(FirebaseUser usuarioAtual, EditText campoEmail, LinearLayout digital);
    }
}
