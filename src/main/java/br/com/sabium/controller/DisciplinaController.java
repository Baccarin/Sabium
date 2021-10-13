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

import br.com.sabium.controller.dto.DisciplinaDTO;
import br.com.sabium.controller.form.DisciplinaForm;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

@RestController
@RequestMapping("/disciplinas")
public class DisciplinaController {

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private DisciplinaRepository disciplinaRepository;

	private List<Disciplina> disciplinas = new ArrayList<>();

	@GetMapping("/todos")
	public ResponseEntity<List<DisciplinaDTO>> listAll() {
		disciplinas = disciplinaRepository.findAll();
		if (!disciplinas.isEmpty()) {
			return ResponseEntity.ok(DisciplinaDTO.converter(disciplinas));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<DisciplinaDTO> detalhesMatriculas(@PathVariable Long id) {
		Optional<Disciplina> disciplina = disciplinaRepository.findById(id);
		if (disciplina != null) {
			return ResponseEntity.ok(new DisciplinaDTO(disciplina.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<DisciplinaDTO> cadastrar(@RequestBody @Valid DisciplinaForm form,
			UriComponentsBuilder uriBuilder) {
		Disciplina disciplina = form.converter(disciplinaRepository, cursoRepository);
		disciplinaRepository.save(disciplina);

		URI uri = uriBuilder.path("/disciplinas/{id}").buildAndExpand(disciplina.getId()).toUri();
		return ResponseEntity.created(uri).body(new DisciplinaDTO(disciplina));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<DisciplinaDTO> atualizar(@PathVariable Long id, @RequestBody @Valid DisciplinaForm form) {
		Optional<Disciplina> optional = disciplinaRepository.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(new DisciplinaDTO(form.atualizar(id, disciplinaRepository)));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Disciplina> optional = disciplinaRepository.findById(id);
		if (optional.isPresent()) {
			disciplinaRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
