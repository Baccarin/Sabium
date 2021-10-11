package br.com.sabium.enumeration.pessoa;

import br.com.sabium.controller.exception.TurnoDontExistException;

public enum Turno {
    
    Manha("Manha"), Tarde("Tarde") , Noite("Noite");
    
    private final String descricao;

    Turno(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeTurno() {
        return descricao;
    }
    
    public static Turno converte(String turno) {
    	try {
    		return Turno.valueOf(turno);
    	}catch (Exception e) {
    		throw new TurnoDontExistException();
    	}
    }

}