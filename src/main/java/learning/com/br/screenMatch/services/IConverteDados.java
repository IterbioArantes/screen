package learning.com.br.screenMatch.services;

public interface IConverteDados {

    <T> T obterDados(String json, Class<T> classe);
}
