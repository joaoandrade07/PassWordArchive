package com.joaoandrade.passwordarchive.UI.TelaPrincipal;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.joaoandrade.passwordarchive.Model.Arvore;
import com.joaoandrade.passwordarchive.Model.ListModel;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter extends ContextWrapper implements HomeContrato.HomePresenter{

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final HomeContrato.HomeView view;

    private final Arvore arvore = new Arvore();

    String[] letras = { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};


    String maior = "";

    String atual = "";

    public HomePresenter(Context context, HomeContrato.HomeView view) {
        super(context);
        this.view = view;
    }

    public void PegarTodasAsListas(String userId){
        db.collection("Usuarios").document(userId).collection("senhas")
                .get().addOnSuccessListener(value -> {
                    ArrayList<ListModel> listaTemporaria = new ArrayList<ListModel>();
                    int contador = 0;
                    for(DocumentSnapshot ds : value.getDocuments()){
                        listaTemporaria.add(ds.toObject(ListModel.class));
                        listaTemporaria.get(contador).setId(ds.getId());
                        arvore.addNo(listaTemporaria.get(contador).getConta(), listaTemporaria.get(contador).getUsuario(),
                                listaTemporaria.get(contador).getSenha());
                        Log.d("ListaTemporaria", "Conta: " + listaTemporaria.get(contador).getConta()+ ", Usuário: " +
                                listaTemporaria.get(contador).getUsuario() +", Senha: " + listaTemporaria.get(contador).getSenha());
                        contador++;
                    }
                    view.imprimirEmOrdem(arvore.getRaiz());
                    view.MostrarLista(listaTemporaria);
                });
    }

    public void MergeSort(List<ListModel> lista) {
        MergeSort(lista, 0, lista.size() - 1);
    }

    private static void MergeSort(List<ListModel> lista, int inicio, int fim) {
        if (inicio >= fim) return;

        int meio = (inicio + fim) / 2;

        MergeSort(lista, inicio, meio);
        MergeSort(lista, meio + 1, fim);
        Merge(lista, inicio, meio, fim);
    }

    private static void Merge(List<ListModel> lista, int inicio, int meio, int fim) {

        int i = inicio;
        int j = meio + 1;

        List<ListModel> temp = new ArrayList<>(fim - inicio + 1);

        while (i <= meio && j <= fim) {
            ListModel a = lista.get(i);
            ListModel b = lista.get(j);
            if (a.getConta().toLowerCase().compareTo(b.getConta().toLowerCase())< 0) {//a < b
                temp.add(a);
                i++;
            } else {
                temp.add(b);
                j++;
            }
        }

        while (i <= meio) {
            temp.add(lista.get(i));
            i++;
        }

        while (j <= fim) {
            temp.add(lista.get(j));
            j++;
        }

        for (int k = 0; k < temp.size(); k++) {
            lista.set(k + inicio, temp.get(k));
        }
    }

}
