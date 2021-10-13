package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.pessoa.Estudante;

public class EstudanteSimplificadoDTO extends PessoaDTO {

	private String turno;

	public EstudanteSimplificadoDTO(Estudante estudante) {
		super(estudante);
		this.turno = estudante.getTurno().getNomeTurno();
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public static List<EstudanteSimplificadoDTO> converter(List<Estudante> estudantes) {
		return estudantes.stream().map(EstudanteSimplificadoDTO::new).collect(Collectors.toList());
	}

}
