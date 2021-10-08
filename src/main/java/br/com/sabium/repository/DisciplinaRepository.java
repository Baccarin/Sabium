package br.com.sabium.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sabium.model.administrativo.Disciplina;
public interface DisciplinaRepository extends JpaRepository<Disciplina, Long>{

	Optional<Disciplina> findById(Long id);

	Disciplina findByNome(String nome);
	
	List<Disciplina> findByCursoId(Long id);
	
	@Query( "select m.disciplina from Matricula m where m.id =:id" )
	List<Disciplina> findByMatricula(@Param("id") Long id);
	
	
}
