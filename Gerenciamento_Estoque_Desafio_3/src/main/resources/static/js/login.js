document.addEventListener("DOMContentLoaded", function() {
    document.getElementById("loginForm").addEventListener("submit", async function(event) {
        event.preventDefault();

        const matricula = document.getElementById("matricula").value;
        const senha = document.getElementById("senha").value;

        const response = await fetch("/usuario/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ matricula, senha })
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem("token", data.token);

            const tokenPayload = JSON.parse(atob(data.token.split('.')[1]));
            const funcao = tokenPayload.funcao;

            if (funcao === "GP") {
                window.location.href = "/static/gp-dashboard.html";
            } else if (funcao === "RT") {
                window.location.href = "/static/rt-dashboard.html";
            } else {
                alert("Usuário sem permissão.");
            }
        } else {
            alert("Login inválido!");
        }
    });
});

