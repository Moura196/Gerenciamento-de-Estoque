package estoque.desafio.gerenciamento.services;

import estoque.desafio.gerenciamento.entities.Invoice;
import estoque.desafio.gerenciamento.repositories.InvoiceRepository;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;


    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

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

}
