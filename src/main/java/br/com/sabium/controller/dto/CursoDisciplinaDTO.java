package br.com.sabium.controller.dto;

import java.util.List;

import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.repository.DisciplinaRepository;

public class CursoDisciplinaDTO extends CursoSimplificadoDTO {

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

	public static CursoDisciplinaDTO converter(Curso curso, DisciplinaRepository disciplinaRepository) {
		CursoDisciplinaDTO cursoDisciplinaDTO = new CursoDisciplinaDTO(curso);
		List<Disciplina> disciplinas = disciplinaRepository.findByCursoId(curso.getId());
		if (!disciplinas.isEmpty()) {
			List<DisciplinaDTO> dtos = DisciplinaDTO.converter(disciplinas);
			cursoDisciplinaDTO.setDisciplinas(dtos);
		}
		return cursoDisciplinaDTO;
	}

}
