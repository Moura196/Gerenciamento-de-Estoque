if (!window.fetchWithAuth) {
    console.error('Erro crítico: auth.js não foi carregado corretamente');
    window.handleAuthError();
    throw new Error('Dependência auth.js não carregada');
}
//Carrega página
function initDashboard() {  
    checkAuth().then(() => {
        setupDashboard();
        loadSection('usuarios');
    }).catch(error => {
        console.error("Authentication check failed:", error);
        window.handleAuthError();
    });
}
//Confere elementos e chama setupEventlistners
async function setupDashboard() {
    setupEventListeners();  
}
//confirmação da presença do <scrip></scrip> na página
function loadScript(src, callback) {
    return new Promise((resolve, reject) => {
        const script = document.createElement('script');
        script.src = src;
        script.onload = () => {
            if (callback) callback();
            resolve();
        };
        script.onerror = () => {
            console.error(`Erro ao carregar o script: ${src}`);
            reject(new Error(`Falha ao carregar ${src}`));
        };
        document.head.appendChild(script);
    });
}
//Carega as sections conforme o botão
async function loadSection(sectionName) {
    try {
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) {
            throw new Error('Elemento contentSection não encontrado');
        }
        const sectionMap = {
            'usuarios': {
                html: 'users.html',
                js: 'users.js',
                initFunction: 'initUsers'
            },
            'fornecedores': {
                html: 'fornecedores.html',
                js: 'fornecedores.js',
                initFunction: 'initFornecedores'
            },
            'projetos': {
                html: 'projetos.html',
                js: 'projetos.js',
                initFunction: 'initProjetos'
            },
            'compras': {
                html: 'compras.html',
                js: 'compras.js',
                initFunction: 'initCompras'
            },
            'itens': {
                html: 'itens.html',
                js: 'itens.js',
                initFunction: 'initItens'
            },
            'relatorios': {
                html: 'relatorios.html',
                js: 'relatorios.js',
                initFunction: 'initRelatorios'
            }
        };

        const section = sectionMap[sectionName];
        if (!section) {
            throw new Error(`Seção '${sectionName}' não configurada`);
        }
        const htmlResponse = await fetch(`/static/sections/${section.html}`);
        if (!htmlResponse.ok) throw new Error(`Erro HTTP no HTML! status: ${htmlResponse.status}`);
        contentSection.innerHTML = await htmlResponse.text();
        await new Promise(resolve => setTimeout(resolve, 100));

        await loadScript(`/static/js/${section.js}`);

        const initFunction = window[section.initFunction];
        if (typeof initFunction !== 'function') {
            throw new Error(`Função ${section.initFunction} não disponível`);
        }
        
        initFunction();
    } catch (error) {
        console.error(`Erro ao carregar seção ${sectionName}:`, error);
        showError(`Falha ao carregar ${sectionName}: ${error.message}`);
    }
}
//Valida autenticação
async function checkAuth() {
    const token = localStorage.getItem('token');
    if (!token) {
        throw new Error('No token found');
    }
    
    try {
        const response = await fetch('/api/auth/validate', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (!response.ok) {
            localStorage.removeItem('token');
            throw new Error('Token invalid');
        }
        return true;
    } catch (error) {
        localStorage.removeItem('token');
        throw error;
    }
}
//Mostra erros
function showError(message) {
    const contentSection = document.getElementById('contentSection');
    if (contentSection) {
        contentSection.innerHTML = `
            <div class="p-4 text-red-700 bg-red-100 rounded">
                ${message}
            </div>
        `;
    }
}
//Listners de eventos da página
function setupEventListeners() {
    const toggleSidebar = document.getElementById("toggleSidebar");
    if (toggleSidebar) {
        toggleSidebar.addEventListener("click", function() {
            const sidebar = document.getElementById("sidebar");
            const texts = document.querySelectorAll(".text");
            sidebar.classList.toggle("w-12");
            sidebar.classList.toggle("items-center");  
            texts.forEach(text => text.classList.toggle("hidden")); 
        });
    }
    document.querySelectorAll(".menu-btn").forEach(button => {
        button.addEventListener("click", function() {
            const sectionName = this.dataset.section;
            loadSection(sectionName);
        });
    });

    const logoutBtn = document.getElementById("logoutBtn");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function() {
            localStorage.removeItem("token");
            window.location.href = "/login.html";
        });
    }
}

//Evento de confirmação initDashbord()
document.addEventListener('DOMContentLoaded', function() {
    if (window.initDashboard) {
        initDashboard();
    } else {
        console.error('initDashboard não está disponível!');
    }
});

window.initDashboard = initDashboard;
window.initSection = loadSection;