package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.pessoa.Professor;

public class ProfessorDTO extends PessoaDTO {

	private String graduacao;

	public ProfessorDTO(Professor professor) {
		super(professor);
		this.graduacao = professor.getGraduacao().getDescricao();
	}

	public String getGraduacao() {
		return graduacao;
	}

	public void setGraduacao(String graduacao) {
		this.graduacao = graduacao;
	}

	public static List<ProfessorDTO> converter(List<Professor> professores) {
		return professores.stream().map(ProfessorDTO::new).collect(Collectors.toList());
	}

}
