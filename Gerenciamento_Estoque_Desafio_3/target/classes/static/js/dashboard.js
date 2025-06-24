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
//Objeto de mapeamento de seções 
const sectionMap = {
    'usuarios': {
        name: 'usuarios',
        html: 'users.html',
        js: 'users.js',
        initFunction: 'initUsers'
    },
    'fornecedores': {
        name: 'fornecedores',
        html: 'fornecedores.html',
        js: 'fornecedores.js',
        initFunction: 'initFornecedores'
    },
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
    'itens': {
        name: 'itens',
        html: 'itens.html',
        js: 'itens.js',
        initFunction: 'initItens',
        dependencies: ['fetchWithAuth'],
        css: null
    },
    'armazenamentos': {
        name: 'armazenamentos',
        html: 'armazenamentos.html',
        js: 'armazenamentos.js',
        initFunction: 'initArmazenamentos'
    }
};
let currentSection = null;
let currentCleanup = null;

async function loadSection(sectionKey) {
    try {
        if (currentCleanup) {
            currentCleanup();
            currentCleanup = null;
        }
        if (!sectionMap[sectionKey]) {
            throw new Error(`Seção '${sectionKey}' não encontrada`);
        }
        const sectionConfig = sectionMap[sectionKey];
        const contentSection = document.getElementById('contentSection');
        if (!contentSection) throw new Error('Elemento contentSection não encontrado');

        const htmlResponse = await fetch(`/static/sections/${sectionConfig.html}`);
        if (!htmlResponse.ok) throw new Error(`Erro ao carregar HTML: ${htmlResponse.status}`);
        
        contentSection.innerHTML = await htmlResponse.text();
        document.querySelectorAll(`script[src*="${sectionConfig.js}"]`).forEach(script => script.remove());

        await new Promise((resolve, reject) => {
            const script = document.createElement('script');
            script.src = `/static/js/${sectionConfig.js}`;
            script.onload = resolve;
            script.onerror = () => reject(new Error(`Falha ao carregar script: ${sectionConfig.js}`));
            document.body.appendChild(script);
        });
    
        if (typeof window[sectionConfig.initFunction] !== 'function') {
            throw new Error(`Função de inicialização não encontrada: ${sectionConfig.initFunction}`);
        }

        const cleanup = window[sectionConfig.initFunction]();
        if (typeof cleanup === 'function') {
            currentCleanup = cleanup;
        } else {
            console.warn(`A função ${sectionConfig.initFunction} não retornou uma função de cleanup`);
        }
        currentSection = sectionKey;

    } catch (error) {
        console.error(`Erro ao carregar seção ${sectionKey}:`, error);
        showError(`Falha ao carregar ${sectionKey}: ${error.message}`);
        
        if (error.message.includes('401') || error.message.includes('token')) {
            window.handleAuthError();
        }
    }
}
// async function loadSection(sectionKey) {
//     try {
//         if (!sectionMap.hasOwnProperty(sectionKey)) {
//             throw new Error(`Seção '${sectionKey}' não encontrada no mapeamento`);
//         }

//         const sectionConfig = sectionMap[sectionKey];
        
//         if (currentSection && window[`cleanup${currentSection}`]) {
//             window[`cleanup${currentSection}`]();
//         }

//         const contentSection = document.getElementById('contentSection');
//         if (!contentSection) throw new Error('Elemento contentSection não encontrado');

//         console.log(`Carregando HTML: ${sectionConfig.html}`);
//         const htmlResponse = await fetch(`/static/sections/${sectionConfig.html}`);
//         if (!htmlResponse.ok) throw new Error(`HTTP error! status: ${htmlResponse.status}`);

//         contentSection.innerHTML = await htmlResponse.text();

//         const oldScript = document.querySelector(`script[src*="${sectionConfig.js}"]`);
//         if (oldScript) oldScript.remove();

//         if (!window[sectionConfig.initFunction]) {
//             console.log(`Carregando JS: ${sectionConfig.js}`);
            
//             const script = document.createElement('script');
//             script.src = `/static/js/${sectionConfig.js}`;
//             script.async = true;
            
//             script.onerror = () => {
//                 throw new Error(`Falha ao carregar o script: ${sectionConfig.js}`);
//             };
            
//             document.body.appendChild(script);

//             await new Promise((resolve, reject) => {
//                 const timeout = setTimeout(() => {
//                     reject(new Error(`Timeout ao carregar ${sectionConfig.initFunction}`));
//                 }, 5000);

//                 const checkFunction = setInterval(() => {
//                     if (window[sectionConfig.initFunction]) {
//                         clearTimeout(timeout);
//                         clearInterval(checkFunction);
//                         resolve();
//                     }
//                 }, 100);
//             });
//         }

//         console.log(`Inicializando: ${sectionConfig.initFunction}`);
//         if (typeof window[sectionConfig.initFunction] === 'function') {
//             window[sectionConfig.initFunction]();
//         } else {
//             throw new Error(`Função de inicialização não encontrada: ${sectionConfig.initFunction}`);
//         }

//         currentSection = sectionConfig.name;

//     } catch (error) {
//         console.error(`Erro ao carregar seção ${sectionKey}:`, error);
//         showError(`Falha ao carregar ${sectionKey}: ${error.message}`);
        
//         if (error.message.includes('401') || error.message.includes('token')) {
//             window.handleAuthError();
//         }
//     }
// }
//Helper para esperar por uma condição
function waitFor(condition, timeout = 3000) {
    return new Promise((resolve, reject) => {
        const start = Date.now();
        const check = () => {
            if (condition()) {
                resolve();
            } else if (Date.now() - start > timeout) {
                reject(new Error('Timeout esperando por: ${condition.toString()}'));
            } else {
                requestAnimationFrame(check);
            }
        };
        check();
    });
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
window.initLoadSection = loadSection;