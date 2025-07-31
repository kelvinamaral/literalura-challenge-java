package service;

public interface IConvertDados {

    <T> T obterDados(String Json, Class<T> tClass);
}
