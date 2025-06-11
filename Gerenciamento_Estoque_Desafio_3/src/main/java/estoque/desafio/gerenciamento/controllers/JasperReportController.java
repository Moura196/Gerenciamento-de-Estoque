package estoque.desafio.gerenciamento.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.dtos.RelatorioDadosDTO;
import estoque.desafio.gerenciamento.services.JasperReportService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/relatorio")
public class JasperReportController {

    private final JasperReportService jasperReportService;

    public JasperReportController(JasperReportService jasperReportService) {
        this.jasperReportService = jasperReportService;
    }

    @Operation(summary = "Gera um relatório de uma compra:")
    @PostMapping("/gerar")
    public ResponseEntity<?> gerarRelatorio(@RequestBody RelatorioDadosDTO dadosRelatorioRequest) {
        try {
            byte[] pdf = jasperReportService.gerarRelatorio(dadosRelatorioRequest);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_compra.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao gerar relatório: " + e.getMessage());
        }
    }
}