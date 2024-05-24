package com.joaoandrade.passwordarchive.UI.TelaPrincipal;

import android.content.DialogInterface;

import androidx.appcompat.widget.SearchView;

import com.google.firebase.firestore.QuerySnapshot;
import com.joaoandrade.passwordarchive.Model.ListModel;
import com.joaoandrade.passwordarchive.Model.No;

import java.util.ArrayList;
import java.util.List;

public interface HomeContrato {

    interface HomeView{
        void MostrarLista(ArrayList<ListModel> listaTemporaria);

        void DialogCloseListener(DialogInterface dialog);

        void imprimirEmOrdem(No noAtual);
    }

    interface HomePresenter{
        void PegarTodasAsListas(String userId);

        void MergeSort(List<ListModel> lista);

    }


}
