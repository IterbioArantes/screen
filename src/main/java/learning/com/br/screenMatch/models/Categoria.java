package learning.com.br.screenMatch.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


public enum Categoria {
    ACAO("Action","acao"),
    ROMANCE("Romance","romance"),
    COMEDIA("Comedy","comedia"),
    DRAMA("Drama","drama"),
    CRIME("Crime","crime"),
    NA("N/A","");

    private String categoria;
    private String categoriaPtbr;

    Categoria(String categoria, String categoriaPtbr) {

        this.categoria = categoria;
        this.categoriaPtbr = categoriaPtbr;
    }

    public static Categoria categoriaPrincipal(String listaCategorias){
        for(Categoria c : Categoria.values()){
            if(c.categoria.equalsIgnoreCase(listaCategorias.split(",")[0].trim()))
               return c;
        }
        return NA;
    }

    public static Categoria categoriaPrincipalPtbr(String categoria){
        for(Categoria c : Categoria.values()){
            if(c.categoriaPtbr.equalsIgnoreCase(categoria.split(",")[0].trim()))
                return c;
        }
        return NA;
    }
}