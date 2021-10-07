package br.com.sabium.controller.form;

import javax.management.RuntimeErrorException;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.sabium.controller.exception.DisciplinaNotExistException;
import br.com.sabium.controller.exception.EstudanteNotExistException;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.model.administrativo.Matricula;
import br.com.sabium.model.pessoa.Estudante;
import br.com.sabium.repository.DisciplinaRepository;
import br.com.sabium.repository.EstudanteRepository;
import br.com.sabium.repository.MatriculaRepository;

public class MatriculaForm {

	@NotNull
	@NotEmpty
	private String nomeEstudante;

	@NotNull
	@NotEmpty
	private String nomeDisciplina;

	public String getNomeEstudante() {
		return nomeEstudante;
	}

	public void setNomeEstudante(String nomeEstudante) {
		this.nomeEstudante = nomeEstudante;
	}

	public String getNomeDisciplina() {
		return nomeDisciplina;
	}

	public void setNomeDisciplina(String nomeDisciplina) {
		this.nomeDisciplina = nomeDisciplina;
	}

	public Matricula converter(DisciplinaRepository disciplinaRepository, EstudanteRepository estudanteRepository) {
		Disciplina disciplina = disciplinaRepository.findByNome(nomeDisciplina);
		if (disciplina != null) {
			Estudante estudante = estudanteRepository.findByNome(nomeEstudante);
			if (estudante != null) {
				return new Matricula(estudante, disciplina);
			}
			throw new EstudanteNotExistException();
		}
		throw new DisciplinaNotExistException();
	}

	public Matricula atualizar(Long id, MatriculaRepository matriculaRepository) {
		throw new RuntimeErrorException(null, "Impossível atualizar uma matrícula");
	}

}
