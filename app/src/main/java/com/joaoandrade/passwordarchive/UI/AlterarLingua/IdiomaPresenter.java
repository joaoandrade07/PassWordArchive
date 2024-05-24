package com.joaoandrade.passwordarchive.UI.AlterarLingua;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

public class IdiomaPresenter implements IdiomaContrato.IdiomaPresenter{

    public IdiomaPresenter(){

    }

    @Override
    public void MudarLinguagem(String lingugem){
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(lingugem);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }
}
