package br.com.sabium.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.pessoa.Estudante;

public class EstudanteDisciplinaDTO extends EstudanteDTO {


	private List<DisciplinaDTO> disciplinas = new ArrayList<>();

	public EstudanteDisciplinaDTO(Estudante estudante) {
		super(estudante);
	}

	public List<DisciplinaDTO> getDisciplinas() {
		return disciplinas;
	}

	public void setDisciplinas(List<DisciplinaDTO> disciplinas) {
		this.disciplinas = disciplinas;
	}
	
	public void adicionaDisciplina(DisciplinaDTO disciplina) {
		this.disciplinas.add(disciplina);
	}
	
	public void adicionaDisciplinas(List<DisciplinaDTO> disciplina) {
		this.disciplinas.addAll(disciplina);
	}

	public static List<EstudanteDisciplinaDTO> converterDTO(List<Estudante> estudantes) {
		return estudantes.stream().map(EstudanteDisciplinaDTO::new).collect(Collectors.toList());
	}

}
