package service;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertDados implements IConvertDados {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        return null;
    }
}
