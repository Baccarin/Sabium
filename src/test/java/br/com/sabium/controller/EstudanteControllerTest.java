package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.util.StringUtils;

import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

public class EstudanteControllerTest {

	@MockBean
	private CursoRepository cursoRepository;

	@MockBean
	private DisciplinaRepository disciplinaRepository;

	private WebDriver browser;

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
	public void deveriaAcessarUmDetalhadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/estudantes/detalhado/67");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes/detalhado/67"));
		assertTrue(this.browser.getPageSource().contains("cpf"));
		assertTrue(this.browser.getPageSource().contains("\"disciplinas\""));

		this.browser.close();
	}
	
	@Test
	public void deveriaAcessarTodosDetalhadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/estudantes/detalhado/67");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes/detalhado/todos"));
		assertTrue(this.browser.getPageSource().contains("cpf"));
		assertTrue(this.browser.getPageSource().contains("\"disciplinas\""));
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids >= 2);
		
		this.browser.close();
	}
	
	@Test
	public void deveriaAcessarUmSimplificadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/estudantes/simplificado/67");

		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes/simplificado/67"));
		
		assertTrue(ids <= 1);
		
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes/simplificado/67"));
		assertTrue(this.browser.getPageSource().contains("67"));
		assertFalse(this.browser.getPageSource().contains("\"disciplinas\""));

		this.browser.close();
	}

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/estudantes/incompativel");

		
		
		assertFalse(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes"));
		assertTrue(this.browser.getPageSource().contains("Whitelabel Error Page"));

		this.browser.close();
	}

	@Test
	public void deveriaRetornarMaisDeUSimplificadomResultado() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/estudantes/simplificado/todos");
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes/simplificado/todos"));
		assertTrue(ids >= 2);
		assertFalse(this.browser.getPageSource().contains("\"disciplinas\""));
		
		this.browser.close();
	}


}
