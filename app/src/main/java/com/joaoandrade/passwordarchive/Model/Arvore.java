package com.joaoandrade.passwordarchive.Model;


public class Arvore {
    private No raiz;

    public Arvore() {
        raiz = null;
    }

    public void addNo(String conta, String usuario, String senha) {
        No novoNo = new No(conta, usuario, senha);

        if (this.raiz == null) {
            this.raiz = novoNo;
        } else {
            addNoRec(raiz, novoNo);
        }
    }



    private void addNoRec(No noAtual, No novoNo) {
        int comparacao = noAtual.getConta().toLowerCase().compareTo(
                novoNo.getConta().toLowerCase()
        );//Double.compare(noAtual.getPorcentagem(), novoNo.getPorcentagem())

        if (comparacao > 0) {
            if (noAtual.getEsquerda() == null) {
                noAtual.setEsquerda(novoNo);
            } else {
                addNoRec(noAtual.getEsquerda(), novoNo);
            }
        } else if (comparacao < 0) {
            if (noAtual.getDireita() == null) {
                noAtual.setDireita(novoNo);
            } else {
                addNoRec(noAtual.getDireita(), novoNo);
            }
        }
    }

    // getters e setters...

    public No getRaiz() {
        return raiz;
    }

    public void setRaiz(No raiz) {
        this.raiz = raiz;
    }
}
