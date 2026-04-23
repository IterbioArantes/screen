package learning.com.br.screenMatch.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


public enum Categoria {
    ACAO("Action"),
    ROMANCE("Romance"),
    COMEDIA("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),
    NA("N/A");

    private String categoria;

    Categoria(String categoria) {
        this.categoria = categoria;
    }

    public static Categoria categoriaPrincipal(String listaCategorias){
        for(Categoria c : Categoria.values()){
            if(c.categoria.equalsIgnoreCase(listaCategorias.split(",")[0].trim()))
               return c;
        }
        return NA;
    }
}