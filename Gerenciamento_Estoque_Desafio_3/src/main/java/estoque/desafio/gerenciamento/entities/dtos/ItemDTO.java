package estoque.desafio.gerenciamento.entities.dtos;

import java.math.BigDecimal;

public class ItemDTO {
    private Long id;
    private String descricao;
    private BigDecimal valorUnitario;
    private String patrimonio;
    private Integer quantidadeComprada;
    private String armazenamento;
    private String fornecedor;

    // Construtor vazio
    public ItemDTO() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(BigDecimal valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public Integer getQuantidadeComprada() {
        return quantidadeComprada;
    }

    public void setQuantidadeComprada(Integer quantidadeComprada) {
        this.quantidadeComprada = quantidadeComprada;
    }

    public String getArmazenamento() {
        return armazenamento;
    }

    public void setArmazenamento(String armazenamento) {
        this.armazenamento = armazenamento;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    // Método toString para facilitar a visualização
    @Override
    public String toString() {
        return "ItemDTO{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", valorUnitario=" + valorUnitario +
                ", patrimonio='" + patrimonio + '\'' +
                ", quantidadeComprada=" + quantidadeComprada +
                ", armazenamento='" + armazenamento + '\'' +
                ", fornecedor='" + fornecedor + '\'' +
                '}';
    }
}