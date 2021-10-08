package br.com.sabium.model.administrativo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import br.com.sabium.model.pessoa.Estudante;

@Entity
public class Matricula {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "matricula_generator")
	@SequenceGenerator(name="matricula_generator", sequenceName = "matricula_sequence", allocationSize=50)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "estudante_id", nullable = false)
	private Estudante estudante;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "disciplina_id", nullable = false)
	private Disciplina disciplina;

	public Matricula() {

	}

	public Matricula(Estudante estudante, Disciplina disciplina) {
		this();
		this.estudante = estudante;
		this.disciplina = disciplina;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Estudante getEstudante() {
		return estudante;
	}

	public void setEstudante(Estudante estudante) {
		this.estudante = estudante;
	}

	public Disciplina getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(Disciplina disciplina) {
		this.disciplina = disciplina;
	}

}
