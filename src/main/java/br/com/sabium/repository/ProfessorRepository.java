package br.com.sabium.repository;

import java.util.List;

import br.com.sabium.enumeration.pessoa.Graduacao;
import br.com.sabium.model.pessoa.Professor;

public interface ProfessorRepository  extends PessoaRepository<Professor>  {

	List<Professor> findByGraduacao(Graduacao graduacao);
	
}
