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

import br.com.sabium.controller.dto.CursoSimplificadoDTO;
import br.com.sabium.controller.dto.CursoDisciplinaDTO;
import br.com.sabium.controller.dto.DisciplinaDTO;
import br.com.sabium.controller.exception.CursoWithDisciplinasAssociateException;
import br.com.sabium.controller.form.CursoForm;
import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

@RestController
@RequestMapping("/cursos")
public class CursoController {

	@Autowired
	private CursoRepository cursoRepository;

	@Autowired
	private DisciplinaRepository disciplinaRepository;

	private List<Curso> cursos = new ArrayList<>();
	
	@GetMapping("/simplificado/todos")
	public ResponseEntity<List<CursoSimplificadoDTO>> listAll() {
		cursos = cursoRepository.findAll();
		if (!cursos.isEmpty()) {
			return ResponseEntity.ok(CursoSimplificadoDTO.converter(cursos));
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/detalhado/{id}")
	public ResponseEntity<CursoDisciplinaDTO> detalhar(@PathVariable Long id) {
		Optional<Curso> curso = cursoRepository.findById(id);
		if (curso != null) {
			CursoDisciplinaDTO cursoDisciplinaDTO = new CursoDisciplinaDTO(curso.get());
			List<Disciplina> disciplinas = disciplinaRepository.findByCursoId(curso.get().getId());
			if (!disciplinas.isEmpty()) {
				List<DisciplinaDTO> dtos = DisciplinaDTO.converter(disciplinas);
				cursoDisciplinaDTO.setDisciplinas(dtos);
			}
			return ResponseEntity.ok(cursoDisciplinaDTO);
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/detalhado/todos")
	public ResponseEntity<List<CursoDisciplinaDTO>> detalharTodo() {
		List<Curso> cursos = cursoRepository.findAll();
		List<CursoDisciplinaDTO> cursoDisciplinaDTOs = new ArrayList<>();
		if (!cursos.isEmpty()) {
			cursos.forEach( c -> {
				cursoDisciplinaDTOs.add(CursoDisciplinaDTO.converter(c, disciplinaRepository));
			});
			return ResponseEntity.ok(cursoDisciplinaDTOs);
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	@Transactional
	public ResponseEntity<CursoSimplificadoDTO> cadastrar(@RequestBody @Valid CursoForm form, UriComponentsBuilder uriBuilder) {
		Curso curso = form.converter(cursoRepository);
		cursoRepository.save(curso);

		URI uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();
		return ResponseEntity.created(uri).body(new CursoSimplificadoDTO(curso));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<CursoSimplificadoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid CursoForm form) {
		Optional<Curso> optional = cursoRepository.findById(id);
		if (optional.isPresent()) {
			Curso curso = form.atualizar(id, cursoRepository);
			return ResponseEntity.ok(new CursoSimplificadoDTO(curso));
		}

		return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Curso> optional = cursoRepository.findById(id);
		if (optional.isPresent()) {
			if (optional.get().getDisciplinas().isEmpty()) {
				cursoRepository.deleteById(id);
				return ResponseEntity.ok().build();
			}
			throw new CursoWithDisciplinasAssociateException();
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/todos")
	@Transactional
	public ResponseEntity<?> removerTodos() {
		List<Curso> lista = cursoRepository.findAll();
		if (!lista.isEmpty()) {
			cursoRepository.deleteAll();
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
