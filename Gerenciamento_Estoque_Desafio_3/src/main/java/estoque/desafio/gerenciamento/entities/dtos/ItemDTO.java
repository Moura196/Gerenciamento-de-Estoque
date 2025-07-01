package estoque.desafio.gerenciamento.entities.dtos;
import java.math.BigDecimal;

public class ItemDTO {
    
    private Long codigo;
    private String patrimonio;
    private String descricao;
    private String tipo;
    private BigDecimal valorUnitario;
    private Integer quantComprada;
    private Long fornecedorCodigo;
    private Long armazenamentoCodigo;
    private Long compraCodigo;

  
    public ItemDTO() {
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

     
    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public Integer getQuantComprada() {
        return quantComprada;
    }

    public void setQuantComprada(Integer quantComprada) {
        this.quantComprada = quantComprada;
    }

    public Long getArmazenamentoCodigo() {
        return armazenamentoCodigo;
    }

    public void setArmazenamentoCodigo(Long armazenamentoCodigo) {
        this.armazenamentoCodigo = armazenamentoCodigo;
    }

    public Long getCompraCodigo() {
        return compraCodigo;
    }

    public void setCompraCodigo(Long compraCodigo) {
        this.compraCodigo = compraCodigo;
    }

    public Long getFornecedorCodigo() {
        return fornecedorCodigo;
    }

    public void setFornecedorCodigo(Long fornecedorCodigo) {
        this.fornecedorCodigo = fornecedorCodigo;
    }
    // Método toString para facilitar a visualização
    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + codigo +
                ", descricao='" + descricao + '\'' +
                ", valorUnitario=" + valorUnitario +
                ", patrimonio='" + patrimonio + '\'' +
                ", quantidadeComprada=" + quantComprada +
                ", armazenamento='" + armazenamentoCodigo + '\'' +
                ", fornecedor='" + fornecedorCodigo + '\'' +
                ", compra='" + compraCodigo + '\'' +
                '}';
    }
}