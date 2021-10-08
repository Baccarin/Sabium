package br.com.sabium.model.administrativo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity

public class Disciplina{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    private String nome;
    private Integer duracao;
    
    @OneToMany(mappedBy = "disciplina", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    private List<Matricula> matriculas;
    
    @ManyToOne
    private Curso curso;
    
    public Disciplina() {
    	
    }

    public Disciplina (String nome, Integer duracao){  
        this.nome = nome;
        this.duracao = duracao;
    }

    public Disciplina(String nome, Integer duracao, Curso curso) {
    	this(nome, duracao);
		this.curso = curso;
	}

	public Disciplina (String nome, Integer duracao, Curso curso, ArrayList<Matricula> matriculas){  
        this(nome, duracao, curso);
        this.matriculas = matriculas;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getDuracao() {
        return this.duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	public List<Matricula> getMatriculas() {
		return matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	@Override
	public String toString() {
		return "Disciplina [id=" + id + ", nome=" + nome + ", duracao=" + duracao + ", matriculas=" + matriculas
				+ ", curso=" + curso + "]";
	}

}