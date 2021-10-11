package br.com.sabium.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.sabium.controller.exception.ProfessorAlreadyExistException;
import br.com.sabium.enumeration.pessoa.Graduacao;
import br.com.sabium.model.pessoa.Professor;
import br.com.sabium.repository.ProfessorRepository;

public class ProfessorForm {

	@NotNull
	@NotEmpty
	private String nome;

	@NotNull
	@NotEmpty
	private String cpf;

	@NotNull
	@NotEmpty
	private String sexo;

	private Double salario;

	private String graduacao;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Double getSalario() {
		return salario;
	}

	public void setSalario(Double salario) {
		this.salario = salario;
	}

	public String getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(String graduacao) {
		this.graduacao = graduacao;
	}

	public Professor converter(ProfessorRepository professorRepository) {
		Professor professor = professorRepository.findByCpf(cpf);
		if (professor != null) {
			throw new ProfessorAlreadyExistException();
		}
		return new Professor(nome, cpf, sexo, salario, Graduacao.converte(graduacao));
	}

	public Professor atualizar(Long id, ProfessorRepository professorRepository) {
		Optional<Professor> optional = professorRepository.findById(id);
		Professor professor = optional.get();
		professor.setNome(nome);
		professor.setCpf(cpf);
		professor.setSexo(sexo);
		professor.setSalario(salario);
		professor.setGraduacao(Graduacao.converte(graduacao));
		return professor;
	}

}
