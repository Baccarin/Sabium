package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.enumeration.pessoa.Graduacao;
import br.com.sabium.model.pessoa.Professor;
import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

public class ProfessorControllerTest {

	@MockBean
	private CursoRepository cursoRepository;

	@MockBean
	private DisciplinaRepository disciplinaRepository;

	private String URI_LOCAL = "http://localhost:8090/professores";

	private RestTemplate restTemplate = new RestTemplate();

	private HttpHeaders headers = new HttpHeaders();

	private long ID_PROFESSOR = 101l;
	private String GRADUACAO = "Mestre";
	
	@Test
	public void deveriaAcessarUmSimplificadoId() throws URISyntaxException {

		URI uri = new URI(URI_LOCAL.concat("/simplificado/" + ID_PROFESSOR));
		try {

			ResponseEntity<Professor> professor = restTemplate.getForEntity(uri, Professor.class);
			assertEquals(ID_PROFESSOR, professor.getBody().getId());
			assertEquals(200, professor.getStatusCodeValue());
			assertEquals(Graduacao.MESTRE, professor.getBody().getGraduacao());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}

	}

	@Test
	public void deveriaAcessarTodosSimplificado() throws URISyntaxException {

		URI uri = new URI(URI_LOCAL.concat("/simplificado/todos"));
		try {
			ResponseEntity<List<Professor>> listaResponse = restTemplate.exchange(uri, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Professor>>() {
					});
			List<Professor> lista = listaResponse.getBody();
			assertEquals(200, listaResponse.getStatusCodeValue());
			assertTrue(lista.size() >= 2);
			assertTrue(lista.get(0).getGraduacao() != null);

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}

	}

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		try {
			URI uri = new URI(URI_LOCAL.concat("/indisponivel"));
			ResponseEntity<Professor> professor = restTemplate.getForEntity(uri, Professor.class);
			assertFalse(ID_PROFESSOR == professor.getBody().getId());

		} catch (HttpClientErrorException | URISyntaxException ex) {
			assertEquals(405, ((RestClientResponseException) ex).getRawStatusCode());
		}

	}

	@Test
	public void deveriaRetornarGraduacaoCorreto() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/graduacao/" + GRADUACAO));
		try {
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);

			assertTrue(result.getBody().contains(GRADUACAO));
			assertEquals(200, result.getStatusCodeValue());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}
	
	@Test
	public void naoDeveriaRetornarGraduacaoCorreto() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL.concat("/graduacao/naoExiste"));
		try {
			assertThrows(HttpServerErrorException.class, () -> restTemplate.getForEntity(uri, String.class));
		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}
	
	
	@Test
	public void deveriaCriarNovoProfessorComGraduacao() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL);
		Professor professor = new Professor("Nome", "000.000.000-00", "Sexo", 100.55, Graduacao.PHD);
		HttpEntity<Professor> request = new HttpEntity<>(professor, headers);

		try {
			ResponseEntity<Professor> novoProfessor = restTemplate.postForEntity(uri, request, Professor.class);
			assertEquals(professor.getNome(), novoProfessor.getBody().getNome());
			assertEquals(201, novoProfessor.getStatusCodeValue());
			assertEquals(Graduacao.PHD, novoProfessor.getBody().getGraduacao());
			restTemplate.delete(URI_LOCAL + "/" + novoProfessor.getBody().getId());
			
		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
	}

	@Test
	public void deveriaAtualizarProfessor() throws URISyntaxException {
		URI uri = new URI(URI_LOCAL);
		HttpEntity<Professor> request = new HttpEntity<>
		(new Professor("Nome", "100.000.000-00", "Sexo", 100.55, Graduacao.PHD), headers);
		
		ResponseEntity<Professor> cursoPostTeste = restTemplate.postForEntity(uri, request, Professor.class);
		uri = new URI(URI_LOCAL.concat("/simplificado/" + cursoPostTeste.getBody().getId()));
		assertEquals("Nome", cursoPostTeste.getBody().getNome());

		try {
			ResponseEntity<Professor> novoProfessor = restTemplate.getForEntity(uri, Professor.class);
			request = new HttpEntity<>(novoProfessor.getBody(), headers);
			novoProfessor = retornaProfessor(novoProfessor);
			uri = new URI(URI_LOCAL + "/" + novoProfessor.getBody().getId());

			restTemplate.put(uri, request);

			assertEquals("Teste Put JUnit", novoProfessor.getBody().getNome());
			assertTrue(novoProfessor.getBody().getId().equals(cursoPostTeste.getBody().getId()));
			assertEquals(200, novoProfessor.getStatusCodeValue());

			restTemplate.delete(URI_LOCAL + "/" + novoProfessor.getBody().getId());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
		}
	}
	
	public ResponseEntity<Professor> retornaProfessor(ResponseEntity<Professor> novoProfessor){
		novoProfessor.getBody().setNome("Teste Put JUnit");
		novoProfessor.getBody().setCpf("220.555.666-59");
		novoProfessor.getBody().setSalario(100.50);
		novoProfessor.getBody().setGraduacao(Graduacao.MESTRE);
		return novoProfessor;
	}


}
