package br.com.sabium.model.pessoa;

import javax.persistence.Entity;

import br.com.sabium.enumeration.pessoa.Graduacao;

@Entity
public class Professor extends Pessoa{

	private Double salario;
	private Graduacao graduacao;
	
	public Professor() {
		
	}

	public Professor(String nome, String cpf, String sexo, Double salario) {
		super(nome, cpf, sexo);
		this.salario = salario;
	}
	
	public Professor(String nome, String cpf, String sexo, Double salario, Graduacao graduacao) {
		this(nome, cpf, sexo, salario);
		this.graduacao = graduacao;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public Graduacao getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(Graduacao graduacao) {
		this.graduacao = graduacao;
	}
	
	
}
