if (!window.fetchWithAuth) {
    console.error('Erro crítico: fetchWithAuth não está disponível');
    throw new Error('Dependência fetchWithAuth não carregada');
}

//Renderiza a tabela de itens
function renderItens(itens) {
    try {
        const tableBody = document.getElementById('itens-table-tbody');
        if (!tableBody) throw new Error('Tabela de itens não encontrada');
        tableBody.innerHTML = itens.map(item => `
            <tr class="hover:bg-gray-50 transition-colors">
                <td class="px-6 py-4 whitespace-nowrap">${item.codigo}</td>
                <td class="px-6 py-4 whitespace-nowrap">${item.armazenamento?.codigo || 'N/A'}</td>
                <td class="px-6 py-4 whitespace-nowrap">${item.descricao}</td>
                <td class="px-6 py-4 whitespace-nowrap">${item.tipo}</td>
                <td class="px-6 py-4 whitespace-nowrap">${formatCurrency(item.valorUnitario)}</td>
                <td class="px-6 py-4 whitespace-nowrap">${item.quantComprada}</td>
                <td class="px-6 py-4 whitespace-nowrap">${formatCurrency(item.valorTotalItem)}</td>
                <td class="px-6 py-4 whitespace-nowrap">${item.fornecedor?.nome || 'N/A'}</td>
                <td class="px-6 py-4 whitespace-nowrap">
                    <div class="flex space-x-2">
                        <button data-item-id="${item.codigo}" class="edit-btn p-2 bg-blue-50 text-blue-600 rounded-full hover:bg-blue-100 transition-colors" title="Editar item">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                            </svg>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');

        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', () => openEditModal(btn.dataset.itemId));
        });

    } catch (error) {
        console.error('Erro ao renderizar itens:', error);
        throw error;
    }
}

//Carrega itens do servidor
async function loadItens() {
    try {
        const response = await window.fetchWithAuth('/item/buscar');
        if (!response.ok) throw new Error(`Erro: ${response.status}`);

        const itens = await response.json();
        if (!Array.isArray(itens)) {
            throw new Error('Resposta inválida do servidor');
        }

        renderItens(itens);
    } catch (error) {
        console.error('Falha ao carregar itens:', error);
        showNotification(`Erro: ${error.message}`, 'error');
    }
}

//Carrega formulário de novo item
async function loadNewItemForm() {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) throw new Error('Elemento contentSection não encontrado');

        const response = await fetch('/static/sections/novo-item.html');
        if (!response.ok) throw new Error(`HTTP error: ${response.status}`);

        contentSection.innerHTML = await response.text();
        setupNewItemForm();
    } catch (error) {
        console.error('Erro ao carregar formulário:', error);
        showNotification(`Falha: ${error.message}`, 'error');
    }
}

//Configura o formulário de novo item
function setupNewItemForm() {
    const btnVoltar = document.getElementById('btnVoltarItens');
    const btnCancelar = document.getElementById('btnCancelarNovoItem');
    const form = document.getElementById('formNovoItem');

    if (btnVoltar) btnVoltar.addEventListener('click', () => window.loadSection('itens'));
    if (btnCancelar) btnCancelar.addEventListener('click', () => window.loadSection('itens'));

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createItem();
        });

        loadFormOptions();

        setupCalculoValorTotal();
    }
}

function setupCalculoValorTotal() {
    const valorUnitario = document.getElementById('novoValorUnitario');
    const quantComprada = document.getElementById('novoQuantComprada');
    const valorTotal = document.getElementById('novoValorTotalItem');

    if (valorUnitario && quantComprada && valorTotal) {
        const calcular = () => {
            const valor = parseFloat(valorUnitario.value) || 0;
            const quant = parseInt(quantComprada.value) || 0;
            valorTotal.value = (valor * quant).toFixed(2);
        };

        valorUnitario.addEventListener('input', calcular);
        quantComprada.addEventListener('input', calcular);
    }
}
//Helper para preencher selects
function fillSelect(id, items, templateFn, defaultOption = '') {
    const select = document.getElementById(id);
    if (select) {
        select.innerHTML = defaultOption + (items?.map(templateFn)?.join('') || '');
    } else {
        console.warn(`Elemento #${id} não encontrado para preenchimento`);
    }
}
//Carrega opções
async function loadFormOptions() {
    try {

        const [fornecedoresRes, armazenamentosRes, comprasRes] = await Promise.all([
            window.fetchWithAuth('/fornecedor/buscar'),
            window.fetchWithAuth('/armazenamento/buscar'),
            window.fetchWithAuth('/compra/buscar')
        ]);

        const fornecedores = fornecedoresRes.ok ? await fornecedoresRes.json() : [];
        const armazenamentos = armazenamentosRes.ok ? await armazenamentosRes.json() : [];
        const compras = comprasRes.ok ? await comprasRes.json() : [];
        const armazenamentoSelect = document.getElementById('novoArmazenamento');

        fillSelect('novoFornecedor', fornecedores, f => `
            <option value="${f.codigo}" data-entity='${JSON.stringify(f)}'>
                ${f.nome}
            </option>
        `, '<option value="">Selecione um fornecedor</option>');

        fillSelect('novoArmazenamento', armazenamentos, a => `
            <option value="${a.codigo}" data-entity='${JSON.stringify(a)}'>
                Sala ${a.sala} - Armário ${a.armario}
            </option>
        `, '<option value="">Selecione um armazenamento</option>');

        fillSelect('novoCompra', compras, c => `
            <option value="${c.codigo}" data-entity='${JSON.stringify(c)}'>
                Compra #${c.codigo} (${new Date(c.dataCompra).toLocaleDateString()})
            </option>
        `, '<option value="">Selecione uma compra</option>');

    } catch (error) {
        console.error('Erro ao carregar opções:', error);
        showNotification('Erro ao carregar opções do formulário', 'error');
    }
}
//Cria um novo item
async function createItem() {
    const form = document.getElementById('formNovoItem');
    if (!form) return;

    try {
        const formData = new FormData(form);

        const fornecedorSelect = document.getElementById('novoFornecedor');
        const armazenamentoSelect = document.getElementById('novoArmazenamento');
        const compraSelect = document.getElementById('novoCompra');

        if (!fornecedorSelect.value || !armazenamentoSelect.value) {
            throw new Error('Selecione todas as opções obrigatórias');
        }

        const itemDTO = {
            patrimonio: formData.get('patrimonio'),
            descricao: formData.get('descricao'),
            tipo: formData.get('tipo'),
            valorUnitario: parseFloat(formData.get('valorUnitario')),
            quantComprada: parseInt(formData.get('quantComprada')),
            fornecedorCodigo: parseInt(fornecedorSelect.value),
            armazenamentoCodigo: parseInt(armazenamentoSelect.value),
            compraCodigo: parseInt(compraSelect.value)
        };

        const response = await window.fetchWithAuth('/item/adicionar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(itemDTO)
        });

        if (!response.ok) throw new Error(await response.text());

        const responseData = await response.json();
        
        showNotification('Item criado com sucesso!', 'success');

        setTimeout(() => window.loadSection('itens'), 500);
    } catch (error) {
        console.error('Erro ao criar item:', error);
        showNotification(`Erro: ${error.message}`, 'error');
    }
}
//Efeito de aparecer do Modal
function showEditItemModal() {
    const modal = document.getElementById('editItemModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('invisible', 'opacity-0');
    content.classList.remove('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('opacity-100', 'visible');
        content.classList.add('opacity-100', 'translate-y-0', 'sm:scale-100');
    }, 20);
}
//Efeito de desaparecer
function hideEditItemModal() {
    const modal = document.getElementById('editItemModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('opacity-100', 'visible');
    content.classList.remove('opacity-100', 'translate-y-0', 'sm:scale-100');

    modal.classList.add('opacity-0');
    content.classList.add('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('invisible');
        document.getElementById('editItemForm').reset();
    }, 300);
}
//Abre modal
async function openEditModal(itemId) {
    try {
        const item = await fetchItemData(itemId);

        document.getElementById('displayItemId').textContent = item.codigo;
        document.getElementById('editItemId').value = item.codigo;

        document.getElementById('editArmazenamento').value = item.armazenamento?.codigo || '';
        document.getElementById('editDescricao').value = item.descricao || '';
        document.getElementById('editTipo').value = item.tipo || '';
        document.getElementById('editValorUnitario').value = item.valorUnitario || '';
        document.getElementById('editQuantComprada').value = item.quantComprada || '';

        await loadEditFormOptions(item);

        setupEditCalculoValorTotal();

        showEditItemModal();
    } catch (error) {
        console.error('Erro ao abrir modal de edição:', error);
        showNotification('Erro ao carregar dados do item', 'error');
    }
}
//Campos de edição
async function loadEditFormOptions(item) {
    try {
        const [fornecedoresRes, armazenamentosRes, comprasRes] = await Promise.all([
            window.fetchWithAuth('/fornecedor/buscar'),
            window.fetchWithAuth('/armazenamento/buscar'),
            window.fetchWithAuth('/compra/buscar')
        ]);

        const fornecedores = fornecedoresRes.ok ? await fornecedoresRes.json() : [];
        const armazenamentos = armazenamentosRes.ok ? await armazenamentosRes.json() : [];
        const compras = comprasRes.ok ? await comprasRes.json() : [];

        const fornecedorSelect = document.getElementById('editFornecedor');
        fornecedorSelect.innerHTML = '<option value="">Selecione um fornecedor</option>' +
            fornecedores.map(f => `
                <option value="${f.codigo}" ${item.fornecedor?.codigo === f.codigo ? 'selected' : ''}>
                    ${f.nome}
                </option>
            `).join('');

        const armazenamentoSelect = document.getElementById('editArmazenamento');
        armazenamentoSelect.innerHTML = '<option value="">Selecione um armazenamento</option>' +
            armazenamentos.map(a => `
                <option value="${a.codigo}" ${item.armazenamento?.codigo === a.codigo ? 'selected' : ''}>
                    Sala ${a.sala} - Armário ${a.armario}
                </option>
            `).join('');

        const compraSelect = document.getElementById('editCompra');
        compraSelect.innerHTML = '<option value="">Selecione uma compra</option>' +
            compras.map(c => `
                <option value="${c.codigo}" ${item.compra?.codigo === c.codigo ? 'selected' : ''}>
                    Compra #${c.codigo} (${new Date(c.dataCompra).toLocaleDateString()})
                </option>
            `).join('');

    } catch (error) {
        console.error('Erro ao carregar opções:', error);
        throw error;
    }
}
//Busca por id
async function fetchItemData(itemId) {
    try {
        const response = await window.fetchWithAuth(`/item/buscar/id/${itemId}`);
        if (!response.ok) throw new Error(`Erro: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error('Erro ao buscar item:', error);
        throw error;
    }
}
//Calcula toal
function setupEditCalculoValorTotal() {
    const valorUnitario = document.getElementById('editValorUnitario');
    const quantComprada = document.getElementById('editQuantComprada');

    if (valorUnitario && quantComprada) {
        const calcular = () => {
            const valor = parseFloat(valorUnitario.value) || 0;
            const quant = parseInt(quantComprada.value) || 0;
            // Você pode exibir em algum lugar se quiser
            console.log('Valor total calculado:', (valor * quant).toFixed(2));
        };

        valorUnitario.addEventListener('input', calcular);
        quantComprada.addEventListener('input', calcular);
    }
}
//listners do modal
function setupEditModalListeners() {

    document.getElementById('updateItemBtn').addEventListener('click', updateItem);

    document.getElementById('cancelEditItemBtn').addEventListener('click', hideEditItemModal);

    document.getElementById('editItemModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('editItemModal')) {
            hideEditItemModal();
        }
    });

    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' && !document.getElementById('editItemModal').classList.contains('invisible')) {
            hideEditItemModal();
        }
    });
}
//Atualiza item
async function updateItem() {
    const itemId = document.getElementById('editItemId').value;
    if (!itemId) return;

    try {
        const formData = {
            codigo: parseInt(itemId),
            patrimonio: document.getElementById('editPatrimonio').value,
            descricao: document.getElementById('editDescricao').value,
            tipo: document.getElementById('editTipo').value,
            valorUnitario: parseFloat(document.getElementById('editValorUnitario').value),
            quantComprada: parseInt(document.getElementById('editQuantComprada').value),
            fornecedorCodigo: parseInt(document.getElementById('editFornecedor').value) || null,
            armazenamentoCodigo: parseInt(document.getElementById('editArmazenamento').value) || null,
            compraCodigo: document.getElementById('novoCompra').value 
            ? parseInt(document.getElementById('novoCompra').value) 
            : null
        };

        const response = await window.fetchWithAuth('/item/atualizar', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(formData)
        });

        if (!response.ok) throw new Error(await response.text());

        showNotification('Item atualizado com sucesso!', 'success');
        hideEditItemModal();
        loadItens();
    } catch (error) {
        console.error('Erro ao atualizar item:', error);
        showNotification(`Erro: ${error.message}`, 'error');
    }
}

//Funções auxiliares
function formatCurrency(value) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);
}

function showNotification(message, type = 'success') {
    alert(`${type.toUpperCase()}: ${message}`);
}

//Inicialização do módulo
function initItens() {
    setupEditModalListeners();
    if (document.getElementById('itens-table-tbody')) {
        loadItens().catch(error => {
            console.log('Erro no initItens', error);
        });
        const btnNovoItem = document.getElementById('btnNovoItem');
        if (btnNovoItem) {
            btnNovoItem.addEventListener('click', loadNewItemForm);
        } else {
            console.warn('Botão btnNovoItem não encontrado na inicialização');
        }
    }
    if (document.getElementById('formNovoItem')) {
        setupNewItemForm();
    }
    return function () {
        if (btnNovoItem) {
            btnNovoItem.removeEventListener('click', loadNewItemForm);
        }
        cleanupItens();
    };

}

function cleanupItens() {

    const btn = document.getElementById('btnNovoItem');
    if (btn) btn.replaceWith(btn.cloneNode(true));

}

//Exporta funções para o escopo global
window.initItens = initItens;
window.loadItens = loadItens;
window.loadNewItemForm = loadNewItemForm;
window.cleanupItens = cleanupItens;