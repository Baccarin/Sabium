package br.com.sabium.controller.dto;

import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.controller.exception.DisciplinaWithOutCursoException;
import br.com.sabium.model.administrativo.Disciplina;

public class DisciplinaDTO {

	private Long id;
	private String nome;
	private Integer duracao;
	
	public DisciplinaDTO(Disciplina disciplina) {
		this.id = disciplina.getId();
		this.nome = disciplina.getNome();
		this.duracao = disciplina.getDuracao();
		if (disciplina.getCurso() == null) {
			throw new DisciplinaWithOutCursoException();
		}
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

	public Integer getDuracao() {
		return duracao;
	}

	public void setDuracao(Integer duracao) {
		this.duracao = duracao;
	}

	public static List<DisciplinaDTO> converter(List<Disciplina> disciplinas) {
		return disciplinas.stream().map(DisciplinaDTO::new).collect(Collectors.toList());
	}

	@Override
	public String toString() {
		return "DisciplinaDTO [id=" + id + ", nome=" + nome + ", duracao=" + duracao + "]";
	}

}
