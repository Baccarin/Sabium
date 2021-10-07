package br.com.sabium.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.sabium.controller.dto.ProfessorDTO;
import br.com.sabium.controller.form.ProfessorForm;
import br.com.sabium.model.pessoa.Professor;
import br.com.sabium.repository.ProfessorRepository;

@RestController
@RequestMapping("/professores")
public class ProfessorController {
	
	@Autowired
	private ProfessorRepository professorRepository;

	@GetMapping
	public List<ProfessorDTO> lista(String nomePessoa) {
		List<Professor> professores = new ArrayList<>();
		if (nomePessoa == null || nomePessoa.isEmpty()) {
			professores = professorRepository.findAll();
			return ProfessorDTO.converter(professores);
		}
		professores.add(professorRepository.findByNome(nomePessoa));
		return ProfessorDTO.converter(professores);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<ProfessorDTO> cadastrar(@RequestBody @Valid ProfessorForm form,
			UriComponentsBuilder uriBuilder) {
		Professor professor = form.converter(professorRepository);
		professorRepository.save(professor);

		URI uri = uriBuilder.path("/professores/{id}").buildAndExpand(professor.getId()).toUri();
		return ResponseEntity.created(uri).body(new ProfessorDTO(professor));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProfessorDTO> detalhar(@PathVariable Long id) {
		Optional<Professor> professor = professorRepository.findById(id);
		if (professor != null) {
			return ResponseEntity.ok(new ProfessorDTO(professor.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<ProfessorDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProfessorForm form) {
		Optional<Professor> optional = professorRepository.findById(id);
		if (optional.isPresent()) {
			Professor professor = form.atualizar(id, professorRepository);
			return ResponseEntity.ok(new ProfessorDTO(professor));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Professor> optional = professorRepository.findById(id);
		if (optional.isPresent()) {
			professorRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
