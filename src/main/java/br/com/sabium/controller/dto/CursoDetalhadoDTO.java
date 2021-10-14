package br.com.sabium.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.repository.DisciplinaRepository;

public class CursoDetalhadoDTO extends CursoSimplificadoDTO {

	private List<DisciplinaDTO> disciplinas = new ArrayList<>();

	public CursoDetalhadoDTO(Curso curso) {
		super(curso);
	}

	public CursoDetalhadoDTO(Curso curso, List<DisciplinaDTO> disciplinas) {
		super(curso);
		this.disciplinas = disciplinas;
	}

	public List<DisciplinaDTO> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<DisciplinaDTO> disciplinas) {
		this.disciplinas = disciplinas;
	}

	public static CursoDetalhadoDTO converter(Curso curso, DisciplinaRepository disciplinaRepository) {
		CursoDetalhadoDTO cursoDisciplinaDTO = new CursoDetalhadoDTO(curso);
		List<Disciplina> disciplinas = disciplinaRepository.findByCursoId(curso.getId());
		if (!disciplinas.isEmpty()) {
			List<DisciplinaDTO> dtos = DisciplinaDTO.converter(disciplinas);
			cursoDisciplinaDTO.setDisciplinas(dtos);
		}
		return cursoDisciplinaDTO;
	}

}
