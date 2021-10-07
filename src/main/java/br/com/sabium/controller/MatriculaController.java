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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.sabium.controller.dto.MatriculaDTO;
import br.com.sabium.controller.form.MatriculaForm;
import br.com.sabium.model.administrativo.Matricula;
import br.com.sabium.repository.DisciplinaRepository;
import br.com.sabium.repository.EstudanteRepository;
import br.com.sabium.repository.MatriculaRepository;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController {

	@Autowired
	private MatriculaRepository matriculaRepository;

	@Autowired
	private DisciplinaRepository disciplinaRepository;
	
	@Autowired
	private EstudanteRepository estudanteRepository;
	
	@GetMapping
	public List<MatriculaDTO> listByDisciplinaNome(String nomeDisciplina) {
		List<Matricula> matriculas = new ArrayList<>();
		if (nomeDisciplina == null) {
			matriculas = matriculaRepository.findAll();
		} else {
			matriculas.add(matriculaRepository.findByDisciplinaNome(nomeDisciplina));
		}
		return MatriculaDTO.converter(matriculas);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<MatriculaDTO> cadastrar(@RequestBody @Valid MatriculaForm form, UriComponentsBuilder uriBuilder) {
		Matricula matricula = form.converter(disciplinaRepository, estudanteRepository);
		matriculaRepository.save(matricula);

		URI uri = uriBuilder.path("/matriculas/{id}").buildAndExpand(matricula.getId()).toUri();
		return ResponseEntity.created(uri).body(new MatriculaDTO(matricula));
	}

	@GetMapping("/{id}")
	public ResponseEntity<MatriculaDTO> detalhar(@PathVariable Long id) {
		Optional<Matricula> matricula = matriculaRepository.findById(id);
		if (matricula != null) {
			return ResponseEntity.ok(new MatriculaDTO(matricula.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Matricula> optional = matriculaRepository.findById(id);
		if (optional.isPresent()) {
			matriculaRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
