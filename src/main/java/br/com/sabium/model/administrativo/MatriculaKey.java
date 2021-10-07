package br.com.sabium.model.administrativo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MatriculaKey implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "estudante_id")
    private Long estudanteId;

    @Column(name = "disciplina_id")
    private Long disciplinaId;

	public MatriculaKey() {
	}

	public Long getEstudanteId() {
		return estudanteId;
	}

	public void setEstudanteId(Long estudanteId) {
		this.estudanteId = estudanteId;
	}

	public Long getDisciplinaId() {
		return disciplinaId;
	}

	public void setDisciplinaId(Long disciplinaId) {
		this.disciplinaId = disciplinaId;
	}
	
}
