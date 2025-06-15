package estoque.desafio.gerenciamento.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class GpController {

    @GetMapping("static/gp-dashboard")
    public String showDashboard() {
        return "gp-dashboard";
    }
}
