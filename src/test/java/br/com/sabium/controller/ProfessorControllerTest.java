package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.enumeration.pessoa.Graduacao;
import br.com.sabium.model.administrativo.Curso;
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
			ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
			int ids = StringUtils.countOccurrencesOf(result.getBody(), "{\"id\"");
			assertTrue(ids >= 2);
			assertEquals(200, result.getStatusCodeValue());
			assertTrue(result.getBody().contains("graduacao"));

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
//
//	@Test
//	public void deveriaRetornarGraduacaoCorreto() {
//		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//		this.browser = new ChromeDriver();
//		this.browser.navigate().to("http://localhost:8090/professores/graduacao/mestre");
//		
//		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
//		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
//		assertTrue(ids >= 2);
//		
//		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/professores/graduacao/mestre"));		
//		assertTrue(this.browser.getPageSource().contains("\"graduacao\": \"Mestre\""));
//		
//		this.browser.close();
//	}
//	
//	@Test
//	public void deveriaRetornarGraduacaoIncorreto() {
//		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//		this.browser = new ChromeDriver();
//		this.browser.navigate().to("http://localhost:8090/professores/graduacao/mestres");
//		
//		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
//		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
//		assertTrue(ids >= 2);
//			
//		assertFalse(this.browser.getPageSource().contains("\"graduacao\": \"Mestre\""));	
//		assertTrue(this.browser.getPageSource().contains("Whitelabel Error Page"));
//		
//		this.browser.close();
//	}

}
