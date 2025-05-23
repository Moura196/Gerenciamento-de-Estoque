package estoque.desafio.gerenciamento.controllers;

import java.io.FileNotFoundException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import estoque.desafio.gerenciamento.entities.dtos.RelatorioDadosDTO;
import estoque.desafio.gerenciamento.services.JasperReportService;

@RestController
@RequestMapping("/relatorio")
public class JasperReportController {
	
	private final JasperReportService jasperReportService;

	public JasperReportController(JasperReportService jasperReportService) {
		this.jasperReportService = jasperReportService;
	}
	
	@PostMapping("/gerar")
	public void gerarRelatorio(@RequestBody RelatorioDadosDTO dadosRelatorioRequest) throws FileNotFoundException {
		this.jasperReportService.gerarRelatorio(dadosRelatorioRequest);
	}
	
}
