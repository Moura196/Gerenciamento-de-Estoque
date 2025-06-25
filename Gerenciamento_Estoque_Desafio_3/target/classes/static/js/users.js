if (!window.fetchWithAuth) {
    console.error('Erro crítico: fetchWithAuth não está disponível');
    throw new Error('Dependência fetchWithAuth não carregada');
}

//Forma a tabela
function renderUsers(users) {
    try {
        const tableBody = document.getElementById('users-table-tbody');
        if (!tableBody) {
            throw new Error('Tabela de usuários não encontrada');
        }

        console.log('Dados recebidos:', users);

        tableBody.innerHTML = users.map(user => `
            <tr>
                <td class="px-6 py-4 whitespace-nowrap">${user.codigo}</td>
                <td class="px-6 py-4 whitespace-nowrap">${user.matricula}</td>
                <td class="px-6 py-4 whitespace-nowrap font-medium">${user.nome}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <span class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                        ${user.funcao === 'GP' ? 'bg-green-100 text-green-800' : 'bg-blue-100 text-blue-800'}">
                        ${user.funcao}
                    </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <button data-user-id="${user.codigo}" class="edit-btn text-indigo-600 hover:text-indigo-900">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline mr-1" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                        </svg>
                        Editar
                    </button>
                </td>
            </tr>
        `).join('');
        document.querySelectorAll('.edit-btn').forEach(button => {
            button.addEventListener('click', () => {
                const userId = button.dataset.userId;
                console.log('Editando usuário:', userId);
                openEditModal(userId);
            });
        });
    } catch (error) {
        console.error('Erro ao renderizar usuários:', error);
        throw error;
    }
}

//Busca usuários
async function loadUsers() {
    try {
        console.log('Iniciando carga de usuários...');
        const response = await window.fetchWithAuth('/usuario/buscar');

        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const users = await response.json();
        console.log('Usuários carregados:', users);
        renderUsers(users);
    } catch (error) {
        console.error('Falha ao carregar usuários:', error);
        throw error;
    }
}
//Carrega formulário de novo usuário
async function loadNewUserForm() {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) {
            throw new Error('Elemento contentSection não encontrado');
        }

        const response = await fetch('/static/sections/novo-usuario.html');
        if (!response.ok) throw new Error(`Erro HTTP! status: ${response.status}`);

        contentSection.innerHTML = await response.text();
        setupNewUserForm();
    } catch (error) {
        console.error('Erro ao carregar formulário de novo usuário:', error);
        showError(`Falha ao carregar formulário: ${error.message}`);
    }
}
//configura os listners do formulário de novo usuário
function setupNewUserForm() {
    const btnVoltar = document.getElementById('btnVoltarUsuarios');
    const btnCancelar = document.getElementById('btnCancelarNovoUsuario');
    const form = document.getElementById('formNovoUsuario');
    if (btnVoltar) {
        btnVoltar.addEventListener('click', () => {
            window.loadSection('usuarios');
        });
    }

    if (btnCancelar) {
        btnCancelar.addEventListener('click', () => {
            window.loadSection('usuarios');
        });
    }

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createUser();
        });
    }
}
//Cria novo usuário
async function createUser() {
    const nome = document.getElementById('nome').value;
    const funcao = document.getElementById('funcao').value;
    const senha = document.getElementById('senha').value;
    const confirmarSenha = document.getElementById('confirmarSenha').value;

    // Validações
    if (!nome || !funcao || !senha || !confirmarSenha) {
        showNotification('Por favor, preencha todos os campos.', 'error');
        return;
    }

    if (senha !== confirmarSenha) {
        showNotification('As senhas não coincidem!', 'error');
        return;
    }

    if (senha.length < 6) {
        showNotification('A senha deve ter pelo menos 6 caracteres.', 'error');
        return;
    }

    // Dados do novo usuário
    const userData = {
        nome,
        funcao,
        senha
    };

    try {
        // Desativar o botão para evitar múltiplos cliques
        const submitBtn = document.querySelector('#formNovoUsuario button[type="submit"]');
        submitBtn.disabled = true;
        submitBtn.innerHTML = 'Salvando...';

        const response = await window.fetchWithAuth('/usuario/adicionar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(userData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao criar usuário');
        }

        const result = await response.json();
        showNotification('Usuário criado com sucesso!', 'success');

        // Volta para a lista de usuários após 1 segundo
        setTimeout(() => {
            window.loadSection('usuarios');
        }, 1000);

    } catch (error) {
        console.error('Erro ao criar usuário:', error);
        showNotification(`Erro ao criar usuário: ${error.message}`, 'error');
    } finally {
        const submitBtn = document.querySelector('#formNovoUsuario button[type="submit"]');
        if (submitBtn) {
            submitBtn.disabled = false;
            submitBtn.innerHTML = 'Salvar Usuário';
        }
    }
}
//cerificação do Modal
function setupEditListeners() {
    window.openEditModal = (userId) => {
        console.log('Edit user:', userId);
    };
}

function showNotification(message, type = 'success') {
    alert(`${type.toUpperCase()}: ${message}`);
}

//Mostrar o modal
function showEditModal() {
    const modal = document.getElementById('editUserModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('invisible', 'opacity-0');
    content.classList.remove('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('opacity-100', 'visible');
        content.classList.add('opacity-100', 'translate-y-0', 'sm:scale-100');
    }, 20);
}

//Esconder o modal
function hideEditModal() {
    const modal = document.getElementById('editUserModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('opacity-100', 'visible');
    content.classList.remove('opacity-100', 'translate-y-0', 'sm:scale-100');

    modal.classList.add('opacity-0');
    content.classList.add('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('invisible');
        document.getElementById('editUserForm').reset();
        document.getElementById('displayUserId').textContent = '';
        document.getElementById('displayUserMatricula').textContent = '';
    }, 300);
}

//Abre modal
function openEditModal(userId) {
    console.log('Abrindo modal para editar usuário ID:', userId);
    document.getElementById('editUserModal').classList.remove('invisible');

    fetchUserData(userId)
        .then(user => {
            document.getElementById('displayUserId').textContent = user.codigo;
            document.getElementById('displayUserMatricula').textContent = user.matricula;

            document.getElementById('editUserId').value = user.codigo;
            document.getElementById('editNome').value = user.nome;
            document.getElementById('editFuncao').value = user.funcao;

            showEditModal();
        })
        .catch(error => {
            console.error('Erro ao carregar dados do usuário:', error);
            showNotification('Erro ao carregar dados do usuário 1', 'error');
            hideEditModal();
        });
}
//Busca usuário pelo id
async function fetchUserData(userId) {
    console.log(`Buscando usuário com ID: ${userId}`);
    try {
        const url = `/usuario/buscar/${userId}`;
        console.log(`URL da requisição: ${url}`);
        const response = await window.fetchWithAuth(url);
        console.log(`Resposta recebida. Status: ${response.status}`);

        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const userData = await response.json();
        console.log('Dados do usuário carregados:', userData);
        return userData;
    } catch (error) {
        console.error('Erro ao buscar dados do usuário:', error);
        throw error;
    }
}
//Atualiza usuário
async function updateUser(userId, userData) {
    const userIdNum = Number(userId);
    if (isNaN(userIdNum)) {
        throw new Error(`ID do usuário inválido: ${userId}`);
    }
    const payload = {
        codigo: userIdNum,
        nome: userData.nome || null,
        funcao: userData.funcao || null
    };
    console.log("Enviando dados para atualização:", payload);
    try {
        const response = await window.fetchWithAuth('/usuario/atualizar', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
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

//Eventos do modal
function setupEditModalListeners() {
    document.getElementById('updateUserBtn').addEventListener('click', async () => {
        const userId = document.getElementById('editUserId').value;

        const userData = {
            codigo: parseInt(userId),
            nome: document.getElementById('editNome').value,
            funcao: document.getElementById('editFuncao').value
        };
        console.log('Dados indo para upDateUser:', userData);
        try {
            const updatedUser = await updateUser(userId, userData);
            console.log('Usuário atualizado:', updatedUser);

            hideEditModal();
            showNotification('Usuário atualizado com sucesso!', 'success');
            loadUsers();
        } catch (error) {
            console.error('Erro ao atualizar usuário:', error);
            showNotification('Erro ao atualizar usuário', 'error');
        }
    });
    //Fecha modal
    const cancelButton = document.getElementById('cancelEditBtn');
    if (cancelButton) {
        cancelButton.addEventListener('click', () => {
            console.log('Cancelar edição clicado');
            hideEditModal();
        });
    } else {
        console.error('Botão cancelEditBtn não encontrado!');
    }
    //Fecha modal ao clicar fora do conteúdo
    document.getElementById('editUserModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('editUserModal')) {
            hideEditModal();
        }
    });
    //Fecha modal com a tecla ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' &&
            !document.getElementById('editUserModal').classList.contains('hidden')) {
            hideEditModal();
        }
    });
}
//Confirma a inicialização
function initUsers() {
    console.log('Inicializando módulo de usuários...');
    setupEditModalListeners();

    const btnNovoUsuario = document.getElementById('btnNovoUsuario');
    if (btnNovoUsuario) {
        btnNovoUsuario.addEventListener('click', loadNewUserForm);
    } else {
        console.error('Botão btnNovoUsuario não encontrado!');
    }
    loadUsers().catch(error => {
        console.error('Erro no initUsers:', error);
    });
    return function() {
        console.log('Executando cleanup de Users...');
        if (btnNovoUsuario) {
            btnNovoUsuario.removeEventListener('click', loadNewUserForm);
        }
        cleanupUsers();
    };
}
//REmove listenrs
function cleanupUsers() {

    const btn = document.getElementById('btnNovoUsuario');
    if (btn) btn.replaceWith(btn.cloneNode(true));

}

window.initUsers = initUsers;
window.loadUsers = loadUsers;
window.cleanupUsers = cleanupUsers;
// Teste de funcionamento do botão Cancelar
console.log('Botão Cancelar:', document.getElementById('cancelEditBtn') ? 'Encontrado' : 'Não encontrado');

// Teste da função hideEditModal
console.log('Função hideEditModal:', typeof hideEditModal === 'function' ? 'Definida' : 'Indefinida');