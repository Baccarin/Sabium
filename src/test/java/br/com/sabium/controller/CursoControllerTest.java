package br.com.sabium.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CursoControllerTest {

	@Autowired
	private CursoController controller;

	@Test
	public void contextLoads() throws Exception {

		assertNotNull(controller);

	}

}
