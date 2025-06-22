package estoque.desafio.gerenciamento.entities.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CompraInputDTO {
    @NotNull(message = "Data da compra é obrigatória")
    private LocalDate dataCompra;
    private LocalDate dataEnvio;
    private LocalDate dataEmissaoInvoice;
    @NotNull(message = "Valor total é obrigatório")
    private BigDecimal valorTotalInvoice;
    private String observacao;
    @NotNull(message = "Projeto é obrigatório")
    private Long projeto;  
    private Set<Long> itensCodigos = new HashSet<>();

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public LocalDate getDataEmissaoInvoice() {
        return dataEmissaoInvoice;
    }

    public BigDecimal getValorTotalInvoice() {
        return valorTotalInvoice;
    }

    public String getObservacao() {
        return observacao;
    }

    public Long getProjeto() {
        return projeto;
    }

    public Set<Long> getItensCodigos() {
        return itensCodigos;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public void setDataEmissaoInvoice(LocalDate dataEmissaoInvoice) {
        this.dataEmissaoInvoice = dataEmissaoInvoice;
    }

    public void setValorTotalInvoice(BigDecimal valorTotalInvoice) {
        this.valorTotalInvoice = valorTotalInvoice;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setProjeto(Long projeto) {
        this.projeto = projeto;
    }

    public void setItensCodigos(Set<Long> itensCodigos) {
        this.itensCodigos = itensCodigos != null ? itensCodigos : new HashSet<>();
    }

    @Override
    public String toString() {
        return "CompraInputDTO{" +
                "dataCompra=" + dataCompra +
                ", dataEnvio=" + dataEnvio +
                ", dataEmissaoInvoice=" + dataEmissaoInvoice +
                ", valorTotalInvoice=" + valorTotalInvoice +
                ", observacao='" + observacao + '\'' +
                ", projeto=" + projeto +
                ", itensCodigos=" + itensCodigos +
                '}';
    }
}
