package estoque.desafio.gerenciamento.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String matricula;
	private String nome;
	private String senha;
	
	@OneToOne
	@JoinColumn(name = "Gerente de Projeto", nullable = false)
	@JsonIgnoreProperties("usuarioGP")
	private Projeto projetoGP;
	
//	@OneToOne
//	@JoinColumn(name = "Responsável Técnico", nullable = false)
//	@JsonIgnoreProperties("usuarioRT")
//	private Projeto projetoRT;
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public Projeto getProjetoGP() {
		return projetoGP;
	}

	public void setProjetoGP(Projeto projetoGP) {
		this.projetoGP = projetoGP;
	}

//	public Projeto getProjetoRT() {
//		return projetoRT;
//	}

//	public void setProjetoRT(Projeto projetoRT) {
//		this.projetoRT = projetoRT;
//	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
