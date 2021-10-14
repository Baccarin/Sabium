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

public class DisciplinaControllerTest {

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
	public void deveriaAcessarDisciplinaSimplificadoTodos() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/disciplinas/simplificado/todos");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/disciplinas/simplificado/todos"));
		assertTrue(this.browser.getPageSource().contains("duracao"));
		
		this.browser.close();
	}

	@Test
	public void deveriaRetornar404ParaURIDesconhecida() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/disciplinas/incompativel");

		assertFalse(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos"));
		assertTrue(this.browser.getPageSource().contains("has no explicit mapping for /"));

		this.browser.close();
	}

	@Test
	public void deveriaRetornarDisciplinaId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/cursos/simplificado/65");
		
		// péssimo jeito - seria melhor se conseguisse capturar o json e transformar ele em um objeto
		int ids = StringUtils.countOccurrencesOf(browser.getPageSource(), "id");
		
		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos/simplificado/65"));
		assertTrue(ids <= 1);
		
		this.browser.close();
	}


}
