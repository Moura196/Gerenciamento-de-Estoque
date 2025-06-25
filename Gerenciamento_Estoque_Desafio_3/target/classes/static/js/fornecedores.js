
if (!window.fetchWithAuth) {
    console.error('Erro crítico: fetchWithAuth não está disponível');
    throw new Error('Dependência fetchWithAuth não carregada');
}
//Tabela de fornecedores
function renderFornecedores(fornecedores) {
    try {
        const tableBody = document.getElementById('fornecedores-table-tbody');
        if (!tableBody) {
            throw new Error('Tabela de fornecedores não encontrada');
        }
        
        tableBody.innerHTML = fornecedores.map(fornecedor => `
            <tr class="hover:bg-gray-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap">${fornecedor.codigo}</td>
                <td class="px-6 py-4 whitespace-nowrap font-medium">${fornecedor.nome}</td>
                <td class="px-6 py-4 whitespace-nowrap">${fornecedor.cnpj}</td>
                <td class="px-6 py-4 whitespace-nowrap">${fornecedor.telefone}</td>
                <td class="px-6 py-4 whitespace-nowrap">${fornecedor.email}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex space-x-2">
                    <!-- Botão Editar -->
                        <button data-fornecedor-codigo="${fornecedor.codigo}" 
                            class="edit-btn p-2 bg-blue-50 text-blue-600 rounded-full hover:bg-blue-100 transition-colors"
                            title="Editar fornecedor">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                        </button>   
                    </div>
                </td>
            </tr>
        `).join('');
        
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', () => {
                const fornecedorId = button.dataset.fornecedorCodigo;
                openEditModal(fornecedorId);
            });
        });
        
        const btnNovoFornecedor = document.getElementById('btnNovoFornecedor');
        if (btnNovoFornecedor) {
            btnNovoFornecedor.addEventListener('click', loadNewFornecedorForm);
        }
    } catch (error) {
        console.error('Erro ao renderizar fornecedores:', error);
        throw error;
    }
}
//Busca fornecedores
async function loadFornecedores() {
    try {
        const response = await window.fetchWithAuth('/fornecedor/buscar');
        
        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }
        
        const fornecedores = await response.json();
        renderFornecedores(fornecedores);
    } catch (error) {
        console.error('Falha ao carregar fornecedores:', error);
        showNotification(`Erro ao carregar fornecedores: ${error.message}`, 'error');
    }
}
//Abre modal de edição
function openEditModal(fornecedorId) {
    if (!fornecedorId) {
        console.error('ID do fornecedor não fornecido');
        showNotification('Erro: ID do fornecedor inválido', 'error');
        return;
    }
    fetchFornecedorData(fornecedorId)
        .then(fornecedor => {
            document.getElementById('editFornecedorId').value = fornecedor.codigo;
            document.getElementById('editFornecedorNomeOriginal').value = fornecedor.nome;
            document.getElementById('editNome').value = fornecedor.nome;
            document.getElementById('editCnpj').value = fornecedor.cnpj;
            document.getElementById('editTelefone').value = fornecedor.telefone;
            document.getElementById('editEmail').value = fornecedor.email;
            document.getElementById('editEndereco').value = fornecedor.endereco || '';
            
            showEditModal();
        })
        .catch(error => {
            console.error('Erro ao carregar dados do fornecedor:', error);
            showNotification('Erro ao carregar dados do fornecedor', 'error');
        });
}
//Busca fornecedor pelo id
async function fetchFornecedorData(fornecedorCodigo) {
    try {
        const response = await window.fetchWithAuth(`/fornecedor/buscar/codigo/${encodeURIComponent(fornecedorCodigo)}`);
        
        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar dados do fornecedor:', error);
        throw error;
    }
}
//Mostrar o modal de edição
function showEditModal() {
    const modal = document.getElementById('editFornecedorModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('invisible', 'opacity-0');
    content.classList.remove('opacity-0', 'translate-y-4', 'sm:scale-95');
    
    setTimeout(() => {
        modal.classList.add('visible', 'opacity-100');
        content.classList.add('opacity-100', 'translate-y-0', 'sm:scale-100');
    }, 20);
}
//Esconder o modal
function hideEditModal() {
    const modal = document.getElementById('editFornecedorModal');
    const content = modal.querySelector('.transform');
    
    modal.classList.remove('visible', 'opacity-100');
    content.classList.remove('opacity-100', 'translate-y-0', 'sm:scale-100');
    
    modal.classList.add('opacity-0');
    content.classList.add('opacity-0', 'translate-y-4', 'sm:scale-95');
    
    setTimeout(() => {
        modal.classList.add('invisible');
        document.getElementById('editFornecedorForm').reset();
    }, 300);
}
//Atualiza fornecedor
async function updateFornecedor(codigo, fornecedorData) {
    try {
        const url = `/fornecedor/alterar/${codigo}`;
        console.log("Enviando PATCH para:", url, "com dados:", fornecedorData);
        
        const response = await window.fetchWithAuth(url, {
            method: 'PATCH',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(fornecedorData)
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro na atualização: ${response.status} - ${errorText}`);
        }
        
        return await response.json();
    } catch (error) {
        console.error('Erro na requisição de atualização:', error);
        throw error;
    }
}
//Eventos do modal
function setupEditModalListeners() {
    document.getElementById('updateFornecedorBtn').addEventListener('click', async () => {
         const codigo = document.getElementById('editFornecedorId').value;
        
        const fornecedorData = {
            nome: document.getElementById('editNome').value,
            cnpj: document.getElementById('editCnpj').value,
            telefone: document.getElementById('editTelefone').value,
            email: document.getElementById('editEmail').value,
            endereco: document.getElementById('editEndereco').value
        };
        
        try {
            await updateFornecedor(codigo, fornecedorData);
            hideEditModal();
            showNotification('Fornecedor atualizado com sucesso!', 'success');
            window.loadSection('fornecedores');
        } catch (error) {
            console.error('Erro ao atualizar fornecedor:', error);
            showNotification('Erro ao atualizar fornecedor: ${error.message}', 'error');
        }
    });
    
    document.getElementById('cancelEditBtn').addEventListener('click', hideEditModal);

    document.getElementById('editFornecedorModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('editFornecedorModal')) {
            hideEditModal();
        }
    });
    
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            hideEditModal();
        }
    });
}
//Carrega formulário de novo fornecedor
function loadNewFornecedorForm() {
    const contentSection = document.getElementById('contentSection');
    contentSection.innerHTML = '';
    
    fetch('/static/sections/novo-fornecedor.html')
        .then(response => response.text())
        .then(html => {
            contentSection.innerHTML = html;
            setupNewFornecedorForm();
        })
        .catch(error => {
            console.error('Erro ao carregar formulário:', error);
            showNotification('Erro ao carregar formulário de novo fornecedor', 'error');
        });
}
//Form de novo fornecedor
function setupNewFornecedorForm() {
    const form = document.getElementById('formNovoFornecedor');
    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createFornecedor();
        });
    }
    
    const btnCancelar = document.getElementById('btnCancelarNovoFornecedor');
    if (btnCancelar) {
        btnCancelar.addEventListener('click', () => {
            window.loadSection('fornecedores')
        });
    }
     const btnVoltar = document.getElementById('btnVoltarFornecedores');
    if (btnVoltar) {
        btnVoltar.addEventListener('click', () => {
            window.loadSection('fornecedores');
        });
    }
}
//Cria novo fornecedor
async function createFornecedor() {
    const nome = document.getElementById('nome').value;
    const cnpj = document.getElementById('cnpj').value;
    const telefone = document.getElementById('telefone').value;
    const email = document.getElementById('email').value;
    const endereco = document.getElementById('endereco').value;
    
    const fornecedorData = {
        nome,
        cnpj,
        telefone,
        email,
        endereco
    };
    
    try {
        const response = await window.fetchWithAuth('/fornecedor/adicionar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(fornecedorData)
        });
        
        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro ao criar: ${response.status} - ${errorText}`);
        }
        
        showNotification('Fornecedor criado com sucesso!', 'success');
        setTimeout(() => {
            window.loadSection('fornecedores');
        }, 1000);
    } catch (error) {
        console.error('Erro ao criar fornecedor:', error);
        showNotification(`Erro ao criar fornecedor: ${error.message}`, 'error');
    }
}

function showNotification(message, type = 'success') {
    alert(`${type.toUpperCase()}: ${message}`);
}

function initFornecedores() {
    console.log('Inicializando módulo de fornecedores...');
    setupEditModalListeners();
    const btnNovoFornecedor = document.getElementById('btnNovoFornecedor');
    if (btnNovoFornecedor) {
        btnNovoFornecedor.addEventListener('click', loadNewFornecedorForm);
    } else {
        console.error('Botão btnNovoFornecedor não encontrado!');
    }
    loadFornecedores().catch(error => {
        console.log('Erro no initArmazenamentos', error);
    });
    return function() {
        console.log('Executando cleanup de fornecedores...');
        if (btnNovoFornecedor) {
            btnNovoFornecedor.removeEventListener('click', loadNewFornecedorForm);
        }
        cleanupFornecedores();
    };

}

function cleanupFornecedores() {

    const btn = document.getElementById('btnNovoFornecedor');
    if (btn) btn.replaceWith(btn.cloneNode(true));
    
}

window.initFornecedores = initFornecedores;
window.cleanupFornecedores = cleanupFornecedores;