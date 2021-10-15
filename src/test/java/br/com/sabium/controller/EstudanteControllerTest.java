package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.enumeration.pessoa.Turno;
import br.com.sabium.model.pessoa.Estudante;

public class EstudanteControllerTest {

	private String URI_LOCAL = "http://localhost:8090/estudantes";

	private RestTemplate restTemplate = new RestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	private long ID_ESTUDANTE = 67l;

	private String TURNO = "ManhA";

	@Test
	public void deveriaRetornar405ParaURIDesconhecida() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/indisponivel"));
			ResponseEntity<Estudante> estudante = restTemplate.getForEntity(uri, Estudante.class);
			assertFalse(ID_ESTUDANTE == estudante.getBody().getId());

		} catch (HttpClientErrorException | URISyntaxException ex) {
			assertEquals(405, ((RestClientResponseException) ex).getRawStatusCode());
		}

	}

	@Test
	public void deveriaAcessaEstudantesDetalhadosId() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/detalhado/" + ID_ESTUDANTE));
		try {
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
			assertTrue(result.getBody().contains(String.valueOf(ID_ESTUDANTE)));

			int ids = StringUtils.countOccurrencesOf(result.getBody(), "{\"id\"");
			assertEquals(200, result.getStatusCodeValue());
			assertTrue(ids >= 2);
			assertTrue(result.getBody().contains("cpf"));

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}

	}

	@Test
	public void deveriaRetornarDetalhadoTodos() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/detalhado/todos"));
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

			int ids = StringUtils.countOccurrencesOf(result.getBody(), "{\"id\"");
			assertTrue(ids >= 2);
			assertEquals(200, result.getStatusCodeValue());
			assertTrue(result.getBody().contains("disciplinas"));

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void deveriaRetornarSimplificadoId() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/simplificado/" + ID_ESTUDANTE));
			ResponseEntity<Estudante> estudante = restTemplate.getForEntity(uri, Estudante.class);

			assertEquals(200, estudante.getStatusCodeValue());
			assertTrue(estudante.getBody().getId() == ID_ESTUDANTE);

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void deveriaRetornarSimplificadosTodos() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/simplificado/todos"));
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

			int ids = StringUtils.countOccurrencesOf(result.getBody(), "{\"id\"");
			assertTrue(ids >= 2);
			assertEquals(200, result.getStatusCodeValue());
			assertFalse(result.getBody().contains("disciplinas"));

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void deveriaCriarNovoEstudante() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL);
		Estudante estudante = new Estudante("Estudante Nome", "Estudante CPF", "Masculino", Turno.NOITE);
		HttpEntity<Estudante> request = new HttpEntity<>(estudante, headers);
		ResponseEntity<Estudante> novoEstudante = restTemplate.postForEntity(uri, request, Estudante.class);
		try {
			assertEquals(estudante.getNome(), novoEstudante.getBody().getNome());
			assertEquals(201, novoEstudante.getStatusCodeValue());
			restTemplate.delete(URI_LOCAL + "/" + novoEstudante.getBody().getId());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void deveriaAtualizarEstudante() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL);
		Estudante estudante = new Estudante("Estudante Nome", "CPF", "Masculino", Turno.NOITE);
		HttpEntity<Estudante> request = new HttpEntity<>(estudante, headers);
		ResponseEntity<Estudante> novoEstudante = restTemplate.postForEntity(uri, request, Estudante.class);

		uri = new URI(URI_LOCAL.concat("/detalhado/" + novoEstudante.getBody().getId()));
		assertEquals(estudante.getNome(), novoEstudante.getBody().getNome());
		assertEquals(201, novoEstudante.getStatusCodeValue());

		try {
			ResponseEntity<Estudante> novoEstudantePut = restTemplate.getForEntity(uri, Estudante.class);

			novoEstudantePut.getBody().setNome("Teste Put JUnit");
			uri = new URI(URI_LOCAL + "/" + novoEstudantePut.getBody().getId());
			restTemplate.put(uri, request);

			assertEquals("Teste Put JUnit", novoEstudantePut.getBody().getNome());
			assertTrue(novoEstudantePut.getBody().getId().equals(novoEstudante.getBody().getId()));
			assertEquals(200, novoEstudantePut.getStatusCodeValue());

			restTemplate.delete(URI_LOCAL + "/" + novoEstudante.getBody().getId());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void deveriaRetornarTurnoCorreto() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/turno/" + TURNO));
		try {
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

			assertTrue(result.getBody().contains("turno"));
			assertEquals(200, result.getStatusCodeValue());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void naoDeveriaRetornarTurnoCorreto() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/turno/naoExiste"));
		try {
			assertThrows(HttpServerErrorException.class, () -> restTemplate.getForEntity(uri, String.class));
		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

}
