package br.com.sabium.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sabium.model.administrativo.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, Long>{

	Optional<Matricula> findById(Long id);
	
	Matricula findByDisciplinaNome(String nome);
	
	List<Optional<Matricula>> findByEstudanteId(Long estudanteId);
	
	List<Matricula> findByDisciplinaId(Long disciplinaId);
	
}
