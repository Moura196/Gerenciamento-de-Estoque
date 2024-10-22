package estoque.desafio.gerenciamento.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String matricula;
	private String nome;
	private String senha;
	
	@OneToMany(mappedBy = "usuarioGP") // Chave estrangeira. Usuário têm várias compras e a relação se dar pelo atributo "usuario" na tabela de Compra
	@JsonIgnoreProperties("usuario")
	private Set<Compra> comprasGP;
	
	@OneToMany(mappedBy = "usuarioRT") // Chave estrangeira. Usuário têm várias compras e a relação se dar pelo atributo "usuario" na tabela de Compra
	@JsonIgnoreProperties("usuario")
	private Set<Compra> comprasRT;

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

	public Set<Compra> getComprasGP() {
		return comprasGP;
	}

	public void setComprasGP(Set<Compra> comprasGP) {
		this.comprasGP = comprasGP;
	}

	public Set<Compra> getComprasRT() {
		return comprasRT;
	}

	public void setComprasRT(Set<Compra> comprasRT) {
		this.comprasRT = comprasRT;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
