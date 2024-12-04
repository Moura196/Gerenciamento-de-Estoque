package estoque.desafio.gerenciamento.entities.dtos.usuario;

public class AtualizarSenhaDTO {
	
	private Long codigo;
	private String senha;
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public String getSenha() {
		return senha;
	}
	
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
