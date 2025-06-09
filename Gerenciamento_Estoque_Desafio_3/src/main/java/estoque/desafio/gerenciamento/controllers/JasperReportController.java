package estoque.desafio.gerenciamento.controllers;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.Fornecedor;
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
	public ResponseEntity<?> gerarRelatorio(@RequestBody RelatorioDadosDTO dadosRelatorioRequest) throws FileNotFoundException {
		try {
			this.jasperReportService.gerarRelatorio(dadosRelatorioRequest);
			return ResponseEntity.ok("Relatório gerado com sucesso!");
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatusCode.valueOf(504));
			//"Erro ao gerar relatório", HttpStatusCode.valueOf(504)
		}
	}
	
}