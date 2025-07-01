if (!window.fetchWithAuth) {
    console.error('Erro crítico: fetchWithAuth não está disponível');
    throw new Error('Dependência fetchWithAuth não carregada');
}
function renderArmazenamentos(armazenamentos) {
    try {
        const tableBody = document.getElementById('armazenamentos-table-tbody');
        if (!tableBody) throw new Error('Tabela de armazenamentos não encontrada');

        tableBody.innerHTML = armazenamentos.map(armazenamento => `
            <tr class="hover:bg-gray-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap">${armazenamento.codigo}</td>
                <td class="px-6 py-4 whitespace-nowrap">${armazenamento.sala}</td>
                <td class="px-6 py-4 whitespace-nowrap">${armazenamento.armario}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex space-x-2">
                        <button data-armazenamento-id="${armazenamento.codigo}" 
                                class="edit-btn p-2 bg-blue-50 text-blue-600 rounded-full hover:bg-blue-100 transition-colors" 
                                title="Editar armazenamento">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', () => openEditModal(btn.dataset.armazenamentoId));
        });

    } catch (error) {
        console.error('Erro ao renderizar armazenamentos:', error);
        throw error;
    }
}
//Carrega armazenamentos do servidor
async function loadArmazenamentos() {
    try {
        const response = await window.fetchWithAuth('/armazenamento/buscar');
        if (!response.ok) throw new Error(`Erro: ${response.status}`);

        const armazenamentos = await response.json();
        if (!Array.isArray(armazenamentos)) {
            throw new Error('Resposta inválida do servidor');
        }

        renderArmazenamentos(armazenamentos);
    } catch (error) {
        console.error('Falha ao carregar armazenamentos:', error);
        showNotification(`Erro: ${error.message}`, 'error');
    }
}
//Carrega formulário de novo armazenamento
async function loadNewArmazenamentoForm() {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) throw new Error('Elemento contentSection não encontrado');

        const response = await fetch('/static/sections/novo-armazenamento.html');
        if (!response.ok) throw new Error('Arquivo não encontrado');

        contentSection.innerHTML = await response.text();
        setupNewArmazenamentoForm();
    } catch (error) {
        console.error('Erro ao carregar formulário:', error);
        showNotification(`Falha: ${error.message}`, 'error');
    }
}
//Configura o formulário de novo armazenamento
function setupNewArmazenamentoForm() {
    const btnVoltar = document.getElementById('btnVoltarArmazenamentos');
    const btnCancelar = document.getElementById('btnCancelarNovoArmazenamento');
    const form = document.getElementById('formNovoArmazenamento');

    if (btnVoltar) btnVoltar.addEventListener('click', () => window.loadSection('armazenamentos'));
    if (btnCancelar) btnCancelar.addEventListener('click', () => window.loadSection('armazenamentos'));

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createArmazenamento();
        });
    }
}
//Cria um novo armazenamento
async function createArmazenamento() {
    const form = document.getElementById('formNovoArmazenamento');
    if (!form) return;

    try {
        const formData = {
            sala: document.getElementById('novoSala').value,
            armario: document.getElementById('novoArmario').value
        };

        const response = await window.fetchWithAuth('/armazenamento/adicionar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (!response.ok) throw new Error(await response.text());

        showNotification('Armazenamento criado com sucesso!', 'success');
        window.loadSection('armazenamentos');
    } catch (error) {
        console.error('Erro ao criar armazenamento:', error);
        showNotification(`Erro: ${error.message}`, 'error');
    }
}
//Modal de edição
function showEditArmazenamentoModal() {
    const modal = document.getElementById('editArmazenamentoModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('invisible', 'opacity-0');
    content.classList.remove('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('opacity-100', 'visible');
        content.classList.add('opacity-100', 'translate-y-0', 'sm:scale-100');
    }, 20);
}

function hideEditArmazenamentoModal() {
    const modal = document.getElementById('editArmazenamentoModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('opacity-100', 'visible');
    content.classList.remove('opacity-100', 'translate-y-0', 'sm:scale-100');

    modal.classList.add('opacity-0');
    content.classList.add('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('invisible');
        document.getElementById('editArmazenamentoForm').reset();
    }, 300);
}

async function openEditModal(armazenamentoId) {
    try {
        const armazenamento = await fetchArmazenamentoData(armazenamentoId);

        document.getElementById('displayArmazenamentoId').textContent = armazenamento.codigo;
        document.getElementById('editArmazenamentoId').value = armazenamento.codigo;

        document.getElementById('editSala').value = armazenamento.sala || '';
        document.getElementById('editArmario').value = armazenamento.armario || '';

        showEditArmazenamentoModal();
    } catch (error) {
        console.error('Erro ao abrir modal de edição:', error);
        showNotification('Erro ao carregar dados do armazenamento', 'error');
    }
}

async function fetchArmazenamentoData(armazenamentoId) {
    try {
        const response = await window.fetchWithAuth(`/armazenamento/buscar/id/${armazenamentoId}`);
        if (!response.ok) throw new Error(`Erro: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar armazenamento:', error);
        throw error;
    }
}

function setupEditModalListeners() {
    document.getElementById('updateArmazenamentoBtn').addEventListener('click', updateArmazenamento);
    document.getElementById('cancelEditArmazenamentoBtn').addEventListener('click', hideEditArmazenamentoModal);

    document.getElementById('editArmazenamentoModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('editArmazenamentoModal')) {
            hideEditArmazenamentoModal();
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && !document.getElementById('editArmazenamentoModal').classList.contains('invisible')) {
            hideEditArmazenamentoModal();
        }
    });
}

async function updateArmazenamento() {
    const armazenamentoId = document.getElementById('editArmazenamentoId').value;
    if (!armazenamentoId) return;

    try {
        const formData = {
            sala: document.getElementById('editSala').value,
            armario: document.getElementById('editArmario').value
        };

        const response = await window.fetchWithAuth(`/armazenamento/atualizar/${armazenamentoId}`, {
            method: 'PATCH',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (!response.ok) throw new Error(await response.text());

        showNotification('Armazenamento atualizado com sucesso!', 'success');
        hideEditArmazenamentoModal();
        loadArmazenamentos();
    } catch (error) {
        console.error('Erro ao atualizar armazenamento:', error);
        showNotification(`Erro: ${error.message}`, 'error');
    }
}

function showNotification(message, type = 'success') {
    alert(`${type.toUpperCase()}: ${message}`);
}
//Inicialização do módulo
function initArmazenamentos() {
    setupEditModalListeners();
    const btnNovoArmazenamento = document.getElementById('btnNovoArmazenamento');
    if (btnNovoArmazenamento) {
        btnNovoArmazenamento.addEventListener('click', loadNewArmazenamentoForm);
    } else {
        console.error('Botão btnNovoArmazenamento não encontrado!');
    }
    loadArmazenamentos().catch(error => {
        console.log('Erro no initArmazenamentos')
    })
    return function() {
        console.log('Executando cleanup de armazenamentos...');
        if (btnNovoArmazenamento) {
            btnNovoArmazenamento.removeEventListener('click', loadNewArmazenamentoForm);
        }  
        cleanupArmazenamentos();
    };
}

function cleanupArmazenamentos() {
    const btn = document.getElementById('btnNovoArmazenamento');
    if (btn) btn.replaceWith(btn.cloneNode(true));
}
//Exporta funções para o escopo global
window.initArmazenamentos = initArmazenamentos;
window.loadArmazenamentos = loadArmazenamentos;
window.loadNewArmazenamentoForm = loadNewArmazenamentoForm;
window.cleanupArmazenamentos = cleanupArmazenamentos;