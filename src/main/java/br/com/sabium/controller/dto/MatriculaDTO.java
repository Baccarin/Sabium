package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.administrativo.Matricula;

public class MatriculaDTO {

	private Long idMatricula;
	private EstudanteDTO estudante;
	private DisciplinaDTO disciplina;

	public MatriculaDTO(Matricula matricula) {
		this.idMatricula = matricula.getId();
		this.estudante = new EstudanteDTO(matricula.getEstudante());
		this.disciplina = new DisciplinaDTO(matricula.getDisciplina());
	}

	public Long getIdMatricula() {
		return idMatricula;
	}

	public void setIdMatricula(Long idMatricula) {
		this.idMatricula = idMatricula;
	}

	public EstudanteDTO getEstudante() {
		return estudante;
	}

	public void setEstudante(EstudanteDTO estudante) {
		this.estudante = estudante;
	}

	public DisciplinaDTO getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(DisciplinaDTO disciplina) {
		this.disciplina = disciplina;
	}

	public static List<MatriculaDTO> converter(List<Matricula> matriculas) {
		return matriculas.stream().map(MatriculaDTO::new).collect(Collectors.toList());
	}

}
