package estoque.desafio.gerenciamento.entities;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	@NotNull
	private String matricula;
	@NotNull
	private String nome;
	@NotNull
	private String senha;
	@NotNull
	private String funcao; // role: GP ou RT

	@OneToMany(mappedBy = "usuario")
	@JsonIgnoreProperties("usuario")
	private Set<Projeto> projetos;

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

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getFuncao() {
		return funcao;
	}

	public void setFuncao(String funcao) {
		if (!funcao.equals("GP") && !funcao.equals("RT")) {
			throw new IllegalArgumentException("Função deve ser GP ou RT");
		}
		this.funcao = funcao;
	}

	    public Set<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(Set<Projeto> projetos) {
        this.projetos = projetos;
    }


	@Override
	public String toString() {
		return "Usuario{" +
				"codigo=" + codigo +
				", matricula='" + matricula + '\'' +
				", nome='" + nome + '\'' +
				", funcao='" + funcao + '\'' +
				'}';
	}

}
