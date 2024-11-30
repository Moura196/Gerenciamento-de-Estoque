package estoque.desafio.gerenciamento.controllers;

import estoque.desafio.gerenciamento.entities.Invoice;
import estoque.desafio.gerenciamento.services.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping
    public ResponseEntity<Object> criarInvoice(@RequestBody Invoice invoice) {
        try {
            Invoice novaInvoice = invoiceService.criarInvoice(invoice);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaInvoice);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar a invoice: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editarInvoice(@PathVariable Long id, @RequestBody Invoice invoice) {
        if (!id.equals(invoice.getCodigo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("O ID fornecido não corresponde ao ID da invoice.");
        }
        try {
            Invoice invoiceAtualizada = invoiceService.editarInvoice(invoice);
            return ResponseEntity.status(HttpStatus.OK).body(invoiceAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao editar a invoice: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invoice com ID " + id + " não encontrada.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(invoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirInvoice(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoice(id);
        if (invoice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invoice com ID " + id + " não encontrada.");
        }
        try {
            invoiceService.excluirInvoice(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body("Invoice com ID " + id + " foi excluída com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao excluir a invoice: " + e.getMessage());
        }
    }
}
