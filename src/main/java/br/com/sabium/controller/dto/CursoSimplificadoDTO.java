package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.administrativo.Curso;

public class CursoSimplificadoDTO {

	private Long id;
	private String nome;


	public CursoSimplificadoDTO(Curso curso) {
		this.id = curso.getId();
		this.nome = curso.getNome();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public static List<CursoSimplificadoDTO> converter(List<Curso> cursos) {
		return cursos.stream().map(CursoSimplificadoDTO::new).collect(Collectors.toList());
	}

}
