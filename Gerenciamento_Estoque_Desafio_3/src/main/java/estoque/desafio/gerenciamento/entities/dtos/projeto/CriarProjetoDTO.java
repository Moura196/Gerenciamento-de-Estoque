package estoque.desafio.gerenciamento.entities.dtos.projeto;

public class CriarProjetoDTO {
	
	private Long codigo;
	private int idProjeto;
	private String apelidoProjeto;
	private Long codigoUsuario;
	
	public Long getCodigo() {
		return codigo;
	}
	
	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}
	
	public int getIdProjeto() {
		return idProjeto;
	}
	
	public void setIdProjeto(int idProjeto) {
		this.idProjeto = idProjeto;
	}
	
	public String getApelidoProjeto() {
		return apelidoProjeto;
	}
	
	public void setApelidoProjeto(String apelidoProjeto) {
		this.apelidoProjeto = apelidoProjeto;
	}

	public Long getCodigoUsuario() {
		return codigoUsuario;
	}

	public void setCodigoUsuario(Long codigoUsuario) {
		this.codigoUsuario = codigoUsuario;
	}
	
}
