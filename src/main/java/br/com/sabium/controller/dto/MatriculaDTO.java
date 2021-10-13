package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.administrativo.Matricula;

public class MatriculaDTO {

	private Long idMatricula;
	private EstudanteSimplificadoDTO estudante;
	private DisciplinaDTO disciplina;
	private String status;

	public MatriculaDTO(Matricula matricula) {
		this.idMatricula = matricula.getId();
		this.estudante = new EstudanteSimplificadoDTO(matricula.getEstudante());
		this.disciplina = new DisciplinaDTO(matricula.getDisciplina());
		this.status = matricula.getStatus().getDescricao();
	}

	public Long getIdMatricula() {
		return idMatricula;
	}

	public void setIdMatricula(Long idMatricula) {
		this.idMatricula = idMatricula;
	}

	public EstudanteSimplificadoDTO getEstudante() {
		return estudante;
	}

	public void setEstudante(EstudanteSimplificadoDTO estudante) {
		this.estudante = estudante;
	}

	public DisciplinaDTO getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(DisciplinaDTO disciplina) {
		this.disciplina = disciplina;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public static List<MatriculaDTO> converter(List<Matricula> matriculas) {
		return matriculas.stream().map(MatriculaDTO::new).collect(Collectors.toList());
	}

}
