package br.com.sabium.controller.form;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.sabium.controller.exception.DisciplinaNotExistException;
import br.com.sabium.controller.exception.EstudanteAlreadyMatriculadoOnDisciplinaException;
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
	private String idEstudante;

	@NotNull
	@NotEmpty
	private String idDisciplina;

	public String getIdEstudante() {
		return idEstudante;
	}

	public void setIdEstudante(String idEstudante) {
		this.idEstudante = idEstudante;
	}

	public String getIdDisciplina() {
		return idDisciplina;
	}

	public void setIdDisciplina(String idDisciplina) {
		this.idDisciplina = idDisciplina;
	}

	public Matricula converter(DisciplinaRepository disciplinaRepository, EstudanteRepository estudanteRepository,
			MatriculaRepository matriculaRepository) {
		Optional<Disciplina> disciplina = disciplinaRepository.findById(Long.parseLong(idDisciplina));
		if (disciplina != null) {
			Optional<Estudante> estudante = estudanteRepository.findById(Long.parseLong(idEstudante));
			if (estudante != null) {
				List<Matricula> matriculas = matriculaRepository.findByEstudanteId(estudante.get().getId());
				for (Matricula m : matriculas) {
					if (m.getDisciplina() == disciplina.get()) {
						throw new EstudanteAlreadyMatriculadoOnDisciplinaException();
					}
				}
				return new Matricula(estudante.get(), disciplina.get());
			}
			throw new EstudanteNotExistException();
		}
		throw new DisciplinaNotExistException();
	}

}
