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
import br.com.sabium.enumeration.pessoa.Graduacao;
import br.com.sabium.model.pessoa.Professor;
import br.com.sabium.repository.ProfessorRepository;

@RestController
@RequestMapping("/professores")
public class ProfessorController {
	
	@Autowired
	private ProfessorRepository professorRepository;

	private List<Professor> professores = new ArrayList<>();

	@GetMapping("/simplificado/todos")
	public ResponseEntity<List<ProfessorDTO>> listAll() {
		professores = professorRepository.findAll();
		if (!professores.isEmpty()) {
			return ResponseEntity.ok(ProfessorDTO.converter(professores));
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/simplificado/{id}")
	public ResponseEntity<ProfessorDTO> detalhar(@PathVariable Long id) {
		Optional<Professor> professor = professorRepository.findById(id);
		if (professor != null) {
			return ResponseEntity.ok(new ProfessorDTO(professor.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/graduacao/{graduacao}")
	public List<ProfessorDTO> listByGraduacao(@PathVariable String graduacao) {
		List<Professor> professores = professorRepository.findByGraduacao(Graduacao.converte(graduacao));
		if (professores != null) {
			return ProfessorDTO.converter(professores);
		}
		return ProfessorDTO.converter(professorRepository.findAll());
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
	
	@DeleteMapping("/todos")
	@Transactional
	public ResponseEntity<?> removerTodos() {
		List<Professor> professores = professorRepository.findAll();
		if (!professores.isEmpty()) {
			professorRepository.deleteAll();
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
