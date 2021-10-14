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

public class ProfessorControllerTest {

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
	public void deveriaAcessarTodosSimplificadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/professores/simplificado/todos");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/professores/simplificado/todos"));
		assertTrue(this.browser.getPageSource().contains("graduacao"));
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids >= 2);

		this.browser.close();
	}
	
	@Test
	public void deveriaAcessarUmSimplificadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/professores/simplificado/102");

		assertFalse(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes/detalhado/todos"));
		assertTrue(this.browser.getPageSource().contains("graduacao"));
		assertTrue(this.browser.getPageSource().contains("sexo"));
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids <= 1);
		
		this.browser.close();
	}


	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/professores/incompativel");
		
		assertFalse(this.browser.getCurrentUrl().equals("http://localhost:8090/estudantes"));
		assertTrue(this.browser.getPageSource().contains("Whitelabel Error Page"));

		this.browser.close();
	}

	@Test
	public void deveriaRetornarGraduacaoCorreto() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/professores/graduacao/mestre");
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids >= 2);
		
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/professores/graduacao/mestre"));		
		assertTrue(this.browser.getPageSource().contains("\"graduacao\": \"Mestre\""));
		
		this.browser.close();
	}
	
	@Test
	public void deveriaRetornarGraduacaoIncorreto() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/professores/graduacao/mestres");
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids >= 2);
			
		assertFalse(this.browser.getPageSource().contains("\"graduacao\": \"Mestre\""));	
		assertTrue(this.browser.getPageSource().contains("Whitelabel Error Page"));
		
		this.browser.close();
	}


}
