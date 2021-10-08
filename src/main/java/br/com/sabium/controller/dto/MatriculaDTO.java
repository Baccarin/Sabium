package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.administrativo.Matricula;

public class MatriculaDTO {

	private Long id;
	private String nomeEstudante;
	private String nomeDisciplina;

	public MatriculaDTO(Matricula matricula) {
		this.id = matricula.getId();
		this.nomeEstudante = matricula.getEstudante().getNome();
		this.nomeDisciplina = matricula.getDisciplina().getNome();
	}

	public MatriculaDTO(String nomeEstudante, String nomeDisciplina) {
		this.nomeEstudante = nomeEstudante;
		this.nomeDisciplina = nomeDisciplina;
	}

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static List<MatriculaDTO> converter(List<Matricula> matriculas) {
		return matriculas.stream().map(MatriculaDTO::new).collect(Collectors.toList());
	}

}
