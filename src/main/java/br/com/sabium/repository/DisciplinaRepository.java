package br.com.sabium.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sabium.model.administrativo.Disciplina;
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>{

	Optional<Disciplina> findById(Long id);

	Disciplina findByNome(String nome);
	
	List<Disciplina> findByCursoId(Long id);
	
}
