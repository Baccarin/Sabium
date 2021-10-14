package br.com.sabium.enumeration.pessoa;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.sabium.controller.exception.TurnoDontExistException;

public enum Turno {
    
    @JsonProperty("Manha")
    MANHA("Manha"),
    @JsonProperty("Tarde")
    TARDE("Tarde") , 
    @JsonProperty("Noite")
    NOITE("Noite");
    
    private final String descricao;

    Turno(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeTurno() {
        return descricao;
    }
    
    public static Turno converte(String turno) {
    	try {
    		return Turno.valueOf(turno.toUpperCase());
    	}catch (Exception e) {
    		throw new TurnoDontExistException();
    	}
    }

}