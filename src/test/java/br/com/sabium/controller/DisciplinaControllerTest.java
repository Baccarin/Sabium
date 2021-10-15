package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.model.administrativo.Disciplina;

public class DisciplinaControllerTest {

	private long ID_DISCIPLINA = 65L;
	private int DURACAO_DISCIPLINA = 5;
	private String URI_LOCAL = "http://localhost:8090/disciplinas";

	private RestTemplate restTemplate = new RestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	@Test
	public void deveriaAcessarDisciplinaSimplificadoId() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/simplificado/" + ID_DISCIPLINA));
		try {
			ResponseEntity<Disciplina> novoDisciplina = restTemplate.getForEntity(uri, Disciplina.class);
			assertEquals(ID_DISCIPLINA, novoDisciplina.getBody().getId());
			assertEquals(200, novoDisciplina.getStatusCodeValue());
			assertTrue(DURACAO_DISCIPLINA == novoDisciplina.getBody().getDuracao());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}

	}

	@Test
	public void deveriaRetornar405ParaURIDesconhecida() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/indisponivel"));
			ResponseEntity<Disciplina> novoDisciplina = restTemplate.getForEntity(uri, Disciplina.class);
			assertFalse(ID_DISCIPLINA == novoDisciplina.getBody().getId());

		} catch (HttpClientErrorException | URISyntaxException ex) {
			assertEquals(405, ((RestClientResponseException) ex).getRawStatusCode());
		}

	}

	@Test
	public void deveriaRetornarTodosSimplificados() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/simplificado/todos"));
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

			int ids = StringUtils.countOccurrencesOf(result.getBody(), "{\"id\"");
			assertTrue(ids >= 2);
			assertEquals(200, result.getStatusCodeValue());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deveriaCriarNovaDisciplina() throws URISyntaxException {
		long idCurso = 0l;
		URI uri = new URI(URI_LOCAL);
		Curso curso = new Curso("TESTE CURSO");
		HttpEntity<Curso> request = new HttpEntity<>(curso, headers);

		try {
			ResponseEntity<Curso> novoCurso = restTemplate.postForEntity("http://localhost:8090/cursos", request,
					Curso.class);

			assertEquals(curso.getNome(), novoCurso.getBody().getNome());
			assertEquals(201, novoCurso.getStatusCodeValue());

			idCurso = novoCurso.getBody().getId();
			Disciplina disciplina = new Disciplina("TESTE DISCIPLINA", 5, novoCurso.getBody());
			HttpEntity<Disciplina> requestDisciplina = new HttpEntity<>(disciplina, headers);

			ResponseEntity<Disciplina> novaDisciplina = restTemplate.postForEntity(uri, requestDisciplina,
					Disciplina.class);

			assertEquals(disciplina.getNome(), novaDisciplina.getBody().getNome());
			assertEquals(201, novaDisciplina.getStatusCodeValue());

		} catch (HttpClientErrorException ex) {
			if (idCurso != 0l) {
				restTemplate.delete("http://localhost:8090/cursos/" + idCurso);
			}
			assertEquals(400, ex.getRawStatusCode());
		}
	}

}
