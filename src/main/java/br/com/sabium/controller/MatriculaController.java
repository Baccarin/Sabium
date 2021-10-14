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

import br.com.sabium.controller.dto.MatriculaDTO;
import br.com.sabium.controller.exception.MatriculaAprovadaException;
import br.com.sabium.controller.exception.MatriculaCanceladaException;
import br.com.sabium.controller.form.MatriculaForm;
import br.com.sabium.enumeration.pessoa.Status;
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

	private List<Matricula> matriculas = new ArrayList<>();

	@GetMapping("/detalhado/todos")
	public ResponseEntity<List<MatriculaDTO>> listAllDetalhado() {
		matriculas = matriculaRepository.findAll();
		if (!matriculas.isEmpty()) {
			return ResponseEntity.ok(MatriculaDTO.converter(matriculas));
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/detalhado/{id}")
	public ResponseEntity<MatriculaDTO> listByIdDetalhado(@PathVariable Long id) {
		Optional<Matricula> matricula = matriculaRepository.findById(id);
		if (matricula != null) {
			return ResponseEntity.ok(new MatriculaDTO(matricula.get()));
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<MatriculaDTO> cadastrar(@RequestBody @Valid MatriculaForm form,
			UriComponentsBuilder uriBuilder) {
		Matricula matricula = form.converter(disciplinaRepository, estudanteRepository, matriculaRepository);
		matriculaRepository.save(matricula);

		URI uri = uriBuilder.path("/matriculas/{id}").buildAndExpand(matricula.getId()).toUri();
		return ResponseEntity.created(uri).body(new MatriculaDTO(matricula));
	}

	@PutMapping("/autorizar/{id}")
	@Transactional
	public ResponseEntity<MatriculaDTO> autorizaMatricula(@PathVariable Long id, UriComponentsBuilder uriBuilder)
			throws MatriculaCanceladaException {
		Matricula matricula = this.autorizaMatricula(matriculaRepository, id);
		matriculaRepository.save(matricula);

		URI uri = uriBuilder.path("/matriculas/{id}").buildAndExpand(matricula.getId()).toUri();
		return ResponseEntity.created(uri).body(new MatriculaDTO(matricula));
	}

	@PutMapping("/cancelar/{id}")
	@Transactional
	public ResponseEntity<MatriculaDTO> cancelaMatricula(@PathVariable Long id, UriComponentsBuilder uriBuilder)
			throws MatriculaCanceladaException, MatriculaAprovadaException {
		Matricula matricula = this.cancelaMatricula(matriculaRepository, id);
		matriculaRepository.save(matricula);

		URI uri = uriBuilder.path("/matriculas/{id}").buildAndExpand(matricula.getId()).toUri();
		return ResponseEntity.created(uri).body(new MatriculaDTO(matricula));
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

	@DeleteMapping("/todos")
	@Transactional
	public ResponseEntity<?> removerTodos() {
		List<Matricula> lista = matriculaRepository.findAll();
		if (!lista.isEmpty()) {
			matriculaRepository.deleteAll();
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

	
	//Esse tipo de método deveria estar aonde ???
	public Matricula autorizaMatricula(MatriculaRepository matriculaRepository, Long id)
			throws MatriculaCanceladaException {
		Optional<Matricula> optional = matriculaRepository.findById(id);
		Matricula matricula = optional.get();
		if (matricula.getStatus().equals(Status.CANCELADA)) {
			throw new MatriculaCanceladaException();
		}
		matricula.setStatus(Status.APROVADA);
		return matricula;
	}

	private Matricula cancelaMatricula(MatriculaRepository matriculaRepository, Long id)
			throws MatriculaAprovadaException {
		Optional<Matricula> optional = matriculaRepository.findById(id);
		Matricula matricula = optional.get();
		if (matricula.getStatus().equals(Status.APROVADA)) {
			throw new MatriculaAprovadaException();
		}
		matricula.setStatus(Status.CANCELADA);
		return matricula;
	}

	// VALIDAR COMO SE ALTERA UM OBJETO DENTRO DE OUTRO VIA JSON

}
