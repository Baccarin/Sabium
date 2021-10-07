package br.com.sabium.model.administrativo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import br.com.sabium.model.pessoa.Estudante;

@Entity
public class Matricula {

	//@EmbeddedId
	//private MatriculaKey id;
	
	@Id
	@GeneratedValue (strategy =  GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	//@MapsId("estudanteId")
	@JoinColumn(name = "estudante_id")
	private Estudante estudante;

	@ManyToOne
	//@MapsId("disciplinaId")
	@JoinColumn(name = "disciplina_id")
	private Disciplina disciplina;

	public Matricula(Estudante estudante, Disciplina disciplina) {
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
