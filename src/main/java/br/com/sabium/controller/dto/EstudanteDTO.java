package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.pessoa.Estudante;

public class EstudanteDTO extends PessoaDTO {

	private String turno;
	private List<MatriculaDTO> matriculas;

	public EstudanteDTO(Estudante estudante) {
		super(estudante);
		this.turno = estudante.getTurno().getNomeTurno();
	}

	public EstudanteDTO(Estudante estudante, List<MatriculaDTO> matriculas) {
		this(estudante);
		this.matriculas = matriculas;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public List<MatriculaDTO> getMatriculass() {
		return matriculas;
	}

	public void setMatriculas(List<MatriculaDTO> matriculas) {
		this.matriculas = matriculas;
	}

	public static List<EstudanteDTO> converter(List<Estudante> estudantes) {
		return estudantes.stream().map(EstudanteDTO::new).collect(Collectors.toList());
	}

}
