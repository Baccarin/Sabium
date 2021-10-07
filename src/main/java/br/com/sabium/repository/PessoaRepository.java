package br.com.sabium.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sabium.model.pessoa.Pessoa;

public interface PessoaRepository<T extends Pessoa> extends JpaRepository<T, Long>{

	Optional<T> findById(Long id);
	
	T findByNome(String nome);
	
	T findByCpf(String cpf);

	
}
