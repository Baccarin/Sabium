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

import br.com.sabium.controller.dto.EstudanteDTO;
import br.com.sabium.controller.dto.MatriculaDTO;
import br.com.sabium.controller.form.EstudanteForm;
import br.com.sabium.model.administrativo.Matricula;
import br.com.sabium.model.pessoa.Estudante;
import br.com.sabium.repository.EstudanteRepository;
import br.com.sabium.repository.MatriculaRepository;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

	@Autowired
	private EstudanteRepository estudanteRepository;

	@Autowired
	private MatriculaRepository matriculaRepository;

	@GetMapping
	public List<EstudanteDTO> lista(String nomePessoa) {
		List<Estudante> estudantes = new ArrayList<>();
		if (nomePessoa == null) {
			estudantes = estudanteRepository.findAll();
			return EstudanteDTO.converter(estudantes);
		}
		estudantes.add(estudanteRepository.findByNome(nomePessoa));
		return EstudanteDTO.converter(estudantes);
	}

	@PostMapping
	@Transactional
	public ResponseEntity<EstudanteDTO> cadastrar(@RequestBody EstudanteForm form, UriComponentsBuilder uriBuilder) {

		Estudante estudante = form.converter(estudanteRepository);
		estudanteRepository.save(estudante);

		URI uri = uriBuilder.path("/estudantes/{id}").buildAndExpand(estudante.getId()).toUri();
		return ResponseEntity.created(uri).body(new EstudanteDTO(estudante));
	}

	@GetMapping("/{id}")
	public ResponseEntity<EstudanteDTO> detalhar(@PathVariable Long id) {
		Optional<Estudante> estudante = estudanteRepository.findById(id);
		if (estudante != null) {
			EstudanteDTO estudanteDTO = new EstudanteDTO(estudante.get());
			List<Optional<Matricula>> matriculasOptional = matriculaRepository
					.findByEstudanteId(estudante.get().getId());
			if (!matriculasOptional.isEmpty()) {
				List<Matricula> matriculas = new ArrayList<Matricula>();
				matriculasOptional.forEach(mo -> matriculas.add(mo.get()));
				List<MatriculaDTO> dtos = MatriculaDTO.converter(matriculas);
				estudanteDTO.setMatriculas(dtos);
			}
			return ResponseEntity.ok(estudanteDTO);
		}
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<EstudanteDTO> atualizar(@PathVariable Long id, @RequestBody @Valid EstudanteForm form) {
		Optional<Estudante> optional = estudanteRepository.findById(id);
		if (optional.isPresent()) {
			Estudante estudante = form.atualizar(id, estudanteRepository);
			return ResponseEntity.ok(new EstudanteDTO(estudante));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Estudante> optional = estudanteRepository.findById(id);
		if (optional.isPresent()) {
			estudanteRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}
}
