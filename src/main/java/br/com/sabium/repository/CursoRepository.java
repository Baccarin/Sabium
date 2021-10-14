package br.com.sabium.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.sabium.model.administrativo.Curso;

public interface CursoRepository extends JpaRepository<Curso, Long> {

	Optional<Curso> findById(Long id);

	Curso findByNome(String nome);
}
