package estoque.desafio.gerenciamento.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RpController {

    @GetMapping("static/rp-dashboard")
    public String showDashboard() {
        return "rp-dashboard";
    }
}