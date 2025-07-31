package com.alura.literalura.service;

public interface IConvertDados {

    <T> T obterDados(String Json, Class<T> tClass);
}
