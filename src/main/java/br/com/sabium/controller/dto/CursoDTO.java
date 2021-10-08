package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.administrativo.Curso;

public class CursoDTO {

	private Long id;
	private String nome;
	private List<DisciplinaDTO> disciplinas;

	public CursoDTO(Curso curso) {
		this.id = curso.getId();
		this.nome = curso.getNome();
	}

	public CursoDTO(Curso curso, List<DisciplinaDTO> disciplinas) {
		this(curso);
		this.disciplinas = disciplinas;
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

	public List<DisciplinaDTO> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<DisciplinaDTO> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public static List<CursoDTO> converter(List<Curso> cursos) {
		return cursos.stream().map(CursoDTO::new).collect(Collectors.toList());
	}

}
