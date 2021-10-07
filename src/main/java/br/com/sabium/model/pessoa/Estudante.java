package br.com.sabium.model.pessoa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import br.com.sabium.model.administrativo.Matricula;
import br.com.sabium.model.administrativo.Turno;

@Entity
public class Estudante extends Pessoa {
    
    private Turno turno;
    
    @OneToMany(mappedBy = "estudante")
    private List<Matricula> matriculas;

    public Estudante() {
    	
    }
    
    public Estudante(String nome, String cpf, String sexo){
        super(nome,cpf,sexo);
    }

    public Estudante(String nome, String cpf, String sexo , Turno turno){
        super(nome,cpf,sexo);
        this.turno = turno;
    }    

    public Turno getTurno() {
        return this.turno;
    }

    public void setTurno(Turno turno) {
        this.turno = turno;
    }

    @Override
    public String toString(){
        
        return super.toString() +
            " Turno: " + this.turno ;
    }


}