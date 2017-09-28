package com.jasper.exemploverificavel.entidades;

import java.util.ArrayList;
import java.util.List;

public class Produto {
    
    private String nome;
    
    private String codigo;

    public Produto(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
 
    public static List<Produto> mockaProdutos(){
        List<Produto> produtos = new ArrayList<>();
        
        for(int i = 0; i < 5; i++){
            String nome = "produto" + i;
            String codigo = ""+i;
            Produto p = new Produto(nome, codigo);
            produtos.add(p);
        }
        
        return produtos;
    }
    
}
