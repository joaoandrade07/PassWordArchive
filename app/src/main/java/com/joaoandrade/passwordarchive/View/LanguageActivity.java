package com.joaoandrade.passwordarchive.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.joaoandrade.passwordarchive.databinding.ActivityLanguageBinding;

public class LanguageActivity extends AppCompatActivity {

    ActivityLanguageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLanguageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}