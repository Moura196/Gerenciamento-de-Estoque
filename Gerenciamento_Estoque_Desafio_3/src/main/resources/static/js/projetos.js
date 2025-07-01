if (!window.fetchWithAuth) {
    console.error('Erro crítico: fetchWithAuth não está disponível');
    throw new Error('Dependência fetchWithAuth não carregada');
}
//Renderiza a tabela de projetos
function renderProjetos(projetos) {
    try {
        const tableBody = document.getElementById('projetos-table-tbody');
        if (!tableBody) {
            throw new Error('Tabela de projetos não encontrada');
        }
        tableBody.innerHTML = projetos.map(projeto => `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${projeto.codigo}</td>
                <td class="px-6 py-4 whitespace-nowrap font-medium">${projeto.nome || 'N/A'}</td>
                <td class="px-6 py-4 whitespace-nowrap">${projeto.apelidoProjeto || 'N/A'}</td>
                <td class="px-6 py-4 whitespace-nowrap">${projeto.usuario ? projeto.usuario.nome : 'N/A'}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <button data-projeto-id="${projeto.codigo}" class="edit-btn text-indigo-600 hover:text-indigo-900 mr-2">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline mr-1" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                        </svg>
                        Editar
                    </button>
                </td>
            </tr>
        `).join('');
        //Adiciona listeners para os botões de edição
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', () => {
                const projetoId = button.dataset.projetoId;
                openEditModal(projetoId);
            });
        });
    } catch (error) {
        console.error('Erro ao renderizar projetos:', error);
        throw error;
    }
}
//Carrega projetos do servidor
async function loadProjetos() {
    try {
        const response = await window.fetchWithAuth('/projeto/buscar', {
        });

        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const projetos = await response.json();
        renderProjetos(projetos);
    } catch (error) {
        console.error('Falha ao carregar projetos:', error);
        throw error;
    }
}
//Carrega formulário de novo projeto
async function loadNewProjetoForm() {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) throw new Error('Elemento contentSection não encontrado');
        let response;
        try {
            response = await fetch('/static/sections/novo-projeto.html');
            if (!response.ok) throw new Error('Arquivo não encontrado');
        } catch (error) {
            console.warn('Falha ao carregar formulário, usando fallback:', error);
            contentSection.innerHTML = `
                <div class="p-6 bg-white rounded-lg shadow-md">
                    <h2 class="text-2xl font-semibold mb-4">Novo Projeto</h2>
                    <p class="text-red-500">Formulário não carregado. Recarregue a página.</p>
                </div>
            `;
            return;
        }

        contentSection.innerHTML = await response.text();
        setupNewProjetoForm();
    } catch (error) {
        console.error('Erro ao carregar formulário:', error);
        showNotification(`Falha ao carregar formulário: ${error.message}`, 'error');
    }
}

async function fetchUsuarios() {
    try {
        const response = await window.fetchWithAuth('/usuario/buscar');
        if (!response.ok) throw new Error('Erro ao carregar usuários');
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar usuários:', error);
        showNotification('Erro ao carregar lista de usuários', 'error');
        return [];
    }
}

async function fetchComprasDisponiveis() {
    try {
        const response = await window.fetchWithAuth('/compra/buscar');
        if (!response.ok) throw new Error('Erro ao carregar compras');

        const todasCompras = await response.json();
        return todasCompras.filter(compra => !compra.projeto);

    } catch (error) {
        console.error('Erro ao buscar compras:', error);
        showNotification('Erro ao carregar compras disponíveis', 'error');
        return [];
    }
}

function formatarData(dataString) {
    if (!dataString) return 'Sem data';
    const options = { day: '2-digit', month: '2-digit', year: 'numeric' };
    return new Date(dataString).toLocaleDateString('pt-BR', options);
}

function renderComprasDisponiveis(compras) {
    const listaCompras = document.getElementById('listaCompras');

    if (!listaCompras) {
        console.error('Elemento #listaCompras não encontrado no DOM');
        return;
    }

    try {
        listaCompras.innerHTML = '';

        if (!compras || compras.length === 0) {
            listaCompras.innerHTML = `
                <div class="p-3 bg-gray-50 rounded text-center text-gray-500 text-sm">
                    Nenhuma compra disponível para vincular
                </div>`;
            return;
        }
        //Adiciona cada compra
        compras.forEach(compra => {
            const item = document.createElement('div');
            item.className = 'flex items-center p-2 hover:bg-gray-50 rounded';
            item.innerHTML = `
                <input type="checkbox"
                       id="compra-${compra.codigo}"
                       value="${compra.codigo}"
                       class="h-4 w-4 text-blue-600 rounded focus:ring-blue-500">
                <label for="compra-${compra.codigo}" class="ml-2 block text-sm">
                    <span class="font-medium text-gray-700">#${compra.codigo}</span>
                    <span class="block text-xs text-gray-500">
                        ${compra.descricao || 'Sem descrição'} • 
                        ${formatarData(compra.dataCompra)}
                    </span>
                </label>
            `;
            listaCompras.appendChild(item);
        });

    } catch (error) {
        console.error('Erro ao renderizar compras:', error);
        listaCompras.innerHTML = `
            <div class="p-3 bg-red-50 text-red-600 rounded text-sm">
                Erro ao carregar lista de compras
            </div>`;
    }
}
//Configura os listeners do formulário de novo projeto
async function setupNewProjetoForm() {
    try {
        const form = document.getElementById('formNovoProjeto');
        if (!form) {
            throw new Error('Formulário não encontrado');
        }
        const [usuarios, compras] = await Promise.all([
            fetchUsuarios(),
            fetchComprasDisponiveis()
        ]);
        const usuarioSelect = document.getElementById('usuario');
        if (usuarioSelect) {
            usuarioSelect.innerHTML = '<option value="">Selecione um responsável</option>';
            usuarios.forEach(usuario => {
                const option = document.createElement('option');
                option.value = usuario.codigo;
                option.textContent = usuario.nome;
                usuarioSelect.appendChild(option);
            });
        }
        renderComprasDisponiveis(compras);

        const voltar = () => window.loadSection('projetos');
        document.getElementById('btnVoltarProjetos')?.addEventListener('click', voltar);
        document.getElementById('btnCancelarNovoProjeto')?.addEventListener('click', voltar);

        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await criarProjeto();
        });

    } catch (error) {
        console.error('Erro no setup do formulário:', error);
        showNotification('Falha ao carregar o formulário', 'error');
        const contentSection = document.getElementById('contentSection');
        if (contentSection) {
            contentSection.innerHTML = `
                <div class="p-6 bg-white rounded-lg shadow-md">
                    <h2 class="text-xl font-semibold text-red-600">Erro no formulário</h2>
                    <p class="mt-2 text-gray-600">Recarregue a página ou tente novamente mais tarde.</p>
                    <button onclick="window.location.reload()" 
                            class="mt-4 px-4 py-2 bg-blue-600 text-white rounded-md">
                        Recarregar
                    </button>
                </div>`;
        }
    }
}
//Carrega usuários para o select no formulário
async function loadUsersForSelect() {
    try {
        const response = await window.fetchWithAuth('/usuario/buscar');
        if (!response.ok) throw new Error('Erro ao carregar usuários');

        const users = await response.json();
        const userSelect = document.getElementById('usuario');

        if (userSelect) {
            userSelect.innerHTML = users.map(user =>
                `<option value="${user.codigo}">${user.nome} (${user.matricula})</option>`
            ).join('');
        }
    } catch (error) {
        console.error('Erro ao carregar usuários para select:', error);
    }
}
//Cria novo projeto
async function criarProjeto(event) {
    // Previne o comportamento padrão do formulário
    if (event) event.preventDefault();

    // Obtém o formulário corretamente
    const form = document.getElementById('formNovoProjeto');
    if (!form) {
        console.error('Formulário não encontrado');
        showNotification('Erro: Formulário não carregado', 'error');
        return;
    }

    // Referência ao botão de submit
    const btnSubmit = form.querySelector('button[type="submit"]');
    const btnOriginalText = btnSubmit.innerHTML;

    try {
        btnSubmit.disabled = true;
        btnSubmit.innerHTML = '<i class="fas fa-spinner fa-spin mr-2"></i> Salvando...';

        const projetoData = {
            idProjeto: parseInt(form.idProjeto.value),
            nome: form.nome.value,
            apelidoProjeto: form.apelidoProjeto.value || null,
            usuarioId: parseInt(form.usuario.value),
            comprasIds: Array.from(
                document.querySelectorAll('#listaCompras input[type="checkbox"]:checked')
            ).map(checkbox => parseInt(checkbox.value))
        };

        const response = await window.fetchWithAuth('/projeto/adicionar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(projetoData)
        });

        if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || 'Erro ao criar projeto');
        }

        showNotification('Projeto criado com sucesso!', 'success');
        window.loadSection('projetos');

    } catch (error) {
        console.error('Erro ao criar projeto:', error);
        showNotification(`Erro: ${error.message}`, 'error');
        form.scrollIntoView({ behavior: 'smooth' });

    } finally {
        if (btnSubmit) {
            btnSubmit.disabled = false;
            btnSubmit.innerHTML = btnOriginalText;
        }
    }
}
//Mostrar modal de edição
function showEditModal() {
    const modal = document.getElementById('editProjetoModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('invisible', 'opacity-0');
    content.classList.remove('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('opacity-100', 'visible');
        content.classList.add('opacity-100', 'translate-y-0', 'sm:scale-100');
    }, 20);
}
//Esconder modal de edição
function hideEditModal() {
    const modal = document.getElementById('editProjetoModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('opacity-100', 'visible');
    content.classList.remove('opacity-100', 'translate-y-0', 'sm:scale-100');

    modal.classList.add('opacity-0');
    content.classList.add('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('invisible');
        document.getElementById('editProjetoForm').reset();
    }, 300);
}
//Abre modal de edição com dados do projeto
async function openEditModal(projetoId) {
    try {
        const projeto = await fetchProjetoData(projetoId);
        const users = await fetchUsers();
        //Preenche os dados do formulário
        document.getElementById('editProjetoId').value = projeto.codigo;
        document.getElementById('editIdProjeto').value = projeto.idProjeto || '';
        document.getElementById('editNome').value = projeto.nome || '';
        document.getElementById('editApelidoProjeto').value = projeto.apelidoProjeto || '';
        //Preenche o select de usuários
        const userSelect = document.getElementById('editUsuario');
        userSelect.innerHTML = users.map(user =>
            `<option value="${user.codigo}" ${user.codigo === projeto.usuario.codigo ? 'selected' : ''}>
                ${user.nome} (${user.matricula})
            </option>`
        ).join('');

        showEditModal();
    } catch (error) {
        console.error('Erro ao carregar dados do projeto:', error);
        showNotification('Erro ao carregar dados do projeto', 'error');
    }
}
//Busca dados de um projeto específico
async function fetchProjetoData(projetoId) {
    try {
        const response = await window.fetchWithAuth(`/projeto/buscar`);

        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const projetos = await response.json();
        const projeto = projetos.find(p => p.codigo == projetoId);

        if (!projeto) {
            throw new Error(`Projeto com ID ${projetoId} não encontrado`);
        }

        return projeto;
    } catch (error) {
        console.error('Erro ao buscar dados do projeto:', error);
        throw error;
    }
}
//Busca lista de usuários
async function fetchUsers() {
    try {
        const response = await window.fetchWithAuth('/usuario/buscar');
        if (!response.ok) throw new Error('Erro ao carregar usuários');
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar usuários:', error);
        throw error;
    }
}
//Atualiza projeto
async function updateProjeto(projetoId, projetoData) {
    try {
        const response = await window.fetchWithAuth(`/projeto/alterar/${projetoId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                idProjeto: projetoData.idProjeto,
                nome: projetoData.nome,
                apelidoProjeto: projetoData.apelidoProjeto,
                usuario: { codigo: projetoData.usuario.codigo }
            })
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Erro na atualização: ${response.status} - ${errorText}`);
        }

        return response.json();
    } catch (error) {
        console.error('Erro na requisição de atualização:', error);
        throw error;
    }
}
//Exclui projeto
async function deleteProjeto(projetoId) {
    if (!confirm('Tem certeza que deseja excluir este projeto?')) {
        return;
    }

    try {
        const response = await window.fetchWithAuth(`/projeto/excluir/${projetoId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao excluir projeto');
        }

        showNotification('Projeto excluído com sucesso!', 'success');
        loadProjetos();
    } catch (error) {
        console.error('Erro ao excluir projeto:', error);
        showNotification(`Erro ao excluir projeto: ${error.message}`, 'error');
    }
}
//Configura listeners do modal de edição
function setupEditModalListeners() {
    //Listener para o botão de atualização
    document.getElementById('updateProjetoBtn').addEventListener('click', async () => {
        const projetoId = document.getElementById('editProjetoId').value;

        const projetoData = {
            idProjeto: parseInt(document.getElementById('editIdProjeto').value),
            nome: document.getElementById('editNome').value,
            apelidoProjeto: document.getElementById('editApelidoProjeto').value,
            usuario: { codigo: parseInt(document.getElementById('editUsuario').value) }
        };
        try {
            await updateProjeto(projetoId, projetoData);
            hideEditModal();
            showNotification('Projeto atualizado com sucesso!', 'success');
            loadProjetos();
        } catch (error) {
            console.error('Erro ao atualizar projeto:', error);
            showNotification('Erro ao atualizar projeto', 'error');
        }
    });
    //Listener para o botão de cancelar
    const cancelButton = document.getElementById('cancelEditBtn');
    if (cancelButton) {
        cancelButton.addEventListener('click', () => {
            hideEditModal();
        });
    }
    //Fecha modal ao clicar fora do conteúdo
    document.getElementById('editProjetoModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('editProjetoModal')) {
            hideEditModal();
        }
    });
    //Fecha modal com a tecla ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && !document.getElementById('editProjetoModal').classList.contains('hidden')) {
            hideEditModal();
        }
    });
}
//Mostra notificação
function showNotification(message, type = 'success') {
    alert(`${type.toUpperCase()}: ${message}`);
}
//Inicializa o módulo de projetos
function initProjetos() {
    
    setupEditModalListeners();
    const btnNovoProjeto = document.getElementById('btnNovoProjeto');
    if (btnNovoProjeto) {
        btnNovoProjeto.addEventListener('click', loadNewProjetoForm);
    } else {
        console.error('Botão btnNovoProjeto não encontrado!');
    }
    loadProjetos().catch(error => {
        console.error('Erro no initProjetos:', error);
    });
    return function () {
        if (btnNovoProjeto) {
            btnNovoProjeto.removeEventListener('click', loadNewProjetoForm);
        }
        cleanupProjetos();
    };
}
//Limpa listeners
function cleanupProjetos() {
    const btn = document.getElementById('btnNovoProjeto');
    if (btn) btn.replaceWith(btn.cloneNode(true));
}

window.initProjetos = initProjetos;
window.loadProjetos = loadProjetos;
window.cleanupProjetos = cleanupProjetos;