package com.joaoandrade.passwordarchive.UI.TelaPrincipal;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.joaoandrade.passwordarchive.Adapter.ListaSenhasAdapter;
import com.joaoandrade.passwordarchive.Model.No;
import com.joaoandrade.passwordarchive.UI.AdicionarSenha.AdicionarSenhaActivity;
import com.joaoandrade.passwordarchive.Model.ListModel;
import com.joaoandrade.passwordarchive.R;
import com.joaoandrade.passwordarchive.UI.TelaConfiguracoes.ConfiguracoesActivity;
import com.joaoandrade.passwordarchive.databinding.HomeBinding;
import java.util.ArrayList;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity implements HomeContrato.HomeView{

    private ArrayList<ListModel> listSenhas;

    private ListaSenhasAdapter senhasAdapter;

    private HomePresenter presenter;

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

//    AlgoritmoLCS algoritmoLCS;

    String[] letras = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", " "};


    String maior = "";

    String atual = null;


//    private UserModel user;

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeBinding binding = HomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        presenter = new HomePresenter(this, this);
        listSenhas = new ArrayList<>();

        senhasAdapter = new ListaSenhasAdapter(this);//listSenhas

//        Log.d("TESTE DE QUEM VEM PRIMEIRO", String.valueOf("ã".compareTo("á")));
//        Log.d("TESTE DE QUEM VEM PRIMEIRO", String.valueOf("a".compareTo("é")));


        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(senhasAdapter);

        presenter.PegarTodasAsListas(Objects.requireNonNull(auth.getCurrentUser()).getUid());

//        System.out.println("  texto com   espaços   em  branco".replaceAll("\\s+", ""));

        binding.toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(this, ConfiguracoesActivity.class));
        });

        binding.menuItem1.setOnClickListener(view -> {
            Log.d("Maior sequencia", maior);
            AdicionarSenhaActivity.newInstance().show(getSupportFragmentManager(), AdicionarSenhaActivity.TAG);
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void MostrarLista(ArrayList<ListModel> listaTemporaria) {
        listSenhas.clear();
        listSenhas.addAll(listaTemporaria);
        presenter.MergeSort(listSenhas);
        senhasAdapter.setTask(listSenhas);
        senhasAdapter.notifyDataSetChanged();
    }

    @Override
    public void DialogCloseListener(DialogInterface dialog){
        presenter.PegarTodasAsListas(Objects.requireNonNull(auth.getCurrentUser()).getUid());
    }


    //Para criar as opções do menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    //Quando selecionar um item do menu faz tal ação
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.searchView) {
            ArrayList<ListModel> copy = new ArrayList<>(listSenhas);
            SearchView searchView = (SearchView) item.getActionView();
            if (searchView != null) {
                searchView.setMaxWidth(Integer.MAX_VALUE);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        senhasAdapter.filtro(query, copy);
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        senhasAdapter.filtro(newText, copy);
                        return true;
                    }
                });
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void imprimirEmOrdem(No noAtual) {
        if (noAtual != null) {
            imprimirEmOrdem(noAtual.getEsquerda());
            imprimirEmOrdem(noAtual.getDireita());
            verificarMaiorPalavra(noAtual.getConta().toLowerCase());//vai verificar a maior sequencia dentro de cada
                                                                    //conta
            Log.d("ArvoreBinaria", "Conta: " + noAtual.getConta() + ", Usuário: " + noAtual.getUsuario() + ", Senha: " + noAtual.getSenha());
        }
    }


    private void verificarMaiorPalavra(String palavra){
//        String atual = null;
        for(int i = 0; i < palavra.length(); i++){//percorre a frase
            if(atual == null){//verifica se a maior sequencia atual é nula
                atual = String.valueOf(palavra.charAt(i));//se for adiciona aa primeira letra
            }else{
                int letraPosicao = buscaBinaria(letras, //verifica a posição da ultima letra da maior
                        String.valueOf(atual.charAt(atual.length() - 1)));//sequencia atual no array de letras

            //verifica se a ultima letra da palavra atual mais um é igual a letra na posição i
                if(String.valueOf(palavra.charAt(i)).equals(letras[letraPosicao + 1]) && letraPosicao != -1){
                    atual = atual.concat(String.valueOf(palavra.charAt(i)));
                    //se for adiciona a palavra atual
                }else{
                    if(atual.length()> maior.length()){// se não for verifica se a palavra atual é maior que a maior palavra atualmente
                        maior = atual;//se for a nova maior palavra vai ser a atual
                    }
                    atual = String.valueOf(palavra.charAt(i));//iguala a letra na posicao i na palavra atual
                }
            }
        }
    }

    private static int buscaBinaria(String[] array, String letra){
        return buscaBinaria(array, letra, 0, array.length-1);
    }

    private static int buscaBinaria(String[] array, String letra, int inicio, int fim) {
        int meio = (inicio+fim)/2;
        if (inicio <= fim) {

            if (array[meio].equals(letra)) {
                return meio;
            }
            if (array[meio].compareTo(letra) < 0) {
                return buscaBinaria(array, letra, meio+1, fim);
            }
            else {
                return buscaBinaria(array, letra, inicio, meio-1);
            }
        }
        return -1;
    }


}