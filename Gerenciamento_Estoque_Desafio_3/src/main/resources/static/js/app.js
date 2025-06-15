const DEV_MODE = true;
import { initDashboard } from './dashboard.js';

export function openEditModal(userId) {
  console.log('Abrindo modal para usuário:', userId);

}

document.addEventListener('DOMContentLoaded', () => {
  if (document.body.classList.contains('gp-dashboard-page')) {
    initDashboard();
  }
});

if (DEV_MODE) {
    console.warn('Executando em modo de desenvolvimento - segurança desabilitada');
    // Desabilita verificações de autenticação
    window.checkAuth = function() {
        return Promise.resolve(true);
    };
    window.fetchWithAuth = fetch; // Usa fetch normal sem token
}