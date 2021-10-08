package br.com.sabium.controller.dto;

import java.util.List;

import br.com.sabium.model.administrativo.Curso;

public class CursoDisciplinaDTO extends CursoDTO {
	
	private List<DisciplinaDTO> disciplinas;

	public CursoDisciplinaDTO(Curso curso) {
		super(curso);
	}
	
	public CursoDisciplinaDTO(Curso curso, List<DisciplinaDTO> disciplinas) {
		super(curso);
		this.disciplinas = disciplinas;
	}

	public List<DisciplinaDTO> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<DisciplinaDTO> disciplinas) {
		this.disciplinas = disciplinas;
	}
	
}
