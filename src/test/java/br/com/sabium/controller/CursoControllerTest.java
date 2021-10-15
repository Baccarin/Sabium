package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.model.administrativo.Curso;

@SpringBootTest
@AutoConfigureMockMvc
public class CursoControllerTest {

	private String URI_LOCAL = "http://localhost:8090/cursos";

	private RestTemplate restTemplate = new RestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	@Test
	public void deveriaAcessarCursosDetalhadosId() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/detalhado/65"));
		try {
			ResponseEntity<Curso> novoCurso = restTemplate.getForEntity(uri, Curso.class);
			assertEquals(65l, novoCurso.getBody().getId());
			assertEquals(200, novoCurso.getStatusCodeValue());
			assertTrue(novoCurso.getBody().getDisciplinas().size() > 0);

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}

	}

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/indisponivel"));
			ResponseEntity<Curso> novoCurso = restTemplate.getForEntity(uri, Curso.class);
			assertFalse(65l == novoCurso.getBody().getId());

		} catch (HttpClientErrorException | URISyntaxException ex) {
			assertEquals(405, ((RestClientResponseException) ex).getRawStatusCode());
		}

	}

	@Test
	public void deveriaRetornarTodosSimplificados() {
		try {
			boolean vazio = true;
			URI uri = new URI(URI_LOCAL.concat("/simplificado/todos"));
			
			ResponseEntity<List<Curso>> listaResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Curso>>() {
					});
			List<Curso> lista = listaResponse.getBody();

			assertTrue(lista.size() >= 2);
			assertEquals(200, listaResponse.getStatusCodeValue());
			for (Curso c : lista) {
				vazio = c.getDisciplinas().isEmpty();
			}
			assertTrue(vazio);

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deveriaRetornarTodosDetalhados() {
		try {
			boolean vazio = true;
			URI uri = new URI(URI_LOCAL.concat("/detalhado/todos"));
			ResponseEntity<List<Curso>> listaResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Curso>>() {
					});
			List<Curso> lista = listaResponse.getBody();

			assertTrue(lista.size() >= 2);
			assertEquals(200, listaResponse.getStatusCodeValue());
			for (Curso c : lista) {
				if (vazio) {
					vazio = c.getDisciplinas().isEmpty();					
				}
			}
			assertTrue(vazio);

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void deveriaAcessarCursoSimplificadoId() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/simplificado/65"));
			ResponseEntity<Curso> novoCurso = restTemplate.getForEntity(uri, Curso.class);
			assertEquals(200, novoCurso.getStatusCodeValue());
			assertTrue(novoCurso.getBody().getDisciplinas().isEmpty());
			assertTrue(novoCurso.getBody().getId() == 65l);

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void deveriaCriarNovoCurso() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL);
		Curso curso = new Curso("TESTE CURSO");
		HttpEntity<Curso> request = new HttpEntity<>(curso, headers);

		try {
			ResponseEntity<Curso> novoCurso = restTemplate.postForEntity(uri, request, Curso.class);
			assertEquals(curso.getNome(), novoCurso.getBody().getNome());
			assertEquals(201, novoCurso.getStatusCodeValue());
			restTemplate.delete(URI_LOCAL + "/" + novoCurso.getBody().getId());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void deveriaAtualizarCurso() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL);
		HttpEntity<Curso> request = new HttpEntity<>(new Curso("TESTE PUT CURSO"), headers);
		ResponseEntity<Curso> cursoPostTeste = restTemplate.postForEntity(uri, request, Curso.class);
		uri = new URI(URI_LOCAL.concat("/detalhado/" + cursoPostTeste.getBody().getId()));
		assertEquals("TESTE PUT CURSO", cursoPostTeste.getBody().getNome());

		try {
			ResponseEntity<Curso> novoCurso = restTemplate.getForEntity(uri, Curso.class);
			request = new HttpEntity<>(novoCurso.getBody(), headers);
			novoCurso.getBody().setNome("Teste Put JUnit");
			uri = new URI(URI_LOCAL + "/" + novoCurso.getBody().getId());

			restTemplate.put(uri, request);

			assertEquals("Teste Put JUnit", novoCurso.getBody().getNome());
			assertTrue(novoCurso.getBody().getId().equals(cursoPostTeste.getBody().getId()));
			assertEquals(200, novoCurso.getStatusCodeValue());

			restTemplate.delete(URI_LOCAL + "/" + novoCurso.getBody().getId());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

}
