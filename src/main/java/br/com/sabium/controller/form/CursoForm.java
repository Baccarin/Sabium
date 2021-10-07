package br.com.sabium.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.sabium.controller.exception.CursoAlreadyExistsException;
import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.repository.CursoRepository;

public class CursoForm {

	@NotNull
	@NotEmpty
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Curso converter(CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(nome);
		if (curso != null) {
			throw new CursoAlreadyExistsException();
		}
		return new Curso(nome);
	}

	public Curso atualizar(Long id, CursoRepository cursoRepository) {
		Optional<Curso> optional = cursoRepository.findById(id);
		Curso curso = optional.get();
		curso.setNome(this.nome);
		return curso;
	}

}
