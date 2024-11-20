package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Invoice;
import estoque.desafio.gerenciamento.entities.Codigo;
import estoque.desafio.gerenciamento.entities.DataEmissao;
import estoque.desafio.gerenciamento.entities.ValorTotalInvoice;
import estoque.desafio.gerenciamento.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    
    public Invoice criarInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice editarInvoice(Invoice invoice) {
        return invoiceRepository.save(invoice);
    }

    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public void excluirInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    public Codigo criarCodigo(Codigo codigo) {
        return invoiceRepository.saveCodigo(codigo);
    }

    public Codigo editarCodigo(Codigo codigo) {
        return invoiceRepository.saveCodigo(codigo);
    }

    public Codigo getCodigo(Long id) {
        return invoiceRepository.findCodigoById(id);
    }

    public void excluirCodigo(Long id) {
        invoiceRepository.deleteCodigoById(id);
    }

    
    public DataEmissao criarDataEmissao(DataEmissao dataEmissao) {
        return invoiceRepository.saveDataEmissao(dataEmissao);
    }

    public DataEmissao getDataEmissao(Long id) {
        return invoiceRepository.findDataEmissaoById(id);
    }

    public void excluirDataEmissao(Long id) {
        invoiceRepository.deleteDataEmissaoById(id);
    }

    
    public ValorTotalInvoice criarValorTotalInvoice(ValorTotalInvoice valorTotalInvoice) {
        return invoiceRepository.saveValorTotalInvoice(valorTotalInvoice);
    }

    public ValorTotalInvoice editarValorTotalInvoice(ValorTotalInvoice valorTotalInvoice) {
        return invoiceRepository.saveValorTotalInvoice(valorTotalInvoice);
    }

    public ValorTotalInvoice getValorTotalInvoice(Long id) {
        return invoiceRepository.findValorTotalInvoiceById(id);
    }

    public void excluirValorTotalInvoice(Long id) {
        invoiceRepository.deleteValorTotalInvoiceById(id);
    }
}
