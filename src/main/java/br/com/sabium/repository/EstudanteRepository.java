package br.com.sabium.repository;

import java.util.List;

import br.com.sabium.enumeration.pessoa.Turno;
import br.com.sabium.model.pessoa.Estudante;

public interface EstudanteRepository extends PessoaRepository<Estudante> {

	List<Estudante> findByTurno(Turno turno);

}
