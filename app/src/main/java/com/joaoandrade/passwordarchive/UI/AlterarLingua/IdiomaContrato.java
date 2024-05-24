package com.joaoandrade.passwordarchive.UI.AlterarLingua;

public interface IdiomaContrato {

    interface IdiomaView{
        void VerificarLinguagem();
    }

    interface IdiomaPresenter{
        void MudarLinguagem(String lingugem);
    }
}
