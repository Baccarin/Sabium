package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.sabium.model.administrativo.Curso;
import br.com.sabium.repository.CursoRepository;
import br.com.sabium.repository.DisciplinaRepository;

public class CursoControllerTest {

	@MockBean
	private CursoRepository cursoRepository;

	@MockBean
	private CursoController cursoController;

	@MockBean
	private DisciplinaRepository disciplinaRepository;

	private WebDriver browser;

	private MockMvc mockMvc;
	
	String exampleCourseJson = "{\"nome\":\"teste\"}";


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
	public void deveriaAcessarEndPointCursosDetalhadosId() {
		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
		this.browser = new ChromeDriver();
		this.browser.navigate().to("http://localhost:8090/cursos/detalhado/65");

		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos/detalhado/65"));
		assertTrue(this.browser.getPageSource().contains("Teste"));
		assertTrue(this.browser.getPageSource().contains("65"));

		this.browser.close();
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
	public void deveriaRetornarMaisDeUmResultado() {
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

//	@Test
//	public void deveriaRetornarNovoCurso() {
//		
//		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//		this.browser = new ChromeDriver();
//		this.browser.navigate().to("http://localhost:8090/cursos/simplificado/todos");
//		
//		Curso curso = new Curso("JUnit");
//		curso = cursoRepository.save(curso);
//		cursoRepository.flush();
//		
//		System.out.println("----" + curso.getId());
//		
//		assertTrue(this.browser.getCurrentUrl().equals("http://localhost:8090/cursos"));
//		assertTrue(this.browser.getPageSource().contains("{\"id\":" + curso.getId()));
//		
//		
//		this.browser.close();
//	}
//	

//	@Test
//	public void addEmployee() throws Exception {
//		System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
//		this.browser = new ChromeDriver();
//		this.browser.navigate().to("http://localhost:8090/cursos");
//
//		Curso emp = new Curso("curso_name");// whichever data your entity class have
//
//		Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(emp);
////
////        mockMvc.perform(MockMvcRequestBuilders.post("/cursos")
////                .content(asJsonString(emp))
////                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));
//
//		MvcResult result = mockMvc
//				.perform(MockMvcRequestBuilders.post("/cursos").contentType(MediaType.APPLICATION_JSON)
//						.content(asJsonString(emp).getBytes(StandardCharsets.UTF_8)).accept(MediaType.APPLICATION_JSON))
//				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
//
//		this.browser.close();
//
//	}

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void teste2() throws Exception {
		Curso mockCourse = new Curso("Smallest Number");

		// studentService.addCourse to respond back with mockCourse
		Mockito.when(cursoRepository.save(Mockito.any(Curso.class))).thenReturn(mockCourse);

		// Send course as body to /students/Student1/courses
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/cursos")
				.accept(MediaType.APPLICATION_JSON).content(exampleCourseJson).contentType(MediaType.APPLICATION_JSON);

		MvcResult result = mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();

		assertEquals(HttpStatus.CREATED.value(), response.getStatus());

		assertEquals("http://localhost/cursos/simplificado/todos", response.getHeader(HttpHeaders.LOCATION));

	}

//    @Test
//    public void getAllRecords_success() throws Exception {
//        List<Curso> cursos = new ArrayList<>();
//        cursos.add(new Curso("teste1"));
//        cursos.add(new Curso("teste2"));
//        
//        
//        
//        Mockito.when(cursoRepository.findAll()).thenReturn(cursos);
//        
//        mockMvc.perform(MockMvcRequestBuilders
//                .get("/cursos/simplificado/todos")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[2].nome", is("teste2")));
//    }

//	@BeforeEach
//	public void setup() throws Exception {
//		this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
//	}
//
//	@Test
//	public void deveriaEncontrarEndPointRaiz() {
//		ServletContext servletContext = webApplicationContext.getServletContext();
//
//		Assert.assertNotNull(servletContext);
//		Assert.assertTrue(servletContext instanceof MockServletContext);
//		Assert.assertNotNull(webApplicationContext.getBean("cursoController"));
//	}

//	@Test
//	public void teste() {
//		assertNotNull(cursoController.detalhar(130l));
//	}
//	
//	@Test
//	public void testeAll() {
//		assertTrue(cursoController.listAll().getBody().size() > 0) ;
//	}

//    @Test
//    public void teste1() {
//    	List<CursoSimplificadoDTO> cursos = (List<CursoSimplificadoDTO>) cursoController.listAll();
//    	assertTrue(cursos.size() > 0 );
//    }
//

}
