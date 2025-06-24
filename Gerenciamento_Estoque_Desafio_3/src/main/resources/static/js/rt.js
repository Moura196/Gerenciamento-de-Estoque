if (!localStorage.getItem('token') && !window.DEV_MODE) {
    window.location.href = '/login.html';
}

const API_BASE_URL = window.API_BASE_URL || '/api';
const DEV_MODE = window.DEV_MODE || true;

// Função para carregar dependências essenciais
async function loadDependencies() {
    if (!window.fetchWithAuth) {
        await new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = '/static/js/auth.js';
            script.onload = resolve;
            script.onerror = () => reject(new Error('Falha ao carregar auth.js'));
            document.head.appendChild(script);
        });
        
        // Verificação adicional para garantir que fetchWithAuth foi carregado
        if (!window.fetchWithAuth) {
            throw new Error('fetchWithAuth não foi carregado corretamente');
        }
    }
}

const sectionMap = {
    'projetos': {
        name: 'projetos',
        html: 'projetos.html',
        js: 'projetos.js',
        initFunction: 'initProjetos'
    },
    'compras': {
        name: 'compras',
        html: 'compras.html',
        js: 'compras.js',
        initFunction: 'initCompras'
    },
    'fornecedores': {
        name: 'fornecedores',
        html: 'fornecedores.html',
        js: 'fornecedores.js',
        initFunction: 'initFornecedores'
    },
    'itens': {
        name: 'itens',
        html: 'itens.html',
        js: 'itens.js',
        initFunction: 'initItens'
    },
    'armazenamento': {
        name: 'armazenamento',
        html: 'armazenamentos.html',
        js: 'armazenamentos.js',
        initFunction: 'initArmazenamentos'
    }
};

const appState = {
    currentSection: null,
    activeCleanup: null,
    scriptRegistry: new Set()
};

// Inicialização modificada para carregar dependências primeiro
async function initRTDashboard() {
    try {
        await loadDependencies(); // Carrega fetchWithAuth antes de tudo
        setupPermanentListeners();
        await loadSection('projetos');
    } catch (error) {
        console.error("Initialization failed:", error);
        showError(`Erro de inicialização: ${error.message}`);
    }
}

function setupPermanentListeners() {
    // Toggle sidebar - mantém direto pois é único
    const toggleSidebar = document.getElementById("toggleSidebar");
    if (toggleSidebar) {
        toggleSidebar._clickHandler = toggleSidebar._clickHandler || (() => {
            const sidebar = document.getElementById("sidebar");
            const texts = document.querySelectorAll(".text");
            sidebar?.classList.toggle("w-12");
            sidebar?.classList.toggle("items-center");  
            texts.forEach(text => text?.classList.toggle("hidden")); 
        });
        toggleSidebar.addEventListener("click", toggleSidebar._clickHandler);
    }

    // Menu buttons - usando delegação de eventos
    document.body.addEventListener('click', (e) => {
        const button = e.target.closest('.menu-btn[data-section]');
        if (button && sectionMap[button.dataset.section]) {
            loadSection(button.dataset.section);
        }
    });

    // Logout - mantém direto pois é único
    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn._clickHandler = logoutBtn._clickHandler || (() => {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
        });
        logoutBtn.addEventListener("click", logoutBtn._clickHandler);
    }
}

async function loadSection(sectionKey) {
    try {
        if (!sectionMap[sectionKey]) {
            throw new Error(`Seção '${sectionKey}' não encontrada`);
        }

        await performFullCleanup();

        const sectionConfig = sectionMap[sectionKey];
        const contentSection = document.getElementById("contentSection");
        if (!contentSection) throw new Error('Área de conteúdo não encontrada');

        const htmlResponse = await fetch(`/static/sections/${sectionConfig.html}`);
        if (!htmlResponse.ok) throw new Error(`Erro no carregamento do HTML: ${htmlResponse.status}`);
        contentSection.innerHTML = await htmlResponse.text();

        await loadSectionScript(sectionConfig);

        if (typeof window[sectionConfig.initFunction] === 'function') {
            appState.activeCleanup = window[sectionConfig.initFunction]();
            appState.currentSection = sectionKey;
        } else {
            throw new Error(`Função de inicialização não encontrada: ${sectionConfig.initFunction}`);
        }

    } catch (error) {
        console.error(`Erro ao carregar seção ${sectionKey}:`, error);
        showError(`Falha ao carregar ${sectionKey}: ${error.message}`);
        
        if (appState.currentSection) {
            loadSection(appState.currentSection);
        }
    }
}

async function performFullCleanup() {
    if (appState.activeCleanup) {
        try {
            console.log(`Executando cleanup para ${appState.currentSection}`);
            appState.activeCleanup();
        } catch (e) {
            console.error('Erro durante cleanup:', e);
        }
        appState.activeCleanup = null;
    }

    const contentSection = document.getElementById("contentSection");
    if (contentSection) {
        const clone = contentSection.cloneNode(false);
        contentSection.replaceWith(clone);
    }

    document.querySelectorAll('script[data-dynamic]').forEach(script => {
        if (!script.src.includes('auth.js')) {
            script.remove();
        }
    });

    appState.scriptRegistry.clear();
}

async function loadSectionScript(sectionConfig) {
    document.querySelectorAll(`script[src*="${sectionConfig.js}"]`).forEach(s => s.remove());

    if (!appState.scriptRegistry.has(sectionConfig.js)) {
        await new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = `/static/js/${sectionConfig.js}`;
            script.dataset.dynamic = 'true';
            script.onload = () => {
                appState.scriptRegistry.add(sectionConfig.js);
                resolve();
            };
            script.onerror = () => reject(new Error(`Falha ao carregar ${sectionConfig.js}`));
            document.body.appendChild(script);
        });

        await new Promise((resolve, reject) => {
            const start = Date.now();
            const check = () => {
                if (window[sectionConfig.initFunction]) {
                    resolve();
                } else if (Date.now() - start > 3000) {
                    reject(new Error(`Timeout aguardando ${sectionConfig.initFunction}`));
                } else {
                    setTimeout(check, 100);
                }
            };
            check();
        });
    }
}

function showError(message) {
    const contentSection = document.getElementById("contentSection");
    if (contentSection) {
        contentSection.innerHTML = `
            <div class="p-4 text-red-700 bg-red-100 rounded">
                ${message}
            </div>
        `;
    }
}

document.addEventListener('DOMContentLoaded', initRTDashboard);


window.loadSection = loadSection;