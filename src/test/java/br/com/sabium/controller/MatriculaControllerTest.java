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

public class MatriculaControllerTest {

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
	public void deveriaAcessarTodosDetalhados() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/matriculas/detalhado/todos");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/matriculas/detalhado/todos"));
		assertTrue(this.browser.getPageSource().contains("estudante"));
		assertTrue(this.browser.getPageSource().contains("\"idMatricula\""));
		assertTrue(this.browser.getPageSource().contains("status"));

		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele
		// em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids >= 3);

		this.browser.close();
	}

	@Test
	public void deveriaAcessaDetalhadoId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/matriculas/detalhado/4852");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/matriculas/detalhado/4852"));
		assertTrue(this.browser.getPageSource().contains("estudante"));
		assertTrue(this.browser.getPageSource().contains("\"idMatricula\""));
		assertTrue(this.browser.getPageSource().contains("status"));

		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele
		// em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		assertTrue(ids <= 3);

		this.browser.close();
	}

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/matriculas/incompativel");

		assertFalse(this.browser.getCurrentUrl().equals("http://localhost:8090/matriculas"));
		assertTrue(this.browser.getPageSource().contains("Whitelabel Error Page"));

		this.browser.close();
	}


}
