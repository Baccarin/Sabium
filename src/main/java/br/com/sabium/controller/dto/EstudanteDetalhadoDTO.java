package br.com.sabium.controller.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.sabium.model.pessoa.Estudante;

public class EstudanteDetalhadoDTO extends EstudanteSimplificadoDTO {

	private List<DisciplinaDTO> disciplinas = new ArrayList<>();
	private String cpf;

	public EstudanteDetalhadoDTO(Estudante estudante) {
		super(estudante);
		this.cpf = estudante.getCpf();
		estudante.getMatriculas().forEach(m -> this.adicionaDisciplina(new DisciplinaDTO(m.getDisciplina())));
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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public static List<EstudanteDetalhadoDTO> converterDTO(List<Estudante> estudantes) {
		return estudantes.stream().map(EstudanteDetalhadoDTO::new).collect(Collectors.toList());
	}

}
