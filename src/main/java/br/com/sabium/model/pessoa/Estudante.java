package br.com.sabium.model.pessoa;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import br.com.sabium.model.administrativo.Matricula;
import br.com.sabium.model.administrativo.Turno;

@Entity
public class Estudante extends Pessoa {

	private Turno turno;

	@OneToMany(mappedBy = "estudante", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Matricula> matriculas;

	public Estudante() {

	}

	public Estudante(String nome, String cpf, String sexo) {
		super(nome, cpf, sexo);
	}

	public Estudante(String nome, String cpf, String sexo, Turno turno) {
		super(nome, cpf, sexo);
		this.turno = turno;
	}

	public Turno getTurno() {
		return this.turno;
	}

	public void setTurno(Turno turno) {
		this.turno = turno;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	@Override
	public String toString() {

		return super.toString() + " Turno: " + this.turno;
	}

}