if (!window.fetchWithAuth) {
    console.error('Erro crítico: fetchWithAuth não está disponível');
    throw new Error('Dependência fetchWithAuth não carregada');
}
//Renderiza a tabela de compras
function renderCompras(compras) {
    try {
        const tableBody = document.getElementById('compras-table-tbody');
        if (!tableBody) {
            throw new Error('Tabela de compras não encontrada');
        }

        console.log('Dados recebidos:', compras);

        tableBody.innerHTML = compras.map(compra => `
            <tr class="hover:bg-gray-50">
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${compra.codigo}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${formatDate(compra.dataCompra)}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${formatDate(compra.dataEnvio)}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    ${compra.valorTotalInvoice ? 'R$ ' + compra.valorTotalInvoice.toFixed(2).replace('.', ',') : '-'}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${compra.projeto || '-'}</td>
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
                    <button data-compra-id="${compra.codigo}" class="edit-btn text-indigo-600 hover:text-indigo-900 mr-3">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline mr-1" viewBox="0 0 20 20" fill="currentColor">
                            <path d="M13.586 3.586a2 2 0 112.828 2.828l-.793.793-2.828-2.828.793-.793zM11.379 5.793L3 14.172V17h2.828l8.38-8.379-2.83-2.828z" />
                        </svg>
                        Editar
                    </button>
                    <button target="_blank" data-compra-id="${compra.codigo}" class="report-btn text-green-600 hover:text-green-900">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 inline mr-1" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M3 17a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm3.293-7.707a1 1 0 011.414 0L9 10.586V3a1 1 0 112 0v7.586l1.293-1.293a1 1 0 111.414 1.414l-3 3a1 1 0 01-1.414 0l-3-3a1 1 0 010-1.414z" clip-rule="evenodd" />
                        </svg>
                        Relatório
                    </button>
                </td>
            </tr>
        `).join('');

        document.querySelectorAll('.report-btn').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                e.preventDefault();
                const compraId = e.currentTarget.getAttribute('data-compra-id');
                const btn = e.currentTarget;

                try {
                    // Mostrar loading
                    btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-1"></i> Gerando...';
                    btn.disabled = true;

                    // Chamada POST com fetchWithAuth
                    const response = await window.fetchWithAuth('/relatorio/gerar', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({ codigoCompra: compraId })
                    });

                    if (!response.ok) throw new Error('Erro ao gerar relatório');

                    // Criar e disparar download
                    const blob = await response.blob();
                    const url = window.URL.createObjectURL(blob);
                    const a = document.createElement('a');
                    a.href = url;
                    a.download = `relatorio_compra_${compraId}.pdf`;
                    a.click();

                    // Limpar memória
                    setTimeout(() => window.URL.revokeObjectURL(url), 10000);

                } catch (error) {
                    console.error('Erro:', error);
                    alert('Falha ao gerar relatório: ' + error.message);
                } finally {
                    // Restaurar botão
                    btn.innerHTML = '<i class="fas fa-file-pdf mr-1"></i> Relatório';
                    btn.disabled = false;
                }
            });
        });

        // Mantenha os eventos de edição existentes
        document.querySelectorAll('.edit-btn').forEach(btn => {
            btn.addEventListener('click', () => {
                const compraId = btn.getAttribute('data-compra-id');
                openEditModal(compraId);
            });
        });
    } catch (error) {
        console.error('Erro ao renderizar compras:', error);
        throw error;
    }
}
//Formata data para o padrão brasileiro
function formatDate(dateString) {
    if (!dateString) return '-';
    const date = new Date(dateString);
    return date.toLocaleDateString('pt-BR');
}
//Carrega a lista de compras
async function loadCompras() {
    try {
        console.log('Iniciando carga de compras...');
        const response = await window.fetchWithAuth('/compra/buscar');

        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const compras = await response.json();
        console.log('Compras carregadas:', compras);
        renderCompras(compras);
    } catch (error) {
        console.error('Falha ao carregar compras:', error);
        throw error;
    }
}
//Carrega formulário de nova compra
async function loadNewCompraForm() {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) {
            throw new Error('Elemento contentSection não encontrado');
        }

        const response = await fetch('/static/sections/novo-compra.html');
        if (!response.ok) throw new Error(`Erro HTTP! status: ${response.status}`);

        contentSection.innerHTML = await response.text();
        setupNewCompraForm();
    } catch (error) {
        console.error('Erro ao carregar formulário de nova compra:', error);
        showError(`Falha ao carregar formulário: ${error.message}`);
    }
}
//Configura os listeners do formulário de nova compra
function setupNewCompraForm() {
    const btnVoltar = document.getElementById('btnVoltarCompras');
    const form = document.getElementById('formNovaCompra');
    const btnBuscarItens = document.getElementById('btnBuscarItens');
    const inputBusca = document.getElementById('buscarItem');
    const btnCancelar = document.getElementById('btnCancelarNovaCompra');

    if (btnVoltar) {
        btnVoltar.addEventListener('click', () => {
            window.loadSection('compras');
        });
    }

    if (form) {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();
            await createCompra();
        });
    }
    if (btnBuscarItens && inputBusca) {
        btnBuscarItens.addEventListener('click', async () => {
            const termo = inputBusca.value.trim();
            if (termo.length < 1) {
                alert('Digite pelo menos 2 caracteres para buscar');
                return;
            }
            await buscarItens(termo);
        });
        //Permitir busca com Enter
        inputBusca.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                btnBuscarItens.click();
            }
        });
    }
    if (btnCancelar) {
        btnCancelar.addEventListener('click', cancelarNovaCompra);
    }
    //Fechar modal
    const fecharModal = document.getElementById('fecharModalItens');
    if (fecharModal) {
        fecharModal.addEventListener('click', () => {
            document.getElementById('modalItens').classList.add('hidden');
        });
    }

    inicializarListaItens();
}
//Busca itens
async function buscarItens(termoBusca) {
    const btn = document.getElementById('btnBuscarItens');
    if (!btn) {
        console.error('Botão de busca não encontrado');
        return;
    }
    const originalHtml = btn.innerHTML;

    try {
        btn.innerHTML = '<i class="fas fa-spinner fa-spin mr-1"></i> Buscando...';
        btn.disabled = true;
        const response = await window.fetchWithAuth(`/item/buscar?termo=${encodeURIComponent(termoBusca)}`);

        if (!response.ok) {
            throw new Error(`Erro ${response.status}: ${await response.text()}`);
        }
        const itens = await response.json();
        exibirResultadosBusca(itens);

    } catch (error) {
        console.error('Erro na busca:', error);
        alert('Erro ao buscar itens: ' + error.message);
    } finally {
        btn.innerHTML = originalHtml;
        btn.disabled = false;
    }
}
//Exibe resultados
function exibirResultadosBusca(itens) {
    const modal = document.getElementById('modalItens');
    const lista = modal.querySelector('#listaItensBusca');
    lista.innerHTML = '';

    if (itens.length === 0) {
        lista.innerHTML = '<div class="p-4 text-center text-gray-500">Nenhum item encontrado</div>';
        modal.classList.remove('hidden');
        return;
    }

    lista.innerHTML = itens.map(item => `
    <div class="flex items-center justify-between p-3 border-b">
        <div class="flex-1">
            <p class="text-sm text-gray-600">${item.descricao || 'Sem descrição'}</p>
            <p class="text-sm">R$ ${item.valorTotalItem?.toFixed(2).replace('.', ',') || '0,00'}</p>
        </div>
        <button class="adicionar-item-btn ml-4 px-3 py-1 bg-blue-600 hover:bg-blue-700 text-white rounded-md transition-colors" 
                data-item-id="${item.codigo}"
                data-item-descricao="${item.descricao}"
                data-item-valor-total="${item.valorTotalItem}"
                data-item-quantidade="${item.quantComprada}">
            Adicionar
        </button>
    </div>
    `).join('');
    //Configurar eventos dos botões
    modal.querySelectorAll('.adicionar-item-btn').forEach(btn => {
        btn.onclick = () => {
            const item = {
                codigo: btn.dataset.itemId,
                descricao: btn.dataset.itemDescricao,
                valorTotalItem: parseFloat(btn.dataset.itemValorTotal),
                quantComprada: parseInt(btn.dataset.itemQuantidade)
            };
            adicionarItemALista(item);
            modal.classList.add('hidden');

            const btnBusca = document.getElementById('btnBuscarItens');
            if (btnBusca) {
                btnBusca.innerHTML = '<i class="fas fa-search mr-1"></i> Buscar';
                btnBusca.disabled = false;
            }
        };
    });

    modal.classList.remove('hidden');
}
//Inicializa lista
function inicializarListaItens() {
    const lista = document.getElementById('itensAdicionados');
    if (!lista) return;

    lista.addEventListener('click', (e) => {
        if (e.target.classList.contains('remover-item-btn') || e.target.closest('.remover-item-btn')) {
            const itemId = e.target.dataset.itemId || e.target.closest('[data-item-id]').dataset.itemId;
            removerItemDaLista(itemId);
        }
    });
}

function adicionarItemALista(item) {
    const lista = document.getElementById('itensAdicionados');
    const semItens = document.getElementById('semItens');

    if (!item || !item.codigo || !item.descricao || item.valorTotalItem === undefined) {
        console.error('Item inválido:', item);
        return;
    }

    if (semItens) {
        semItens.remove();
    }

    // Verifica se item já existe na lista
    const itemExistente = lista.querySelector(`tr[data-item-id="${item.codigo}"]`);
    if (itemExistente) {
        return; // Não permite adicionar o mesmo item duas vezes
    }

    // Adiciona novo item
    const novaLinha = document.createElement('tr');
    novaLinha.className = 'item-compra';
    novaLinha.dataset.itemId = item.codigo;
    novaLinha.innerHTML = `
        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.descricao}</td>
        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
            ${item.quantComprada}
        </td>
        <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500 valor-total-item">
            R$ ${item.valorTotalItem.toFixed(2).replace('.', ',')}
        </td>
        <td class="px-6 py-4 whitespace-nowrap text-sm font-medium">
            <button class="remover-item-btn text-red-600 hover:text-red-900" data-item-id="${item.codigo}">
                <i class="fas fa-trash"></i>
            </button>
        </td>
    `;

    lista.appendChild(novaLinha);
    calcularTotal();
}

function removerItemDaLista(itemId) {
    const lista = document.getElementById('itensAdicionados');
    const item = lista.querySelector(`tr[data-item-id="${itemId}"]`);

    if (item) {
        item.remove();
        calcularTotal();

        // Mostra mensagem se lista estiver vazia
        if (lista.querySelectorAll('tr.item-compra').length === 0) {
            lista.innerHTML = `
                <tr id="semItens">
                    <td colspan="5" class="px-6 py-4 text-center text-gray-500">Nenhum item adicionado</td>
                </tr>
            `;
        }
    }
}

function calcularTotal() {
    const itens = document.querySelectorAll('#itensAdicionados tr.item-compra');
    let total = 0;

    itens.forEach(item => {
        const valorTotal = parseFloat(
            item.querySelector('.valor-total-item').textContent
                .replace('R$ ', '').replace(',', '.')
        );
        total += valorTotal;
    });

    document.getElementById('totalItens').textContent =
        'R$ ' + total.toFixed(2).replace('.', ',');
}

function cancelarNovaCompra() {
    if (confirm('Tem certeza que deseja cancelar esta compra? Todos os dados não salvos serão perdidos.')) {
        window.loadSection('compras');
    }
}
//Cria nova compra
async function createCompra() {
    const form = document.getElementById('formNovaCompra');
    if (!form) return;
    try {
        console.log('Iniciando criação de compra...');
        const dataCompra = document.getElementById('dataCompra').value;
        if (!dataCompra) throw new Error('Data da compra é obrigatória');
        const linhasItens = document.querySelectorAll('#itensAdicionados tr.item-compra');
        console.log('Número de itens encontrados:', linhasItens.length); // Debug
        
        if (linhasItens.length === 0) {
            throw new Error('Adicione pelo menos um item à compra');
        }
        const itensCodigos = Array.from(linhasItens).map(row => row.dataset.itemId);
        console.log('Códigos dos itens:', itensCodigos);
        const totalItensElement = document.getElementById('totalItens');
        
        let valorTotalInvoice = totalItensElement ? 
            parseFloat(totalItensElement.textContent.replace('R$ ', '').replace(',', '.')) : 0;

        const projetoId = document.getElementById('projetoId')?.value || null;
        if (!projetoId) throw new Error('Projeto é obrigatório');

        if (!projetoId) throw new Error('Projeto é obrigatório');
        const compraData = {
            dataCompra: dataCompra,
            dataEnvio: document.getElementById('dataEnvio')?.value || null,
            dataEmissaoInvoice:document.getElementById('dataEmissaoInvoice')?.value || null,
            valorTotalInvoice: valorTotalInvoice,
            observacao: document.getElementById('observacao')?.value || null,
            projeto: projetoId, 
            itensCodigos: itensCodigos
        };
        console.log('Dados da compra a serem enviados:', compraData);
        const response = await window.fetchWithAuth('/compra/adicionar', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(compraData)
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(errorText || 'Erro ao criar compra');
        }

        showNotification('Compra criada com sucesso!', 'success');
        setTimeout(() => window.loadSection('compras'), 1000);

    } catch (error) {
        console.error('Erro detalhado:', error);
        showNotification(`Erro ao criar compra: ${error.message}`, 'error');
    }
}
//Mostra modal de edição
function showEditModal() {
    const modal = document.getElementById('editCompraModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('invisible', 'opacity-0');
    content.classList.remove('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('opacity-100', 'visible');
        content.classList.add('opacity-100', 'translate-y-0', 'sm:scale-100');
    }, 20);
}
//Esconde modal de edição
function hideEditModal() {
    const modal = document.getElementById('editCompraModal');
    const content = modal.querySelector('.transform');

    modal.classList.remove('opacity-100', 'visible');
    content.classList.remove('opacity-100', 'translate-y-0', 'sm:scale-100');

    modal.classList.add('opacity-0');
    content.classList.add('opacity-0', 'translate-y-4', 'sm:scale-95');

    setTimeout(() => {
        modal.classList.add('invisible');
        document.getElementById('editCompraForm').reset();
    }, 300);
}
//Abre modal de edição
function openEditModal(compraId) {
    console.log('Abrindo modal para editar compra ID:', compraId);

    fetchCompraData(compraId)
        .then(compra => {
            //Preenche os campos do modal
            document.getElementById('editCompraId').value = compra.codigo;
            document.getElementById('displayCompraId').textContent = compra.codigo;
            document.getElementById('editDataCompra').value = compra.dataCompra?.split('T')[0] || '';
            document.getElementById('editDataEnvio').value = compra.dataEnvio?.split('T')[0] || '';
            document.getElementById('editDataEmissaoInvoice').value = compra.dataEmissaoInvoice?.split('T')[0] || '';
            document.getElementById('editValorTotalInvoice').value = compra.valorTotalInvoice || '';
            document.getElementById('editObservacao').value = compra.observacao || '';
            //Preenche itens da compra
            const itensList = document.getElementById('itensCompraList');
            itensList.innerHTML = compra.itens?.map(item => `
                <tr>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-900">${item.descricao}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${item.quantComprada}</td>
                    <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                        R$ ${item.valorTotalItem?.toFixed(2).replace('.', ',') || '0,00'}
                    </td>
                </tr>
            `).join('') || '<tr><td colspan="4" class="px-6 py-4 text-center text-sm text-gray-500">Nenhum item encontrado</td></tr>';
             console.log('Dados da compra para editar: ', compra);
            showEditModal();
        })
        .catch(error => {
            console.error('Erro ao carregar dados da compra:', error);
            showNotification('Erro ao carregar dados da compra', 'error');
        });
}
//Busca dados de uma compra específica
async function fetchCompraData(compraId) {
    console.log(`Buscando compra com ID: ${compraId}`);
    try {
        const response = await window.fetchWithAuth(`/compra/buscar/${compraId}`);

        if (!response.ok) {
            throw new Error(`Erro na requisição: ${response.status}`);
        }

        const compraData = await response.json();
        console.log('Dados da compra carregados:', compraData);
        return compraData;
    } catch (error) {
        console.error('Erro ao buscar dados da compra:', error);
        throw error;
    }
}
//Atualiza uma compra
async function updateCompra(compraId, compraData) {
    try {
        const response = await window.fetchWithAuth(`/compra/alterar/${compraId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(compraData)
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
//Confirma exclusão de compra
function confirmDelete(compraId) {
    if (confirm('Tem certeza que deseja excluir esta compra?')) {
        deleteCompra(compraId);
    }
}
//Exclui uma compra
async function deleteCompra(compraId) {
    try {
        const response = await window.fetchWithAuth(`/compra/excluir/${compraId}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error(`Erro ao excluir: ${response.status}`);
        }

        showNotification('Compra excluída com sucesso!', 'success');
        loadCompras();
    } catch (error) {
        console.error('Erro ao excluir compra:', error);
        showNotification('Erro ao excluir compra', 'error');
    }
}
//Configura listeners do modal de edição
function setupEditModalListeners() {
    document.getElementById('updateCompraBtn').addEventListener('click', async () => {
        const compraId = document.getElementById('editCompraId').value;

        const compraData = {
            dataCompra: document.getElementById('editDataCompra').value || null,
            dataEnvio: document.getElementById('editDataEnvio').value || null,
            dataEmissaoInvoice: document.getElementById('editDataEmissaoInvoice').value || null,
            valorTotalInvoice: parseFloat(document.getElementById('editValorTotalInvoice').value) || null,
            observacao: document.getElementById('editObservacao').value || null
        };

        try {
            await updateCompra(compraId, compraData);

            hideEditModal();
            showNotification('Compra atualizada com sucesso!', 'success');
            loadCompras();
        } catch (error) {
            console.error('Erro ao atualizar compra:', error);
            showNotification('Erro ao atualizar compra', 'error');
        }
    });
    //Fecha modal
    document.getElementById('cancelEditBtn').addEventListener('click', () => {
        hideEditModal();
    });
    //Fecha modal ao clicar fora do conteúdo
    document.getElementById('editCompraModal').addEventListener('click', (e) => {
        if (e.target === document.getElementById('editCompraModal')) {
            hideEditModal();
        }
    });
    //Fecha modal com ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape' &&
            !document.getElementById('editCompraModal').classList.contains('invisible')) {
            hideEditModal();
        }
    });
}
//Função de inicialização
function initCompras() {
    console.log('Inicializando módulo de compras...');

    setupEditModalListeners();
    loadCompras().catch(error => {
        console.error('Erro no initCompras:', error);
    });

    const btnNovaCompra = document.getElementById('btnNovaCompra');
    if (btnNovaCompra) {
        btnNovaCompra.addEventListener('click', loadNewCompraForm);
    } else {
        console.error('Botão btnNovaCompra não encontrado!');
    }
}
//Função de limpeza
function cleanupCompras() {
    const btn = document.getElementById('btnNovaCompra');
    if (btn) btn.replaceWith(btn.cloneNode(true));
}
//Função auxiliar para mostrar notificações
function showNotification(message, type = 'success') {
    // Implemente sua lógica de notificação ou use alert
    alert(`${type.toUpperCase()}: ${message}`);
}
//Exporta funções para o escopo global
window.initCompras = initCompras;
window.cleanupCompras = cleanupCompras;
//Auto-inicialização se os elementos existirem
if (document.getElementById('compras-table-tbody')) {
    initCompras();
}
