package br.com.sabium.controller.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.sabium.model.administrativo.Curso;

@Repository
public class CursoDAO {

	private EntityManager em;

	@Autowired
	public CursoDAO(EntityManager em) {
		this.em = em;
	}

	public Curso salvar(Curso curso) {
		return em.merge(curso);
	}

	public Curso buscarPorId(Long id) {
		return em.find(Curso.class, id);
	}

	public List<Curso> buscarTodos() {
		return em.createQuery("SELECT * FROM CURSO ", Curso.class).getResultList();
	}

}
