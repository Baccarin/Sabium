package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.enumeration.pessoa.Status;
import br.com.sabium.enumeration.pessoa.Turno;
import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.model.administrativo.Disciplina;
import br.com.sabium.model.administrativo.Matricula;
import br.com.sabium.model.pessoa.Estudante;

public class MatriculaControllerTest {

	private String URI_LOCAL = "http://localhost:8090/matriculas";

	private RestTemplate restTemplate = new RestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	private long ID_MATRICULA = 4852l;

	private long ID_MATRICULA_CANCELADA = 4853l;
	private long ID_MATRICULA_APROVADA = 4852l;
	private long ID_MATRICULA_PENDENTE = 6504l;

	private String NOME_ESTUDANTE = "Guilherme";
	private String NOME_DISCIPLINA = "Nome Disciplina";

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/indisponivel"));
			ResponseEntity<Matricula> novoMatricula = restTemplate.getForEntity(uri, Matricula.class);
			assertFalse(ID_MATRICULA == novoMatricula.getBody().getId());

		} catch (HttpClientErrorException | URISyntaxException ex) {
			assertEquals(405, ((RestClientResponseException) ex).getRawStatusCode());
		}
	}

	@Test
	public void deveriaAcessarMatriculaDetalhadosId() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/detalhado/" + ID_MATRICULA));
		try {
			ResponseEntity<Matricula> matricula = restTemplate.getForEntity(uri, Matricula.class);
			assertEquals(200, matricula.getStatusCodeValue());
			assertEquals(NOME_DISCIPLINA, matricula.getBody().getDisciplina().getNome());
			assertEquals(NOME_ESTUDANTE, matricula.getBody().getEstudante().getNome());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void deveriaRetornarMatriculaTodosDetalhados() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/detalhado/todos"));
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

			int ids = StringUtils.countOccurrencesOf(result.getBody(), "{\"id\"");
			assertTrue(ids >= 3);
			assertEquals(200, result.getStatusCodeValue());
			assertTrue(result.getBody().contains("disciplina"));
			assertTrue(result.getBody().contains("estudante"));

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deveriaAutorizarMatricula() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/detalhado/" + ID_MATRICULA_PENDENTE));
		try {
			restTemplate.put(URI_LOCAL.concat("/autorizar/" + ID_MATRICULA_PENDENTE),
					restTemplate.getForEntity(uri, Matricula.class));
			ResponseEntity<Matricula> matricula = restTemplate.getForEntity(uri, Matricula.class);

			assertEquals(200, matricula.getStatusCodeValue());
			assertEquals(Status.APROVADA, matricula.getBody().getStatus());

		} catch (Exception ex) {
			assertEquals(500, ((RestClientResponseException) ex).getRawStatusCode());
		}
	}

	@Test
	public void deveriaCancelar() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/detalhado/" + ID_MATRICULA_PENDENTE));
		try {
			restTemplate.put(URI_LOCAL.concat("/cancelar/" + ID_MATRICULA_PENDENTE),
					restTemplate.getForEntity(uri, Matricula.class));
			ResponseEntity<Matricula> matricula = restTemplate.getForEntity(uri, Matricula.class);

			assertEquals(200, matricula.getStatusCodeValue());
			assertEquals(Status.CANCELADA, matricula.getBody().getStatus());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void naoDeveriaCancelarMatriculaAprovada() throws URISyntaxException {

		URI uri = new URI(URI_LOCAL.concat("/detalhado/" + ID_MATRICULA_APROVADA));
		try {
			restTemplate.put(URI_LOCAL.concat("/cancelar/" + ID_MATRICULA_APROVADA),
					restTemplate.getForEntity(uri, Matricula.class));

		} catch (Exception ex) {
			assertEquals(500, ((RestClientResponseException) ex).getRawStatusCode());
		}
	}

	@Test
	public void naoDeveriaAutorizarMatriculaCancelada() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/detalhado/" + ID_MATRICULA_CANCELADA));
		try {
			restTemplate.put(URI_LOCAL.concat("/cancelar/" + ID_MATRICULA_APROVADA),
					restTemplate.getForEntity(uri, Matricula.class));

		} catch (Exception ex) {
			assertEquals(500, ((RestClientResponseException) ex).getRawStatusCode());
		}
	}

	@Test
	public void deveriaCriarNovaMatricula() throws URISyntaxException {
		long idCurso = 0l;
		long idDisciplina = 0l;
		long idEstudante = 0l;
		long idMatricula = 0l;

		try {
			URI uri = new URI(URI_LOCAL);
			Curso curso = new Curso("TESTE CURSO");
			HttpEntity<Curso> request = new HttpEntity<>(curso, headers);
			ResponseEntity<Curso> novoCurso = restTemplate.postForEntity("http://localhost:8090/cursos", request,
					Curso.class);

			assertEquals(curso.getNome(), novoCurso.getBody().getNome());
			assertEquals(201, novoCurso.getStatusCodeValue());
			idCurso = novoCurso.getBody().getId();

			Disciplina disciplina = new Disciplina("TESTE DISCIPLINA", 5, novoCurso.getBody());
			HttpEntity<Disciplina> requestDisciplina = new HttpEntity<>(disciplina, headers);
			ResponseEntity<Disciplina> novaDisciplina = restTemplate.postForEntity("http://localhost:8090/disciplinas",
					requestDisciplina, Disciplina.class);

			assertEquals(disciplina.getNome(), novaDisciplina.getBody().getNome());
			assertEquals(201, novaDisciplina.getStatusCodeValue());
			idDisciplina = novaDisciplina.getBody().getId();

			Estudante estudante = new Estudante("Estudante Nome", "Estudante CPF", "Masculino", Turno.NOITE);
			HttpEntity<Estudante> requestEstudante = new HttpEntity<>(estudante, headers);
			ResponseEntity<Estudante> novoEstudante = restTemplate.postForEntity("http://localhost:8090/estudantes",
					requestEstudante, Estudante.class);

			assertEquals(estudante.getNome(), novoEstudante.getBody().getNome());
			assertEquals(201, novaDisciplina.getStatusCodeValue());
			idEstudante = novoEstudante.getBody().getId();

			Matricula matricula = new Matricula(novoEstudante.getBody(), novaDisciplina.getBody());
			HttpEntity<Matricula> requestMatricula = new HttpEntity<>(matricula, headers);
			ResponseEntity<Matricula> novaMatricula = restTemplate.postForEntity(uri, requestMatricula,
					Matricula.class);

			assertEquals(matricula.getEstudante().getNome(), novaMatricula.getBody().getEstudante().getNome());
			assertEquals(matricula.getDisciplina().getNome(), novaMatricula.getBody().getDisciplina().getNome());
			assertEquals(201, novaMatricula.getStatusCodeValue());
			idMatricula = novaMatricula.getBody().getId();

		} catch (Exception ex) {
			if (idMatricula != 0l) {
				restTemplate.delete("http://localhost:8090/matriculas/" + idMatricula);
			}
			if (idEstudante != 0l) {
				restTemplate.delete("http://localhost:8090/estudantes/" + idEstudante);
			}
			if (idDisciplina != 0l) {
				restTemplate.delete("http://localhost:8090/disciplinas/" + idDisciplina);
			}
			if (idCurso != 0l) {
				restTemplate.delete("http://localhost:8090/cursos/" + idCurso);
			}
			assertEquals(200, ((RestClientResponseException) ex).getRawStatusCode());

		}

	}
}
