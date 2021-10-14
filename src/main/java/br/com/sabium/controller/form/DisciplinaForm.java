package br.com.sabium.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.sabium.controller.exception.DisciplinaAlreadyExistsException;
import br.com.sabium.controller.exception.DisciplinaWithOutCursoException;
import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

public class DisciplinaForm {

	@NotNull
	@NotEmpty
	private String nome;

	@NotNull
	@NotEmpty
	private String nomeCurso;

	@NotNull
	@NotEmpty
	private String duracao;
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNomeCurso() {
		return nomeCurso;
	}

	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getDuracao() {
		return duracao;
	}

	public void setDuracao(String duracao) {
		this.duracao = duracao;
	}
	
	public Disciplina converter(DisciplinaRepository disciplinaRepository, CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(nomeCurso);
		if (curso != null) {
			Disciplina disciplina = disciplinaRepository.findByNome(nome);
			if (disciplina != null && disciplina.getCurso() == curso) {
				throw new DisciplinaAlreadyExistsException();
			}
			return new Disciplina(nome, Integer.parseInt(duracao), curso);
		}
		throw new DisciplinaWithOutCursoException();
	}

	public Disciplina atualizar(Long id, DisciplinaRepository disciplinaRepository) {
		Optional<Disciplina> optional = disciplinaRepository.findById(id);
		Disciplina disciplina = optional.get();
		disciplina.setNome(this.nome);
		disciplina.setDuracao(Integer.parseInt(duracao));
		return disciplina;
	}


}
