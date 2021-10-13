package br.com.sabium.controller.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.com.sabium.model.administrativo.Disciplina;

@Repository
public class DisciplinaDAO {

	private EntityManager em;

	@Autowired
	public DisciplinaDAO(EntityManager em) {
		this.em = em;
	}

	public Disciplina salvar(Disciplina disciplina) {
		return em.merge(disciplina);
	}

	public Disciplina buscarPorId(Long id) {
		return em.find(Disciplina.class, id);
	}

	public List<Disciplina> buscarTodos() {
		return em.createQuery("SELECT l FROM Disciplina l", Disciplina.class).getResultList();
	}

}
