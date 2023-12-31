package com.joaoandrade.passwordarchive.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.joaoandrade.passwordarchive.databinding.ActivityLanguageBinding;

public class LanguageActivity extends AppCompatActivity {
    private ActivityLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.voltarSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.voltarHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LanguageActivity.this, HomeActivity.class));
                finish();
            }
        });

        binding.portugues.setOnClickListener(v -> {
            MudarLinguagem("pt-BR");
        });

        binding.ingles.setOnClickListener(v -> {
            MudarLinguagem("en-US");
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarLinguagem();
    }

    private void MudarVisualizacao(ImageView idVisivel, ImageView idGone){
        idVisivel.setVisibility(View.GONE);
        idGone.setVisibility(View.VISIBLE);
    }

    private void VerificarLinguagem(){
        LocaleListCompat appLocale = AppCompatDelegate.getApplicationLocales();
        if(appLocale.toLanguageTags().equals("en-US") ){
            MudarVisualizacao(binding.ptBR, binding.enUS);
        }
    }

    private void MudarLinguagem(String lingugem){
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(lingugem);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }
}