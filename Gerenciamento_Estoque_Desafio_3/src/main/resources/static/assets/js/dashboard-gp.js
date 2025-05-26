import { loadSection } from './templating.js';

const SIDEBAR_ITEMS_GP = [
    { icon: '📦', text: 'Armazenamento', section: 'armazenamento' },
    { icon: '🛒', text: 'Compras', section: 'compras' },
    { icon: '🏢', text: 'Fornecedores', section: 'fornecedores' },
    { icon: '📋', text: 'Itens', section: 'itens' },
    { icon: '📂', text: 'Projetos', section: 'projetos', default: true },
    { icon: '👥', text: 'Usuários', section: 'usuarios' }
];

function buildSidebar(items) {
    const sidebar = document.getElementById('sidebar');
    sidebar.innerHTML = items.map(item => `
        <div class="sidebar-item ${item.default ? 'active' : ''}" 
             data-section="${item.section}">
            <span class="icon">${item.icon}</span>
            <span class="text">${item.text}</span>
        </div>
    `).join('');

    // Event listeners
    document.querySelectorAll('.sidebar-item').forEach(item => {
        item.addEventListener('click', () => {
            loadSection(item.dataset.section);
            setActiveItem(item);
        });
    });
}

// Carrega a seção padrão (Projetos)
document.addEventListener('DOMContentLoaded', () => {
    buildSidebar(SIDEBAR_ITEMS_GP);
    loadSection('projetos');
});