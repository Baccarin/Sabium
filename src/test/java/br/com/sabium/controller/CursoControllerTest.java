package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class CursoControllerTest {

	@MockBean
	private CursoRepository cursoRepository;

	@MockBean
	private CursoController cursoController;

	@MockBean
	private DisciplinaRepository disciplinaRepository;

	private WebDriver browser;

	String exampleCourseJson = "{\"nome\":\"teste\"}";

	private String URI_LOCAL = "http://localhost:8090/cursos";
	

	@BeforeAll
	public void beforeAll() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
	}

	@BeforeEach
	public void beforeEach() {
		this.browser = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		this.browser.close();
	}

	@Test
	public void deveriaAcessarCursosDetalhadosId() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		URI uri = new URI(URI_LOCAL.concat("/detalhado/65"));
		HttpHeaders headers = new HttpHeaders();
		
		try {
			ResponseEntity<Curso> novoCurso = restTemplate.getForEntity(uri, Curso.class);

			assertEquals(65l, novoCurso.getBody().getId());
			assertEquals(200, novoCurso.getStatusCodeValue());
		//	restTemplate.delete(URI_LOCAL + "/" + novoCurso.getBody().getId());

		} catch (HttpClientErrorException ex) {
			assertEquals(400, ex.getRawStatusCode());
			assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
		}
		
		
//		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//		this.browser = new ChromeDriver();
//		this.browser.navigate().to("http://localhost:8090/cursos/detalhado/65");
//
//		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos/detalhado/65"));
//		assertTrue(this.browser.getPageSource().contains("id"));
//		assertTrue(this.browser.getPageSource().contains("65"));
//		assertTrue(this.browser.getPageSource().contains("disciplinas"));
//
//		this.browser.close();
	}

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/incompativel");

		assertFalse(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos"));
		assertTrue(this.browser.getPageSource().contains("type=Not Found"));

		this.browser.close();
	}

	@Test
	public void deveriaRetornarTodosSimplificados() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/cursos/detalhado/todos");

		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele
		// em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "{\"id\"");

		assertTrue(this.browser.getPageSource().contains("{\"id\""));
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos/detalhado/todos"));
		assertTrue(this.browser.getPageSource().contains("disciplinas"));

		assertTrue(ids > 2);

		this.browser.close();
	}

	@Test
	public void deveriaRetornarTodosDetalhados() {

		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/cursos/simplificado/todos");
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele
		// em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "{\"id\"");

		assertTrue(this.browser.getPageSource().contains("{\"id\""));
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos/simplificado/todos"));
		assertTrue(ids > 2);
		this.browser.close();

	}

	@Test
	public void deveriaAcessarEndPointCursoSimplificadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/cursos/simplificado/65");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos/simplificado/65"));
		assertTrue(this.browser.getPageSource().contains("id"));
		assertTrue(this.browser.getPageSource().contains("65"));
		assertFalse(this.browser.getPageSource().contains("disciplinas"));

		this.browser.close();
	}

	// MUDANDO O JEITO DE TESTAR
	
	@Test
	public void deveriaCriarNovoCurso() throws URISyntaxException {
		RestTemplate restTemplate = new RestTemplate();
		URI uri = new URI(URI_LOCAL);
		Curso curso = new Curso("TESTE CURSO");
		HttpHeaders headers = new HttpHeaders();
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
		RestTemplate restTemplate = new RestTemplate();
		URI uri = new URI(URI_LOCAL);
		Curso curso = new Curso("TESTE PUT CURSO");
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<Curso> request = new HttpEntity<>(curso, headers);
		ResponseEntity<Curso> cursoPostTeste = restTemplate.postForEntity(uri, request, Curso.class);

		uri = new URI(URI_LOCAL.concat("/detalhado/" + cursoPostTeste.getBody().getId()));

		try {
			assertEquals("TESTE PUT CURSO", cursoPostTeste.getBody().getNome());
			
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
