package com.joaoandrade.passwordarchive.UI.AlterarLingua;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.joaoandrade.passwordarchive.UI.TelaPrincipal.HomeActivity;
import com.joaoandrade.passwordarchive.databinding.ActivityLanguageBinding;

public class IdiomaActivity extends AppCompatActivity implements IdiomaContrato.IdiomaView{
    private ActivityLanguageBinding binding;

    private IdiomaContrato.IdiomaPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        presenter = new IdiomaPresenter();

        binding.voltarSettings.setOnClickListener(v -> {
            finish();
        });

        binding.voltarHome.setOnClickListener(v ->{
            startActivity(new Intent(IdiomaActivity.this, HomeActivity.class));
            finish();
        });

        binding.portugues.setOnClickListener(v -> {
            presenter.MudarLinguagem("pt-BR");
        });

        binding.ingles.setOnClickListener(v -> {
            presenter.MudarLinguagem("en-US");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarLinguagem();
    }

//    private void MudarVisualizacao(ImageView idVisivel, ImageView idGone){
//        idVisivel.setVisibility(View.GONE);
//        idGone.setVisibility(View.VISIBLE);
//    }

    @Override
    public void VerificarLinguagem(){
        LocaleListCompat appLocale = AppCompatDelegate.getApplicationLocales();
        binding.ptBR.setVisibility(View.GONE);
        switch (appLocale.toLanguageTags()){
            case "en-US":
                binding.enUS.setVisibility(View.VISIBLE);
                break;
            case "pt-BR":
                binding.ptBR.setVisibility(View.VISIBLE);
                break;
        }
    }
}