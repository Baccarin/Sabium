package br.com.sabium.controller.form;

import java.util.Optional;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import br.com.sabium.controller.exception.CadastroEstudanteInconsistenteException;
import br.com.sabium.controller.exception.EstudanteAlreadyExistException;
import br.com.sabium.enumeration.pessoa.Turno;
import br.com.sabium.model.pessoa.Estudante;
import br.com.sabium.repository.EstudanteRepository;

public class EstudanteForm {

	/*
	 * Validar se da pra jogar os campos para outro form e herdar
	 * extends PessoaForm
	 */
	
	@NotNull
	@NotEmpty
	private String nome;

	@NotNull
	@NotEmpty
	private String cpf;

	@NotNull
	@NotEmpty
	private String sexo;
	
	@NotNull
	@NotEmpty
	private String turno;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}
	
	public Estudante converter(EstudanteRepository estudanteRepository) {
		if (validaCampos()) {
			Estudante estudante = estudanteRepository.findByCpf(cpf);
			if (estudante != null) {
				throw new EstudanteAlreadyExistException();
			}
			return new Estudante(nome, cpf, sexo, Turno.converte(turno));			
		}
		throw new CadastroEstudanteInconsistenteException();
	}

	public Estudante atualizar(Long id, EstudanteRepository estudanteRepository) {
		Optional<Estudante> optional = estudanteRepository.findById(id);
		Estudante estudante = optional.get();
		estudante.setNome(nome);
		estudante.setCpf(cpf);
		estudante.setSexo(sexo);
		return estudante;
	}
	
	public boolean validaCampos() {
		if(nome == null || nome.equals("")) {
			return false;
		}
		if (cpf == null || cpf.equals("")) {
			return false;
		}
		if(sexo == null || sexo.equals("")) {
			return false;
		}
		if(turno == null || turno.equals("")) {
			return false;
		}
		return true;
	}


}
