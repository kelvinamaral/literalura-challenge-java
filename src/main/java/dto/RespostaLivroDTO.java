package dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RespostaLivroDTO {

    @JsonProperty("results")
    private List<LivroDTO> livros;

    public List<LivroDTO> getLivros(){
        return livros;
    }

    public void setLivros(List<LivroDTO> livros){
        this.livros = livros;
    }




}
