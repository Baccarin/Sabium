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

import br.com.sabium.controller.dto.EstudanteDetalhadoDTO;
import br.com.sabium.controller.dto.EstudanteSimplificadoDTO;
import br.com.sabium.controller.form.EstudanteForm;
import br.com.sabium.enumeration.pessoa.Turno;
import br.com.sabium.model.pessoa.Estudante;
import br.com.sabium.repository.EstudanteRepository;

@RestController
@RequestMapping("/estudantes")
public class EstudanteController {

	@Autowired
	private EstudanteRepository estudanteRepository;

	private List<Estudante> estudantes = new ArrayList<>();

	@GetMapping("/simplificado/todos")
	public ResponseEntity<List<EstudanteSimplificadoDTO>> listAll() {
		estudantes = estudanteRepository.findAll();
		if (!estudantes.isEmpty()) {
			return ResponseEntity.ok(EstudanteSimplificadoDTO.converter(estudantes));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/simplificado/{id}")
	public ResponseEntity<EstudanteSimplificadoDTO> findByIdSimplificado(@PathVariable Long id) {
		Optional<Estudante> estudante = estudanteRepository.findById(id);
		if (estudante != null) {
			return ResponseEntity.ok(new EstudanteSimplificadoDTO(estudante.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/detalhado/todos")
	public ResponseEntity<List<EstudanteDetalhadoDTO>> findAllDetalhado() {
		estudantes = estudanteRepository.findAll();
		if (!estudantes.isEmpty()) {
			List<EstudanteDetalhadoDTO> listaDTO = new ArrayList<>();
			for (Estudante estudante : estudantes) {
				listaDTO.add(new EstudanteDetalhadoDTO(estudante));
			}
			return ResponseEntity.ok(listaDTO);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/detalhado/{id}")
	public ResponseEntity<EstudanteSimplificadoDTO> findByIdDetalhado(@PathVariable Long id) {
		Optional<Estudante> estudante = estudanteRepository.findById(id);
		if (estudante != null) {
			return ResponseEntity.ok(new EstudanteDetalhadoDTO(estudante.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/turno/{turno}")
	public List<EstudanteSimplificadoDTO> listByTurno(@PathVariable String turno) {
		List<Estudante> estudantes = estudanteRepository.findByTurno(Turno.converte(turno));
		if (estudantes != null) {
			return EstudanteSimplificadoDTO.converter(estudantes);
		}
		return EstudanteSimplificadoDTO.converter(estudanteRepository.findAll());
	}

	@PostMapping
	@Transactional
	public ResponseEntity<EstudanteSimplificadoDTO> cadastrar(@RequestBody EstudanteForm form,
			UriComponentsBuilder uriBuilder) {

		Estudante estudante = form.converter(estudanteRepository);
		estudanteRepository.save(estudante);

		URI uri = uriBuilder.path("/estudantes/{id}").buildAndExpand(estudante.getId()).toUri();
		return ResponseEntity.created(uri).body(new EstudanteSimplificadoDTO(estudante));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<EstudanteSimplificadoDTO> atualizar(@PathVariable Long id,
			@RequestBody @Valid EstudanteForm form) {
		Optional<Estudante> optional = estudanteRepository.findById(id);
		if (optional.isPresent()) {
			Estudante estudante = form.atualizar(id, estudanteRepository);
			return ResponseEntity.ok(new EstudanteSimplificadoDTO(estudante));
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
