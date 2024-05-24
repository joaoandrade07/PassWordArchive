package com.joaoandrade.passwordarchive.UI.AdicionarSenha;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.joaoandrade.passwordarchive.Controller.ExternalListener;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.UI.TelaPrincipal.HomeContrato;
import com.joaoandrade.passwordarchive.databinding.ActivityAddNovaSenhaBinding;
import java.util.Objects;

public class AdicionarSenhaActivity extends BottomSheetDialogFragment implements AdicionarSenhaContrato.AdicionarSenhaView{

    private EditText Conta, Usuario, Senha;

    private TextView BtnCancelar, BtnSalvar;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch Switch;

    private ExternalListener externalListener;

    private boolean isUpdate = false;

    public static final String TAG = "ActionBottomDialog";

    private ActivityAddNovaSenhaBinding binding;

    AdicionarSenhaContrato.AdicionarSenhaPresenter presenter;

    public static AdicionarSenhaActivity newInstance(){ return new AdicionarSenhaActivity(); }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AdicionarSenha);
        binding = ActivityAddNovaSenhaBinding.inflate(getLayoutInflater());
        externalListener = new ExternalListener();
        presenter = new AdicionarSenhaPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_nova_senha, container, false);
        Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Senha = requireView().findViewById(binding.senha.getId());
        BtnCancelar = requireView().findViewById(binding.cancelarSenha.getId());
        BtnSalvar = requireView().findViewById(binding.salvarSenha.getId());
        Switch = requireView().findViewById(binding.gerarSenha.getId());
        Conta = requireView().findViewById(binding.conta.getId());
        Usuario = requireView().findViewById(binding.usuario.getId());

        final Bundle bundle = getArguments();
        if(bundle!= null){
            isUpdate = true;

            Conta.setText(bundle.getString("conta"));
            Usuario.setText(bundle.getString("usuario"));
            Senha.setText(bundle.getString("senha"));
//            Conta.setEnabled(false);
        }

        BtnCancelar.setOnClickListener(v -> {
            isUpdate = false;
            dismiss();
        });

        BtnSalvar.setOnClickListener(v -> {
            if(Usuario.getText().toString().isEmpty() || Conta.getText().toString().isEmpty() ||
                (!Switch.isChecked() && Senha.getText().toString().isEmpty())){
                    MostrarErro("Os campos não podem ficar vazio");
            }else if(isUpdate && bundle != null){
                presenter.AtualizarDados(bundle.getString("conta"), Conta.getText().toString(), Usuario.getText().toString(), Senha.getText().toString(), bundle.getString("id"));
            }else{
                presenter.SalvarDados(Conta.getText().toString(), Usuario.getText().toString(), Senha.getText().toString());
            }
        });

        Switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.GerarSenha(isChecked, Senha, requireView().findViewById(binding.viewSenha.getId()));
            }
        });
    }

    public void MostrarErro(String message){
        externalListener.IniciarToast(getContext(), message);
    }

    @Override
    public void Sair(){
        dismiss();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if(activity instanceof HomeContrato.HomeView){
            ((HomeContrato.HomeView) activity).DialogCloseListener(dialog);
        }
    }
}